package domain;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public abstract class AutoCompleteService {
    public abstract void enableAutoComplete(JTextComponent textComponent, JPopupMenu popup);

    protected void showPopup(JPopupMenu popup, JTextComponent textComponent, String[] suggestions) {
        popup.removeAll(); // Clear previous suggestions
        for (String suggestion : suggestions) {
            JMenuItem item = new JMenuItem(suggestion);
            item.addActionListener(e -> {
                textComponent.replaceSelection(suggestion); // Insert suggestion into the text component
                popup.setVisible(false); // Hide the popup after selection
            });
            popup.add(item);
        }
        popup.show(textComponent, textComponent.getCaret().getMagicCaretPosition().x, textComponent.getCaret().getMagicCaretPosition().y);
    }
}
