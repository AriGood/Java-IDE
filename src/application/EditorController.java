package application;

import infrastructure.FileService;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class EditorController {
    private final domain.AutoCompleteService autoCompleteService;
    private final FileService fileService;

    public EditorController() {
        this.autoCompleteService = new domain.AutoCompleteService();
        this.fileService = new FileService();
    }

    public void handleAutoComplete(JTextComponent textComponent, JPopupMenu popup) {
        autoCompleteService.enableAutoComplete(textComponent, popup);
    }
    public void runCode(JTextPane editor, JTextArea outputArea) {
        String code = editor.getText();
        File sourceFile = new File("Main.java");

        try {
            // Step 1: Save the code to Main.java
            FileWriter fileWriter = new FileWriter(sourceFile);
            fileWriter.write(code);
            fileWriter.close();

            // Step 2: Compile Main.java
            ProcessBuilder pbCompile = new ProcessBuilder("javac", sourceFile.getName());
            Process compileProcess = pbCompile.start();
            compileProcess.waitFor();

            // Check for compilation errors
            String line;
            if (compileProcess.exitValue() != 0) {
                outputArea.setText("Compilation Error:\n");
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
                while ((line = errorReader.readLine()) != null) {
                    outputArea.append(line + "\n");
                }
                errorReader.close();
            } else {
                // Step 3: Run the compiled program
                ProcessBuilder pbRun = new ProcessBuilder("java", "Main");
                Process runProcess = pbRun.start();

                // Capture output from the program
                BufferedReader outputReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                StringBuilder output = new StringBuilder();
                while ((line = outputReader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                outputReader.close();

                // Capture any runtime errors
                BufferedReader runtimeErrorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                while ((line = runtimeErrorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
                runtimeErrorReader.close();

                // Display the output or errors
                if (errorOutput.length() > 0) {
                    outputArea.setText("Runtime Error:\n" + errorOutput);
                } else {
                    outputArea.setText("Program Output:\n" + output);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            outputArea.setText("An error occurred: " + e.getMessage());
        }
    }

}
