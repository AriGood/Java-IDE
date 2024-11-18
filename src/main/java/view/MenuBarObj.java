package view;

import app.IDEAppBuilder;
import use_case.FileManagement.TabManagement;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBarObj {
    private JMenuBar menuBar;

    public MenuBarObj() {
        menuBar = new JMenuBar();
    }

    private void addFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New File");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");

        // Add ActionListeners to the menu items
        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });

        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select a File to Open");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int option = fileChooser.showOpenDialog(null);

                if (option == JFileChooser.APPROVE_OPTION) {
                    IDEAppBuilder.tabManagement.newTab(fileChooser.getSelectedFile());
                    IDEAppBuilder.editorScrollPane = IDEAppBuilder.makeEditorPanel();

                }
            }
        });

        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });

        fileMenu.add(newFile);
        fileMenu.add(openFile);
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
