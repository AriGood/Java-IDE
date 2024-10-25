package presentation;

import application.EditorController;

import javax.swing.*;
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

    public IDE() {
        frame = new JFrame("Minimal IDE");
        editor = new JTextPane();
        popup = new JPopupMenu();
        outputArea = new JTextArea();
        editorController = new EditorController();

        setupEditor();
        setupFrame();
        loadCodeFromFile();  // Load code from TempProgram.java when the IDE starts
    }

    private void setupEditor() {
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (Character.isLetterOrDigit(e.getKeyChar())) {
                    editorController.handleAutoComplete(editor, popup);
                }
            }
        });
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
