package entity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {
    /**
     * Executes a command and returns the output as a list of strings.
     *
     * @param command The command to execute.
     * @return The output of the command as a list of strings.
     */
    public List<String> runCommand(String command) {
        List<String> output = new ArrayList<>();
        try {
            // Use ProcessBuilder to execute the command
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // Read the output from the process
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }

            // Wait for the process to finish
            process.waitFor();
        } catch (Exception e) {
            output.add("Error executing command: " + e.getMessage());
        }
        return output;
    }
}