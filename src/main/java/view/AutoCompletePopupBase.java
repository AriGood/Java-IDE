package view;

import javax.swing.text.JTextComponent;
import java.util.List;

public abstract class AutoCompletePopupBase {
    /**
     * Displays suggestions in the UI.
     * @param textComponent The text component for the suggestions.
     * @param suggestions The list of suggestions to display.
     * @param handler A handler to process the selection.
     */
    public abstract void showSuggestions(JTextComponent textComponent, List<String> suggestions, SuggestionSelectionHandler handler);

    /**
     * Hides the suggestion popup.
     */
    public abstract void hide();

    @FunctionalInterface
    public interface SuggestionSelectionHandler {
        void onSuggestionSelected(JTextComponent textComponent, String suggestion);
    }
}
