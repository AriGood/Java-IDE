package view;

import app.IDEAppBuilder;

import javax.swing.*;
import java.awt.*;

public class EditorObj {
    private JTextArea textArea;
    private JTextArea lineNums;

    public void EditorObj() {
        textArea = new JTextArea();
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 1; i <= 500; i++) {
            strBuilder.append(i).append("\n");
        }
        lineNums = new JTextArea(strBuilder.toString());
        lineNums.setEditable(false);
        lineNums.setBackground(Color.LIGHT_GRAY);
        textArea.setText(IDEAppBuilder.tabManagement.currentEditor.toString());
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JTextArea getLineNums() {
        return lineNums;
    }
}
