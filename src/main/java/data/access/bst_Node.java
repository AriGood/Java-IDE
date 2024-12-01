package data.access;

/**
 * Public class to build nodes for a BST.
 */
public class bst_Node {

    String word;
    int frequency;
    bst_Node left;
    bst_Node right;

    public bst_Node(String word) {
        this.word = word;
        this.frequency = 1;
        left = null;
        right = null;
    }

}
