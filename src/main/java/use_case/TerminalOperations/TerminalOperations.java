package use_case.TerminalOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalOperations {

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

        callback.onOutput("> " + command + "\n");

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("bash", "-c", command);
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
