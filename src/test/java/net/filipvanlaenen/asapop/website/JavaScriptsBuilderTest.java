package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;

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
        ModifiableMap<Path, String> map = ModifiableMap.<Path, String>empty();
        map.put(Paths.get("_js", "internationalization.js"),
                new InternationalizationScriptBuilder(createInternationalization()).build());
        map.put(Paths.get("_js", "navigation.js"), navigationScriptContent);
        map.put(Paths.get("_js", "sorting.js"), sortingScriptContent);
        map.put(Paths.get("_js", "tooltip.js"), tooltipScriptContent);
        assertTrue(map.containsSame(builder.build()));
    }
}
