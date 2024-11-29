package use_case.FileManagement;

import java.io.*;
import java.util.logging.Logger;

public class FileOperations extends Operations {

    private static final Logger logger = Logger.getLogger(FileOperations.class.getName());

    public FileOperations(File target) {
        super(target);
    }

    @Override
    public void copy(File destination) {
        try {
            File newFile = new File(destination, target.getName());

            // Prevent overwriting existing files
            if (newFile.exists()) {
                System.err.println("File already exists: " + newFile.getAbsolutePath());
                return;
            }

            // Copy file content
            java.nio.file.Files.copy(target.toPath(), newFile.toPath());
            System.out.println("File copied to " + newFile.getAbsolutePath());

            // Refresh file system metadata
            destination.listFiles();

        } catch (IOException e) {
            System.err.println("Failed to copy file: " + target.getAbsolutePath());
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        // Delete the file
        if (target.delete()) {
            System.out.println("Deleted file: " + target.getAbsolutePath());
        } else {
            System.err.println("Failed to delete file: " + target.getAbsolutePath());
        }
    }

    @Override
    public void paste(File destination) {
        System.out.println("Pasting file to " + destination.getAbsolutePath());
        copy(destination);
    }

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
            view.DisplayErrors.displayFileSaveError(e);
        }
    }

    public static String fileContent(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            view.DisplayErrors.displayFileLoadError(e);
        }
        return content.toString();
    }
}
