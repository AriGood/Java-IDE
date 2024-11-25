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
            IDEAppBuilder.buildTree();
            IDEAppBuilder.buildIDE();
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

    private void addEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        JMenuItem find = new JMenuItem("Find");
        JMenuItem findAll = new JMenuItem("Find All");

    }

    private void addGitMenu(){
        JMenu gitMenu = new JMenu("Git");
        JMenuItem commit = new JMenuItem("Commit");
        JMenuItem push = new JMenuItem("Push");
        JMenuItem pull = new JMenuItem("Pull");
    }
    public void buildMenu() {
        addFileMenu();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
