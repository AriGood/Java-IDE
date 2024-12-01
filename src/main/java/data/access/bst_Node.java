package data.access;

/**
 * Public class to build nodes for a BST.
 */
public class bst_Node {

    String word;
    int frequency; //Track usage frequency
    bst_Node left, right;

    public bst_Node(String word) {
        this.word = word;
        this.frequency = 1;
        left = right = null;
    }

    public void incrementFrequency() {
        this.frequency++;
    }
}
