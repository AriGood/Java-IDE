package use_case.AutoCompleteOperations;

import data_access.AutoCompleteBST;
import use_case.FileManagement.TabManagement;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;


// recursive tree with weights (use LLM) - weighted prefix trees
// code system to read tree
// use key interpreter
// popup suggestion - 3 or 4

// where to get key interpretation - in ideappbuilder private makeeditorpanel and get text
//TODO split off view logic to view folder should not even be importing swing
//TODO: refactor so popup code is in view

public class AutoCompleteOperations {
    private final AutoCompleteBST autoCompleteBST;

    public AutoCompleteOperations(AutoCompleteBST autoCompleteBST) {
        this.autoCompleteBST = autoCompleteBST;
    }

    public void enableAutoComplete(TabManagement tabManagement, JTextComponent textComponent, JPopupMenu popup) {
        // use json file or string?
        // rewrite code to take info from weighted BST
        // connect to code editor (doesnt take anything from ideappbuilder yet)
        textComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String text = tabManagement.currentEditor.toString();
                String lastWord = getLastWord(text);
                if (lastWord.length() > 0) {
                    List<String> suggestions = autoCompleteBST.autocomplete(lastWord);
                    popup.removeAll();
                    for (String suggestion : suggestions) {
                        JMenuItem item = new JMenuItem(suggestion);
                        item.addActionListener(event -> {
                            replaceLastWord(textComponent, lastWord, suggestion);
                        popup.setVisible(false);
                        });
                        popup.add(item);
                    }

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

    private void replaceLastWord(JTextComponent textComponent, String lastWord, String suggestion) {
        String text = textComponent.getText();
        int lastIndex = text.lastIndexOf(lastWord);
        //textComponent.setText(text.substring(0, lastIndex) + lastWord + text.substring(lastIndex + suggestion.length()));
        textComponent.setText(text.substring(0, lastIndex) + suggestion);
    }

    private String getLastWord(String text) {
        String[] words = text.split("\\s+");
        return words[words.length - 1];
    }
}
