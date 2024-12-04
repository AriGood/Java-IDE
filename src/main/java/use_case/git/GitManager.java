package use_case.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class GitManager {

    public static final String DIR_NULL_OR_EMPTY = "Directory path cannot be null or empty.";
    public static final String REPOSITORY_NULL_OR_EMPTY = "Repository URL cannot be null or empty.";
    private Git currentRepository;
    private File currentDirectory;
    private String remoteUrl;

    public GitManager() {
        // Initialize without a repository
        this.remoteUrl = null;
        this.currentRepository = null;
        this.currentDirectory = null;
    }

    /**
     * Clone a repository to a specified directory.
     * @param repoUrl The URL of the repository to clone
     * @param directoryPath The local directory to clone into
     * @throws GitAPIException if cloning fails
     * @throws IllegalArgumentException if input is invalid
     */
    public void cloneRepository(String repoUrl, String directoryPath) throws GitAPIException {
        if (repoUrl == null || repoUrl.isEmpty()) {
            throw new IllegalArgumentException(REPOSITORY_NULL_OR_EMPTY);
        }
        if (directoryPath == null || directoryPath.isEmpty()) {
            throw new IllegalArgumentException(DIR_NULL_OR_EMPTY);
        }

        this.currentDirectory = new File(directoryPath);
        try {
            this.currentRepository = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(currentDirectory)
                    .call();

            if (currentRepository != null) {
                System.out.println("Repository cloned successfully.");
            } else {
                System.out.println("Failed to clone repository, currentRepository is still null.");
            }
        } catch (GitAPIException e) {
            System.out.println("Error during clone: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Initialize a new repository in a specified directory.
     * @param directoryPath The local directory to initialize as a Git repository
     * @param remote remote repo url
     * @throws GitAPIException if initialization fails
     */
    public void createRepository(String directoryPath, String remote) throws GitAPIException {
        if (directoryPath == null || directoryPath.isEmpty()) {
            throw new IllegalArgumentException(DIR_NULL_OR_EMPTY);
        }

        this.currentDirectory = new File(directoryPath);
        this.currentRepository = Git.init()
                .setDirectory(currentDirectory)
                .call();

        if (remote != null && !remote.isEmpty()) {
            this.remoteUrl = remote;
        }
    }

    /**
     * Commit changes in the current repository.
     * @param message The commit message
     * @throws GitAPIException if commit fails
     * @throws IllegalArgumentException if commit message is null or empty
     */
    public void commitChanges(String message) throws GitAPIException {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Commit message cannot be null or empty.");
        }

        try {
            // Stage all changes (modified, added, deleted files)
            currentRepository.add().addFilepattern(".").call();

            // Get the repository status to ensure there are changes to commit
            Status status = currentRepository.status().call();

            if (!status.hasUncommittedChanges()) {
                System.out.println("No changes to commit.");
                return;
            }

            // Commit the changes with the provided message
            currentRepository.commit().setMessage(message).call();
            System.out.println("Changes committed successfully.");
        } catch (GitAPIException e) {
            System.err.println("Error committing changes: " + e.getMessage());
            throw e;
        }
    }


    /**
     * Open an existing Git repository in a specified directory.
     *
     * @param directoryPath The local directory that contains the existing Git repository.
     * @throws IOException If there is an issue with accessing the directory.
     * @throws GitAPIException If the directory is not a valid Git repository or cannot be opened.
     * @throws IllegalArgumentException if dir path is null
     */
    public void openRepository(String directoryPath) throws IOException, GitAPIException {
        if (directoryPath == null || directoryPath.isEmpty()) {
            throw new IllegalArgumentException(DIR_NULL_OR_EMPTY);
        }

        this.currentDirectory = new File(directoryPath);
        if (!currentDirectory.exists() || !currentDirectory.isDirectory()) {
            throw new IOException("The specified directory does not exist or is not a directory.");
        }

        // Check if the directory contains a .git folder, indicating it's a Git repository
        File gitDir = new File(currentDirectory, ".git");
        if (!gitDir.exists() || !gitDir.isDirectory()) {
            throw new GitAPIException("The specified directory is not a valid Git repository.") {};
        }

        // Open the existing repository
        this.currentRepository = Git.open(currentDirectory);
        System.out.println("Repository opened: " + directoryPath);
    }

    /**
     * Push changes to a remote repository.
     * @param credential The [username, password] for authentication
     * @throws GitAPIException if push fails
     * @throws IllegalStateException if there is no repo
     * @throws IllegalArgumentException if login fails
     */
    public void pushChanges(String[] credential) throws GitAPIException, IllegalStateException, IllegalArgumentException {
        if (currentRepository == null) {
            throw new IllegalStateException("No repository is currently opened. Open or clone a repository first.");
        }

        if (remoteUrl == null || remoteUrl.isEmpty()) {
            throw new IllegalStateException("No remote URL configured for the repository.");
        }

        if (credential == null || credential.length < 2 || credential[0].isEmpty() || credential[1].isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials. Provide both username and password.");
        }

        try {
            Iterable<PushResult> results = currentRepository.push()
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(credential[0], credential[1]))
                    .call();

            for (PushResult result : results) {
                System.out.println("Push messages: " + result.getMessages());
            }
            System.out.println("Push operation completed.");
        } catch (GitAPIException e) {
            System.err.println("Error during push: " + e.getMessage());
            throw e;
        }
    }


    /**
     * Create a new branch in the current repository.
     * @param branchName The name of the new branch
     * @throws GitAPIException if branch creation fails
     */
    public void createBranch(String branchName) throws GitAPIException {
        currentRepository.branchCreate().setName(branchName).call();
    }

    /**
     * Checkout an existing branch in the current repository.
     * @param branchName The name of the branch to checkout
     * @throws GitAPIException if checkout fails
     */
    public void checkoutBranch(String branchName) throws GitAPIException {
        currentRepository.checkout().setName(branchName).call();
    }

    /**
     * Get the current repository instance.
     * @return The current Git repository, or null if none is loaded
     */
    public Git getCurrentRepository() {
        return currentRepository;
    }

    /**
     * Get the current directory.
     * @return The current directory as a File
     */
    public File getCurrentDirectory() {
        return currentDirectory;
    }


    /**
     * Add specific files to the staging area.
     * @param filePaths List of file paths to add to staging.
     * @throws GitAPIException if adding files fails.
     * @throws IllegalArgumentException input path is wrong.
     */
    public void addFiles(List<String> filePaths) throws GitAPIException, IllegalArgumentException {
        if (filePaths == null || filePaths.isEmpty()) {
            throw new IllegalArgumentException("File paths cannot be null or empty.");
        }

        for (String filePath : filePaths) {
            if (filePath == null || filePath.isEmpty()) {
                throw new IllegalArgumentException("File path cannot be null or empty.");
            }
            currentRepository.add().addFilepattern(filePath).call();
        }
    }


    /**
     * Remove specific files from the repository.
     * @param filePaths List of file paths to remove.
     * @throws GitAPIException if removing files fails.
     * @throws IllegalArgumentException if file path is null
     */
    public void removeFiles(List<String> filePaths) throws GitAPIException {

        if (filePaths == null || filePaths.isEmpty()) {
            throw new IllegalArgumentException("File paths cannot be null or empty.");
        }

        for (String filePath : filePaths) {
            if (filePath == null || filePath.isEmpty()) {
                throw new IllegalArgumentException("File path cannot be null or empty.");
            }
            currentRepository.rm().addFilepattern(filePath).call();
        }
    }

    /**
     * Get the status of the current repository (modified, staged, untracked files).
     * @return Status object containing repository state information.
     * @throws GitAPIException if status retrieval fails.
     */
    public Status getStatus() throws GitAPIException {

        return currentRepository.status().call();
    }

    /**
     * Get a list of recent commits from the current branch.
     * @param maxCount The maximum number of commits to retrieve.
     * @return List of RevCommit objects representing recent commits.
     * @throws GitAPIException if commit retrieval fails.
     */
    public List<RevCommit> getCommitHistory(int maxCount) throws GitAPIException {
        Iterable<RevCommit> commits = currentRepository.log().setMaxCount(maxCount).call();
        List<RevCommit> commitList = new ArrayList<>();
        for (RevCommit commit : commits) {
            commitList.add(commit);
        }
        return commitList;
    }

    /**
     * Revert unstaged changes to tracked files in the working directory.
     * @throws GitAPIException if revert operation fails.
     */
    public void revertUnstagedChanges() throws GitAPIException {
        currentRepository.checkout().setAllPaths(true).call();
    }

    /**
     * Merge a branch into the current branch.
     * @param branchName The name of the branch to merge into the current branch.
     * @throws GitAPIException if merge operation fails.
     */
    public void mergeBranch(String branchName) throws GitAPIException, IOException {

        currentRepository.merge()
                .include(currentRepository.getRepository().resolve(branchName))
                .call();
    }

    /**
     * Close the current repository if it is open.
     * @throws IOException if closing fails
     */
    public void closeRepository() throws IOException {
        if (currentRepository != null) {
            currentRepository.close();
            currentRepository = null;
            currentDirectory = null;
        }
    }

    public void setCurrentRepository(Git currentRepository) {
        this.currentRepository = currentRepository;
    }
    public void setCurrentDirectory(File currentDirectory) {
        this.currentDirectory = currentDirectory;
    }
    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }
    public String getRemoteUrl() {
        return remoteUrl;
    }
}
