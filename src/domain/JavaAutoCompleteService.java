package domain;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class JavaAutoCompleteService extends AutoCompleteService {
    private final String[] keywords = {"class", "public", "private", "protected", "void", "static", "if", "else", "for", "while"};

    @Override
    public void enableAutoComplete(JTextComponent textComponent, JPopupMenu popup) {
        // Logic to gather auto-complete suggestions based on current text
        String currentText = textComponent.getText();
        showPopup(popup, textComponent, keywords); // Show keywords as suggestions
    }
}
