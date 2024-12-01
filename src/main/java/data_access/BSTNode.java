package data_access;

public class BSTNode {
//    private BSTNode left, right;
//
//    public BSTNode(String word) {
//        super(word);
//    }
//
//    public BSTNode getLeft() {
//        return left;
//    }
//
//    public void setLeft(AbstractBSTNode<String> left) {
//        this.left = (BSTNode) left;
//    }
//
//    public BSTNode getRight() {
//        return right;
//    }
//    public void setRight(AbstractBSTNode<String> right) {
//        this.right = (BSTNode) right;
//    }

    String word;
    int frequency; //Track usage frequency
    BSTNode left, right;

    public BSTNode(String word) {
        this.word = word;
        this.frequency = 1; //Default frequency
        left = right = null;
    }
    public void incrementFrequency() {
        this.frequency++;
    }
}
