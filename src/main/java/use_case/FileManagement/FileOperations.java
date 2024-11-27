package use_case.FileManagement;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

// Make sure all methods here do not include any UI-related code - responsibility for interacting with the user
// should lie in the UI layer (FilePopup in this case)

// All operations related to Files
public class FileOperations {

    private static final Logger logger = Logger.getLogger(FileOperations.class.getName());

    /**
     * This function saves the provided content to the specified file.
     *
     * @param file the file to save to
     * @param content the content to save into the file
     */
    public static void saveFile(File file, String content) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);

            logger.info("File saved successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            logger.severe("File could not be saved: " + e.getMessage());
        }
    }

    /**
     * Creates a new file in the specified parent directory.
     *
     * @param parentDirectory The directory where the new file will be created.
     * @param fileName        The name of the new file.
     * @return The newly created file.
     * @throws Exception If the file already exists or cannot be created.
     */
    public static File createFile(File parentDirectory, String fileName) throws Exception {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty.");
        }

        File newFile = new File(parentDirectory, fileName);
        if (!parentDirectory.isDirectory()) {
            throw new IllegalArgumentException("Parent must be a directory.");
        }
        if (newFile.exists()) {
            throw new IOException("File already exists.");
        }
        if (!newFile.createNewFile()) {
            throw new IOException("Failed to create file.");
        }
        return newFile;
    }

    /**
     * Copies a file or directory to the specified destination.
     *
     * @param sourceFile The file or directory to copy.
     * @param destination The destination directory.
     * @throws IOException If the copy operation fails.
     */
    public static void copyFile(File sourceFile, File destination) throws IOException {
        if (!sourceFile.exists()) {
            throw new IllegalArgumentException("Source file does not exist: " + sourceFile.getAbsolutePath());
        }
        if (sourceFile.isFile()) {
            // Copy single file
            Files.copy(sourceFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else if (sourceFile.isDirectory()) {
            // Recursively copy directory
            if (!destination.exists() && !destination.mkdir()) {
                throw new IOException("Failed to create destination directory: " + destination.getAbsolutePath());
            }
            for (File file : sourceFile.listFiles()) {
                File destFile = new File(destination, file.getName());
                copyFile(file, destFile);
            }
        }
    }

    /**
     * Pastes a file or directory into the specified destination directory.
     *
     * @param sourceFile The file or directory to paste.
     * @param destination The destination directory or parent directory.
     * @throws IOException If the paste operation fails.
     */
    public static void pasteFile(File sourceFile, File destination) throws IOException {
        if (!destination.exists()) {
            throw new IllegalArgumentException("Destination does not exist.");
        }

        if (!destination.isDirectory()) {
            // Use parent directory if destination is a file
            destination = destination.getParentFile();
        }

        if (destination == null || !destination.isDirectory()) {
            throw new IllegalArgumentException("Destination must be a valid directory.");
        }

        File destinationFile = new File(destination, sourceFile.getName());
        copyFile(sourceFile, destinationFile);
    }

    /**
     * Copies the file to the specified destination directory.
     * @param file the file to load
     */
    public JTextArea loadFile(File file) {
        //TODO
        JTextArea result = new JTextArea();
        return result;
    }

    public void deleteFile(File file){
        //TODO
    }

    // TODO: remove UI related code - JOptionPane
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
