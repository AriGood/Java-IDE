package view;

import use_case.FileManagement.DirectoryOperations;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
import java.io.File;

public class PopupMenuHandler {
    private File copiedFileOrDirectory; // Stores the file or directory to copy
    private boolean isCopyingDirectory; // Tracks whether the copied item is a directory
    private final FileTreeObj fileTreeObj; // To update the tree

    public PopupMenuHandler(FileTreeObj fileTreeObj) {
        this.fileTreeObj = fileTreeObj;
    }

    public JPopupMenu createFilePopupMenu(File file) {
        JPopupMenu popupMenu = new JPopupMenu();

        // Rename File Option
        JMenuItem renameFileItem = new JMenuItem("Rename");
        renameFileItem.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Enter new name:");
            if (newName != null && !newName.trim().isEmpty()) {
                new FileOperations(file).rename(newName);
                fileTreeObj.updateTree(fileTreeObj.getDirectory()); // Update the tree
            }
        });

        // Copy File Option
        JMenuItem copyFileItem = new JMenuItem("Copy");
        copyFileItem.addActionListener(e -> {
            if (file != null && file.exists()) {
                copiedFileOrDirectory = file;
                isCopyingDirectory = false;
                System.out.println("Copied file: " + file.getName());
            } else {
                JOptionPane.showMessageDialog(null, "Cannot copy a deleted or invalid file.", "Copy Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete File Option
        JMenuItem deleteFileItem = new JMenuItem("Delete");
        deleteFileItem.addActionListener(e -> {
            try {
                new FileOperations(file).delete();
                System.out.println("Deleted file: " + file.getAbsolutePath());

                // Clear stale reference
                if (file.equals(copiedFileOrDirectory)) {
                    copiedFileOrDirectory = null;
                    isCopyingDirectory = false;
                }

                fileTreeObj.updateTree(fileTreeObj.getDirectory());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to delete: " + ex.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        popupMenu.add(copyFileItem);
        popupMenu.add(renameFileItem);
        popupMenu.add(deleteFileItem);

        return popupMenu;
    }

    public JPopupMenu createDirectoryPopupMenu(File directory) {
        JPopupMenu popupMenu = new JPopupMenu();

        // Create New File Option
        JMenuItem newFileItem = new JMenuItem("New File");
        newFileItem.addActionListener(e -> {
            String fileName = JOptionPane.showInputDialog("Enter file name:");
            if (fileName != null && !fileName.trim().isEmpty()) {
                File newFile = new File(directory, fileName);
                try {
                    if (newFile.createNewFile()) {
                        System.out.println("Created file: " + newFile.getName());
                        fileTreeObj.updateTree(fileTreeObj.getDirectory()); // Update the tree
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to create file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Rename Directory Option
        JMenuItem renameDirItem = new JMenuItem("Rename");
        renameDirItem.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Enter new name:");
            if (newName != null && !newName.trim().isEmpty()) {
                new DirectoryOperations(directory).rename(newName);
                fileTreeObj.updateTree(fileTreeObj.getDirectory()); // Update the tree
            }
        });

        // Copy Directory Option
        JMenuItem copyDirItem = new JMenuItem("Copy");
        copyDirItem.addActionListener(e -> {
            if (directory != null && directory.exists()) {
                copiedFileOrDirectory = directory;
                isCopyingDirectory = true;
                System.out.println("Copied directory: " + directory.getName());
            } else {
                JOptionPane.showMessageDialog(null, "Cannot copy a deleted or invalid directory.", "Copy Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Paste Option
        JMenuItem pasteDirItem = new JMenuItem("Paste");
        pasteDirItem.addActionListener(e -> {
            if (copiedFileOrDirectory != null && copiedFileOrDirectory.exists()) {
                try {
                    if (isCopyingDirectory) {
                        new DirectoryOperations(copiedFileOrDirectory).copy(directory);
                    } else {
                        new FileOperations(copiedFileOrDirectory).copy(directory);
                    }
                    System.out.println("Pasted into: " + directory.getAbsolutePath());
                    fileTreeObj.updateTree(fileTreeObj.getDirectory());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Failed to paste: " + ex.getMessage(), "Paste Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Clear reference after paste
                    copiedFileOrDirectory = null;
                    isCopyingDirectory = false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nothing to paste or the copied item no longer exists.", "Paste Error", JOptionPane.ERROR_MESSAGE);
                copiedFileOrDirectory = null;
                isCopyingDirectory = false;
            }
        });

        // Delete Directory Option
        JMenuItem deleteDirItem = new JMenuItem("Delete");
        deleteDirItem.addActionListener(e -> {
            try {
                new DirectoryOperations(directory).delete();
                System.out.println("Deleted directory: " + directory.getAbsolutePath());

                // Clear stale reference
                if (directory.equals(copiedFileOrDirectory)) {
                    copiedFileOrDirectory = null;
                    isCopyingDirectory = false;
                }

                fileTreeObj.updateTree(fileTreeObj.getDirectory());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to delete: " + ex.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        popupMenu.add(newFileItem);
        popupMenu.add(copyDirItem);
        popupMenu.add(pasteDirItem);
        popupMenu.add(renameDirItem);
        popupMenu.add(deleteDirItem);

        return popupMenu;
    }
}
