package view;

import app.IDEAppBuilder;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoRemoteRepositoryException;
import use_case.git.CredentialUseCase;
import use_case.git.SecureKeyManager;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.KeyException;

public class gitMenuObj {
    public JMenu gitMenu = new JMenu("Git");

    public gitMenuObj() {
        JMenuItem commit = new JMenuItem("Commit");
        JMenuItem push = new JMenuItem("Push");
        JMenuItem pull = new JMenuItem("Pull");
        JMenuItem login = new JMenuItem("Login");
        JMenuItem createBranch = new JMenuItem("Create Branch");
        JMenuItem checkoutBranch = new JMenuItem("Checkout Branch");
        JMenuItem setRemote = new JMenuItem("Set Remote URL");
        JMenuItem cloneRepository = new JMenuItem("Clone Repository");
        JMenuItem mergeBranch = new JMenuItem("Merge Branch");

        // Commit action
        commit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String message = JOptionPane.showInputDialog(null, "Enter commit message:");
                    if (message != null && !message.isEmpty()) {
                        IDEAppBuilder.gitManager.commitChanges(message);
                        JOptionPane.showMessageDialog(null, "Changes committed successfully.");
                    }
                } catch (GitAPIException ex) {
                    warningNoGit();
                }
            }
        });

        // Push action
        push.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    IDEAppBuilder.gitManager.pushChanges(getLogin());
                    JOptionPane.showMessageDialog(null, "Changes pushed successfully.");
                } catch (GitAPIException ex) {
                    warningNoGit();
                } catch (KeyException | LoginException | NoRemoteRepositoryException ex) {
                    warningNoLogin();
                }
            }
        });

        // Pull action
        pull.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    IDEAppBuilder.gitManager.getCurrentRepository().pull().call();
                    JOptionPane.showMessageDialog(null, "Repository pulled successfully.");
                } catch (GitAPIException ex) {
                    warningNoGit();
                }
            }
        });

        // Login action
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CredentialUseCase credentialUseCase = new CredentialUseCase();
                JPasswordField pf = new JPasswordField();
                try {
                    SecureKeyManager.saveKey();
                    String username = JOptionPane.showInputDialog("Enter username:");
                    if (username != null && !username.isEmpty()) {
                        int passwordOption = JOptionPane.showConfirmDialog(null, pf, "Enter Password",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (passwordOption == JOptionPane.OK_OPTION) {
                            credentialUseCase.saveCredentials(username, new String(pf.getPassword()),
                                    SecureKeyManager.loadKey());
                            JOptionPane.showMessageDialog(null, "Login credentials saved successfully.");
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid login. Please try again.");
                }
            }
        });

        // Create Branch action
        createBranch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String branchName = JOptionPane.showInputDialog(null, "Enter new branch name:");
                    if (branchName != null && !branchName.isEmpty()) {
                        IDEAppBuilder.gitManager.createBranch(branchName);
                        JOptionPane.showMessageDialog(null, "Branch created successfully.");
                    }
                } catch (GitAPIException ex) {
                    warningNoGit();
                }
            }
        });

        // Checkout Branch action
        checkoutBranch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String branchName = JOptionPane.showInputDialog(null, "Enter branch name to checkout:");
                    if (branchName != null && !branchName.isEmpty()) {
                        IDEAppBuilder.gitManager.checkoutBranch(branchName);
                        JOptionPane.showMessageDialog(null, "Switched to branch: " + branchName);
                    }
                } catch (GitAPIException ex) {
                    warningNoGit();
                }
            }
        });

        // Set Remote URL action
        setRemote.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String remoteUrl = JOptionPane.showInputDialog(null, "Enter remote URL:");
                    if (remoteUrl != null && !remoteUrl.isEmpty()) {
                        IDEAppBuilder.gitManager.setRemoteUrl(remoteUrl);
                        JOptionPane.showMessageDialog(null, "Remote URL set successfully.");
                    }
                } catch (Exception ex) {
                    warningNoGit();
                }
            }
        });

        // Clone Repository action with directory chooser
        cloneRepository.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String repoUrl = JOptionPane.showInputDialog(null, "Enter repository URL to clone:");
                    if (repoUrl != null && !repoUrl.isEmpty()) {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setDialogTitle("Select Directory to Clone Into");
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int returnValue = chooser.showOpenDialog(null);
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            File selectedDirectory = chooser.getSelectedFile();
                            IDEAppBuilder.gitManager.cloneRepository(repoUrl, selectedDirectory.getAbsolutePath());
                            JOptionPane.showMessageDialog(null, "Repository cloned successfully.");
                        }
                    }
                } catch (GitAPIException ex) {
                    JOptionPane.showMessageDialog(null, "Error cloning repository: " + ex.getMessage());
                }
            }
        });

        // Merge Branch action
        mergeBranch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String branchName = JOptionPane.showInputDialog(null, "Enter branch name to merge into current branch:");
                    if (branchName != null && !branchName.isEmpty()) {
                        IDEAppBuilder.gitManager.mergeBranch(branchName);
                        JOptionPane.showMessageDialog(null, "Branch merged successfully.");
                    }
                } catch (GitAPIException ex) {
                    warningNoGit();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error during merge: " + ex.getMessage());
                }
            }
        });

        // Adding menu items to Git menu
        gitMenu.add(commit);
        gitMenu.add(push);
        gitMenu.add(pull);
        gitMenu.add(login);
        gitMenu.add(createBranch);
        gitMenu.add(checkoutBranch);
        gitMenu.add(setRemote);
        gitMenu.add(cloneRepository);
        gitMenu.add(mergeBranch);
    }

    private String[] getLogin() throws KeyException, LoginException {
        return (CredentialUseCase.loadCredentials(SecureKeyManager.loadKey()));
    }

    private void warningNoGit() {
        JOptionPane.showMessageDialog(null, "Warning: No Git repository open.");
    }

    private void warningNoLogin() {
        JOptionPane.showMessageDialog(null, "Warning: Not logged in.");
    }

    public JMenu getGitMenu() {
        return gitMenu;
    }
}
