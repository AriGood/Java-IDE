package use_case.AutoCompleteOperations;

import javax.swing.text.JTextComponent;
import java.util.List;

public abstract class AutoCompleteOperationsBase {
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
