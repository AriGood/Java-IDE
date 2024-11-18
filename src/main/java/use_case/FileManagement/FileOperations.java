package use_case.FileManagement;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileOperations {

    public void saveFile(File parentDirectory) {
        //TODO
    }

    public JTextArea loadFile(File file) {
        //TODO
        JTextArea result = new JTextArea();
        return result;
    }

    public static String fileContent(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File Could Not Load: " + e.getMessage(),
                    "File Load Error", JOptionPane.ERROR_MESSAGE);
        }
        return content.toString();
    }
}
