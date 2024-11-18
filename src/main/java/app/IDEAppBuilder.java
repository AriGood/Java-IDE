package app;

import data_access.AutoCompleteBST;
import use_case.AutoCompleteOperations.AutoCompleteOperations;
import data_access.AutoCompleteBST;
import use_case.FileManagement.TabManagement;
import view.*;

import javax.swing.*;

import java.util.List;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private AutoCompleteOperations autoCompleteOperations;
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

    // TODO: move this to DAO folder
    private AutoCompleteBST BSTBuilder() {
        // Initialize autocomplete BST and add Java keywords
        AutoCompleteBST autocompleteBST = new AutoCompleteBST();
        autocompleteBST.insert("abstract");
        autocompleteBST.insert("assert");
        autocompleteBST.insert("boolean");
        autocompleteBST.insert("break");
        autocompleteBST.insert("byte");
        autocompleteBST.insert("case");
        autocompleteBST.insert("catch");
        autocompleteBST.insert("char");
        autocompleteBST.insert("class");
        autocompleteBST.insert("continue");
        autocompleteBST.insert("default");
        autocompleteBST.insert("do");
        autocompleteBST.insert("double");
        autocompleteBST.insert("else");
        autocompleteBST.insert("enum");
        autocompleteBST.insert("extends");
        autocompleteBST.insert("final");
        autocompleteBST.insert("finally");
        autocompleteBST.insert("float");
        autocompleteBST.insert("for");
        autocompleteBST.insert("goto");
        autocompleteBST.insert("if");
        autocompleteBST.insert("implements");
        autocompleteBST.insert("import");
        autocompleteBST.insert("instanceof");
        autocompleteBST.insert("int");
        autocompleteBST.insert("interface");
        autocompleteBST.insert("long");
        autocompleteBST.insert("native");
        autocompleteBST.insert("new");
        autocompleteBST.insert("package");
        autocompleteBST.insert("private");
        autocompleteBST.insert("protected");
        autocompleteBST.insert("public");
        autocompleteBST.insert("return");
        autocompleteBST.insert("short");
        autocompleteBST.insert("static");
        autocompleteBST.insert("strictfp");
        autocompleteBST.insert("super");
        autocompleteBST.insert("switch");
        autocompleteBST.insert("synchronized");
        autocompleteBST.insert("transient");
        autocompleteBST.insert("volatile");
        autocompleteBST.insert("while");
        autocompleteBST.insert("volatile");

        return autocompleteBST;
    }

    public void initializeAutoComplete(AutoCompleteBST autocompleteBST) {
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


//    public void initializeAutoComplete(AutoCompleteBST autocompleteBST) {
//        JPopupMenu popup = new JPopupMenu();
//        autoCompleteOperations = new AutoCompleteOperations(autocompleteBST);
//        autoCompleteOperations.enableAutoComplete(tabManagement,codeEditor, popup);
//    }

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
