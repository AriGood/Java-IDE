package view;

import use_case.FileManagement.FileOperations;
import use_case.FileManagement.FileTreeGenerator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class FilePopup {
    private final JTree fileTree;
    private File fileToCopy;

    public FilePopup(JTree fileTree) {
        this.fileTree = fileTree;

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
                TreePath path = fileTree.getPathForLocation(e.getX(), e.getY());
                if (path != null) {
                    fileTree.setSelectionPath(path);
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                    File selectedFile = getFileFromNode(selectedNode);

                    if (selectedFile != null) {
                        JPopupMenu popupMenu = createPopupMenu(selectedFile);
                        popupMenu.show(fileTree, e.getX(), e.getY());
                    }
                }
            }

            private File getFileFromNode(DefaultMutableTreeNode node) {
                String filePath = buildFilePath(node);
                return new File(filePath);
            }

            private String buildFilePath(DefaultMutableTreeNode node) {
                StringBuilder path = new StringBuilder(node.toString());
                while ((node = (DefaultMutableTreeNode) node.getParent()) != null) {
                    path.insert(0, node + File.separator);
                }
                return path.toString();
            }

            private JPopupMenu createPopupMenu(File selectedFile) {
                JPopupMenu popupMenu = new JPopupMenu();

                // New File Option
                //TODO: fix
                JMenuItem newFileItem = new JMenuItem("New File");


                // Paste Option
                //TODO: fix
                JMenuItem pasteItem = new JMenuItem("Paste");
                pasteItem.addActionListener(e -> {
                    if (fileToCopy != null) {
                        // Determine the correct destination
                        File destination = selectedFile.isDirectory() ? selectedFile : selectedFile.getParentFile();
                        try {
                            FileOperations.pasteFile(fileToCopy, destination);
                            JOptionPane.showMessageDialog(null, "File or directory pasted successfully!");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error pasting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No file or directory copied!", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                });

                JMenuItem copyItem = new JMenuItem("Copy");
                copyItem.addActionListener(e -> fileToCopy = selectedFile); // Works for files and directories

                // Delete option
                //TODO: actually implement this
                JMenuItem deleteItem = new JMenuItem("Delete");

                popupMenu.add(newFileItem);
                popupMenu.add(copyItem);
                popupMenu.add(pasteItem);
                popupMenu.add(deleteItem);

                return popupMenu;

                // Note: when you copy and paste a file or directory or create a new file
                // git should be updated as well ... need to involve that
            }
        });
    }
}
