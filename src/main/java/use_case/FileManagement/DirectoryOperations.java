package use_case.FileManagement;

import java.io.File;

public class DirectoryOperations extends Operations{
    public DirectoryOperations(File target) {
        super(target);
    }

    @Override
    public void copy(File destination) {
        File newDir = new File(destination, target.getName());
        if (!newDir.mkdir()) {
            System.err.println("Failed to create directory: " + newDir.getName());
            return;
        }
        for (File file : target.listFiles()) {
            if (file.isDirectory()) {
                new DirectoryOperations(file).copy(newDir);
            } else {
                new FileOperations(file).copy(newDir);
            }
        }
        System.out.println("Directory copied to " + destination.getAbsolutePath());
    }

    @Override
    public void paste(File destination) {
        // Implementation can be added later
        System.out.println("Pasting directory to " + destination.getAbsolutePath());
    }

    @Override
    public void delete() {
        for (File file : target.listFiles()) {
            if (file.isDirectory()) {
                new DirectoryOperations(file).delete();
            } else {
                new FileOperations(file).delete();
            }
        }
        if (target.delete()) {
            System.out.println("Directory deleted: " + target.getName());
        } else {
            System.err.println("Failed to delete directory: " + target.getName());
        }
    }
}

