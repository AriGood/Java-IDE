package use_case.autocompleteoperations;

import java.util.List;

import javax.swing.text.JTextComponent;

import data.access.AutoCompleteBst;

/**
 * New method and class that finalizes autocomplete before sending to pop up.
 */
public class AutoCompleteOperations extends AbstractAutoCompleteOperationsBase {
    private final AutoCompleteBst autoCompleteBst;

    public AutoCompleteOperations(AutoCompleteBst autoCompleteBst) {
        this.autoCompleteBst = autoCompleteBst;
    }

    @Override
    public List<String> getSuggestions(JTextComponent textComponent) {
        String text = textComponent.getText();
        String lastWord = getLastWord(text);
        if (!lastWord.isEmpty()) {
            return autoCompleteBst.autocomplete(lastWord);
        }
        // No suggestions if last word is empty
        return List.of();
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
