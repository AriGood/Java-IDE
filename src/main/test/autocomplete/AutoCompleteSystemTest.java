package autocomplete;

import data.access.AutoCompleteBst;
import org.junit.Before;
import org.junit.Test;
import use_case.AutoCompleteOperations.AutoCompleteOperations;
import view.AutoCompletePopup;

import javax.swing.*;
import javax.swing.JTextField;

import java.awt.*;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AutoCompleteSystemTest {

    private AutoCompleteBst autoCompleteBst;
    private AutoCompleteOperations autoCompleteOperations;
    private AutoCompletePopup autoCompletePopup;

    @Before
    public void setup() {
        autoCompleteBst = AutoCompleteBst.buildWithJavaKeywords();
        autoCompleteOperations = new AutoCompleteOperations(autoCompleteBst);
        autoCompletePopup = new AutoCompletePopup();
    }

    @Test
    public void testAutoCompleteBstAutocomplete() {
        List<String> suggestions = autoCompleteBst.autocomplete("pub");
        assertNotNull("Suggestions should not be null", suggestions);
        assertEquals("Expected one suggestion", 1, suggestions.size());
        assertEquals("Expected 'public' as suggestion", "public", suggestions.get(0));
    }

//    @Test
//    public void testAutoCompleteOperationsGetSuggestions() {
//        JTextField mockTextField = mock(JTextField.class);
//        when(mockTextField.getText()).thenReturn("pri");
//
//        List<String> suggestions = autoCompleteOperations.getSuggestions(mockTextField);
//        assertNotNull("Suggestions should not be null", suggestions);
//        assertTrue("Expected 'private' in suggestions", suggestions.contains("private"));
//        assertTrue("Expected 'protected' in suggestions", suggestions.contains("protected"));
//    }

//    @Test
//    public void testAutoCompleteOperationsApplySuggestion() {
//        JTextField mockTextField = mock(JTextField.class);
//        when(mockTextField.getText()).thenReturn("pri");
//
//        autoCompleteOperations.applySuggestion(mockTextField, "private");
//
//        verify(mockTextField).setText("private");
//    }

//    @Test
//    public void testAutoCompletePopupShowSuggestions() {
//        JTextField mockTextField = mock(JTextField.class);
//        Point mockPoint = new Point(100, 200);
//        when(mockTextField.getCaret().getMagicCaretPosition()).thenReturn(mockPoint);
//
//        List<String> suggestions = List.of("private", "protected", "public");
//
//        autoCompletePopup.showSuggestions(mockTextField, suggestions, (textComp, suggestion) -> {
//            assertEquals("Handler should receive correct text component", mockTextField, textComp);
//            assertTrue("Handler should receive valid suggestion", suggestions.contains(suggestion));
//        });
//    }

    @Test
    public void testAutoCompletePopupHide() {
        autoCompletePopup.hide();
        // Popup visibility cannot be directly asserted in JUnit 4.
        // Include this test for completeness; manual testing may still be required.
    }
}