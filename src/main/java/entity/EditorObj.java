package entity;

import javax.swing.*;
import java.awt.*;

public class EditorObj {
    private JTextArea textArea;
    private JTextArea lineNums;

    public EditorObj() {
        textArea = new JTextArea();
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 1; i <= 500; i++) {
            String digits = String.valueOf(i);
            if (digits.length() == 1) {
                strBuilder.append(" ").append(i).append("     |").append("\n");
            } else if (digits.length() == 2) {
                strBuilder.append(" ").append(i).append("   |").append("\n");
            } else {
                strBuilder.append(" ").append(i).append(" |").append("\n");
            }
        }
        lineNums = new JTextArea(strBuilder.toString());
        lineNums.setEditable(false);
        lineNums.setBackground(Color.WHITE);
        lineNums.setColumns(4);
    }

    public void setTextArea (String text) {
        textArea.setText(text);
    }

    public void updateTextArea(String newText) {
        textArea.setText(newText);
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JTextArea getLineNums() {
        return lineNums;
    }
}
