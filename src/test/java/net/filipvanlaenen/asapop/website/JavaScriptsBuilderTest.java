package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>JavaScriptsBuilder</code> class.
 */
public class JavaScriptsBuilderTest {
    /**
     * Creates the internationalization dictionary.
     *
     * @return The internationalization dictionary.
     */
    private Internationalization createInternationalization() {
        Internationalization internationalization = new Internationalization();
        internationalization.addTranslations("language", Map.of("en", "Language"));
        return internationalization;
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
                tooltipScriptContent, createInternationalization());
        Map<Path, String> map = new HashMap<Path, String>();
        map.put(Paths.get("_js", "internationalization.js"),
                new InternationalizationScriptBuilder(createInternationalization()).build());
        map.put(Paths.get("_js", "navigation.js"), navigationScriptContent);
        map.put(Paths.get("_js", "sorting.js"), sortingScriptContent);
        map.put(Paths.get("_js", "tooltip.js"), tooltipScriptContent);
        assertEquals(map, builder.build());
    }
}
