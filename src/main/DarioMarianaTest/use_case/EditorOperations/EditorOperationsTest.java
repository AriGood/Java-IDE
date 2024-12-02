package use_case.EditorOperations;

import app.IDEAppBuilder;
import entity.LeftIDEJTabbedPane;
import entity.RightIDEJTabbedPane;
import org.junit.Test;

import javax.swing.*;
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
    public void testIsDuplicate() throws IOException {
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
    public void testCloseTab() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);
        RightIDEJTabbedPane rightTabbedPane = new RightIDEJTabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 4; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        Path newFile = Files.createTempFile("newFile" + "4", ".txt");
        tempFiles[4] = newFile.toFile();
        Files.write(newFile, ("Content of file " + "4").getBytes());
        EditorOperations.addTab(tempFiles[4], rightTabbedPane);

        EditorOperations.closeTab(0, rightTabbedPane);
        EditorOperations.closeTab(2, leftTabbedPane);
        assertEquals(0, rightTabbedPane.getTabCount());
        assertFalse(EditorOperations.isDuplicate(tempFiles[2], leftTabbedPane));

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testGetFileIndex() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);
        RightIDEJTabbedPane rightTabbedPane = new RightIDEJTabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 4; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        Path newFile = Files.createTempFile("newFile" + "4", ".txt");
        tempFiles[4] = newFile.toFile();
        Files.write(newFile, ("Content of file " + "4").getBytes());
        EditorOperations.addTab(tempFiles[4], rightTabbedPane);

        assertEquals(EditorOperations.getFileIndex(leftTabbedPane, tempFiles[1]),1);
        assertEquals(EditorOperations.getFileIndex(rightTabbedPane, tempFiles[4]),0);

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testCloseAllTabs() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        EditorOperations.closeAllTabs(leftTabbedPane);
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
    public void testCloseOtherTabs() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        EditorOperations.closeOtherTabs(2, leftTabbedPane);
        for (File tempFile : tempFiles) {
            assertTrue(!EditorOperations.isDuplicate(tempFile, leftTabbedPane) || tempFile == tempFiles[2]);
        }

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testCloseTabsToLeft() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        EditorOperations.closeTabsToLeft(2, leftTabbedPane);
        for (int i = 0; i < 5; i++) {
            assertTrue(EditorOperations.isDuplicate(tempFiles[i], leftTabbedPane) || i < 2);
        }

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testCloseTabsToRight() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        EditorOperations.closeTabsToRight(2, leftTabbedPane);
        for (int i = 0; i < 5; i++) {
            assertTrue(EditorOperations.isDuplicate(tempFiles[i], leftTabbedPane) || i > 2);
        }

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testMergeTab() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);
        RightIDEJTabbedPane rightTabbedPane = new RightIDEJTabbedPane(app);
        JSplitPane newSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTabbedPane, rightTabbedPane);
        app.splitEditor(newSplitPane);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 4; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        Path newFile = Files.createTempFile("newFile" + "4", ".txt");
        tempFiles[4] = newFile.toFile();
        Files.write(newFile, ("Content of file " + "4").getBytes());
        EditorOperations.addTab(tempFiles[4], rightTabbedPane);

        EditorOperations.mergeTab(0, rightTabbedPane, app);
        assertTrue(EditorOperations.isDuplicate(tempFiles[4], leftTabbedPane));
        assertFalse(EditorOperations.isDuplicate(tempFiles[4], rightTabbedPane));

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testSplitTab() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
        LeftIDEJTabbedPane leftTabbedPane = new LeftIDEJTabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        EditorOperations.splitTab(2, leftTabbedPane, app);
        for (int i = 0; i < 5; i++) {
            assertTrue(EditorOperations.isDuplicate(tempFiles[i], leftTabbedPane) || i == 2);
        }
        assertTrue(EditorOperations.isDuplicate(tempFiles[2], app.getRightEditorTabbedPane()));

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}