package use_caseTest.EditorOperationsTest;

import app.IdeAppBuilder;
import entity.LeftIdeJtabbedPane;
import entity.RightIdeJtabbedPane;
import org.junit.Test;
import use_case.EditorManagement.EditorOperations;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class EditorOperationsTest {

    private IdeAppBuilder makeTestBuilder() {
        IdeAppBuilder app = new IdeAppBuilder();
        app.build();
        app.buildIde();

        return app;
    }

    @Test
    public void testCloseAbstractTabLeft() throws IOException {
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);
        RightIdeJtabbedPane rightTabbedPane = new RightIdeJtabbedPane(app);

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
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);
        RightIdeJtabbedPane rightTabbedPane = new RightIdeJtabbedPane(app);

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
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);

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
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);

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
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);
        RightIdeJtabbedPane rightTabbedPane = new RightIdeJtabbedPane(app);

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
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);
        RightIdeJtabbedPane rightTabbedPane = new RightIdeJtabbedPane(app);

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
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);

        File[] tempFiles = new File[5];
        for (int i = 0; i < 5; i++) {
            Path tempFilePath = Files.createTempFile("newFile" + i, ".txt");
            tempFiles[i] = tempFilePath.toFile();
            Files.write(tempFilePath, ("Content of file " + i).getBytes());
            EditorOperations.addTab(tempFilePath.toFile(), leftTabbedPane);
        }

        EditorOperations.closeAllTabs(leftTabbedPane);
        for (File tempFile : tempFiles) {
            assertFalse(EditorOperations.isDuplicate(tempFile, leftTabbedPane));
        }

        for (File tempFile : tempFiles) {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testCloseOtherTabs() throws IOException {
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);

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
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);

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
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);

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
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);
        RightIdeJtabbedPane rightTabbedPane = new RightIdeJtabbedPane(app);
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
        IdeAppBuilder app = makeTestBuilder();
        LeftIdeJtabbedPane leftTabbedPane = new LeftIdeJtabbedPane(app);

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