package use_case.FileOperations;

import org.junit.Test;
import use_case.FileManagement.FileOperations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class FileOperationsTest {

    @Test
    public void testDeleteFile() throws IOException {
        // Create a temporary directory and file for testing
        File tempDirectory = Files.createTempDirectory("testDir").toFile();
        File newFile = new File(tempDirectory, "testFile.txt");
        assertTrue("File creation should succeed", newFile.createNewFile());

        FileOperations fileOps = new FileOperations(newFile);
        fileOps.delete();

        assertFalse("File should be deleted from the system", newFile.exists());

        // Clean up
        tempDirectory.delete();
    }

    @Test
    public void testCopyFile() throws IOException {
        // Create a temporary directory and file for testing
        File tempDirectory = Files.createTempDirectory("testDir").toFile();
        File sourceFile = new File(tempDirectory, "testFile.txt");
        assertTrue("File creation should succeed", sourceFile.createNewFile());

        // Create a subdirectory to test copy operation
        File destinationDir = new File(tempDirectory, "subDir");
        assertTrue("Subdirectory creation should succeed", destinationDir.mkdir());

        FileOperations fileOps = new FileOperations(sourceFile);
        fileOps.copy(destinationDir);

        File copiedFile = new File(destinationDir, sourceFile.getName());
        assertTrue("Copied file should exist in destination", copiedFile.exists());

        // Clean up
        sourceFile.delete();
        copiedFile.delete();
        destinationDir.delete();
        tempDirectory.delete();
    }

    @Test
    public void testSaveFile() throws IOException {
        // Create a temporary directory
        File tempDirectory = Files.createTempDirectory("testDir").toFile();
        File newFile = new File(tempDirectory, "testSaveFile.txt");

        String content = "This is a test content.";
        FileOperations.saveFile(newFile, content);

        assertTrue("File should be created", newFile.exists());
        assertEquals("File content should match", content, FileOperations.fileContent(newFile).trim());

        // Clean up
        newFile.delete();
        tempDirectory.delete();
    }

    @Test
    public void testFileContent() throws IOException {
        // Create a temporary directory and file
        File tempDirectory = Files.createTempDirectory("testDir").toFile();
        File newFile = new File(tempDirectory, "testReadFile.txt");
        String content = "Reading content test.";
        Files.write(newFile.toPath(), content.getBytes());

        String readContent = FileOperations.fileContent(newFile);
        assertEquals("Content read from file should match", content, readContent.trim());

        // Clean up
        newFile.delete();
        tempDirectory.delete();
    }
}
