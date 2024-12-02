package data.access;

/**
 * Public class to build nodes for a BST.
 */
public class BstNode {

    String word;
    int frequency;
    BstNode left;
    BstNode right;

    public BstNode(String word) {
        this.word = word;
        this.frequency = 1;
        left = null;
        right = null;
    }

}
