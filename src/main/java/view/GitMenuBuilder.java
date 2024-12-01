package view;

import app.IDEAppBuilder;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoRemoteRepositoryException;
import use_case.git.CredentialUseCase;
import use_case.git.SecureKeyManager;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.security.KeyException;

public class GitMenuBuilder {
    private JMenu gitMenu;

    /**
     * Initializes the builder for the Git menu.
     */
    public GitMenuBuilder() {
        this.gitMenu = new JMenu("Git");
    }

    /**
     * Adds the "Commit" action to the Git menu.
     *
     * @return The current instance of the builder.
     */
    public GitMenuBuilder addCommitAction() {
        JMenuItem commit = new JMenuItem("Commit");
        commit.addActionListener(e -> {
            try {
                String message = JOptionPane.showInputDialog(null, "Enter commit message:");
                if (message != null && !message.isEmpty()) {
                    IDEAppBuilder.gitManager.commitChanges(message);
                    JOptionPane.showMessageDialog(null, "Changes committed successfully.");
                }
            } catch (GitAPIException ex) {
                warningNoGit();
            }
        });
        gitMenu.add(commit);
        return this;
    }

    /**
     * Adds the "Push" action to the Git menu.
     *
     * @return The current instance of the builder.
     */
    public GitMenuBuilder addPushAction() {
        JMenuItem push = new JMenuItem("Push");
        push.addActionListener(e -> {
            try {
                IDEAppBuilder.gitManager.pushChanges(getLogin());
                JOptionPane.showMessageDialog(null, "Changes pushed successfully.");
            } catch (GitAPIException ex) {
                warningNoGit();
            } catch (KeyException | LoginException | NoRemoteRepositoryException ex) {
                warningNoLogin();
            }
        });
        gitMenu.add(push);
        return this;
    }

    /**
     * Adds the "Pull" action to the Git menu.
     *
     * @return The current instance of the builder.
     */
    public GitMenuBuilder addPullAction() {
        JMenuItem pull = new JMenuItem("Pull");
        pull.addActionListener(e -> {
            try {
                IDEAppBuilder.gitManager.getCurrentRepository().pull().call();
                JOptionPane.showMessageDialog(null, "Repository pulled successfully.");
            } catch (GitAPIException ex) {
                warningNoGit();
            }
        });
        gitMenu.add(pull);
        return this;
    }

    /**
     * Adds the "Login" action to the Git menu.
     *
     * @return The current instance of the builder.
     */
    public GitMenuBuilder addLoginAction() {
        JMenuItem login = new JMenuItem("Login");
        login.addActionListener(e -> handleLogin());
        gitMenu.add(login);
        return this;
    }

    /**
     * Adds the "Create Branch" action to the Git menu.
     *
     * @return The current instance of the builder.
     */
    public GitMenuBuilder addCreateBranchAction() {
        JMenuItem createBranch = new JMenuItem("Create Branch");
        createBranch.addActionListener(e -> handleBranchCreation());
        gitMenu.add(createBranch);
        return this;
    }

    /**
     * Adds the "Checkout Branch" action to the Git menu.
     *
     * @return The current instance of the builder.
     */
    public GitMenuBuilder addCheckoutBranchAction() {
        JMenuItem checkoutBranch = new JMenuItem("Checkout Branch");
        checkoutBranch.addActionListener(e -> handleBranchCheckout());
        gitMenu.add(checkoutBranch);
        return this;
    }

    /**
     * Adds the "Set Remote URL" action to the Git menu.
     *
     * @return The current instance of the builder.
     */
    public GitMenuBuilder addSetRemoteAction() {
        JMenuItem setRemote = new JMenuItem("Set Remote URL");
        setRemote.addActionListener(e -> handleSetRemoteUrl());
        gitMenu.add(setRemote);
        return this;
    }

    /**
     * Adds the "Clone Repository" action to the Git menu.
     *
     * @return The current instance of the builder.
     */
    public GitMenuBuilder addCloneRepositoryAction() {
        JMenuItem cloneRepository = new JMenuItem("Clone Repository");
        cloneRepository.addActionListener(e -> handleCloneRepository());
        gitMenu.add(cloneRepository);
        return this;
    }

    /**
     * Adds the "Merge Branch" action to the Git menu.
     *
     * @return The current instance of the builder.
     */
    public GitMenuBuilder addMergeBranchAction() {
        JMenuItem mergeBranch = new JMenuItem("Merge Branch");
        mergeBranch.addActionListener(e -> handleMergeBranch());
        gitMenu.add(mergeBranch);
        return this;
    }

    /**
     * Finalizes the Git menu construction.
     *
     * @return The constructed Git menu.
     */
    public JMenu build() {
        return gitMenu;
    }

    // Helper Methods

    private String[] getLogin() throws KeyException, LoginException {
        return (CredentialUseCase.loadCredentials(SecureKeyManager.loadKey()));
    }

    private void warningNoGit() {
        JOptionPane.showMessageDialog(null, "Warning: No Git repository open.");
    }

    private void warningNoLogin() {
        JOptionPane.showMessageDialog(null, "Warning: Not logged in.");
    }

    private void handleLogin() {
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

    private void handleBranchCreation() {
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

    private void handleBranchCheckout() {
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

    private void handleSetRemoteUrl() {
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

    private void handleCloneRepository() {
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

    private void handleMergeBranch() {
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
}
