package view;

import use_case.git.CredentialUseCase;
import use_case.git.SecureKeyManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class gitMenuObj {
    public JMenu gitMenu = new JMenu("Git");

    public gitMenuObj() {
        JMenuItem commit = new JMenuItem("Commit");
        JMenuItem push = new JMenuItem("Push");
        JMenuItem pull = new JMenuItem("Pull");
        JMenuItem login = new JMenuItem("login");

        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CredentialUseCase credentialUseCase = new CredentialUseCase();

                JPasswordField pf = new JPasswordField();
                try {
                    SecureKeyManager.saveKey();
                    credentialUseCase.saveCredentials(JOptionPane.showInputDialog("Enter username:"),
                            String.valueOf(JOptionPane.showConfirmDialog(null, pf, "Enter Password",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)), SecureKeyManager.loadKey());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Invalid login");
                    actionPerformed(null);
                }

            }
        });
        gitMenu.add(commit);
        gitMenu.add(push);
        gitMenu.add(pull);
        gitMenu.add(login);
    }
    public JMenu getGitMenu() {
        return gitMenu;
    }
}
