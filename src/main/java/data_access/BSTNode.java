package data_access;

public class BSTNode {
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
