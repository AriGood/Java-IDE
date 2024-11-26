package use_case.FileManagement;

import app.IDEAppBuilder;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Class to convert list of files into a tree.
 */
public class FileTreeGenerator {
    private JTree fileTree;
    private DefaultMutableTreeNode treeRootNode;
    private File directory;
    private IDEAppBuilder appBuilder;

    public FileTreeGenerator(IDEAppBuilder IDEAppBuilder) {
        appBuilder = IDEAppBuilder;
        directory = appBuilder.getDirectory();
    }

    public JTree createFileTree(File directory) {
        treeRootNode = createNodesFromDirectory(directory);
        fileTree = new JTree(treeRootNode);

        // Modified valueChanged method in createFileTree
        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();

                // Check if a node is selected
                if (selectedNode == null) {
                    System.out.println("No file selected.");
                    return;
                }

                // Reconstruct file path from the selected node
                StringBuilder filePathBuilder = new StringBuilder();
                TreeNode[] nodePath = selectedNode.getPath();
                for (TreeNode node : nodePath) {
                    filePathBuilder.append(node.toString()).append(File.separator);
                }
                String filePath = filePathBuilder.toString().replaceAll(File.separator + "$", ""); // Remove trailing separator

                File selectedFile = new File(directory.getParent(), filePath);

                // exists works but isFile does not... weirdly enough
                if (selectedFile.exists()) {
                    try {
                        // Read file content
                        String content = Files.readString(selectedFile.toPath());
                        System.out.println("File content loaded successfully: " + content);

                        // Open the file
                        appBuilder.openFile(selectedFile);

                    } catch (IOException ex) {
                        System.err.println("Could not open file: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Selected node is not a valid file.");
                }
            }
        });

        return fileTree;
    }


    private DefaultMutableTreeNode createNodesFromDirectory(File directory) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(directory.getName());
        File[] files = directory.listFiles();

        if (files != null) {
            Arrays.stream(files).forEach(file -> {
                if (file.isDirectory()) {
                    rootNode.add(createNodesFromDirectory(file));
                } else {
                    rootNode.add(new DefaultMutableTreeNode(file.getName()));
                }
            });
        }
        return rootNode;
    }

    // New
    public void updateTree(File newDirectory) {
        directory = newDirectory;
        treeRootNode = createNodesFromDirectory(directory);
        ((DefaultTreeModel) fileTree.getModel()).setRoot(treeRootNode);
    }

    public File getDirectory() {
        return directory;
    }


    public void createFile(File parent) {
        String fileName = JOptionPane.showInputDialog(null, "File name:", "File", JOptionPane.QUESTION_MESSAGE);

        if (fileName == null || fileName.trim().isEmpty()) {
            return;
        }

        File newFile = new File(parent, fileName);
        try {
            if (newFile.createNewFile()) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newFile.getName());
                DefaultMutableTreeNode parentNode = findNodeByName(parent.getName(), treeRootNode);
                if (parentNode != null) {
                    parentNode.add(newNode);
                    ((DefaultTreeModel) fileTree.getModel()).reload(parentNode);
                } else {
                    System.out.println("File not found");
                    return;
                }

            } else {
                JOptionPane.showMessageDialog(null, "File already exists", "File", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File could not be created", "File", JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(null, "Security access denied", "File", JOptionPane.ERROR_MESSAGE);
        }
    }
    private DefaultMutableTreeNode findNodeByName(String nodeName, DefaultMutableTreeNode root) {
        if (root.toString().equals(nodeName)) {
            return root;
        }
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            DefaultMutableTreeNode result = findNodeByName(nodeName, child);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public void deleteFile(File parentDirectory) {
        //TODO
    }

}

