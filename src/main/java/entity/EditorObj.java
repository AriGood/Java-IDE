package entity;

import java.awt.Color;
import java.io.File;

import javax.swing.JTextArea;

public class EditorObj {
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\n";
    private static final int NUM_COLUMNS = 4;
    private static final int NUM_LINES = 500;

    private final JTextArea textArea;
    private final JTextArea lineNums;
    private File file;

    public EditorObj() {
        textArea = new JTextArea();
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 1; i <= NUM_LINES; i++) {
            String digits = String.valueOf(i);
            if (digits.length() == 1) {
                strBuilder.append(SPACE).append(i).append("     |").append(NEW_LINE);
            }
            else if (digits.length() == 2) {
                strBuilder.append(SPACE).append(i).append("   |").append(NEW_LINE);
            }
            else {
                strBuilder.append(SPACE).append(i).append(" |").append(NEW_LINE);
            }
        }
        lineNums = new JTextArea(strBuilder.toString());
        lineNums.setEditable(false);
        lineNums.setBackground(Color.WHITE);
        lineNums.setColumns(NUM_COLUMNS);
    }

    /**
     * Updates the current EditorObj's text area with the new text.
     * @param text the String to be set as the text area's new text.
     */
    public void setTextArea(String text) {
        textArea.setText(text);
    }

    /**
     * Updates the current EditorObj's associated file with the new File.
     * @param file the File to be set as the EditorObj's associated file.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Returns the JTextArea Associated with this EditorObj.
     * @return JTextArea associated with this EditorObj
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * Returns the line numbers created for this EditorObj.
     * @return JTextArea associated with this EditorObj's line numbers.
     */
    public JTextArea getLineNums() {
        return lineNums;
    }

    public String getContent() {
        return textArea.getText();
    }

    /**
     * Returns the File Associated with this EditorObj.
     * @return File associated with this EditorObj
     */
    public File getFile() {
        return file;
    }
}
