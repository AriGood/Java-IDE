package view;

import app.IDEAppBuilder;
import data_access.AutoCompleteBST;
import entity.EditorObj;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
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

    public void addTab(File file) {
        EditorObj editorObj = new EditorObj();
        editorObj.setTextArea(FileOperations.fileContent(file));
        this.editorObjs.add(editorObj);
        JScrollPane newScrollPane = new JScrollPane(editorObj.getTextArea());
        newScrollPane.setRowHeaderView(editorObj.getLineNums());
        this.add(file.getName(), newScrollPane);

    }

}
