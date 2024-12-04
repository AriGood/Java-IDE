/**
 * Unit tests for EditorOperations functionalities including tab management, file handling, and editor operations.
 */
package use_caseTest.EditorOperationsTest;

import app.IDEAppBuilder;
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

    /**
     * Utility method to create and initialize an IDEAppBuilder instance for testing.
     *
     * @return a fully built instance of IDEAppBuilder
     */
    private IDEAppBuilder makeTestBuilder() {
        IDEAppBuilder app = new IDEAppBuilder();
        app.build();
        app.buildIde();

        return app;
    }

    /**
     * Test for closing a tab associated with a file on the left tabbed pane.
     * Ensures correct tab and duplication behavior.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testCloseAbstractTabLeft() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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

    /**
     * Test for closing a tab associated with a file on the right tabbed pane.
     * Verifies duplication checks and proper tab removal.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testCloseAbstractTabRight() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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

    /**
     * Test for verifying duplicate files within a specified tabbed pane.
     * Ensures accurate identification of duplicate tabs.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testIsDuplicate() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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

    /**
     * Test for adding tabs to a tabbed pane. Confirms tabs are properly added
     * and duplicates identified accurately.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testAddTab() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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

    /**
     * Test for closing individual tabs. Ensures correct removal of tabs
     * from tabbed panes and duplication checks.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testCloseTab() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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

    /**
     * Test for retrieving the index of a file in a specified tabbed pane.
     * Ensures accurate indexing of tabs.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testGetFileIndex() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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

    /**
     * Test for closing all tabs in a tabbed pane. Verifies complete tab
     * removal and updates to duplication checks.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testCloseAllTabs() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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

    /**
     * Test for closing all tabs except one in a tabbed pane. Confirms other
     * tabs are removed and only the specified tab remains.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testCloseOtherTabs() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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

    /**
     * Test for closing tabs to the left of a specified index in a tabbed pane.
     * Ensures accurate tab closure and duplication updates.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testCloseTabsToLeft() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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

    /**
     * Test for closing tabs to the right of a specified index in a tabbed pane.
     * Confirms tabs are removed correctly based on index.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testCloseTabsToRight() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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

    /**
     * Test for merging a tab from one tabbed pane into another. Validates
     * proper transfer and duplication checks.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testMergeTab() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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

    /**
     * Test for splitting a tab from one tabbed pane into another. Ensures tab
     * is moved correctly and duplicates updated.
     *
     * @throws IOException if file handling operations fail
     */
    @Test
    public void testSplitTab() throws IOException {
        IDEAppBuilder app = makeTestBuilder();
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