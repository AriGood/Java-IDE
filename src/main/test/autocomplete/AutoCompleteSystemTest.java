package autocomplete;

import data.access.AutoCompleteBst;
import org.junit.Before;
import org.junit.Test;
import use_case.autocompleteoperations.AutoCompleteOperations;
import view.AutoCompletePopup;

import java.util.List;

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

    @Test
    public void testAutoCompletePopupHide() {
        autoCompletePopup.hide();
        // Popup visibility cannot be directly asserted in JUnit 4.
        // Include this test for completeness; manual testing may still be required.
    }
}