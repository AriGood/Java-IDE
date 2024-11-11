package use_case.AutoCompleteOperations;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// recursive tree with weights (use LLM) - weighted prefix trees
// code system to read tree
// use key interpreter
// popup suggestion - 3 or 4

// where to get key interpretation - in ideappbuilder private makeeditorpanel and get text

public class AutoCompleteOperations {

    public void enableAutoComplete(JTextComponent textComponent, JPopupMenu popup) {
        // use json file or string?
        // rewrite code to take info from weighted BST
        textComponent.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                String text = textComponent.getText();
                //check lastword getter
                //String lastword = text.substring(text.lastIndexOf(" ") + 1);
                //if (e.getKeyChar() == KeyEvent.VK_ENTER) {}
                String lastWord = getLastWord(text);
                if (lastWord.length() > 0) {
                    popup.removeAll();

                    if (popup.getComponentCount() > 0) {
                        popup.show(textComponent, textComponent.getCaretPosition(), textComponent.getCaretPosition());
                        //popup.show(textComponent, textComponent.getCaret().getMagicCaretPosition().x, textComponent.getCaret().getMagicCaretPosition().y);
                    }
                }
                else {
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
