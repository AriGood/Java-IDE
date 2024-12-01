package use_case.EditorOperations;

import app.IDEAppBuilder;
import entity.LeftIDEJTabbedPane;
import entity.RightIDEJTabbedPane;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class EditorOperationsTest {

    private IDEAppBuilder makeTestBuilder() {
        IDEAppBuilder app = new IDEAppBuilder();
        app.build();
        app.buildIDE();

        return app;
    }

    @Test
    public void testCloseAbstractTabLeft() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);
        RightIDEJTabbedPane rightTabbedPane = new RightIDEJTabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        File removeFile = tempFiles[2];
        EditorOperations.closeAbstractTab(app, removeFile);

        assertTrue(EditorOperations.isDuplicate(removeFile, leftTabbedPane));
        assertFalse(EditorOperations.isDuplicate(removeFile, rightTabbedPane));

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testCloseAbstractTabRight() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);
        RightIDEJTabbedPane rightTabbedPane = new RightIDEJTabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), rightTabbedPane);
        }

        File removeFile = tempFiles[2];
        EditorOperations.closeAbstractTab(app, removeFile);

        assertTrue(EditorOperations.isDuplicate(removeFile, rightTabbedPane));
        assertFalse(EditorOperations.isDuplicate(removeFile, leftTabbedPane));

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testsDuplicate() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        assertTrue(EditorOperations.isDuplicate(tempFiles[0], leftTabbedPane));

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }

    }

    @Test
    public void testAddTab() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        for (File tempFile : tempFiles) {
            assertTrue(EditorOperations.isDuplicate(tempFile, leftTabbedPane));
        }

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testCreateTabHeader() {
    }

    @Test
    public void testCreateCloseButton() {
    }

    @Test
    public void testCloseTab() {
    }

    @Test
    public void testGetFileIndex() {
    }

    @Test
    public void testCloseAllTabs() {
    }

    @Test
    public void testCloseOtherTabs() {
    }

    @Test
    public void testCloseTabsToLeft() {
    }

    @Test
    public void testCloseTabsToRight() {
    }

    @Test
    public void testMergeTab() {
    }

    @Test
    public void testSplitTab() {
    }
}