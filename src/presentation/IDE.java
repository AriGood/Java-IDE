package presentation;

import application.EditorController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class IDE extends JFrame {
    private final JTextPane editor;
    private final JTextArea outputArea;
    private final JPopupMenu autoCompletePopup;

    private final EditorController editorController;

    public IDE(EditorController editorController) {
        this.editorController = editorController;
        editor = new JTextPane();
        outputArea = new JTextArea();
        autoCompletePopup = new JPopupMenu();

        setupUI();
    }

    private void setupUI() {
        setTitle("Simple IDE");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        editor.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setEditable(false);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(editor), new JScrollPane(outputArea));
        splitPane.setDividerLocation(400);
        add(splitPane);

        // Set up syntax highlighting
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                deferSyntaxHighlighting();
                showAutoCompletePopup();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                deferSyntaxHighlighting();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                deferSyntaxHighlighting();
            }
        });

        // Add a button to run the code
        JButton runButton = new JButton("Run Code");
        runButton.addActionListener(e -> runCode());
        add(runButton, BorderLayout.SOUTH);

        // Set up key listener for auto-complete selection with Tab
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB && autoCompletePopup.isVisible()) {
                    if (autoCompletePopup.getComponentCount() > 0) {
                        ((JMenuItem) autoCompletePopup.getComponent(0)).doClick();
                    }
                    e.consume();
                }
            }
        });
    }

    private void deferSyntaxHighlighting() {
        SwingUtilities.invokeLater(this::highlightSyntax);
    }

    private void highlightSyntax() {
        editorController.highlightSyntax(editor);
    }

    private void showAutoCompletePopup() {
        editorController.handleAutoComplete(editor, autoCompletePopup);
    }

    private void runCode() {
        editorController.runCode(editor, outputArea);
    }
}
