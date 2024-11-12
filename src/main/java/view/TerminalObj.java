package view;

import javax.swing.*;

public class TerminalObj {
    JTextArea textArea;

    public TerminalObj() {
        textArea = new JTextArea();
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
