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

    // Find suggestions for a given prefix
    public List<String> autocomplete(String prefix) {
        List<String> suggestions = new ArrayList<>();
        autocompleteRec(root, prefix, suggestions);

//        matches.sort((n1, n2) -> Integer.compare(n2.frequency, n1.frequency)); // Sort by frequency descending
//
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
        if (prefix.compareTo(node.word) <= 0) {
            autocompleteRec(node.left, prefix, suggestions);
        }
        if (prefix.compareTo(node.word) >= 0) {
            autocompleteRec(node.right, prefix, suggestions);
        }
    }

    public static AutoCompleteBST buildWithJavaKeywords() {
        AutoCompleteBST autocompleteBST = new AutoCompleteBST();

        // Populate the tree with Java keywords
        String[] javaKeywords = {
                "abstract", "assert", "boolean", "break", "byte", "case", "catch",
                "char", "class", "continue", "default", "do", "double", "else",
                "enum", "extends", "final", "finally", "float", "for", "goto",
                "if", "implements", "import", "instanceof", "int", "interface",
                "long", "native", "new", "package", "private", "protected",
                "public", "return", "short", "static", "strictfp", "super",
                "switch", "synchronized", "transient", "volatile", "while"
        };

        for (String keyword : javaKeywords) {
            autocompleteBST.insert(keyword);
        }

        return autocompleteBST;
    }
//
//    private AutoCompleteBST BSTBuilder() {
//        // Initialize autocomplete BST and add Java keywords
//        AutoCompleteBST autocompleteBST = new AutoCompleteBST();
//        autocompleteBST.insert("abstract");
//        autocompleteBST.insert("assert");
//        autocompleteBST.insert("boolean");
//        autocompleteBST.insert("break");
//        autocompleteBST.insert("byte");
//        autocompleteBST.insert("case");
//        autocompleteBST.insert("catch");
//        autocompleteBST.insert("char");
//        autocompleteBST.insert("class");
//        autocompleteBST.insert("continue");
//        autocompleteBST.insert("default");
//        autocompleteBST.insert("do");
//        autocompleteBST.insert("double");
//        autocompleteBST.insert("else");
//        autocompleteBST.insert("enum");
//        autocompleteBST.insert("extends");
//        autocompleteBST.insert("final");
//        autocompleteBST.insert("finally");
//        autocompleteBST.insert("float");
//        autocompleteBST.insert("for");
//        autocompleteBST.insert("goto");
//        autocompleteBST.insert("if");
//        autocompleteBST.insert("implements");
//        autocompleteBST.insert("import");
//        autocompleteBST.insert("instanceof");
//        autocompleteBST.insert("int");
//        autocompleteBST.insert("interface");
//        autocompleteBST.insert("long");
//        autocompleteBST.insert("native");
//        autocompleteBST.insert("new");
//        autocompleteBST.insert("package");
//        autocompleteBST.insert("private");
//        autocompleteBST.insert("protected");
//        autocompleteBST.insert("public");
//        autocompleteBST.insert("return");
//        autocompleteBST.insert("short");
//        autocompleteBST.insert("static");
//        autocompleteBST.insert("strictfp");
//        autocompleteBST.insert("super");
//        autocompleteBST.insert("switch");
//        autocompleteBST.insert("synchronized");
//        autocompleteBST.insert("transient");
//        autocompleteBST.insert("volatile");
//        autocompleteBST.insert("while");
//        autocompleteBST.insert("volatile");
//
//        return autocompleteBST;
//    }

//    private void findMatches(BSTNode node, String prefix, List<BSTNode> matches) {
//        if (node == null) return;
//
//        if (node.word.startsWith(prefix)) {
//            matches.add(node);
//        }
//
//        if (prefix.compareTo(node.word) <= 0) {
//            findMatches(node.left, prefix, matches);
//        }
//        if (prefix.compareTo(node.word) >= 0) {
//            findMatches(node.right, prefix, matches);
//        }
//    }


}
