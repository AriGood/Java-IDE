package main.test.java.org.use_case.Terminal;


import org.junit.Test;
import use_case.TerminalOperations.TerminalOperations;
import view.TerminalObj;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TerminalUseCaseTest {

    @Test
    public void testClearCommand() {
        TerminalOperations terminalOperations = new TerminalOperations();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TerminalOperations.CommandCallback callback = new TerminalOperations.CommandCallback() {
            @Override
            public void onOutput(String output) {
                outputStream.writeBytes(output.getBytes());
            }

            @Override
            public void onError(String error) {
                outputStream.writeBytes(error.getBytes());
            }
        };

        // Execute the 'clear' command
        terminalOperations.executeCommand("clear", callback);

        // Verify that no output was produced
        String result = outputStream.toString().trim();
        assertEquals("", result, "Clear command should not produce any output.");
    }

    @Test
    public void testChangeDirectory() {
        TerminalOperations terminalOperations = new TerminalOperations();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TerminalOperations.CommandCallback callback = new TerminalOperations.CommandCallback() {
            @Override
            public void onOutput(String output) {
                outputStream.writeBytes(output.getBytes());
            }

            @Override
            public void onError(String error) {
                outputStream.writeBytes(error.getBytes());
            }
        };

        // Execute the 'cd' command
        terminalOperations.executeCommand("cd src", callback);

        // Verify the output message
        String result = outputStream.toString().trim();
        assertTrue("Output should indicate directory change.", result.contains("Changed directory to"));
    }

    @Test
    public void testInvalidCommand() {
        TerminalOperations terminalOperations = new TerminalOperations();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TerminalOperations.CommandCallback callback = new TerminalOperations.CommandCallback() {
            @Override
            public void onOutput(String output) {
                outputStream.writeBytes(output.getBytes());
            }

            @Override
            public void onError(String error) {
                outputStream.writeBytes(error.getBytes());
            }
        };

        // Execute an invalid command
        terminalOperations.executeCommand("invalid_command", callback);

        // Verify the error message
        String result = outputStream.toString().trim();
        assertTrue("Output should indicate an error for an invalid command.", result.contains("Error"));
    }

    @Test
    public void testRunJavaFile() {
        TerminalOperations terminalOperations = new TerminalOperations();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TerminalOperations.CommandCallback callback = new TerminalOperations.CommandCallback() {
            @Override
            public void onOutput(String output) {
                outputStream.writeBytes(output.getBytes());
            }

            @Override
            public void onError(String error) {
                outputStream.writeBytes(error.getBytes());
            }
        };

        // Execute the 'run' command for a sample Java file
        terminalOperations.executeCommand("run Sample.java", callback);

        // Verify that the output contains expected messages
        String result = outputStream.toString().trim();
        assertTrue("Output should indicate success or an error for 'run' command.",
                result.contains("Error") || result.contains("Sample"));
    }
}


