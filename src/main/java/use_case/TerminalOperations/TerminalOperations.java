package use_case.TerminalOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TerminalOperations {
    private Path currentDirectory;
    private final boolean isWindows;

    public TerminalOperations() {
        currentDirectory = Paths.get(System.getProperty("user.dir"));
        isWindows = System.getProperty("os.name").toLowerCase().contains("win");
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
        if (isWindows) {
            executeProcess(new String[]{"cmd.exe", "/c", "cls"}, callback);
        } else {
            executeProcess(new String[]{"bash", "-c", "clear"}, callback);
        }
    }

    private void changeDirectory(String command, CommandCallback callback) {
        String newDirectory = command.substring(3).trim();
        Path newDirPath = currentDirectory.resolve(newDirectory).normalize();

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

        Path javaFilePath = currentDirectory.resolve(fileName);
        if (!javaFilePath.toFile().exists()) {
            callback.onError("Error: File not found: " + javaFilePath + "\n");
            return;
        }

        String className = javaFilePath.getFileName().toString().replace(".java", "");
        Path parentDir = javaFilePath.getParent();

        String compileCommand = "javac " + javaFilePath;
        executeProcess(compileCommand.split(" "), callback);

        Path classFilePath = parentDir.resolve(className + ".class");
        if (!classFilePath.toFile().exists()) {
            callback.onError("Error: Compilation failed, .class file not generated.\n");
            return;
        }

        String runCommand = "java -cp " + parentDir + " " + className;
        executeProcess(runCommand.split(" "), callback);
    }


    private void executeGeneralCommand(String command, CommandCallback callback)
            throws IOException, InterruptedException {
        callback.onOutput("> " + command + "\n");
        String[] fullCommand = isWindows
                ? new String[]{"cmd.exe", "/c", command}
                : new String[]{"bash", "-c", command};
        executeProcess(fullCommand, callback);
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
