package app;

import data_access.AutoCompleteBST;
import use_case.AutoCompleteOperations.AutoCompleteOperations;
import use_case.FileManagement.FileTreeGenerator;
import use_case.FileManagement.TabManagement;
import view.*;
import java.util.List;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Builder for the Note Application.
 */
public class IDEAppBuilder {
    public static final int HEIGHT = 600;
    public static final int WIDTH = 800;
    public static TabManagement tabManagement = new TabManagement();
    public static JScrollPane editorScrollPane;

    private JScrollPane terminalScrollPane;
    private AutoCompleteOperations autoCompleteOperations;
    private JScrollPane fileScrollPane;
    private File directory;
    private static EditorObj editorObj;
    private FileTreeGenerator fileTreeGenerator;

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

        initializeAutoComplete(AutoCompleteBST.buildWithJavaKeywords());
        frame.add(topBottomSplitPane, BorderLayout.CENTER);

        frame.setVisible(true);

        return frame;

    }

    public void initializeAutoComplete(AutoCompleteBST autocompleteBST) {
        AutoCompletePopup suggestionPopup = new AutoCompletePopup();
        autoCompleteOperations = new AutoCompleteOperations(autocompleteBST);

        JTextArea codeEditor = editorObj.getTextArea();

        codeEditor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    List<String> suggestions = autoCompleteOperations.getSuggestions(codeEditor);
                    suggestionPopup.showSuggestions(codeEditor, suggestions);
                });
            }
        });
    }


    private JScrollPane makeFilePanel() {
        FileTreeObj fileTreeObj = new FileTreeObj(this);
        fileScrollPane = new JScrollPane(fileTreeObj.getFileTree());
        directory = fileTreeObj.getDirectory();
        return fileScrollPane;
    }

    private JMenuBar makeMenuBar() {
        MenuBarObj menuBarObj = new MenuBarObj(this);
        menuBarObj.buildMenu();
        return menuBarObj.getMenuBar();
    }

    // New
    public void updateFileTree(File newDirectory) {
        directory = newDirectory;
        fileTreeGenerator.updateTree(directory);
        fileScrollPane.setViewportView(fileTreeGenerator.createFileTree(directory));
    }

    private JScrollPane makeTerminalPanel() {
        TerminalObj terminal = new TerminalObj();
        terminalScrollPane = new JScrollPane(terminal.getTextArea());
        return terminalScrollPane;
    }

    public JScrollPane makeEditorPanel() {
        // make text area an instance variable with this function and create a getter and reference it for autocomp.
        editorObj = new EditorObj();
        editorScrollPane = new JScrollPane(editorObj.getTextArea());
        editorScrollPane.setRowHeaderView(editorObj.getLineNums());
        return editorScrollPane;
    }

    public void createNewTab(File selectedFile) {
        tabManagement.newTab(selectedFile);
    }


//    public void initializeAutoComplete(AutoCompleteBST autocompleteBST) {
//        JPopupMenu popup = new JPopupMenu();
//        autoCompleteOperations = new AutoCompleteOperations(autocompleteBST);
//        autoCompleteOperations.enableAutoComplete(tabManagement,codeEditor, popup);
//    }


    public File getDirectory() {
        return directory;
    }

    public JScrollPane getTerminalScrollPane() {
        return terminalScrollPane;
    }

    public JScrollPane getEditorScrollPane() {
        return editorScrollPane;
    }

    public static void updateText(String newText){
        editorObj.updateTextArea(newText);
    }

}
