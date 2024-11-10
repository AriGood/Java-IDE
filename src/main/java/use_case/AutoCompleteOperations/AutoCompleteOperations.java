package use_case.AutoCompleteOperations;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AutoCompleteOperations {

    public void AutoCompleteItem(JTextComponent textComponent, JPopupMenu popupMenu) {
        // use json file or string?
        textComponent.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                String text = textComponent.getText();
                //check lastword getter
                String lastword = text.substring(text.lastIndexOf(" ") + 1);
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {}
            }
        });
    }
}
