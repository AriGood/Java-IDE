package use_case.EditorOperations;

import app.IDEAppBuilder;
import entity.LeftIDEJTabbedPane;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class EditorOperationsTest {

    @Test
    public void closeAbstractTabLeft() throws IOException {
        IDEAppBuilder app = new IDEAppBuilder();
        app.build();
        app.buildIDE();
        LeftIDEJTabbedPane tabbedPane = new LeftIDEJTabbedPane(app);

        // Create temporary files for testing
        File[] tempFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            // Optional: Write some content to the file to avoid empty file reading issues
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
        }

        // Add tabs using the created files
        for (File tempFile : tempFiles) {
            EditorOperations.addTab(tempFile, tabbedPane);
        }

        // Remove a file and close the corresponding tab
        File removeFile = tempFiles[2]; // Use one of the temp files for removal
        EditorOperations.closeAbstractTab(app, removeFile);

        // Assert that the file is no longer in the tabbed pane (i.e., not a duplicate)
        assertTrue(EditorOperations.isDuplicate(removeFile, tabbedPane));

        // Clean up the temporary files
        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void isDuplicate() {
    }

    @Test
    public void addTab() {
    }

    @Test
    public void createTabHeader() {
    }

    @Test
    public void createCloseButton() {
    }

    @Test
    public void closeTab() {
    }

    @Test
    public void getFileIndex() {
    }

    @Test
    public void closeAllTabs() {
    }

    @Test
    public void closeOtherTabs() {
    }

    @Test
    public void closeTabsToLeft() {
    }

    @Test
    public void closeTabsToRight() {
    }

    @Test
    public void mergeTab() {
    }

    @Test
    public void splitTab() {
    }
}