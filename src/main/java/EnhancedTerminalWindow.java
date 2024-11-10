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

public class EnhancedTerminalWindow extends JFrame {
    private JTextArea outputArea;
    private JTextField inputField;
    private List<String> commandHistory;
    private int historyIndex;

    public EnhancedTerminalWindow() {
        setTitle("IDE Terminal");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        inputField = new JTextField();
        commandHistory = new ArrayList<>();
        historyIndex = -1;

        // Key listener for command history
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

        outputArea.append("\n");  // Adds space between command results for readability
        outputArea.setCaretPosition(outputArea.getDocument().getLength());  // Auto-scroll to the bottom
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EnhancedTerminalWindow terminal = new EnhancedTerminalWindow();
            terminal.setVisible(true);
        });
    }
}
