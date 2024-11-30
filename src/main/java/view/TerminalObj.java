package view;

import use_case.TerminalOperations.TerminalOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class TerminalObj extends JPanel {
    private final JTextArea outputArea;
    private final JTextField inputField;
    private final List<String> commandHistory;
    private int historyIndex;
    private final TerminalOperations terminalOperations;

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

        setupInputFieldListeners();

        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
    }

    private void setupInputFieldListeners() {
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP && historyIndex > 0) {
                    historyIndex--;
                    inputField.setText(commandHistory.get(historyIndex));
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

        inputField.addActionListener(e -> executeCommand(inputField.getText().trim()));
    }

    private void executeCommand(String command) {
        if (command.isEmpty()) return;

        commandHistory.add(command);
        historyIndex = commandHistory.size();

        terminalOperations.executeCommand(command, new TerminalOperations.CommandCallback() {
            @Override
            public void onOutput(String output) {
                handleOutput(command, output);
            }

            @Override
            public void onError(String error) {
                handleOutput(command, error);
            }
        });

        inputField.setText("");
    }

    private void handleOutput(String command, String output) {
        if (command.equalsIgnoreCase("clear")) {
            outputArea.setText("");
        } else {
            outputArea.append(output);
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        }
    }
}

