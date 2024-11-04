package domain;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AutoCompleteService {

    public void enableAutoComplete(JTextComponent textComponent, JPopupMenu popup) {
        // Sample suggestions for demonstration
        String[] suggestions = {"public", "private", "protected", "class", "void", "int", "String", "if", "else"};

        textComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = textComponent.getText();
                String lastWord = getLastWord(text);
                if (lastWord.length() > 0) {
                    popup.removeAll(); // Clear previous suggestions

                    for (String suggestion : suggestions) {
                        if (suggestion.startsWith(lastWord)) {
                            JMenuItem item = new JMenuItem(suggestion);
                            item.addActionListener(actionEvent -> {
                                textComponent.setText(text.substring(0, text.length() - lastWord.length()) + suggestion + " ");
                                popup.setVisible(false); // Hide the popup after selection
                            });
                            popup.add(item);
                        }
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
        return words[words.length - 1];
    }
}
