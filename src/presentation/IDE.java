package presentation;

import application.EditorController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IDE extends JFrame {
    private JTextPane editor;
    private JTextArea outputArea;
    private JPopupMenu autoCompletePopup;
    private final EditorController editorController;

    // Timer for debounce
    private Timer debounceTimer;

    public IDE() {
        editorController = new EditorController(); // Initialize the editor controller
        setupUI();
    }

    private void setupUI() {
        setTitle("Simple IDE");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        editor = new JTextPane();
        outputArea = new JTextArea();
        autoCompletePopup = new JPopupMenu();

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
                resetDebounceTimer();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                resetDebounceTimer();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                resetDebounceTimer();
            }
        });

        // Add a button to run the code
        JButton runButton = new JButton("Run Code");
        runButton.addActionListener(e -> runCode());
        add(runButton, BorderLayout.SOUTH);

        // Initialize the debounce timer
        debounceTimer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Execute highlighting in a separate thread to avoid locking the UI
                SwingUtilities.invokeLater(() -> {
                    highlightSyntax();
                    showAutoCompletePopup();
                });
            }
        });
        debounceTimer.setRepeats(false); // Only execute once per timer reset
    }

    private void resetDebounceTimer() {
        if (debounceTimer.isRunning()) {
            debounceTimer.restart(); // Restart the timer if already running
        } else {
            debounceTimer.start(); // Start the timer if not running
        }
    }

    private void highlightSyntax() {
        // Check if the document is already being modified to avoid IllegalStateException
        try {
            editorController.highlightSyntax(editor);
        } catch (IllegalStateException e) {
            // Handle the case where modifications are attempted during notification
            e.printStackTrace();
        }
    }

    private void showAutoCompletePopup() {
        if (autoCompletePopup.isVisible()) {
            autoCompletePopup.setVisible(false);
        }

        String text = editor.getText();
        // Here, you can implement logic to get suggestions based on the current input
        editorController.handleAutoComplete(editor, autoCompletePopup);
    }

    private void runCode() {
        editorController.runCode(editor, outputArea);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IDE ide = new IDE();
            ide.setVisible(true);
        });
    }
}
