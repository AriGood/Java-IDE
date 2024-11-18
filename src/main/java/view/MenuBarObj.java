package view;

import javax.swing.*;

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
