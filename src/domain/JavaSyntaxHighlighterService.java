package domain;

import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.JEditorPane;  // Importing JEditorPane if needed
import javax.swing.JTextPane;    // Importing JTextPane
import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class JavaSyntaxHighlighterService extends SyntaxHighlighterService {

    // Define keywords for Java syntax highlighting
    private static final List<String> JAVA_KEYWORDS = Arrays.asList(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
            "class", "const", "continue", "default", "do", "double", "else", "enum",
            "extends", "final", "finally", "float", "for", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new",
            "null", "package", "private", "protected", "public", "return", "short",
            "static", "strictfp", "super", "switch", "synchronized", "this", "throw",
            "throws", "transient", "try", "void", "volatile", "while"
    );

    @Override
    public void applyHighlighting(JTextComponent textComponent) {
        // Ensure textComponent is a JTextPane
        if (!(textComponent instanceof JTextPane)) {
            throw new IllegalArgumentException("Text component must be an instance of JTextPane");
        }

        JTextPane textPane = (JTextPane) textComponent;
        StyledDocument doc = textPane.getStyledDocument();
        StyleContext styleContext = new StyleContext();

        // Create a style for keywords
        Style keywordStyle = styleContext.addStyle("KeywordStyle", null);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);

        // Clear existing styles
        doc.setCharacterAttributes(0, doc.getLength(), styleContext.addStyle("DefaultStyle", null), true);

        String text = textComponent.getText();
        for (String keyword : JAVA_KEYWORDS) {
            int index = text.indexOf(keyword);
            while (index >= 0) {
                doc.setCharacterAttributes(index, keyword.length(), keywordStyle, false);
                index = text.indexOf(keyword, index + keyword.length());
            }
        }
    }
}
