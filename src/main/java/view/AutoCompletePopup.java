package view;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.List;
import java.awt.*;

public class AutoCompletePopup extends AutoCompletePopupBase{
    private final JPopupMenu popup;

    public AutoCompletePopup() {
        popup = new JPopupMenu();
    }

    /**
     * Takes a text component and generates a suggestion based on a list of possible suggestions for the letter.
     * @param textComponent The text component for the suggestions.
     * @param suggestions The list of suggestions to display.
     * @param handler A handler to process the selection.
     */
    @Override
    public void showSuggestions(JTextComponent textComponent, List<String> suggestions, SuggestionSelectionHandler
            handler) {
        popup.removeAll();

        for (String suggestion : suggestions) {
            JMenuItem item = new JMenuItem(suggestion);
            item.addActionListener(event -> {
                handler.onSuggestionSelected(textComponent, suggestion);
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

    @Override
    public void hide() {
        popup.setVisible(false);
    }
}
