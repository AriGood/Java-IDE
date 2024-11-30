package entity;

import app.IDEAppBuilder;
import data_access.AutoCompleteBST;
import use_case.EditorOperations.EditorOperations;
import use_case.FileManagement.FileOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RightIDEJTabbedPane extends ParentIDEJTabbedPane {

    public RightIDEJTabbedPane(IDEAppBuilder ideAppBuilder) {
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
