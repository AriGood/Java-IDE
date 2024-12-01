package view;

import app.IDEAppBuilder;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
import java.io.File;

public class MenuBarObj {
    private final JMenuBar menuBar;
    private final IDEAppBuilder IDEAppBuilder;

    public MenuBarObj(IDEAppBuilder newIDEAppBuilder) {
        this.menuBar = new JMenuBar();
        this.IDEAppBuilder = newIDEAppBuilder;
    }

    private void addFileMenu() {
        JMenu fileMenu = new JMenu("File");

        // Menu Items
        JMenuItem newFile = new JMenuItem("New File");
        JMenuItem openDirectory = new JMenuItem("Open Directory");
        JMenuItem saveFile = new JMenuItem("Save");

        // ActionListeners
        newFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Create New File");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                FileOperations fileOperations = new FileOperations(selectedFile);
                fileOperations.saveFile(selectedFile, ""); // Create an empty file with no content
                JOptionPane.showMessageDialog(null, "File created successfully!");
            }
        });

        openDirectory.addActionListener(e -> {
            JFileChooser directoryChooser = new JFileChooser();
            directoryChooser.setDialogTitle("Choose Project Directory");
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int option = directoryChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = directoryChooser.getSelectedFile();
                IDEAppBuilder.buildTree(selectedDirectory); // Pass the selected directory
                IDEAppBuilder.buildIDE(); // Rebuild the IDE with the new directory structure
            }
        });

        saveFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save File");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                FileOperations fileOperations = new FileOperations(selectedFile);
                String content = JOptionPane.showInputDialog("Enter file content:");
                if (content != null) {
                    fileOperations.saveFile(selectedFile, content);
                    JOptionPane.showMessageDialog(null, "File saved successfully!");
                }
            }
        });

        // Add items to the File menu
        //fileMenu.add(newFile);
        fileMenu.add(openDirectory);
        //fileMenu.add(saveFile);

        // Add the File menu to the menu bar
        menuBar.add(fileMenu);
    }

    public void buildMenu() {
        addFileMenu();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}