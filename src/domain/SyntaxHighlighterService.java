package domain;

import javax.swing.text.JTextComponent;

public abstract class SyntaxHighlighterService {

    // Abstract method to apply highlighting to the JTextComponent
    public abstract void applyHighlighting(JTextComponent textComponent);

    // Method to highlight the text (can be used to set up default behavior)
    public void highlight(JTextComponent textComponent) {
        // Perform any common operations before applying language-specific highlighting
        // Example: clear existing styles or set up a default style
        applyHighlighting(textComponent);
    }
}
