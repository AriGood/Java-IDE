package view;

import use_case.git.CredentialUseCase;

import javax.swing.*;

public class CredentialUI {
    private final CredentialUseCase credentialUseCase;

    public CredentialUI(CredentialUseCase credentialUseCase) {
        this.credentialUseCase = credentialUseCase;
    }

    public void displayUI() {
        JFrame frame = new JFrame("Secure Credential Manager");
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JTextField searchField = new JTextField(10);
        JTextArea textArea = new JTextArea(10, 30);
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        saveButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            try {
                credentialUseCase.saveCredentials(username, password);
                JOptionPane.showMessageDialog(frame, "Credentials saved securely.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error saving credentials.");
            }
        });

        loadButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            try {
                String[] credentials = credentialUseCase.loadCredentials(password);
                usernameField.setText(credentials[0]);
                JOptionPane.showMessageDialog(frame, "Credentials loaded successfully.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error loading credentials.");
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(saveButton);
        panel.add(loadButton);

        frame.add(new JScrollPane(textArea), "Center");
        frame.add(panel, "South");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
