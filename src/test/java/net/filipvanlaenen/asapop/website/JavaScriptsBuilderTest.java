package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;

/**
 * Unit tests on the <code>JavaScriptsBuilder</code> class.
 */
public class JavaScriptsBuilderTest {
    /**
     * Creates a set of terms for the internationalization script builder.
     *
     * @return A set of terms for the internationalization script builder.
     */
    private Terms createTerms() {
        Terms terms = new Terms();
        Term term = new Term();
        term.setKey("language");
        term.setTranslations(Map.of("en", "Language"));
        terms.setTerms(Set.of(term));
        return terms;
    }

    /**
     * Verifies that the JavaScript files are build correctly.
     */
    @Test
    public void shouldBuildTheJavaScriptFilesCorrectly() {
        String navigationScriptContent = "function moveToArea(level) {}";
        String sortingScriptContent = "function sortTable(table) {}";
        String tooltipScriptContent = "function tooltip(text) {}";
        JavaScriptsBuilder builder = new JavaScriptsBuilder(navigationScriptContent, sortingScriptContent,
                tooltipScriptContent, createTerms());
        Map<Path, String> map = new HashMap<Path, String>();
        map.put(Paths.get("_js", "internationalization.js"),
                new InternationalizationScriptBuilder(createTerms()).build());
        map.put(Paths.get("_js", "navigation.js"), navigationScriptContent);
        map.put(Paths.get("_js", "sorting.js"), sortingScriptContent);
        map.put(Paths.get("_js", "tooltip.js"), tooltipScriptContent);
        assertEquals(map, builder.build());
    }
}
