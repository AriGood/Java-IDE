package use_case.FileOperations;

import org.junit.Test;
import use_case.FileManagement.DirectoryOperations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class DirectoryOperationsTest {

    @Test
    public void testCopyDirectory() throws IOException {
        // Create a temporary directory with files
        File tempSourceDir = Files.createTempDirectory("sourceDir").toFile();
        File tempFile = new File(tempSourceDir, "testFile.txt");
        assertTrue("File creation should succeed", tempFile.createNewFile());

        // Create a destination directory
        File tempDestDir = Files.createTempDirectory("destDir").toFile();

        // Perform directory copy
        DirectoryOperations dirOps = new DirectoryOperations(tempSourceDir);
        dirOps.copy(tempDestDir);

        File copiedDir = new File(tempDestDir, tempSourceDir.getName());
        File copiedFile = new File(copiedDir, tempFile.getName());

        assertTrue("Copied directory should exist", copiedDir.exists() && copiedDir.isDirectory());
        assertTrue("Copied file should exist in the copied directory", copiedFile.exists());

        // Clean up
        deleteDirectory(tempSourceDir);
        deleteDirectory(tempDestDir);
    }

    @Test
    public void testDeleteDirectory() throws IOException {
        // Create a temporary directory with files
        File tempDir = Files.createTempDirectory("deleteDir").toFile();
        File tempFile = new File(tempDir, "testFile.txt");
        assertTrue("File creation should succeed", tempFile.createNewFile());

        // Perform directory deletion
        DirectoryOperations dirOps = new DirectoryOperations(tempDir);
        dirOps.delete();

        assertFalse("Directory should be deleted", tempDir.exists());

        // Clean up (if needed)
        if (tempDir.exists()) {
            deleteDirectory(tempDir);
        }
    }

    @Test
    public void testPasteDirectory() throws IOException {
        // Create a temporary source directory
        File tempSourceDir = Files.createTempDirectory("sourceDir").toFile();
        File tempFile = new File(tempSourceDir, "testFile.txt");
        assertTrue("File creation should succeed", tempFile.createNewFile());

        // Create a destination directory
        File tempDestDir = Files.createTempDirectory("destDir").toFile();

        // Perform paste operation (copy functionality)
        DirectoryOperations dirOps = new DirectoryOperations(tempSourceDir);
        dirOps.paste(tempDestDir);

        File pastedDir = new File(tempDestDir, tempSourceDir.getName());
        File pastedFile = new File(pastedDir, tempFile.getName());

        assertTrue("Pasted directory should exist", pastedDir.exists() && pastedDir.isDirectory());
        assertTrue("Pasted file should exist in the pasted directory", pastedFile.exists());

        // Clean up
        deleteDirectory(tempSourceDir);
        deleteDirectory(tempDestDir);
    }

    /**
     * Helper method to delete a directory recursively.
     */
    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}

