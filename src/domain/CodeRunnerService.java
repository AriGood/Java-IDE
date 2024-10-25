package domain;

import javax.swing.*;
import java.io.*;

public abstract class CodeRunnerService {
    protected abstract String getFileExtension();

    public void runCode(String code, JTextArea outputArea) {
        File sourceFile = saveCodeToFile(code);
        if (sourceFile == null) {
            outputArea.setText("Failed to save code.");
            return;
        }

        try {
            // Compile the code
            Process compileProcess = compile(sourceFile);
            if (compileProcess.exitValue() != 0) {
                handleCompilationError(compileProcess, outputArea);
                return;
            }

            // Run the code
            Process runProcess = run(sourceFile);
            handleOutput(runProcess, outputArea);
        } catch (Exception e) {
            e.printStackTrace();
            outputArea.setText("An error occurred: " + e.getMessage());
        }
    }

    private File saveCodeToFile(String code) {
        File sourceFile = new File("Main" + getFileExtension());
        try (FileWriter fileWriter = new FileWriter(sourceFile)) {
            fileWriter.write(code);
            return sourceFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Process compile(File sourceFile) throws IOException {
        ProcessBuilder pbCompile = new ProcessBuilder(getCompileCommand(sourceFile));
        return pbCompile.start();
    }

    protected abstract String[] getCompileCommand(File sourceFile);

    private Process run(File sourceFile) throws IOException {
        ProcessBuilder pbRun = new ProcessBuilder("java", "Main");
        return pbRun.start();
    }

    private void handleCompilationError(Process process, JTextArea outputArea) throws IOException {
        outputArea.setText("Compilation Error:\n");
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = errorReader.readLine()) != null) {
            outputArea.append(line + "\n");
        }
    }

    private void handleOutput(Process process, JTextArea outputArea) throws IOException {
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = outputReader.readLine()) != null) {
            output.append(line).append("\n");
        }

        BufferedReader runtimeErrorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        while ((line = runtimeErrorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        if (errorOutput.length() > 0) {
            outputArea.setText("Runtime Error:\n" + errorOutput);
        } else {
            outputArea.setText("Program Output:\n" + output);
        }
    }
}
