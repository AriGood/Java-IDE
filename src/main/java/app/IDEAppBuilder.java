package app;

import data_access.AutoCompleteBST;
import entity.EditorObj;
import use_case.AutoCompleteOperations.AutoCompleteOperations;
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
    public static JScrollPane editorScrollPane;
    public static IDEJTabbedPane editorTabbedPane;

    private JScrollPane terminalScrollPane;
    private AutoCompleteOperations autoCompleteOperations;
    private JScrollPane fileScrollPane;
    private File directory;
    private EditorObj editorObj;
    private FileTreeObj fileTreeObj;
    private JScrollPane currentScrollPane;

    /**
     * Builds the application.
     * @return the JFrame for the application
     */
    public JFrame build() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("IDE Application");
        frame.setSize(WIDTH, HEIGHT);

        //tabManagement.newTab("New Tab");
        frame.setJMenuBar(makeMenuBar());

        frame.add(makeEditorPanel(), BorderLayout.CENTER);
        frame.add(makeFilePanel(), BorderLayout.WEST);
        frame.add(makeTerminalPanel(), BorderLayout.SOUTH);


        JSplitPane leftRightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileScrollPane, editorTabbedPane);
        leftRightSplitPane.setDividerLocation(300);


        JSplitPane topBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, leftRightSplitPane, terminalScrollPane);
        topBottomSplitPane.setDividerLocation(400);

//        initializeAutoComplete(AutoCompleteBST.buildWithJavaKeywords());
        frame.add(topBottomSplitPane, BorderLayout.CENTER);

        frame.setVisible(true);

        return frame;

    }

    public void initializeAutoComplete(AutoCompleteBST autocompleteBST, JTextArea codeEditor) {
        AutoCompletePopup suggestionPopup = new AutoCompletePopup();
        autoCompleteOperations = new AutoCompleteOperations(autocompleteBST);

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
        fileTreeObj = new FileTreeObj();
        fileScrollPane = new JScrollPane(fileTreeObj.getFileTree());
        directory = fileTreeObj.getDirectory();
        return fileScrollPane;
    }

    private JMenuBar makeMenuBar() {
        MenuBarObj menuBarObj = new MenuBarObj(this);
        menuBarObj.buildMenu();
        return menuBarObj.getMenuBar();
    }

    private JScrollPane makeTerminalPanel() {
        TerminalObj terminalWindow = new TerminalObj();
        terminalScrollPane = new JScrollPane(terminalWindow);
        return terminalScrollPane;
    }

    public JTabbedPane makeEditorPanel() {
        // make text area an instance variable with this function and create a getter and reference it for autocomp.
//        editorObj = new EditorObj();
        editorTabbedPane = new IDEJTabbedPane(this);
//        editorScrollPane = new JScrollPane(editorObj.getTextArea());
//        editorScrollPane.setRowHeaderView(editorObj.getLineNums());
//        editorTabbedPane.add("New Tab", editorScrollPane);
        return editorTabbedPane;
    }

    public void openFile(File file) {
        editorTabbedPane.addTab(file);
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

}
