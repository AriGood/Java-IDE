package use_case.TerminalOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TerminalOperations {
    private Path currentDirectory;

    public TerminalOperations() {
        currentDirectory = Paths.get(System.getProperty("user.dir"));
    }

    public interface CommandCallback {
        void onOutput(String output);
        void onError(String error);
    }

    public void executeCommand(String command, CommandCallback callback) {
        try {
            if (command.equalsIgnoreCase("clear")) {
                clearTerminal(callback);
            } else if (command.startsWith("cd ")) {
                changeDirectory(command, callback);
            } else if (command.startsWith("run ")) {
                runJavaFile(command, callback);
            } else {
                executeGeneralCommand(command, callback);
            }
        } catch (Exception e) {
            callback.onError("Error: " + e.getMessage() + "\n");
        }
    }

    private void clearTerminal(CommandCallback callback) throws IOException, InterruptedException {
        callback.onOutput(""); // Clear the terminal UI
        executeProcess(new String[]{"bash", "-c", "clear"}, callback);
    }

    private void changeDirectory(String command, CommandCallback callback) {
        String newDirectory = command.substring(3).trim();
        Path newDirPath = Paths.get(newDirectory).toAbsolutePath();

        if (newDirPath.toFile().exists() && newDirPath.toFile().isDirectory()) {
            currentDirectory = newDirPath;
            callback.onOutput("Changed directory to: " + currentDirectory + "\n");
        } else {
            callback.onError("Error: Directory does not exist.\n");
        }
    }

    private void runJavaFile(String command, CommandCallback callback) throws IOException, InterruptedException {
        String fileName = command.substring(4).trim();
        if (!fileName.endsWith(".java")) {
            callback.onError("Error: Only .java files are supported for 'run' command.\n");
            return;
        }

        String fullPath = currentDirectory.resolve(fileName).toString();
        String className = fileName.replace(".java", "");

        executeProcess(
                new String[]{"bash", "-c", "javac " + fullPath + " && java -cp " + currentDirectory + " " + className},
                callback
        );
    }

    private void executeGeneralCommand(String command, CommandCallback callback) throws IOException, InterruptedException {
        callback.onOutput("> " + command + "\n");
        executeProcess(new String[]{"bash", "-c", command}, callback);
    }

    private void executeProcess(String[] command, CommandCallback callback) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(currentDirectory.toFile());
        Process process = builder.start();

        handleProcessOutput(process, callback);
        process.waitFor();
    }

    private void handleProcessOutput(Process process, CommandCallback callback) throws IOException {
        try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            while ((line = outputReader.readLine()) != null) {
                callback.onOutput(line + "\n");
            }
            while ((line = errorReader.readLine()) != null) {
                callback.onError(line + "\n");
            }
        }
    }
}
