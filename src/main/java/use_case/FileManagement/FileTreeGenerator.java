package use_case.FileManagement;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.Arrays;

/**
 * Class to generate a tree structure from a directory.
 */
public class FileTreeGenerator {
    /**
     * Creates a tree structure starting from the given directory.
     *
     * @param directory The root directory for the tree.
     * @return A DefaultMutableTreeNode representing the root of the tree.
     */
    public DefaultMutableTreeNode createNodesFromDirectory(File directory) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(directory.getName());
        File[] files = directory.listFiles();

        if (files != null) {
            Arrays.stream(files).forEach(file -> {
                if (file.isDirectory()) {
                    // Recursive call for subdirectories
                    rootNode.add(createNodesFromDirectory(file));
                } else {
                    // Add a file as a leaf node
                    rootNode.add(new DefaultMutableTreeNode(file.getName()));
                }
            });
        }
        return rootNode;
    }
}