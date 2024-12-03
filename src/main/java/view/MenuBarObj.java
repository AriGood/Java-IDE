package view;

import app.IDEAppBuilder;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuBarObj {
    private final JMenuBar menuBar;
    private final IDEAppBuilder ideAppBuilder;

    /**
     * Constructor to initialize the MenuBarObj with a given IdeAppBuilder.
     *
     * @param newIDEAppBuilder The IdeAppBuilder instance to integrate with.
     */
    public MenuBarObj(IDEAppBuilder newIDEAppBuilder) {
        this.menuBar = new JMenuBar();
        this.ideAppBuilder = newIDEAppBuilder;
    }

    /**
     * Builds and adds the "File" menu to the menu bar.
     * Includes options for creating, opening, and saving files,
     * with Git integration for new files.
     */
    private void addFileMenu() {
        JMenuItem newFile = new JMenuItem("New File");
        JMenuItem openDirectory = new JMenuItem("Open Directory");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenu fileMenu = new JMenu("File");

        newFile.addActionListener(actionListener -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Create New File");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                FileOperations fileOperations = new FileOperations(selectedFile);

                FileOperations.saveFile(selectedFile, "");
                JOptionPane.showMessageDialog(null, "File created successfully!");

                if (ideAppBuilder.getGitManager() != null) {
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
                            ideAppBuilder.getGitManager().addFiles(file);
                            JOptionPane.showMessageDialog(null, "File added to Git repository.");
                        } 
                        catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error adding file to Git: " + ex.getMessage());
                        }
                    }
                }
            }
        });

        openDirectory.addActionListener(actionListener -> {
            JFileChooser directoryChooser = new JFileChooser();
            directoryChooser.setDialogTitle("Choose Project Directory");
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = directoryChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = directoryChooser.getSelectedFile();
                int choice = JOptionPane.showConfirmDialog(null,
                        "Would you like to open this as a git repository?","Git",JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        ideAppBuilder.getGitManager().createRepository(JOptionPane.showInputDialog(null, "Enter remote URL:")
                                ,selectedDirectory.getAbsolutePath());
                        JOptionPane.showMessageDialog(null, "Repository opened successfully.");
                    }
                    catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error creating repository: " + ex.getMessage());
                    }
                }
                ideAppBuilder.buildTree(selectedDirectory);
                ideAppBuilder.buildIde();
            }
        });

        saveFile.addActionListener(actionListener -> {
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

        fileMenu.add(newFile);
        fileMenu.add(openDirectory);
        fileMenu.add(saveFile);

        GitMenuBuilder git = new GitMenuBuilder(ideAppBuilder);
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
