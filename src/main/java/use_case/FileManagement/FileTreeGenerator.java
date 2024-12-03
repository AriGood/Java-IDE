package use_case.FileManagement;

import java.io.File;
import java.util.Arrays;

import javax.swing.tree.DefaultMutableTreeNode;

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
    public DefaultMutableTreeNode generateTree(File directory) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(directory.getName());
        File[] files = directory.listFiles();

        if (files != null) {
            Arrays.stream(files)
                    .filter(File::exists)
                    .forEach(file -> {
                        if (file.isDirectory()) {
                            rootNode.add(generateTree(file));
                        }
                        else {
                            rootNode.add(new DefaultMutableTreeNode(file.getName()));
                        }
                    });
        }
        return rootNode;
    }
}
