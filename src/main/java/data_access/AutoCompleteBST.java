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

        // Insert into the correct subtree based on alphabetical order
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

        // Recur down left and right subtrees based on prefix
        if (prefix.compareTo(node.word) <= 0) {
            autocompleteRec(node.left, prefix, suggestions);
        }
        if (prefix.compareTo(node.word) >= 0) {
            autocompleteRec(node.right, prefix, suggestions);
        }
    }
}
