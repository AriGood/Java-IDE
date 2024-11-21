package use_case.FileManagement;

import app.IDEAppBuilder;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Class to convert list of files into a tree.
 * I added what I had written in my mock draft - Dario
 * I can't figure out the untracked files thing yet so I copied what Dario has done so far.
 * I'll be working on it on this branch. - Mariana
 */
public class FileTreeGenerator {
    private JTree fileTree;
    //made root an instance variable so we can update the tree.
    private DefaultMutableTreeNode treeRootNode;
    //made the directory an instance variable to recycle it in create, delete, and save.
    private final File directory;

    public FileTreeGenerator(File projectDirectory) {
        directory = projectDirectory;
    }

    /* Reformatted the way this class works by outsourcing any swing operations
    to the app builder. That way "FileTreeGenerator" only performs tree opperations.
    public void chooseDiretory() {
        JFileChooser fileChooser = new JFileChooser();
        //Changed the directory chooser Title at the top.
        fileChooser.setDialogTitle("Select a Project to Open");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            //refactored to the instance variable.
            directory = fileChooser.getSelectedFile();
        }
    }
     */

    // Added Tree Listeners
    public JTree createFileTree(File directory, TabManagement tabManagement, IDEAppBuilder appBuilder) {
        treeRootNode = createNodesFromDirectory(directory);
        fileTree = new JTree(treeRootNode);
        addTreeListeners(fileTree, tabManagement, appBuilder);
        return fileTree;
    }

    // Helper to refactor
    public void addTreeListeners(JTree fileTree, TabManagement tabManagement, IDEAppBuilder appBuilder) {
        fileTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();

            if (selectedNode == null) return;

            String nodeName = selectedNode.toString();
            File selectedFile = new File(directory, nodeName);

            if (selectedFile.isFile()) {
                String fileContent = FileOperations.fileContent(selectedFile);
                tabManagement.newTab(selectedFile);
                appBuilder.updateText(fileContent);
            }
        });
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
