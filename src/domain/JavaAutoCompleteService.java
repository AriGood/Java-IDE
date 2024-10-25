package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaAutoCompleteService extends AutoCompleteService {

    private static final List<String> JAVA_KEYWORDS = Arrays.asList(
            "public", "private", "protected", "class", "void", "int", "String", "if", "else", "while", "for", "return"
    );

    @Override
    protected List<String> getSuggestions(String prefix) {
        List<String> matchingSuggestions = new ArrayList<>();
        for (String keyword : JAVA_KEYWORDS) {
            if (keyword.startsWith(prefix)) {
                matchingSuggestions.add(keyword);
            }
        }
        return matchingSuggestions;
    }
}
