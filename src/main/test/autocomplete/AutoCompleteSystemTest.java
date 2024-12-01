package autocomplete;

import data.access.AutoCompleteBst;
import use_case.AutoCompleteOperations.AutoCompleteOperations;
import view.AutoCompletePopup;

import data.access.AutoCompleteBst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.AutoCompleteOperations.AutoCompleteOperations;
import view.AutoCompletePopup;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.List;

public class AutoCompleteSystemTest {

    private AutoCompleteBst autoCompleteBst;
    private AutoCompleteOperations autoCompleteOperations;
    private AutoCompletePopup autoCompletePopup;

    @BeforeEach
    public void setup() {
        autoCompleteBst = AutoCompleteBst.buildWithJavaKeywords();
        autoCompleteOperations = new AutoCompleteOperations(autoCompleteBst);
        autoCompletePopup = new AutoCompletePopup();
    }

    @Test
    public void testAutoCompleteBstAutocomplete() {
        List<String> suggestions = autoCompleteBst.autocomplete("pub");
        assertNotNull(suggestions);
        assertEquals(1, suggestions.size());
        assertEquals("public", suggestions.get(0));
    }

    @Test
    public void testAutoCompleteOperationsGetSuggestions() {
        JTextComponent mockTextComponent = mock(JTextComponent.class);
        when(mockTextComponent.getText()).thenReturn("pri");

        List<String> suggestions = autoCompleteOperations.getSuggestions(mockTextComponent);
        assertNotNull(suggestions);
        assertTrue(suggestions.contains("private"));
        assertTrue(suggestions.contains("protected"));
    }

    @Test
    public void testAutoCompleteOperationsApplySuggestion() {
        JTextComponent mockTextComponent = mock(JTextComponent.class);
        when(mockTextComponent.getText()).thenReturn("pri");

        autoCompleteOperations.applySuggestion(mockTextComponent, "private");

        verify(mockTextComponent).setText("private");
    }

    @Test
    public void testAutoCompletePopupShowSuggestions() {
        JTextComponent mockTextComponent = mock(JTextComponent.class);
        Point mockPoint = new Point(100, 200);
        when(mockTextComponent.getCaret().getMagicCaretPosition()).thenReturn(mockPoint);

        List<String> suggestions = List.of("private", "protected", "public");

        autoCompletePopup.showSuggestions(mockTextComponent, suggestions, (textComp, suggestion) -> {
            assertEquals(mockTextComponent, textComp);
            assertTrue(suggestions.contains(suggestion));
        });
    }

    @Test
    public void testAutoCompletePopupHide() {
        autoCompletePopup.hide();
        // The popup visibility cannot be directly asserted, so this test is included for completeness.
        // Further manual or UI testing may be needed to ensure visibility behavior.
    }
}
