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
        // working with frequency
//        if (word.equals(node.word)) {
//            node.incrementFrequency(); // Increment frequency if word already exists
//        } else if (word.compareTo(node.word) < 0) {
//            node.left = insertRec(node.left, word);
//        } else {
//            node.right = insertRec(node.right, word);
//        }
        // Insert into the correct subtree based on alphabetical order
        if (word.compareTo(node.word) < 0) {
            node.left = insertRec(node.left, word);
        } else if (word.compareTo(node.word) > 0) {
            node.right = insertRec(node.right, word);
        }
        return node;
    }
    //added a comment to make sure I can merge and pull.

    // Find suggestions for a given prefix
    public List<String> autocomplete(String prefix) {
        List<String> suggestions = new ArrayList<>();
        autocompleteRec(root, prefix, suggestions);

//        matches.sort((n1, n2) -> Integer.compare(n2.frequency, n1.frequency)); // Sort by frequency descending
//        List<String> suggestions = new ArrayList<>();
//        for (BSTNode node : matches) {
//            suggestions.add(node.word);
//        }
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

        // Recur down left and right subtrees based on prefix
//        if (prefix.compareTo(node.word) <= 0) {
//            autocompleteRec(node.left, prefix, suggestions);
//        }
//        if (prefix.compareTo(node.word) >= 0) {
//            autocompleteRec(node.right, prefix, suggestions);
//        }
        autocompleteRec(node.left, prefix, suggestions);
        autocompleteRec(node.right, prefix, suggestions);
    }

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
