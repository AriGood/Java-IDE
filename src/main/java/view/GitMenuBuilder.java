package view;

import app.IDEAppBuilder;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoRemoteRepositoryException;
import use_case.git.CredentialUseCase;
import use_case.git.SecureKeyManager;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.security.KeyException;

public class GitMenuBuilder {
    private final JMenu gitMenu;
    private final IDEAppBuilder IDEAppBuilder;


    /**
     * Initializes the builder for the Git menu.
     */
    public GitMenuBuilder(IDEAppBuilder newIDEAppBuilder) {
        this.IDEAppBuilder = newIDEAppBuilder;

        this.gitMenu = new JMenu("Git");

        // Add all actions to the menu
        addCommitAction();
        addPushAction();
        addPullAction();
        addLoginAction();
        addCreateBranchAction();
        addCheckoutBranchAction();
        addSetRemoteAction();
        addCloneRepositoryAction();
        addMergeBranchAction();
        addOpenExistingRepositoryAction();
    }

    /**
     * Adds the "Commit" action to the Git menu.
     */
    public void addCommitAction() {
        JMenuItem commit = new JMenuItem("Commit");
        commit.addActionListener(e -> {
            ensureRepositoryExists();
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
    }

    /**
     * Adds the "Push" action to the Git menu.
     */
    public void addPushAction() {
        JMenuItem push = new JMenuItem("Push");
        push.addActionListener(e -> {
            ensureRepositoryExists();
            try {
                IDEAppBuilder.gitManager.pushChanges(getLogin());
                JOptionPane.showMessageDialog(null, "Changes pushed successfully.");
            } catch (KeyException | LoginException | NoRemoteRepositoryException | GitAPIException ex) {
                JOptionPane.showMessageDialog(null, "Cannot push changes" + ex.getMessage());
            }
        });
        gitMenu.add(push);
    }

    /**
     * Adds the "Pull" action to the Git menu.
     */
    public void addPullAction() {
        JMenuItem pull = new JMenuItem("Pull");
        pull.addActionListener(e -> {
            ensureRepositoryExists();
            try {
                IDEAppBuilder.gitManager.getCurrentRepository().pull().call();
                JOptionPane.showMessageDialog(null, "Repository pulled successfully.");
            } catch (GitAPIException ex) {
                warningNoGit();
            }
        });
        gitMenu.add(pull);
    }

    /**
     * Adds the "Open Existing Repository" action to the Git menu.
     */
    public void addOpenExistingRepositoryAction() {
        JMenuItem openExistingRepo = new JMenuItem("Open Existing Repository");
        openExistingRepo.addActionListener(e -> handleOpenExistingRepository());
        gitMenu.add(openExistingRepo);
    }

    /**
     * Adds the "Login" action to the Git menu.
     */
    public void addLoginAction() {
        JMenuItem login = new JMenuItem("Login");
        login.addActionListener(e -> handleLogin());
        gitMenu.add(login);
    }

    /**
     * Adds the "Create Branch" action to the Git menu.
     */
    public void addCreateBranchAction() {
        JMenuItem createBranch = new JMenuItem("Create Branch");
        createBranch.addActionListener(e -> handleBranchCreation());
        gitMenu.add(createBranch);
    }

    /**
     * Adds the "Checkout Branch" action to the Git menu.
     */
    public void addCheckoutBranchAction() {
        JMenuItem checkoutBranch = new JMenuItem("Checkout Branch");
        checkoutBranch.addActionListener(e -> handleBranchCheckout());
        gitMenu.add(checkoutBranch);
    }

    /**
     * Adds the "Set Remote URL" action to the Git menu.
     */
    public void addSetRemoteAction() {
        JMenuItem setRemote = new JMenuItem("Set Remote URL");
        setRemote.addActionListener(e -> handleSetRemoteUrl());
        gitMenu.add(setRemote);
    }

    /**
     * Adds the "Clone Repository" action to the Git menu.
     */
    public void addCloneRepositoryAction() {
        JMenuItem cloneRepository = new JMenuItem("Clone Repository");
        cloneRepository.addActionListener(e -> handleCloneRepository());
        gitMenu.add(cloneRepository);
    }

    /**
     * Adds the "Merge Branch" action to the Git menu.
     */
    public void addMergeBranchAction() {
        JMenuItem mergeBranch = new JMenuItem("Merge Branch");
        mergeBranch.addActionListener(e -> handleMergeBranch());
        gitMenu.add(mergeBranch);
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

    private void ensureRepositoryExists() {
        if (IDEAppBuilder.gitManager.getCurrentRepository() == null) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "No repository found. Would you like to create a new one?",
                    "Create Repository", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                File selectedDirectory = null;
                int choice_1 = JOptionPane.showConfirmDialog(null,
                        "Would you like to make the current directory a git repository?",
                        "current directory", JOptionPane.YES_NO_OPTION);
                if (choice_1 == JOptionPane.YES_OPTION) {
                    selectedDirectory = IDEAppBuilder.directory;
                }else {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("Select Directory for New Repository");
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnValue = chooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        selectedDirectory = chooser.getSelectedFile();
                    }
                }
                try {
                    assert selectedDirectory != null;
                    IDEAppBuilder.gitManager.createRepository(JOptionPane.showInputDialog(null, "Enter remote URL:")
                            ,selectedDirectory.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "New repository created successfully.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error creating repository: " + ex.getMessage());
                }
            }
        }
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
        ensureRepositoryExists();
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
        ensureRepositoryExists();
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


    private void handleOpenExistingRepository() {
        // Open a directory chooser dialog to select an existing repository
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Existing Repository");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = chooser.showOpenDialog(null);

        // If the user selects a directory, attempt to open it as a Git repository
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = chooser.getSelectedFile();
            try {
                IDEAppBuilder.gitManager.openRepository(selectedDirectory.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Repository opened successfully.");
            } catch (IOException | GitAPIException ex) {
                JOptionPane.showMessageDialog(null, "Error opening repository: " + ex.getMessage());
            }
        }
    }

    private void handleSetRemoteUrl() {
        ensureRepositoryExists();
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
                    IDEAppBuilder.buildTree(selectedDirectory); // Pass the selected directory
                    IDEAppBuilder.buildIDE();
                }
            }
        } catch (GitAPIException ex) {
            JOptionPane.showMessageDialog(null, "Error cloning repository: " + ex.getMessage());
        }
    }

    private void handleMergeBranch() {
        ensureRepositoryExists();
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