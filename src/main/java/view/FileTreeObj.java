package view;

import app.IDEAppBuilder;
import use_case.FileManagement.FileTreeGenerator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;

public class FileTreeObj {
    private final FileTreeGenerator fileTreeGenerator; // Generates tree structure
    private final IDEAppBuilder appBuilder; // For file-opening functionality
    private final FileTreeObj fileTreeObj = this; // Manages right-click menus
    private File directory; // Root directory of the tree
    private JTree fileTree; // UI component representing the tree

    /**
     * Constructor for FileTreeObj.
     *
     * @param directory  The root directory of the file tree.
     * @param appBuilder The app builder instance for file handling.
     */
    public FileTreeObj(File directory, IDEAppBuilder appBuilder) {
        this.directory = directory;
        this.fileTreeGenerator = new FileTreeGenerator();
        this.appBuilder = appBuilder;
        this.fileTree = createFileTree(directory);
    }

    /**
     * Creates the JTree representing the file system starting from the root directory.
     *
     * @param directory The root directory.
     * @return The JTree component.
     */
    private JTree createFileTree(File directory) {
        DefaultMutableTreeNode rootNode = fileTreeGenerator.generateTree(directory);
        fileTree = new JTree(rootNode);

        // Handle tree selection changes
        fileTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();

            if (selectedNode == null) {
                System.out.println("No file selected.");
                return;
            }

            // Reconstruct the file path from the selected node
            StringBuilder filePathBuilder = new StringBuilder();
            TreeNode[] nodePath = selectedNode.getPath();
            for (TreeNode node : nodePath) {
                filePathBuilder.append(node.toString()).append(File.separator);
            }

            String filePath = filePathBuilder.toString().replaceAll(File.separator + "$", "");
            File selectedFile = new File(directory.getParent(), filePath);

            if (selectedFile.exists() && selectedFile.isFile()) {
                // Example action: Open the file in the editor
                appBuilder.openFile(selectedFile);
            } else {
                System.out.println("Selected node is not a valid file.");
            }
        });

        // Add right-click menu for nodes
        fileTree.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopupMenu(e);
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopupMenu(e);
                }
            }

            private void showPopupMenu(java.awt.event.MouseEvent e) {
                int row = fileTree.getClosestRowForLocation(e.getX(), e.getY());
                fileTree.setSelectionRow(row);

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                if (selectedNode == null) {
                    return;
                }

                // Reconstruct file path
                StringBuilder filePathBuilder = new StringBuilder();
                TreeNode[] nodePath = selectedNode.getPath();
                for (TreeNode node : nodePath) {
                    filePathBuilder.append(node.toString()).append(File.separator);
                }
                String filePath = filePathBuilder.toString().replaceAll(File.separator + "$", "");
                File selectedFile = new File(directory.getParent(), filePath);

                if (selectedFile.exists()) {
                    if (selectedFile.isFile()) {
                        PopupMenuOperations.createFilePopupMenu(selectedFile, fileTreeObj).show(e.getComponent(), e.getX(), e.getY());
                    } else if (selectedFile.isDirectory()) {
                        PopupMenuOperations.createDirectoryPopupMenu(selectedFile, fileTreeObj).show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        return fileTree;
    }

    /**
     * Updates the file tree when changes occur in the file system.
     *
     * @param newDirectory The new root directory for the tree.
     */
    public void updateTree(File newDirectory) {
        this.directory = newDirectory;

        // Generate a fresh tree structure from the current state of the file system
        DefaultMutableTreeNode rootNode = fileTreeGenerator.generateTree(newDirectory);

        // Update the JTree model with the new root
        ((DefaultTreeModel) fileTree.getModel()).setRoot(rootNode);

        // Expand the root node to make the structure visible
        fileTree.expandRow(0);
    }

    public void chooseDirectory() {
        JFileChooser fileChooser = new JFileChooser();

        //TODO Remove this when done.
//        //The next line is only to make testing easier by setting the default directory to where I keep my Files - Dario
//        //Feel free to use your path for testing
        fileChooser.setCurrentDirectory(new File("D:\\Users\\dario\\IdeaProjects\\puentesd"));

        fileChooser.setDialogTitle("Select a Project to Open");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            directory = fileChooser.getSelectedFile();
        }
    }

    public JTree getFileTree() {
        return fileTree;
    }

    public File getDirectory() {
        return directory;
    }

    public IDEAppBuilder getAppBuilder() {
        return appBuilder;
    }
}
