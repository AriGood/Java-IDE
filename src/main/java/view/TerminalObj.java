package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TerminalObj extends JPanel {
    private JTextArea outputArea;
    private JTextField inputField;
    private List<String> commandHistory;
    private int historyIndex;

    public TerminalObj() {
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        inputField = new JTextField();
        commandHistory = new ArrayList<>();
        historyIndex = -1;

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

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText();
                if (!command.trim().isEmpty()) {
                    executeCommand(command);
                    commandHistory.add(command);
                    historyIndex = commandHistory.size();
                }
                inputField.setText("");
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
    }

    private void executeCommand(String command) {
        if (command.equalsIgnoreCase("clear")) {
            outputArea.setText("");
            return;
        }

        outputArea.append("> " + command + "\n");

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("bash", "-c", command);
            Process process = builder.start();

            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = outputReader.readLine()) != null) {
                outputArea.append(line + "\n");
            }

            while ((line = errorReader.readLine()) != null) {
                outputArea.append("Error: " + line + "\n");
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            outputArea.append("Error executing command: " + e.getMessage() + "\n");
        }

        outputArea.append("\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
}
