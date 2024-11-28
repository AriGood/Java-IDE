package view;

import use_case.FileManagement.FileOperations;
import use_case.FileManagement.DirectoryOperations;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PopupMenuHandler {

    public JPopupMenu createFilePopupMenu(File file) {
        JPopupMenu popupMenu = new JPopupMenu();

        // Open File Option
        JMenuItem openFileItem = new JMenuItem("Open");
        openFileItem.addActionListener(e -> System.out.println("Opening file: " + file.getName()));

        // Rename File Option
        JMenuItem renameFileItem = new JMenuItem("Rename");
        renameFileItem.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Enter new name:");
            if (newName != null && !newName.trim().isEmpty()) {
                new FileOperations(file).rename(newName);
            }
        });

        // Delete File Option
        JMenuItem deleteFileItem = new JMenuItem("Delete");
        deleteFileItem.addActionListener(e -> new FileOperations(file).delete());

        popupMenu.add(openFileItem);
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
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Rename Directory Option
        JMenuItem renameDirItem = new JMenuItem("Rename");
        renameDirItem.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Enter new name:");
            if (newName != null && !newName.trim().isEmpty()) {
                new DirectoryOperations(directory).rename(newName);
            }
        });

        // Delete Directory Option
        JMenuItem deleteDirItem = new JMenuItem("Delete");
        deleteDirItem.addActionListener(e -> new DirectoryOperations(directory).delete());

        popupMenu.add(newFileItem);
        popupMenu.add(renameDirItem);
        popupMenu.add(deleteDirItem);

        return popupMenu;
    }
}

