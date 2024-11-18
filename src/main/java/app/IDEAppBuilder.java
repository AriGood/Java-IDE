package app;

import entity.Editor;
import use_case.FileManagement.TabManagement;
import view.EditorObj;
import view.FileTreeObj;
import view.MenuBarObj;
import view.TerminalObj;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Builder for the Note Application.
 */
public class IDEAppBuilder {
    public static final int HEIGHT = 600;
    public static final int WIDTH = 800;
    public static TabManagement tabManagement = new TabManagement();
    public static JScrollPane editorScrollPane;

    private JScrollPane terminalScrollPane;
    private JTextArea codeEditor;
    private JScrollPane fileScrollPane;
    private File directory;

    /**
     * Builds the application.
     * @return the JFrame for the application
     */
    public JFrame build() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("IDE Application");
        frame.setSize(WIDTH, HEIGHT);

        tabManagement.newTab("New Tab");
        frame.setJMenuBar(makeMenuBar());

        frame.add(makeEditorPanel(), BorderLayout.CENTER);
        frame.add(makeFilePanel(), BorderLayout.WEST);
        frame.add(makeTerminalPanel(), BorderLayout.SOUTH);


        JSplitPane leftRightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileScrollPane, editorScrollPane);
        leftRightSplitPane.setDividerLocation(300);


        JSplitPane topBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, leftRightSplitPane, terminalScrollPane);
        topBottomSplitPane.setDividerLocation(400);


        frame.add(topBottomSplitPane, BorderLayout.CENTER);

        frame.setVisible(true);

        return frame;

    }

    private JScrollPane makeFilePanel() {
        FileTreeObj fileTreeObj = new FileTreeObj();
        fileScrollPane = new JScrollPane(fileTreeObj.getFileTree());
        directory = fileTreeObj.getDirectory();
        return fileScrollPane;
    }

    private JMenuBar makeMenuBar() {
        MenuBarObj menuBarObj = new MenuBarObj();
        menuBarObj.buildMenu();
        return menuBarObj.getMenuBar();
    }

    private JScrollPane makeTerminalPanel() {
        TerminalObj terminal = new TerminalObj();
        terminalScrollPane = new JScrollPane(terminal.getTextArea());
        return terminalScrollPane;
    }

    public static JScrollPane makeEditorPanel() {
        // make text area an instance variable with this function and create a getter and reference it for autocomp.
        EditorObj editorObj = new EditorObj();
        editorScrollPane = new JScrollPane(editorObj.getTextArea());
        editorScrollPane.setRowHeaderView(editorObj.getLineNums());
        return editorScrollPane;
    }


    public JTextArea getCodeEditor() {
        return codeEditor;
    }

    public File getDirectory() {
        return directory;
    }

    public JScrollPane getTerminalScrollPane() {
        return terminalScrollPane;
    }

    public JScrollPane getEditorScrollPane() {
        return editorScrollPane;
    }

}
