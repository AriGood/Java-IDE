package use_case.FileManagement;

import javax.swing.*;
import java.io.*;
import java.util.logging.Logger;

public class FileOperations {

    private static final Logger logger = Logger.getLogger(FileOperations.class.getName());
    /**
     * This function saves the provided content to the specified file.
     *
     * @param file the file to save to
     * @param content the content to save into the file
     */
    public void saveFile(File file, String content) {
        //TODO: in progress

        // Note: intellij suggested to use a logging framework...?  instead of system.out

        // Save the file content to the specified file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);

            logger.info("File saved successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            logger.severe("File could not be saved: " + e.getMessage());
        }

        // Also, I could implement the function using the fileContent method.
        // For example, check if there is nothing to be saved before saving...?
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
