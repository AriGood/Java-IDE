package use_case.FileManagement;

import entity.EditorObj;

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
            java.nio.file.Files.copy(target.toPath(), new File(destination, target.getName()).toPath());
            System.out.println("File copied to " + destination.getAbsolutePath());
        } catch (IOException e) {
            view.DisplayErrors.displayFileCopyError(e);
        }
    }

    @Override
    public void paste(File destination) {
        System.out.println("Pasting file to " + destination.getAbsolutePath());
    }

    @Override
    public void delete() {
        if (target.delete()) {
            System.out.println("File deleted: " + target.getName());
        } else {
            view.DisplayErrors.displayFileDeleteError(target.getName());
        }
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
