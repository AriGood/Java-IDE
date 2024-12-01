package data_access;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteBST {
    private BSTNode root;

    // Insert a new word into the BST
    public void insert(String word) {
        root = insertRec(root, word);
    }
    private BSTNode insertRec(BSTNode node, String word) {
        if (node == null) {
            return new BSTNode(word);
        }
        if (word.compareTo(node.word) < 0) {
            node.left = insertRec(node.left, word);
        } else if (word.compareTo(node.word) > 0) {
            node.right = insertRec(node.right, word);
        }
        return node;
    }

    // Find suggestions for a given prefix
    public List<String> autocomplete(String prefix) {
        List<String> suggestions = new ArrayList<>();
        autocompleteRec(root, prefix, suggestions);
        return suggestions;
    }

    private void autocompleteRec(BSTNode node, String prefix, List<String> suggestions) {
        if (node == null) {
            return;
        }

        // Only add words that start with the prefix
        if (node.word.startsWith(prefix)) {
            suggestions.add(node.word);
        }

        autocompleteRec(node.left, prefix, suggestions);
        autocompleteRec(node.right, prefix, suggestions);
    }

    /**
     * Builds an autocompleteBST populated with commonly used java words.
     * @return autocompletebst.
     */
    public static AutoCompleteBST buildWithJavaKeywords() {
        AutoCompleteBST autocompleteBST = new AutoCompleteBST();

        // Populate the tree with Java keywords
        String[] javaKeywords = {
                "ArrayList", "abstract", "assert", "boolean", "break", "byte", "case", "catch",
                "char", "class", "continue", "default", "do", "double", "else",
                "enum", "extends", "equals", "Exception", "final", "finally", "float", "for", "get", "goto",
                "if", "implements", "import", "instanceof", "int", "interface",
                "length", "List", "long", "main", "native", "new", "package", "private", "protected",
                "public", "remove", "return", "short", "static", "strictfp", "String", "super",
                "switch", "synchronized", "transient", "volatile", "while", "@Override"
        };

        for (String keyword : javaKeywords) {
            autocompleteBST.insert(keyword);
        }

        return autocompleteBST;
    }
}
