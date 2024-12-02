package app;

import data.access.AutoCompleteBst;
import entity.EditorObj;
import entity.LeftIDEJTabbedPane;
import entity.ParentIDEJTabbedPane;
import entity.RightIDEJTabbedPane;
import use_case.autocompleteoperations.AutoCompleteOperations;
import use_case.EditorOperations.EditorOperations;
import use_case.FileManagement.FileOperations;
import use_case.FileManagement.FileTreeGenerator;
import use_case.git.GitManager;
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
    public final int HEIGHT = 600;
    public final int WIDTH = 800;
    public JScrollPane editorScrollPane;
    public GitManager gitManager = new GitManager();
    public File directory;

    private JScrollPane terminalScrollPane;
    private AutoCompleteOperations autoCompleteOperations;
    private JScrollPane fileScrollPane;
    private EditorObj editorObj;
    private FileTreeObj fileTreeObj;
    private FileTreeGenerator fileTreeGenerator;
    private JFrame frame;
    private JSplitPane leftRightSplitPane;

    private RightIDEJTabbedPane rightEditorTabbedPane;
    private LeftIDEJTabbedPane leftEditorTabbedPane;

    /**
     * Builds the application.
     * @return the JFrame for the application
     */
    public JFrame build() {
        final JFrame newFrame = new JFrame();
        frame = newFrame;
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("IDE Application");
        frame.setSize(WIDTH, HEIGHT);

        frame.setJMenuBar(makeMenuBar());
        frame.add(makeFilePanel(), BorderLayout.CENTER);

        frame.setVisible(true);
        return frame;
    }

    public void buildIDE() {
        frame.add(makeEditorPanel(), BorderLayout.CENTER);

        frame.add(makeTerminalPanel(), BorderLayout.SOUTH);


        leftRightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileScrollPane, leftEditorTabbedPane);
        leftRightSplitPane.setDividerLocation(300);


        JSplitPane topBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, leftRightSplitPane, terminalScrollPane);
        topBottomSplitPane.setDividerLocation(400);

        frame.add(topBottomSplitPane, BorderLayout.CENTER);
        frame.revalidate();
    }

    public void initializeAutoComplete(AutoCompleteBst autocompleteBST, JTextArea codeEditor) {
        AutoCompletePopup suggestionPopup = new AutoCompletePopup();
        autoCompleteOperations = new AutoCompleteOperations(autocompleteBST);

        codeEditor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    List<String> suggestions = autoCompleteOperations.getSuggestions(codeEditor);
//                    suggestionPopup.showSuggestions(codeEditor, suggestions);
                    suggestionPopup.showSuggestions(codeEditor, suggestions, (textComponent, suggestion) -> {
                        autoCompleteOperations.applySuggestion(textComponent, suggestion);
                    });
                });
            }
        });
    }


    private JScrollPane makeFilePanel() {
        fileScrollPane = new JScrollPane();
        JLabel messageLabel = new JLabel("Select A Project Directory To Get Started", JLabel.CENTER);
        fileScrollPane.setViewportView(messageLabel);
        return fileScrollPane;
    }

    public void buildTree(File directory) {
        directory = directory.getAbsoluteFile();
        fileTreeObj = new FileTreeObj(directory, this);
        fileScrollPane.setViewportView(fileTreeObj.getFileTree());
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
        leftEditorTabbedPane = new LeftIDEJTabbedPane(this);
//        editorScrollPane = new JScrollPane(editorObj.getTextArea());
//        editorScrollPane.setRowHeaderView(editorObj.getLineNums());
//        editorTabbedPane.add("New Tab", editorScrollPane);
        return leftEditorTabbedPane;
    }


    public void openFile(File file) {
        if (file != null && file.exists() && file.isFile()) {
            if (rightEditorTabbedPane == null ||!EditorOperations.isDuplicate(file, rightEditorTabbedPane)) {
                EditorOperations.addTab(file, leftEditorTabbedPane);
            }
        } else {
            System.err.println("Invalid file: " + (file != null ? file.getAbsolutePath() : "null"));
        }
    }


//    public void initializeAutoComplete(AutoCompleteBST autocompleteBST) {
//        JPopupMenu popup = new JPopupMenu();
//        autoCompleteOperations = new AutoCompleteOperations(autocompleteBST);
//        autoCompleteOperations.enableAutoComplete(tabManagement,codeEditor, popup);
//    }


    public File getDirectory() {
        return directory;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JScrollPane getTerminalScrollPane() {
        return terminalScrollPane;
    }

    public JScrollPane getEditorScrollPane() {
        return editorScrollPane;
    }

    public void splitEditor(JSplitPane newSplit) {
        leftRightSplitPane.setRightComponent(newSplit);
        Component rightTabbedComponent = newSplit.getRightComponent();
        Component leftTabbedComponent = newSplit.getLeftComponent();
        if (rightTabbedComponent instanceof RightIDEJTabbedPane) {
            rightEditorTabbedPane = (RightIDEJTabbedPane) rightTabbedComponent;
        }
        if (leftTabbedComponent instanceof LeftIDEJTabbedPane) {
            leftEditorTabbedPane = (LeftIDEJTabbedPane) leftTabbedComponent;
        }
        frame.revalidate();
        frame.repaint();
    }

    // CHANGED: Added handleFileDeletion to manage file deletion and associated tabs

    public void handleFileDeletion(File file) {
        EditorOperations.closeAbstractTab(this, file); // Close associated tabs
    }


    public RightIDEJTabbedPane getRightEditorTabbedPane() {
        return rightEditorTabbedPane;
    }

    public LeftIDEJTabbedPane getLeftEditorTabbedPane() {
        return leftEditorTabbedPane;
    }


    public JSplitPane getLeftRightSplitPane() {
        return leftRightSplitPane;
    }

    public void setRightEditorTabbedPane(RightIDEJTabbedPane newRightPane) {
        rightEditorTabbedPane = newRightPane;
    }
}
