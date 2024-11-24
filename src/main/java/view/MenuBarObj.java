package view;

import app.IDEAppBuilder;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MenuBarObj {
    private JMenuBar menuBar;
    private IDEAppBuilder IDEAppBuilder;

    public MenuBarObj(IDEAppBuilder newIDEAppBuilder) {
        menuBar = new JMenuBar();
        IDEAppBuilder = newIDEAppBuilder;
    }

    private void addFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New File");

        // Changed from openFile to openDirectory
        JMenuItem openDirectory = new JMenuItem("Open Directory");
        JMenuItem saveFile = new JMenuItem("Save");

        // Add ActionListeners to the menu items
        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });

        openDirectory.addActionListener(e -> {
            JFileChooser directoryChooser = new JFileChooser();
            directoryChooser.setDialogTitle("Select a Directory");
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = directoryChooser.showOpenDialog(null);

            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = directoryChooser.getSelectedFile();
                IDEAppBuilder.updateFileTree(selectedDirectory);
            }
        });

        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });

        fileMenu.add(newFile);
        fileMenu.add(openDirectory);
        fileMenu.add(saveFile);
        menuBar.add(fileMenu);
    }

    public void buildMenu() {
        addFileMenu();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
