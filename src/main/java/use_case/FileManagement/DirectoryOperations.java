package use_case.FileManagement;

import java.io.File;
import java.io.IOException;

public class DirectoryOperations extends Operations{
    public DirectoryOperations(File target) {
        super(target);
    }

    @Override
    public void copy(File destination) {
        try {
            File newDir = new File(destination, target.getName());

            // Prevent overwriting existing directories
            if (!newDir.mkdir() && !newDir.exists()) {
                throw new IOException("Failed to create directory: " + newDir.getAbsolutePath());
            }

            // Recursively copy contents of the directory
            File[] files = target.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        new DirectoryOperations(file).copy(newDir);
                    } else {
                        new FileOperations(file).copy(newDir);
                    }
                }
            }

            System.out.println("Directory copied to " + newDir.getAbsolutePath());

            // Refresh file system metadata
            destination.listFiles();

        } catch (Exception e) {
            System.err.println("Failed to copy directory: " + target.getAbsolutePath());
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        File[] files = target.listFiles();

        // Recursively delete contents of the directory
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    new DirectoryOperations(file).delete();
                } else {
                    new FileOperations(file).delete();
                }
            }
        }

        // Delete the directory itself
        if (target.delete()) {
            System.out.println("Deleted directory: " + target.getAbsolutePath());
        } else {
            System.err.println("Failed to delete directory: " + target.getAbsolutePath());
        }
    }

    @Override
    public void paste(File destination) {
        System.out.println("Pasting directory to " + destination.getAbsolutePath());
        copy(destination);
    }
}

