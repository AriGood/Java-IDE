package use_case.EditorManagement;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import app.IdeAppBuilder;
import entity.EditorObj;
import entity.LeftIdeJtabbedPane;
import entity.ParentIdeJtabbedPane;
import entity.RightIdeJtabbedPane;
import use_case.FileManagement.FileOperations;

public class EditorOperations {

    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    private static final int TOP_BOTTOM_MARGIN = 5;
    private static final int LEFT_RIGHT_MARGIN = 10;

    private EditorOperations() {
        throw new UnsupportedOperationException("EditorManagement is a utility class and cannot be instantiated.");
    }

    /**
     * Closes a tab for the specified file, checking if it exists in the left or right editor tabbed pane.
     * If the file exists in the left pane, it is closed from there; otherwise, it is closed from the right pane.
     *
     * @param ideAppBuilder the application builder containing the editor tabbed panes
     * @param file the file whose tab is to be closed
     */
    public static void closeAbstractTab(IdeAppBuilder ideAppBuilder, File file) {
        EditorObj editorObj = new EditorObj();
        editorObj.setFile(file);
        if (isDuplicate(file, ideAppBuilder.getLeftEditorTabbedPane())) {
            LeftIdeJtabbedPane tabbedPane = ideAppBuilder.getLeftEditorTabbedPane();
            closeTab(EditorOperations.getFileIndex(tabbedPane, file), tabbedPane);
        }
        else {
            if (ideAppBuilder.getRightEditorTabbedPane() != null) {
                RightIdeJtabbedPane tabbedPane = ideAppBuilder.getRightEditorTabbedPane();
                closeTab(EditorOperations.getFileIndex(tabbedPane, file), tabbedPane);
            }
        }
    }

