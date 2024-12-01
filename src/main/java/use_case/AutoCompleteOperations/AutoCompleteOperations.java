package use_case.AutoCompleteOperations;

import data_access.AutoCompleteBST;

import javax.swing.text.JTextComponent;
import java.util.List;

public class AutoCompleteOperations extends AutoCompleteOperationsBase {
    private final AutoCompleteBST autoCompleteBST;

    public AutoCompleteOperations(AutoCompleteBST autoCompleteBST) {
        this.autoCompleteBST = autoCompleteBST;
    }

    @Override
    public List<String> getSuggestions(JTextComponent textComponent) {
        String text = textComponent.getText();
        String lastWord = getLastWord(text);
        if (!lastWord.isEmpty()) {
            return autoCompleteBST.autocomplete(lastWord);
        }
        return List.of(); // No suggestions if last word is empty
    }

    /**
     * Takes input and finds last letter to generate a suggestion.
     * @param textComponent The text component to update.
     * @param suggestion The suggestion to apply.
     */
    @Override
    public void applySuggestion(JTextComponent textComponent, String suggestion) {
        String currentText = textComponent.getText();
        String lastWord = getLastWord(currentText);
        int lastIndex = currentText.lastIndexOf(lastWord);
        textComponent.setText(currentText.substring(0, lastIndex) + suggestion);
    }

    private String getLastWord(String text) {
        String[] words = text.split("\\s+");
        return words.length > 0 ? words[words.length - 1] : "";
    }
}
