package domain;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public abstract class AutoCompleteService {

    public void enableAutoComplete(JTextComponent textComponent, JPopupMenu popup) {
        textComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = textComponent.getText();
                String lastWord = getLastWord(text);

                if (lastWord.length() > 0) {
                    List<String> suggestions = getSuggestions(lastWord);
                    popup.removeAll(); // Clear previous suggestions

                    for (String suggestion : suggestions) {
                        JMenuItem item = new JMenuItem(suggestion);
                        item.addActionListener(actionEvent -> {
                            // Replace last word with selected suggestion
                            textComponent.setText(text.substring(0, text.length() - lastWord.length()) + suggestion + " ");
                            popup.setVisible(false); // Hide popup after selection
                        });
                        popup.add(item);
                    }

                    if (popup.getComponentCount() > 0) {
                        popup.show(textComponent, textComponent.getCaret().getMagicCaretPosition().x, textComponent.getCaret().getMagicCaretPosition().y);
                    }
                } else {
                    popup.setVisible(false);
                }
            }
        });
    }

    private String getLastWord(String text) {
        String[] words = text.split("\\s+");
        return words.length > 0 ? words[words.length - 1] : "";
    }

    // Abstract method to get suggestions for a given prefix
    protected abstract List<String> getSuggestions(String prefix);
}
