package use_case.FileManagement;

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
    //TODO: check if this actually works. It automatically generates JTree from chosen direcotry structure...
    private JTree fileTree;
    private JScrollPane fileScrollPane;
    //made root an instance variable so we can update the tree.
    private DefaultMutableTreeNode rootNode;
    //made the directory an instance variable to recycle it in create, delete, and save.
    private File directory;

    public FileTreeGenerator() {
        chooseDiretory();
    }

    private void chooseDiretory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            //refactored to the instance variable.
            directory = fileChooser.getSelectedFile();
            rootNode = createNodesFromDirectory(directory);
            fileTree = new JTree(rootNode);
            fileScrollPane = new JScrollPane(fileTree);
        }
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

    public JScrollPane getFileScrollPane() {
        return fileScrollPane;
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
                DefaultMutableTreeNode parentNode = findNodeByName(parent.getName(), rootNode);
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

    public void saveFile(File parentDirectory) {
        //TODO
    }

}
