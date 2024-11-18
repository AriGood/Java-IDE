package use_case.FileManagement;

import javax.swing.*;
import java.io.*;

public class FileOperations {

    /**
     * This function saves the provided content to the specified file.
     *
     * @param file the file to save to
     * @param content the content to save into the file
     */
    public void saveFile(File file, String content) {
        //TODO: in progress

        // Also there are two options for the second parameter for saveFile: string or JTextArea...
        // I opted for String bc i think its more versatile... but open to ideas.
        try (FileWriter writer = new FileWriter(file)) {
            // Here we write the string content to the specified file
            writer.write(content);
            JOptionPane.showMessageDialog(null, "File saved successfully.", "Save File", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File could not be saved: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }

        // Also, I could implement the function using the fileContent method.
        // For example, check if there is nothing to be saved before saving...?

        // WAIT, it can also be used to retrieve data... or look at the history of a file
        // "backing up original content before saving" - not an as option, just automatically with every save

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
