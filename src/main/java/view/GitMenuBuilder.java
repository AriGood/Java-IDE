package view;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyException;
import javax.security.auth.login.LoginException;
import javax.swing.*;

import org.eclipse.jgit.api.errors.GitAPIException;

import app.IDEAppBuilder;
import use_case.git.CredentialUseCase;


public class GitMenuBuilder {
    private final JMenu gitMenu;
    private final IDEAppBuilder ideAppBuilder;

    /**
     * Initializes the builder for the Git menu.
     * @param newIdeAppBuilder current Ide window
     */
    public GitMenuBuilder(IDEAppBuilder newIdeAppBuilder) {
        this.ideAppBuilder = newIdeAppBuilder;

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
        commit.addActionListener(ActionListener -> {
            ensureRepositoryExists();
            try {
                ideAppBuilder.saveOpen();
                String message = JOptionPane.showInputDialog(null, "Enter commit message:");
                if (message != null && !message.isEmpty()) {
                    ideAppBuilder.gitManager.commitChanges(message);
                    JOptionPane.showMessageDialog(null, "Changes committed successfully.");
                }
            }
            catch (GitAPIException ex) {
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
        push.addActionListener(ActionListener -> {

            ensureRepositoryExists();
            try {
                ideAppBuilder.gitManager.pushChanges(getLogin());
                JOptionPane.showMessageDialog(null, "Changes pushed successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Cannot push changes " + ex + ex.getLocalizedMessage());
            }
        });
        gitMenu.add(push);
    }

    /**
     * Adds the "Pull" action to the Git menu.
     */
    public void addPullAction() {
        JMenuItem pull = new JMenuItem("Pull");
        pull.addActionListener(ActionListener -> {
            ensureRepositoryExists();
            try {
                ideAppBuilder.gitManager.getCurrentRepository().pull().call();
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
        openExistingRepo.addActionListener(ActionListener -> handleOpenExistingRepository());
        gitMenu.add(openExistingRepo);
    }

    /**
     * Adds the "Login" action to the Git menu.
     */
    public void addLoginAction() {
        JMenuItem login = new JMenuItem("Login");
        login.addActionListener(ActionListener -> handleLogin());
        gitMenu.add(login);
    }

    /**
     * Adds the "Create Branch" action to the Git menu.
     */
    public void addCreateBranchAction() {
        JMenuItem createBranch = new JMenuItem("Create Branch");
        createBranch.addActionListener(ActionListener -> handleBranchCreation());
        gitMenu.add(createBranch);
    }

    /**
     * Adds the "Checkout Branch" action to the Git menu.
     */
    public void addCheckoutBranchAction() {
        JMenuItem checkoutBranch = new JMenuItem("Checkout Branch");
        checkoutBranch.addActionListener(ActionListener -> handleBranchCheckout());
        gitMenu.add(checkoutBranch);
    }

    /**
     * Adds the "Set Remote URL" action to the Git menu.
     */
    public void addSetRemoteAction() {
        JMenuItem setRemote = new JMenuItem("Set Remote URL");
        setRemote.addActionListener(ActionListener -> handleSetRemoteUrl());
        gitMenu.add(setRemote);
    }

    /**
     * Adds the "Clone Repository" action to the Git menu.
     */
    public void addCloneRepositoryAction() {
        JMenuItem cloneRepository = new JMenuItem("Clone Repository");
        cloneRepository.addActionListener(ActionListener -> handleCloneRepository());
        gitMenu.add(cloneRepository);
    }

    /**
     * Adds the "Merge Branch" action to the Git menu.
     */
    public void addMergeBranchAction() {
        JMenuItem mergeBranch = new JMenuItem("Merge Branch");
        mergeBranch.addActionListener(ActionListener -> handleMergeBranch());
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
        if (ideAppBuilder.gitManager.getCurrentRepository() == null) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "No repository found. Would you like to create a new one?",
                    "Create Repository", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                File selectedDirectory = null;
                int choice1 = JOptionPane.showConfirmDialog(null,
                        "Would you like to make the current directory a git repository?",
                        "current directory", JOptionPane.YES_NO_OPTION);
                if (choice1 == JOptionPane.YES_OPTION) {
                    selectedDirectory = ideAppBuilder.directory;
                }
                else {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("Select Directory for New Repository");
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnValue = chooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        selectedDirectory = chooser.getSelectedFile();
                    }
                }
                try {
                    ideAppBuilder.gitManager.createRepository(JOptionPane.showInputDialog(null, "Enter remote URL:")
                            ,selectedDirectory.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "New repository created successfully.");
                }
                catch (GitAPIException ex) {
                    JOptionPane.showMessageDialog(null, "Error creating repository: " + ex.getMessage());
                }
            }
        }
    }

    private String[] getLogin() throws KeyException, LoginException {
        return CredentialUseCase.loadCredentials(use_case.git.SecureKeyManager.loadKey());
    }

    private void warningNoGit() {
        JOptionPane.showMessageDialog(null, "Warning: No Git repository open.");
    }

    private void handleLogin() {
        CredentialUseCase credentialUseCase = new CredentialUseCase();
        JPasswordField pf = new JPasswordField();
        try {
            use_case.git.SecureKeyManager.saveKey();
            String username = JOptionPane.showInputDialog("Enter username:");
            if (username != null && !username.isEmpty()) {
                int passwordOption = JOptionPane.showConfirmDialog(null, pf, "Enter Password",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (passwordOption == JOptionPane.OK_OPTION) {
                    credentialUseCase.saveCredentials(username, new String(pf.getPassword()),
                            use_case.git.SecureKeyManager.loadKey());
                    JOptionPane.showMessageDialog(null, "Login credentials saved successfully.");
                }
            }
        }
        catch (GeneralSecurityException ex) {
            JOptionPane.showMessageDialog(null, "Invalid login. Please try again.");
        }
    }

    private void handleBranchCreation() {
        ensureRepositoryExists();
        try {
            String branchName = JOptionPane.showInputDialog(null, "Enter new branch name:");
            if (branchName != null && !branchName.isEmpty()) {
                ideAppBuilder.gitManager.createBranch(branchName);
                JOptionPane.showMessageDialog(null, "Branch created successfully.");
            }
        }
        catch (GitAPIException ex) {
            warningNoGit();
        }
    }

    private void handleBranchCheckout() {
        ensureRepositoryExists();
        try {
            String branchName = JOptionPane.showInputDialog(null, "Enter branch name to checkout:");
            if (branchName != null && !branchName.isEmpty()) {
                ideAppBuilder.gitManager.checkoutBranch(branchName);
                JOptionPane.showMessageDialog(null, "Switched to branch: " + branchName);
            }
        }
        catch (GitAPIException ex) {
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
            ideAppBuilder.buildTree(selectedDirectory);
            ideAppBuilder.buildIDE();

            try {
                // Open the repository
                ideAppBuilder.gitManager.openRepository(selectedDirectory.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Repository opened successfully.");

                // Get the remote URL from the Git configuration
                String remoteUrl = ideAppBuilder.gitManager.getCurrentRepository()
                        .getRepository()
                        .getConfig()
                        .getString("remote", "origin", "url");
                if (remoteUrl != null && !remoteUrl.isEmpty()) {
                    ideAppBuilder.gitManager.setRemoteUrl(remoteUrl);
                    JOptionPane.showMessageDialog(null, "Remote URL: " + remoteUrl);
                }
                else {
                    JOptionPane.showMessageDialog(null, "No remote URL found in the repository configuration.");
                }
            }
             catch (IOException | GitAPIException ex) {
                JOptionPane.showMessageDialog(null, "Error opening repository with git: " + ex.getMessage());
            }
        }
    }

    private void handleSetRemoteUrl() {
        ensureRepositoryExists();
        String remoteUrl = JOptionPane.showInputDialog(null, "Enter remote URL:");
        if (remoteUrl != null && !remoteUrl.isEmpty()) {
            ideAppBuilder.gitManager.setRemoteUrl(remoteUrl);
            JOptionPane.showMessageDialog(null, "Remote URL set successfully.");
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
                    ideAppBuilder.gitManager.cloneRepository(repoUrl, selectedDirectory.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "Repository cloned successfully.");
                    ideAppBuilder.buildTree(selectedDirectory);
                    ideAppBuilder.buildIDE();
                }
            }
        }
        catch (GitAPIException ex) {
            JOptionPane.showMessageDialog(null, "Error cloning repository: " + ex.getMessage());
        }
    }

    private void handleMergeBranch() {
        ensureRepositoryExists();
        try {
            String branchName = JOptionPane.showInputDialog(null, "Enter branch name to merge into current branch:");
            if (branchName != null && !branchName.isEmpty()) {
                ideAppBuilder.gitManager.mergeBranch(branchName);
                JOptionPane.showMessageDialog(null, "Branch merged successfully.");
            }
        }
        catch (GitAPIException ex) {
            warningNoGit();
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error during merge: " + ex.getMessage());
        }
    }
}
