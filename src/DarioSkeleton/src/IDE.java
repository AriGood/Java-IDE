package DarioSkeleton.src;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class IDE extends JFrame {
    private JScrollPane terminalScrollPane;
    private JScrollPane editorScrollPane;
    private JScrollPane fileScrollPane;
    private JTextArea lineNumbersPane;


    public IDE() {
        setTitle("CSC207 IDE");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        makeMenuBar();

        makeFilePanel();

        makeEditorPanel();

        makeTerminalPanel();

        splitPlanes();

        setVisible(true);
    }

    private void makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New File");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void splitPlanes() {

        JSplitPane leftRightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.fileScrollPane, this.editorScrollPane);
        leftRightSplitPane.setDividerLocation(300);


        JSplitPane topBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, leftRightSplitPane, this.terminalScrollPane);
        topBottomSplitPane.setDividerLocation(400);


        add(topBottomSplitPane, BorderLayout.CENTER);
    }

    private void makeTerminalPanel() {
        JTextArea terminal = new JTextArea();
        this.terminalScrollPane = new JScrollPane(terminal);
        add(this.terminalScrollPane, BorderLayout.SOUTH);
    }

    private void makeEditorPanel() {
        JTextArea codeEditor = new JTextArea();
        this.editorScrollPane = new JScrollPane(codeEditor);
        this.lineNumbersPane = new JTextArea("1\n");
        this.lineNumbersPane.setEditable(false);
        this.lineNumbersPane.setBackground(Color.LIGHT_GRAY);
        editorScrollPane.setRowHeaderView(this.lineNumbersPane);
        add(this.editorScrollPane, BorderLayout.CENTER);
    }

    private void makeFilePanel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Project");

        DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("src");
        DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(".idea");

        DefaultMutableTreeNode grandChild = new DefaultMutableTreeNode("Main");
        DefaultMutableTreeNode grandChild2 = new DefaultMutableTreeNode("Test");

        root.add(child1);
        root.add(child2);

        child1.add(grandChild);
        child1.add(grandChild2);

        JTree fileTree = new JTree(root);
        this.fileScrollPane = new JScrollPane(fileTree);
        add(this.fileScrollPane, BorderLayout.WEST);
    }
}