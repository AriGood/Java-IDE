package view;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.List;

public class AutoCompletePopup {
    private final JPopupMenu popup;
    public AutoCompletePopup() {
        popup = new JPopupMenu();
    }

    /**
     * Displays suggestions in a popup menu at the caret's position.
     * @param textComponent The text component where suggestions will appear.
     * @param suggestions The list of suggestions to display.
     */
    public void showSuggestions(JTextComponent textComponent, List<String> suggestions) {
        popup.removeAll();

        for (String suggestion : suggestions) {
            JMenuItem item = new JMenuItem(suggestion);
            item.addActionListener(event -> {
                replaceLastWord(textComponent, getLastWord(textComponent.getText()), suggestion);
                popup.setVisible(false);
            });
            popup.add(item);
        }

        if (!suggestions.isEmpty()) {
            Point caretPos = textComponent.getCaret().getMagicCaretPosition();
            if (caretPos != null) {
                popup.show(textComponent, caretPos.x, caretPos.y + 20); // Offset for visibility
            }
        } else {
            popup.setVisible(false);
        }
    }

    private void replaceLastWord(JTextComponent textComponent, String lastWord, String suggestion) {
        String text = textComponent.getText();
        int lastIndex = text.lastIndexOf(lastWord);
        textComponent.setText(text.substring(0, lastIndex) + suggestion);
    }

    private String getLastWord(String text) {
        String[] words = text.split("\\s+");
        return words.length > 0 ? words[words.length - 1] : "";
    }

    public void hide() {
        popup.setVisible(false);
    }
}
