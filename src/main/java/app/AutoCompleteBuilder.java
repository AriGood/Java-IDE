package app;

import data_access.AutoCompleteBST;

import java.util.List;

public class AutoCompleteBuilder {
    public static void main(String[] args) {
        AutoCompleteBST autocompleteBST = new AutoCompleteBST();

        // Insert some words into the BST
        autocompleteBST.insert("abstract");
        autocompleteBST.insert("assert");
        autocompleteBST.insert("boolean");
        autocompleteBST.insert("break");
        autocompleteBST.insert("byte");
        autocompleteBST.insert("case");
        autocompleteBST.insert("catch");
        autocompleteBST.insert("char");
        autocompleteBST.insert("class");
        autocompleteBST.insert("continue");

        // Get autocomplete suggestions for a prefix
        List<String> suggestions = autocompleteBST.autocomplete("b");
        System.out.println("Autocomplete suggestions for 'b': " + suggestions);

        List<String> suggestions2 = autocompleteBST.autocomplete("cl");
        System.out.println("Autocomplete suggestions for 'cl': " + suggestions2);
    }
}
