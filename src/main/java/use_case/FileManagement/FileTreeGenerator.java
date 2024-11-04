package use_case.FileManagement;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * Class to convert list of files into a tree.
 *
 * I added what I had written in my mock draft - Dario
 */
public class FileTreeGenerator {
    //TODO: Write code that automatically generates a tree given a file list.

    private void makeFilePanel(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Project");

        DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("src");
        DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(".idea");

        DefaultMutableTreeNode grandChild = new DefaultMutableTreeNode("Main");
        DefaultMutableTreeNode grandChild2 = new DefaultMutableTreeNode("Test");

        root.add(child1);
        root.add(child2);

        child1.add(grandChild);
        child1.add(grandChild2);

        JTree fileTree = new JTree(root);
        JScrollPane fileScrollPane = new JScrollPane(fileTree);
    }


}
