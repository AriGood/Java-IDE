package use_case.autocompleteoperations;

import java.util.List;

import javax.swing.text.JTextComponent;

/**
 * New abstract class that initializes autocomplete methods.
 */
public abstract class AbstractAutoCompleteOperationsBase {
    /**
     * Retrieves suggestions based on input text.
     * @param textComponent The text to use for generating suggestions.
     * @return A list of suggestions.
     */
    public abstract List<String> getSuggestions(JTextComponent textComponent);

    /**
     * Applies the selected suggestion to the text component.
     * @param textComponent The text component to update.
     * @param suggestion The suggestion to apply.
     */
    public abstract void applySuggestion(JTextComponent textComponent, String suggestion);
}