    /**
     * Checks if the specified file already exists in the given tabbed pane.
     * If the file exists, the corresponding tab is selected.
     *
     * @param file the file to check for duplicates
     * @param tabbedPane the tabbed pane to check for the file
     * @return true if the file is found in the tabbed pane, false otherwise
     */
    public static boolean isDuplicate(File file, ParentIdeJtabbedPane tabbedPane) {
        for (int i = 0; i < tabbedPane.getEditorObjs().size(); i++) {
            if (tabbedPane.getEditorObjs().get(i).getFile().equals(file)) {
                tabbedPane.setSelectedIndex(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new tab for the specified file to the given tabbed pane.
     * If the file is already open in the tabbed pane, no new tab is created.
     *
     * @param file the file to be added to the tabbed pane
     * @param tabbedPane the tabbed pane to add the file to
     */
    public static void addTab(File file, ParentIdeJtabbedPane tabbedPane) {
        if (!isDuplicate(file, tabbedPane)) {
            EditorObj editorObj = new EditorObj();
            editorObj.setFile(file);
            editorObj.setTextArea(FileOperations.fileContent(file));

            tabbedPane.getEditorObjs().add(editorObj);

            JScrollPane newScrollPane = new JScrollPane(editorObj.getTextArea());
            newScrollPane.setRowHeaderView(editorObj.getLineNums());
            tabbedPane.add(file.getName(), newScrollPane);
            tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, createTabHeader(file, tabbedPane));
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        }
    }

    /**
     * Creates a custom header for a tab containing the file name and a close button.
     *
     * @param file the file for which the tab header is being created
     * @param tabbedPane the tabbed pane containing the tab
     * @return a JPanel containing the tab header with the file name and close button
     */
    public static JPanel createTabHeader(File file, ParentIdeJtabbedPane tabbedPane) {
        JPanel tabHeader = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(file.getName());
        tabHeader.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, LEFT_RIGHT_MARGIN));
        tabHeader.add(titleLabel, BorderLayout.WEST);
        tabHeader.add(createCloseButton(tabbedPane, file), BorderLayout.EAST);
        return tabHeader;
    }

    /**
     * Creates a close button for the tab header, which allows closing the tab.
     *
     * @param tabbedPane the tabbed pane containing the tab
     * @param file the file associated with the tab
     * @return a JButton that will close the tab when clicked
     */
    public static JButton createCloseButton(ParentIdeJtabbedPane tabbedPane, File file) {
        JButton closeButton = new JButton("x");
        closeButton.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        closeButton.setMargin(new Insets(0, 0, TOP_BOTTOM_MARGIN, 0));
        closeButton.setBorder(BorderFactory.createEmptyBorder(TOP_BOTTOM_MARGIN, 0, TOP_BOTTOM_MARGIN, 0));
        closeButton.addActionListener(closeButtonListener -> closeTab(getFileIndex(tabbedPane, file), tabbedPane));
        return closeButton;
    }

    /**
     * Closes a tab at the specified index in the given tabbed pane.
     * The file content is saved before the tab is removed.
     *
     * @param index the index of the tab to close
     * @param tabbedPane the tabbed pane containing the tab
     */
    public static void closeTab(int index, ParentIdeJtabbedPane tabbedPane) {
        EditorObj editorObj = tabbedPane.getEditorObjs().get(index);
        FileOperations.saveFile(editorObj.getFile(), editorObj.getTextArea().getText());
        IdeAppBuilder ideAppBuilder = tabbedPane.getIdeAppBuilder();
        tabbedPane.remove(index);
        tabbedPane.getEditorObjs().remove(index);
        if (tabbedPane.getEditorObjs().isEmpty() && tabbedPane instanceof RightIdeJtabbedPane) {
            ideAppBuilder.getLeftRightSplitPane().setRightComponent(ideAppBuilder.getLeftEditorTabbedPane());
            ideAppBuilder.setRightEditorTabbedPane(null);
        }
        ideAppBuilder.getFrame().revalidate();
        ideAppBuilder.getFrame().repaint();
    }

    /**
     * Retrieves the index of the tab associated with the specified file in the given tabbed pane.
     *
     * @param tabbedPane the tabbed pane to search in
     * @param file the file whose tab index is to be found
     * @return the index of the tab, or -1 if the file is not found
     */
    public static int getFileIndex(ParentIdeJtabbedPane tabbedPane, File file) {
        for (int i = 0; i < tabbedPane.getEditorObjs().size(); i++) {
            if (tabbedPane.getEditorObjs().get(i).getFile().equals(file)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Closes all tabs in the given tabbed pane by iteratively closing each one.
     *
     * @param tabbedPane the tabbed pane whose tabs are to be closed
     */
    public static void closeAllTabs(ParentIdeJtabbedPane tabbedPane) {
        while (tabbedPane.getTabCount() > 0) {
            closeTab(0, tabbedPane);
        }
    }

    /**
     * Closes all tabs in the given tabbed pane, except for the one at the specified index.
     *
     * @param index the index of the tab to keep open
     * @param tabbedPane the tabbed pane from which to close other tabs
     */
    public static void closeOtherTabs(int index, ParentIdeJtabbedPane tabbedPane) {
        if (index != -1) {
            EditorObj keepEditor = tabbedPane.getEditorObjs().get(index);
            closeTabsToLeft(index, tabbedPane);
            closeTabsToRight(0, tabbedPane);
        }
    }

    /**
     * Closes all tabs to the left of the specified index in the given tabbed pane.
     *
     * @param currentIndex the index of the tab from which to start closing tabs to the left
     * @param tabbedPane the tabbed pane containing the tabs
     */
    public static void closeTabsToLeft(int currentIndex, ParentIdeJtabbedPane tabbedPane) {
        int index = currentIndex;
        while (index > 0) {
            closeTab(0, tabbedPane);
            index--;
        }
    }

    /**
     * Closes all tabs to the right of the specified index in the given tabbed pane.
     *
     * @param index the index of the tab from which to start closing tabs to the right
     * @param tabbedPane the tabbed pane containing the tabs
     */
    public static void closeTabsToRight(int index, ParentIdeJtabbedPane tabbedPane) {
        while (tabbedPane.getTabCount() > index + 1) {
            closeTab(index + 1, tabbedPane);
        }
    }

    /**
     * Merges the tab at the specified index from the right tabbed pane into the left tabbed pane.
     * The tab is removed from the right pane and added to the left pane.
     *
     * @param selectedIndex the index of the tab to merge
     * @param tabbedPane the right tabbed pane from which to merge the tab
     * @param ideAppBuilder the application builder that manages the editor panes
     */
    public static void mergeTab(int selectedIndex, RightIdeJtabbedPane tabbedPane, IdeAppBuilder ideAppBuilder) {
        tabbedPane.setSelectedIndex(selectedIndex);
        EditorObj editorObj = tabbedPane.getEditorObjs().get(selectedIndex);
        EditorOperations.closeTab(selectedIndex, tabbedPane);
        EditorOperations.addTab(editorObj.getFile(), ideAppBuilder.getLeftEditorTabbedPane());

        if (tabbedPane.getEditorObjs().isEmpty()) {
            ideAppBuilder.getLeftRightSplitPane().setRightComponent(ideAppBuilder.getLeftEditorTabbedPane());
            ideAppBuilder.setRightEditorTabbedPane(null);
        }
        ideAppBuilder.getFrame().revalidate();
        ideAppBuilder.getFrame().repaint();

    }

    /**
     * Splits the tab at the specified index from the left tabbed pane into a new right tabbed pane.
     * If a right tabbed pane doesn't exist, it is created and the split occurs.
     *
     * @param selectedIndex the index of the tab to split
     * @param tabbedPane the left tabbed pane containing the tab
     * @param ideAppBuilder the application builder that manages the editor panes
     */
    public static void splitTab(int selectedIndex, LeftIdeJtabbedPane tabbedPane, IdeAppBuilder ideAppBuilder) {
        EditorObj editorObj = tabbedPane.getEditorObjs().get(selectedIndex);
        RightIdeJtabbedPane newTabbedPane = new RightIdeJtabbedPane(ideAppBuilder);
        EditorOperations.addTab(editorObj.getFile(), newTabbedPane);
        EditorOperations.closeTab(selectedIndex, tabbedPane);

        if (ideAppBuilder.getRightEditorTabbedPane() != null) {
            EditorOperations.addTab(editorObj.getFile(), ideAppBuilder.getRightEditorTabbedPane());
        }
        else {
            JSplitPane newSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, newTabbedPane);
            ideAppBuilder.splitEditor(newSplitPane);
        }
    }
}
