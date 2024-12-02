package view;

import java.util.List;

import javax.swing.text.JTextComponent;

/**
 * New abstract class.
 */
public abstract class AbstractAutoCompletePopupBase {
    /**
     * Displays suggestions in the UI.
     * @param textComponent The text component for the suggestions.
     * @param suggestions The list of suggestions to display.
     * @param handler A handler to process the selection.
     */
    public abstract void showSuggestions(JTextComponent textComponent, List<String> suggestions,
                                         SuggestionSelectionHandler handler);

    /**
     * Hides the suggestion popup.
     */
    public abstract void hide();

    /**
     * Handles suggestions based on given input letter.
     */
    @FunctionalInterface
    public interface SuggestionSelectionHandler {
        /**
         * New void function.
         * @param textComponent .
         * @param suggestion .
         */
        void onSuggestionSelected(JTextComponent textComponent, String suggestion);
    }
}
