package view;

import use_case.FileManagement.FileTreeGenerator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;

public class FilePopup {
    private final JTree fileTree;
    private File copiedFile; // To keep track of the file to be copied

    public FilePopup(JTree fileTree) {
        this.fileTree = fileTree;

        // Add mouse listener to detect right-click
        fileTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            private void showPopup(MouseEvent e) {
                // Get the tree path at the click location
                TreePath path = fileTree.getPathForLocation(e.getX(), e.getY());
                if (path != null) {
                    fileTree.setSelectionPath(path); // Select the clicked node
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                    File selectedFile = getFileFromNode(selectedNode);

                    if (selectedFile != null) {
                        JPopupMenu popupMenu = createPopupMenu(selectedFile);
                        popupMenu.show(fileTree, e.getX(), e.getY()); // Display the popup menu
                    }
                }
            }

            private File getFileFromNode(DefaultMutableTreeNode node) {
                Object userObject = node.getUserObject();
                if (userObject instanceof String) {
                    // Rebuild the file path based on the node's hierarchy
                    StringBuilder filePathBuilder = new StringBuilder(userObject.toString());
                    TreeNode[] pathNodes = node.getPath();
                    for (int i = 1; i < pathNodes.length; i++) {
                        filePathBuilder.insert(0, File.separator).insert(0, pathNodes[i].toString());
                    }
                    return new File(filePathBuilder.toString());
                }
                return null;
            }

            private JPopupMenu createPopupMenu(File selectedFile) {
                JPopupMenu popupMenu = new JPopupMenu();

                // New File Option
                JMenuItem newFileItem = new JMenuItem("New File");
                newFileItem.addActionListener(e -> {
                    FileTreeGenerator.createFile(selectedFile.isDirectory() ? selectedFile : selectedFile.getParentFile());
                });

                // Copy Option
                JMenuItem copyItem = new JMenuItem("Copy");
                copyItem.addActionListener(e -> copiedFile = selectedFile);

                // Paste Option
                JMenuItem pasteItem = new JMenuItem("Paste");
                pasteItem.addActionListener(e -> {
                    if (copiedFile != null) {
                        File destination = selectedFile.isDirectory() ? selectedFile : selectedFile.getParentFile();
                        File newFile = new File(destination, copiedFile.getName());
                        try {
                            Files.copy(copiedFile.toPath(), newFile.toPath());
                            JOptionPane.showMessageDialog(null, "File pasted successfully!");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Failed to paste file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No file copied!", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                });

                // Add options to the menu
                popupMenu.add(newFileItem);
                popupMenu.add(copyItem);
                popupMenu.add(pasteItem);

                return popupMenu;
            }
        });
    }
}