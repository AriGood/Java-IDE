package use_case.FileManagement;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
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

    public FileTreeGenerator() {
        chooseDiretory();
    }

    private void chooseDiretory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedDir = fileChooser.getSelectedFile();
            DefaultMutableTreeNode rootNode = createNodesFromDirectory(selectedDir);
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

    public JScrollPane getFileScrollPane() {
        return fileScrollPane;
    }

    public void createFile(File parentDirectory) {
        //TODO
    }

    public void deleteFile(File parentDirectory) {
        //TODO
    }

    public void saveFile(File parentDirectory) {
        //TODO
    }

}
