package presentation;

import application.EditorController;
import application.SyntaxHighlighter;
import domain.AutoCompleteService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class IDE {
    private final JFrame frame;
    private final JTextPane editor;
    private final JPopupMenu popup;
    private final EditorController editorController;
    private final JTextArea outputArea;
    private final AutoCompleteService autoCompleteService;

    public IDE() {
        frame = new JFrame("Minimal IDE");
        editor = new JTextPane();
        popup = new JPopupMenu();
        outputArea = new JTextArea();
        editorController = new EditorController();
        autoCompleteService = new AutoCompleteService();

        setupEditor();
        setupFrame();
        loadCodeFromFile();  // Load code from TempProgram.java when the IDE starts
    }

    private void setupEditor() {
        editor.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Set font for better visibility
        SyntaxHighlighter syntaxHighlighter = new SyntaxHighlighter(editor);

        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                syntaxHighlighter.highlight();
                editorController.handleAutoComplete(editor, popup); // Call to handle auto-complete
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                syntaxHighlighter.highlight();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not needed for plain text
            }
        });

        // Add key listener for triggering auto-complete and handling Tab key
//        editor.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_TAB) {
//                    // Check if the auto-complete popup is visible
//                    if (popup.isVisible()) {
//                        // Get the first component in the popup
//                        Component[] items = popup.getComponents();
//                        if (items.length > 0 && items[0] instanceof JMenuItem) {
//                            // Cast the first item to JMenuItem and simulate a click
//                            JMenuItem firstItem = (JMenuItem) items[0];
//                            firstItem.doClick(); // Simulate clicking the first suggestion
//
//                            // Optionally, you can set the text of the editor to the selected suggestion
//                            String selectedText = firstItem.getText();
//                            int caretPosition = editor.getCaretPosition();
//                            try {
//                                // Insert the selected text at the caret position
//                                editor.getDocument().insertString(caretPosition, selectedText, null);
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//
//                            e.consume(); // Prevent further processing of the Tab key
//                        }
//                    }
//                }
//            }
//        });

    }

    private void setupFrame() {
        frame.setLayout(new BorderLayout());

        // Add editor in the center
        frame.add(new JScrollPane(editor), BorderLayout.CENTER);

        // Add output area at the bottom
        outputArea.setEditable(false);
        frame.add(new JScrollPane(outputArea), BorderLayout.SOUTH);
        outputArea.setPreferredSize(new Dimension(800, 200));

        // Add Run button
        JPanel buttonPanel = new JPanel();
        JButton runButton = new JButton("Run");
        runButton.addActionListener(e -> editorController.runCode(editor, outputArea));
        buttonPanel.add(runButton);
        frame.add(buttonPanel, BorderLayout.NORTH);

        // Final settings
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void loadCodeFromFile() {
        File file = new File("Main.java");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder fileContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append("\n");
                }
                editor.setText(fileContent.toString());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error loading code from file: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(IDE::new);
    }
}
