package app;

import data_access.AutoCompleteBST;
import use_case.AutoCompleteOperations.AutoCompleteOperations;
import data_access.AutoCompleteBST;

import javax.swing.*;

import java.awt.*;
import java.io.File;

/**
 * Builder for the Note Application.
 */
public class IDEAppBuilder {
    public static final int HEIGHT = 600;
    public static final int WIDTH = 800;

    private JScrollPane terminalScrollPane;
    private JTextArea codeEditor;
    private AutoCompleteOperations autoCompleteOperations;
    private JScrollPane editorScrollPane;
    private JScrollPane fileScrollPane;
    private JTextArea lineNumbersPane;
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
        chooseDiretory();

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

    private JScrollPane makeFilePanel() {
        use_case.FileManagement.FileTreeGenerator fileTreeGenerator = new use_case.FileManagement.FileTreeGenerator(directory);
        fileScrollPane = new JScrollPane(fileTreeGenerator.createFileTree(directory));
        return fileScrollPane;
    }

    private JMenuBar makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New File");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private JScrollPane makeTerminalPanel() {
        JTextArea terminal = new JTextArea();
        terminalScrollPane = new JScrollPane(terminal);
        return terminalScrollPane;
    }

    private JScrollPane makeEditorPanel() {
        // make text area an instance variable with this function and create a getter and reference it for autocomp.

        JTextArea codeEditor = new JTextArea();
        editorScrollPane = new JScrollPane(codeEditor);
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 1; i <= 500; i++) {
            strBuilder.append(i).append("\n");
        }
        lineNumbersPane = new JTextArea(strBuilder.toString());
        lineNumbersPane.setEditable(false);
        lineNumbersPane.setBackground(Color.LIGHT_GRAY);
        editorScrollPane.setRowHeaderView(lineNumbersPane);
        return editorScrollPane;
    }

    public void initializeAutoComplete(AutoCompleteBST autocompleteBST) {
        JPopupMenu popup = new JPopupMenu();
        autoCompleteOperations = new AutoCompleteOperations(autocompleteBST);
        autoCompleteOperations.enableAutoComplete(codeEditor, popup);
    }

    public JTextArea getCodeEditor() {
        return codeEditor;
    }

    public void chooseDiretory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Project to Open");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            directory = fileChooser.getSelectedFile();
        }
    }

    public File getDirectory() {
        return directory;
    }

    public JScrollPane getTerminalScrollPane() {
        return terminalScrollPane;
    }

    public JScrollPane getEditorScrollPane() {
        editorScrollPane.setRowHeaderView(lineNumbersPane);
        return editorScrollPane;
    }

}
