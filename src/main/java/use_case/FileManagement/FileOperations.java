package use_case.FileManagement;

import javax.swing.*;
import java.io.*;

public class FileOperations {

    public void saveFile(File parentDirectory, String content) {
        //TODO: in progress
        // Also there are two options for the second parameter for saveFile: string or JTextArea...
        // I opted for String bc i think its more versatile... but open to ideas.
        try (FileWriter writer = new FileWriter(parentDirectory)) {
            // Write the string content to the specified file
            writer.write(content);
            JOptionPane.showMessageDialog(null, "File saved successfully.", "Save File", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File could not be saved: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public JTextArea loadFile(File file) {
        //TODO
        JTextArea result = new JTextArea();
        return result;
    }

    public String fileContent(File file) {
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
