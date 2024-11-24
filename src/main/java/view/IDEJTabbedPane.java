package view;

import app.IDEAppBuilder;
import data_access.AutoCompleteBST;
import entity.Editor;
import entity.EditorObj;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IDEJTabbedPane extends JTabbedPane {

    private List<EditorObj> editorObjs = new ArrayList<>();

    public IDEJTabbedPane(IDEAppBuilder appBuilder) {
        this.addChangeListener(e -> {
            int currentTabIndex = getSelectedIndex();
            if (currentTabIndex != -1) {
                JTextArea currentTextArea = editorObjs.get(currentTabIndex).getTextArea();
                appBuilder.initializeAutoComplete(AutoCompleteBST.buildWithJavaKeywords(), currentTextArea);
            }
        });
    }

    private JButton newCloseButton() {
        JButton closeButton = new JButton("x");
        closeButton.setPreferredSize(new Dimension(15, 15));
        closeButton.setMargin(new Insets(0, 0, 5, 0));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        closeButton.addActionListener(e -> {
            closeTab();
        });
        return closeButton;
    }

    private void closeTab() {
        int currentTabIndex = getSelectedIndex();
        if (currentTabIndex != -1) {
            EditorObj currentEditorObj = editorObjs.get(currentTabIndex);
            use_case.FileManagement.FileOperations.saveFile(currentEditorObj.getFile(), currentEditorObj.getTextArea().getText());
            this.remove(currentTabIndex);
            this.editorObjs.remove(currentTabIndex);
        }
    }

    public void addTab(File file) {
        EditorObj editorObj = new EditorObj();
        editorObj.setFile(file);
        editorObj.setTextArea(FileOperations.fileContent(file));
        this.editorObjs.add(editorObj);
        JScrollPane newScrollPane = new JScrollPane(editorObj.getTextArea());
        newScrollPane.setRowHeaderView(editorObj.getLineNums());
        this.add(file.getName(), newScrollPane);
        this.setTabComponentAt(this.getTabCount() - 1, createTabHeader(file.getName()));

    }

    private JPanel createTabHeader(String title) {
        JPanel tabHeader = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        tabHeader.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        tabHeader.add(titleLabel, BorderLayout.WEST);
        tabHeader.add(newCloseButton(), BorderLayout.EAST);
        return tabHeader;
    }

}
