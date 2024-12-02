package use_case.EditorOperations;

import app.IDEAppBuilder;
import entity.EditorObj;
import entity.LeftIDEJTabbedPane;
import entity.ParentIDEJTabbedPane;
import entity.RightIDEJTabbedPane;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class  EditorOperations {

    public static void closeAbstractTab(IDEAppBuilder ideAppBuilder, File file) {
        EditorObj editorObj = new EditorObj();
        editorObj.setFile(file);
        if (isDuplicate(file, ideAppBuilder.getLeftEditorTabbedPane())) {
            LeftIDEJTabbedPane tabbedPane = ideAppBuilder.getLeftEditorTabbedPane();
            closeTab(EditorOperations.getFileIndex(tabbedPane, file), tabbedPane);
        } else {
            if (ideAppBuilder.getRightEditorTabbedPane() != null) {
                RightIDEJTabbedPane tabbedPane = ideAppBuilder.getRightEditorTabbedPane();
                closeTab(EditorOperations.getFileIndex(tabbedPane, file), tabbedPane);
            }
        }
    }

    public static boolean isDuplicate(File file, ParentIDEJTabbedPane tabbedPane) {
        for (int i = 0; i < tabbedPane.getEditorObjs().size(); i++) {
            if (tabbedPane.getEditorObjs().get(i).getFile().equals(file)) {
                tabbedPane.setSelectedIndex(i);
                return true;
            }
        }
        return false;
    }

    public static void addTab(File file, ParentIDEJTabbedPane tabbedPane) {
        EditorObj editorObj = new EditorObj();
        editorObj.setFile(file);
        editorObj.setTextArea(FileOperations.fileContent(file));

        if (isDuplicate(file, tabbedPane)) {
            return;
        }

        tabbedPane.getEditorObjs().add(editorObj);

        JScrollPane newScrollPane = new JScrollPane(editorObj.getTextArea());
        newScrollPane.setRowHeaderView(editorObj.getLineNums());
        tabbedPane.add(file.getName(), newScrollPane);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, createTabHeader(file, tabbedPane));
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    public static JPanel createTabHeader(File file, ParentIDEJTabbedPane tabbedPane) {
        JPanel tabHeader = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(file.getName());
        tabHeader.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        tabHeader.add(titleLabel, BorderLayout.WEST);
        tabHeader.add(createCloseButton(tabbedPane, file), BorderLayout.EAST);
        return tabHeader;
    }

    public static JButton createCloseButton(ParentIDEJTabbedPane tabbedPane, File file) {
        JButton closeButton = new JButton("x");
        closeButton.setPreferredSize(new Dimension(15, 15));
        closeButton.setMargin(new Insets(0, 0, 5, 0));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        closeButton.addActionListener(e -> closeTab(getFileIndex(tabbedPane, file), tabbedPane));
        return closeButton;
    }

    public static void closeTab(int index, ParentIDEJTabbedPane tabbedPane) {
        IDEAppBuilder ideAppBuilder = tabbedPane.getIdeAppBuilder();
        EditorObj editorObj = tabbedPane.getEditorObjs().get(index);
        FileOperations.saveFile(editorObj.getFile(), editorObj.getTextArea().getText());
        tabbedPane.remove(index);
        tabbedPane.getEditorObjs().remove(index);
        if (tabbedPane.getEditorObjs().isEmpty() && tabbedPane instanceof RightIDEJTabbedPane) {
            ideAppBuilder.getLeftRightSplitPane().setRightComponent(ideAppBuilder.getLeftEditorTabbedPane());
            ideAppBuilder.setRightEditorTabbedPane(null);
        }
        ideAppBuilder.getFrame().revalidate();
        ideAppBuilder.getFrame().repaint();
    }

    public static int getFileIndex(ParentIDEJTabbedPane tabbedPane, File file) {
        for (int i = 0; i < tabbedPane.getEditorObjs().size(); i++) {
            if (tabbedPane.getEditorObjs().get(i).getFile().equals(file)) {
                return i;
            }
        }
        return -1;
    }

    public static void closeAllTabs(ParentIDEJTabbedPane tabbedPane) {
        while (tabbedPane.getTabCount() > 0) {
            closeTab(0, tabbedPane);
        }
    }

    public static void closeOtherTabs(int index, ParentIDEJTabbedPane tabbedPane) {
        if (index != -1) {
            EditorObj keepEditor = tabbedPane.getEditorObjs().get(index);
            closeAllTabs(tabbedPane);
            addTab(keepEditor.getFile(), tabbedPane);
        }
    }

    public static void closeTabsToLeft(int index, ParentIDEJTabbedPane tabbedPane) {
        while (index > 0) {
            closeTab(0, tabbedPane);
            index--;
        }
    }

    public static void closeTabsToRight(int index, ParentIDEJTabbedPane tabbedPane) {
        while (tabbedPane.getTabCount() > index + 1) {
            closeTab(index + 1, tabbedPane);
        }
    }

    public static void mergeTab(int selectedIndex, RightIDEJTabbedPane tabbedPane, IDEAppBuilder ideAppBuilder) {
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

    public static void splitTab(int selectedIndex, LeftIDEJTabbedPane tabbedPane, IDEAppBuilder ideAppBuilder) {
        EditorObj editorObj = tabbedPane.getEditorObjs().get(selectedIndex);
        RightIDEJTabbedPane newTabbedPane = new RightIDEJTabbedPane(ideAppBuilder);
        EditorOperations.addTab(editorObj.getFile(), newTabbedPane);
        EditorOperations.closeTab(selectedIndex, tabbedPane);

        if (ideAppBuilder.getRightEditorTabbedPane() != null) {
            EditorOperations.addTab(editorObj.getFile(), ideAppBuilder.getRightEditorTabbedPane());
        } else {
            JSplitPane newSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, newTabbedPane);
            ideAppBuilder.splitEditor(newSplitPane);
        }
    }
}
