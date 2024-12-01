package view;

import app.IDEAppBuilder;
import org.jetbrains.annotations.NotNull;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class MenuBarObj {
    private final JMenuBar menuBar;
    private final IDEAppBuilder IDEAppBuilder;

    /**
     * Constructor to initialize the MenuBarObj with a given IDEAppBuilder.
     *
     * @param newIDEAppBuilder The IDEAppBuilder instance to integrate with.
     */
    public MenuBarObj(IDEAppBuilder newIDEAppBuilder) {
        this.menuBar = new JMenuBar();
        this.IDEAppBuilder = newIDEAppBuilder;
    }

    /**
     * Builds and adds the "File" menu to the menu bar.
     * Includes options for creating, opening, and saving files,
     * with Git integration for new files.
     */
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

                // Create an empty file
                FileOperations.saveFile(selectedFile, "");
                JOptionPane.showMessageDialog(null, "File created successfully!");

                // Check if Git integration is available
                if (IDEAppBuilder.gitManager != null) {
                    int gitOption = JOptionPane.showConfirmDialog(
                            null,
                            "Do you want to add this file to Git?",
                            "Git Integration",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (gitOption == JOptionPane.YES_OPTION) {
                        try {
                            List<String> file = new ArrayList<String>();
                            file.add(selectedFile.getAbsolutePath());
                            IDEAppBuilder.gitManager.addFiles(file);
                            JOptionPane.showMessageDialog(null, "File added to Git repository.");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error adding file to Git: " + ex.getMessage());
                        }
                    }
                }
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
                    FileOperations.saveFile(selectedFile, content);
                    JOptionPane.showMessageDialog(null, "File saved successfully!");
                }
            }
        });

        // Add items to the File menu
        fileMenu.add(newFile);
        fileMenu.add(openDirectory);
        fileMenu.add(saveFile);

        // Add the File menu and Git menu to the menu bar
        GitMenuBuilder git = new GitMenuBuilder(IDEAppBuilder);
        JMenu gitMenu = git.build();
        menuBar.add(fileMenu);
        menuBar.add(gitMenu);
    }

    /**
     * Builds the menu bar by adding all necessary menus.
     */
    public void buildMenu() {
        addFileMenu();
    }

    /**
     * Returns the built menu bar.
     *
     * @return The JMenuBar object containing all menus.
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
