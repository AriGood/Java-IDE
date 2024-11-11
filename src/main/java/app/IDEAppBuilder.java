package app;

import entity.Tab;

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
    public ArrayList<Tab> tabs = null;

    private JScrollPane terminalScrollPane;
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
