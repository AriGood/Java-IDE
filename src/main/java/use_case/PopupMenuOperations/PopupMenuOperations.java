package use_case.PopupMenuOperations;

import app.IDEAppBuilder;
import entity.LeftIDEJTabbedPane;
import entity.ParentIDEJTabbedPane;
import entity.RightIDEJTabbedPane;
import use_case.EditorOperations.EditorOperations;
import use_case.FileManagement.DirectoryOperations;
import use_case.FileManagement.FileOperations;
import view.FileTreeObj;

import javax.swing.*;
import java.io.File;

public class PopupMenuOperations {
    private static File copiedFileOrDirectory;
    private static boolean isCopyingDirectory;

    public static JPopupMenu createTabPopup(ParentIDEJTabbedPane tabbedPane, IDEAppBuilder appBuilder) {

        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem closeAllTabs = new JMenuItem("Close All Tabs");
        JMenuItem closeOtherTabs = new JMenuItem("Close Other Tabs");
        JMenuItem closeTabsToTheLeft = new JMenuItem("Close Tabs To The Left");
        JMenuItem closeTabsToTheRight = new JMenuItem("Close Tabs To The Right");
        JMenuItem tabAction = new JMenuItem();

        closeAllTabs.addActionListener(e -> EditorOperations.closeAllTabs(tabbedPane));
        closeOtherTabs.addActionListener(e -> EditorOperations.closeOtherTabs(tabbedPane.getSelectedIndex(), tabbedPane));
        closeTabsToTheLeft.addActionListener(e -> EditorOperations.closeTabsToLeft(tabbedPane.getSelectedIndex(), tabbedPane));
        closeTabsToTheRight.addActionListener(e -> EditorOperations.closeTabsToRight(tabbedPane.getSelectedIndex(), tabbedPane));
        if (tabbedPane instanceof RightIDEJTabbedPane) {
            tabAction.setText("Merge Tab");
            tabAction.addActionListener(e -> EditorOperations.mergeTab(tabbedPane.getSelectedIndex(), (RightIDEJTabbedPane) tabbedPane, appBuilder));
        } else {
            tabAction.setText("Split Tab");
            tabAction.addActionListener(e -> EditorOperations.splitTab(tabbedPane.getSelectedIndex(),(LeftIDEJTabbedPane) tabbedPane, appBuilder));
        }

        popupMenu.add(closeAllTabs);
        popupMenu.add(closeOtherTabs);
        popupMenu.add(closeTabsToTheLeft);
        popupMenu.add(closeTabsToTheRight);
        popupMenu.add(tabAction);
        popupMenu.setVisible(true);

        return popupMenu;
    }

    public static JPopupMenu createFilePopupMenu(File file, FileTreeObj fileTreeObj) {
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

    public static JPopupMenu createDirectoryPopupMenu(File directory, FileTreeObj fileTreeObj) {
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
