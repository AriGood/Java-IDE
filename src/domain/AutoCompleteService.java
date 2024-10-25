package domain;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AutoCompleteService {
    // Define keywords for auto-complete (can be expanded)
    private final String[] keywords = {
            "public", "private", "protected", "class", "void", "static", "final",
            "int", "double", "float", "String", "new", "return", "if", "else",
            "for", "while", "try", "catch", "import", "package", "boolean",
            "true", "false"
    };

    public void enableAutoComplete(JTextComponent textComponent, JPopupMenu popup) {
        // Register a KeyListener to detect input and trigger suggestions
        textComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    String typedWord = getLastWord(textComponent);
                    if (!typedWord.isEmpty()) {
                        // Get matching suggestions and show popup if found
                        List<String> suggestions = getSuggestions(typedWord);
                        if (!suggestions.isEmpty()) {
                            showSuggestions(textComponent, popup, suggestions);
                        } else {
                            popup.setVisible(false);
                        }
                    }
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private String getLastWord(JTextComponent textComponent) throws BadLocationException {
        int caretPosition = textComponent.getCaretPosition();
        int wordStart = javax.swing.text.Utilities.getWordStart(textComponent, caretPosition);
        return textComponent.getText(wordStart, caretPosition - wordStart).trim();
    }

    private List<String> getSuggestions(String prefix) {
        List<String> suggestions = new ArrayList<>();
        for (String keyword : keywords) {
            if (keyword.startsWith(prefix)) {
                suggestions.add(keyword);
            }
        }
        return suggestions;
    }

    private void showSuggestions(JTextComponent textComponent, JPopupMenu popup, List<String> suggestions) {
        popup.removeAll();

        // Create menu items for each suggestion
        for (String suggestion : suggestions) {
            JMenuItem item = new JMenuItem(suggestion);
            item.addActionListener(e -> {
                try {
                    insertAutoCompleteSuggestion(textComponent, suggestion);
                    popup.setVisible(false);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            });
            popup.add(item);
        }

        // Show popup near the caret position
        try {
            int caretPosition = textComponent.getCaretPosition();
            int x = textComponent.modelToView(caretPosition).x;
            int y = textComponent.modelToView(caretPosition).y + textComponent.getFontMetrics(textComponent.getFont()).getHeight();
            popup.show(textComponent, x, y);
            popup.setVisible(true);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private void insertAutoCompleteSuggestion(JTextComponent textComponent, String suggestion) throws BadLocationException {
        int caretPosition = textComponent.getCaretPosition();
        int wordStart = javax.swing.text.Utilities.getWordStart(textComponent, caretPosition);

        // Replace the current word with the suggestion
        textComponent.getDocument().remove(wordStart, caretPosition - wordStart);
        textComponent.getDocument().insertString(wordStart, suggestion, null);
    }
}
