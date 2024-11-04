package application;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlighter {
    private final JTextPane textPane;
    private final StyleContext styleContext;
    private final DefaultHighlighter.DefaultHighlightPainter highlightPainter;

    // Regular expression for Java keywords
    private final Pattern keywordPattern = Pattern.compile("\\b(public|private|protected|class|void|static|final|int|double|float|String|new|return|if|else|for|while|try|catch|import|package|boolean|true|false)\\b");

    public SyntaxHighlighter(JTextPane textPane) {
        this.textPane = textPane;
        this.styleContext = new StyleContext();
        this.highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.BLUE);
    }

    public void highlight() {
        try {
            // Clear existing highlights
            Highlighter highlighter = textPane.getHighlighter();
            highlighter.removeAllHighlights();

            String text = textPane.getDocument().getText(0, textPane.getDocument().getLength());

            // Match and highlight keywords
            Matcher matcher = keywordPattern.matcher(text);
            while (matcher.find()) {
                highlighter.addHighlight(matcher.start(), matcher.end(), highlightPainter);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
