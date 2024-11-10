package use_case.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

public class GitManager {

    private Git currentRepository;
    private File currentDirectory;

    public GitManager() {
        // Initialize without a repository
        this.currentRepository = null;
        this.currentDirectory = null;
    }

    /**
     * Clone a repository to a specified directory
     * @param repoUrl The URL of the repository to clone
     * @param directoryPath The local directory to clone into
     * @throws GitAPIException if cloning fails
     */
    public void cloneRepository(String repoUrl, String directoryPath) throws GitAPIException {
        this.currentDirectory = new File(directoryPath);
        this.currentRepository = Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(currentDirectory)
                .call();
    }

    /**
     * Initialize a new repository in a specified directory
     * @param directoryPath The local directory to initialize as a Git repository
     * @throws GitAPIException if initialization fails
     */
    public void createRepository(String directoryPath) throws GitAPIException {
        this.currentDirectory = new File(directoryPath);
        this.currentRepository = Git.init()
                .setDirectory(currentDirectory)
                .call();
    }

    /**
     * Commit changes in the current repository
     * @param message The commit message
     * @return The commit object created
     * @throws GitAPIException if commit fails
     */
    public RevCommit commitChanges(String message) throws GitAPIException {
        if (currentRepository == null) {
            throw new IllegalStateException("No repository available to commit changes.");
        }

        currentRepository.add().addFilepattern(".").call();
        return currentRepository.commit().setMessage(message).call();
    }

    /**
     * Push changes to a remote repository
     * @param remoteUrl The remote repository URL
     * @param username The username for authentication
     * @param password The password for authentication
     * @throws GitAPIException if push fails
     */
    public void pushChanges(String remoteUrl, String username, String password) throws GitAPIException {
        if (currentRepository == null) {
            throw new IllegalStateException("No repository available to push changes.");
        }

        currentRepository.push()
                .setRemote(remoteUrl)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call();
    }

    /**
     * Create a new branch in the current repository
     * @param branchName The name of the new branch
     * @throws GitAPIException if branch creation fails
     */
    public void createBranch(String branchName) throws GitAPIException {
        if (currentRepository == null) {
            throw new IllegalStateException("No repository available to create a branch.");
        }

        currentRepository.branchCreate().setName(branchName).call();
    }

    /**
     * Checkout an existing branch in the current repository
     * @param branchName The name of the branch to checkout
     * @throws GitAPIException if checkout fails
     */
    public void checkoutBranch(String branchName) throws GitAPIException {
        if (currentRepository == null) {
            throw new IllegalStateException("No repository available to checkout a branch.");
        }

        currentRepository.checkout().setName(branchName).call();
    }

    /**
     * Get the current repository instance
     * @return The current Git repository, or null if none is loaded
     */
    public Git getCurrentRepository() {
        return currentRepository;
    }

    /**
     * Get the current directory
     * @return The current directory as a File
     */
    public File getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * Close the current repository if it is open
     * @throws IOException if closing fails
     */
    public void closeRepository() throws IOException {
        if (currentRepository != null) {
            currentRepository.close();
            currentRepository = null;
            currentDirectory = null;
        }
    }
}
