package entity;

import app.IDEAppBuilder;
import data_access.AutoCompleteBST;
import use_case.EditorOperations.EditorOperations;
import use_case.PopupMenuOperations.PopupMenuOperations;

import javax.swing.*;

public class LeftIDEJTabbedPane extends ParentIDEJTabbedPane {

    public LeftIDEJTabbedPane(IDEAppBuilder ideAppBuilder) {
        super(ideAppBuilder);

        this.addChangeListener(e -> {
            int currentIndex = getSelectedIndex();
            if (currentIndex != -1) {
                JTextArea currentTextArea = editorObjs.get(currentIndex).getTextArea();
                ideAppBuilder.initializeAutoComplete(AutoCompleteBST.buildWithJavaKeywords(), currentTextArea);
            }
        });
    }
}