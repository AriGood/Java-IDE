package use_case.TerminalOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
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
        if (command.equalsIgnoreCase("clear")) {
            callback.onOutput(""); // Clear the terminal UI

            try {
                ProcessBuilder builder = new ProcessBuilder();
                builder.command("bash", "-c", "clear");
                Process process = builder.start();
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                callback.onError("Error executing 'clear' command: " + e.getMessage() + "\n");
            }

            return;
        }

        if (command.startsWith("cd ")) {
            String newDirectory = command.substring(3).trim();
            Path newDirPath = Paths.get(newDirectory).toAbsolutePath();

            if (newDirPath.toFile().exists() && newDirPath.toFile().isDirectory()) {
                currentDirectory = newDirPath;
                callback.onOutput("Changed directory to: " + currentDirectory + "\n");
            } else {
                callback.onError("Error: Directory does not exist.\n");
            }
            return;
        }

        if (command.startsWith("run ")) {
            String fileName = command.substring(4).trim();
            try {
                ProcessBuilder builder;

                // Check for if the provided file is a .java file
                if (fileName.endsWith(".java")) {
                    // Uses the full file path which must be provided
                    String fullPath = currentDirectory.resolve(fileName).toString();
                    String className = fileName.replace(".java", "");

                    builder = new ProcessBuilder("bash", "-c",
                            "javac " + fullPath +
                                    " && java -cp " + currentDirectory.toString() + " " + className);
                } else {
                    callback.onError("Error: Only .java files are supported for 'run' command.\n");
                    return;
                }

                builder.directory(currentDirectory.toFile()); // Set the working directory to currentDirectory
                Process process = builder.start();

                BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                String line;
                while ((line = outputReader.readLine()) != null) {
                    callback.onOutput(line + "\n");
                }

                while ((line = errorReader.readLine()) != null) {
                    callback.onError("Error: " + line + "\n");
                }

                process.waitFor();
            } catch (IOException | InterruptedException e) {
                callback.onError("Error executing 'run' command: " + e.getMessage() + "\n");
            }
            return;
        }

        callback.onOutput("> " + command + "\n");

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("bash", "-c", command);
            builder.directory(currentDirectory.toFile()); // Set the working directory to currentDirectory
            Process process = builder.start();

            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = outputReader.readLine()) != null) {
                callback.onOutput(line + "\n");
            }

            while ((line = errorReader.readLine()) != null) {
                callback.onError("Error: " + line + "\n");
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            callback.onError("Error executing command: " + e.getMessage() + "\n");
        }

        callback.onOutput("\n");
    }
}
