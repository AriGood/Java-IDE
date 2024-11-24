package view;

import use_case.TerminalOperations.TerminalOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class TerminalObj extends JPanel {
    private JTextArea outputArea;
    private JTextField inputField;
    private List<String> commandHistory;
    private int historyIndex;
    private TerminalOperations terminalOperations;

    public TerminalObj() {
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        inputField = new JTextField();
        inputField.setBackground(new Color(169, 169, 169));
        inputField.setForeground(Color.BLACK);
        commandHistory = new ArrayList<>();
        historyIndex = -1;

        terminalOperations = new TerminalOperations();

        // Listener for command history
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (historyIndex > 0) {
                        historyIndex--;
                        inputField.setText(commandHistory.get(historyIndex));
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (historyIndex < commandHistory.size() - 1) {
                        historyIndex++;
                        inputField.setText(commandHistory.get(historyIndex));
                    } else {
                        inputField.setText("");
                        historyIndex = commandHistory.size();
                    }
                }
            }
        });

        inputField.addActionListener(e -> {
            String command = inputField.getText();
            if (!command.trim().isEmpty()) {
                commandHistory.add(command);
                historyIndex = commandHistory.size();

                terminalOperations.executeCommand(command, new TerminalOperations.CommandCallback() {
                    @Override
                    public void onOutput(String output) {
                        outputArea.append(output);
                        outputArea.setCaretPosition(outputArea.getDocument().getLength());
                    }

                    @Override
                    public void onError(String error) {
                        outputArea.append(error);
                        outputArea.setCaretPosition(outputArea.getDocument().getLength());
                    }
                });
            }
            inputField.setText("");
        });

        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
    }
}
