package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import net.filipvanlaenen.asapop.yaml.Terms;

/**
 * Class building the JavaScripts files.
 */
class JavaScriptsBuilder {
    /**
     * The content of the navigation script.
     */
    private final String navigationScriptContent;
    /**
     * The content of the sorting script.
     */
    private final String sortingScriptContent;
    /**
     * The internationalization terms.
     */
    private final Terms terms;
    private final String tooltipScriptContent;

    /**
     * Constructor taking the internationalization terms as its parameter.
     *
     * @param navigationScriptContent The content of the navigation script.
     * @param sortingScriptContent    The content of the sorting script.
     * @param terms                   The internationalization terms.
     */
    JavaScriptsBuilder(final String navigationScriptContent, final String sortingScriptContent,
            final String tooltipScriptContent, final Terms terms) {
        this.navigationScriptContent = navigationScriptContent;
        this.sortingScriptContent = sortingScriptContent;
        this.tooltipScriptContent = tooltipScriptContent;
        this.terms = terms;
    }

    /**
     * Builds the JavaScripts files.
     *
     * @return A map with the JavaScript files and their paths.
     */
    Map<Path, String> build() {
        Map<Path, String> result = new HashMap<Path, String>();
        result.put(Paths.get("_js", "internationalization.js"), new InternationalizationScriptBuilder(terms).build());
        result.put(Paths.get("_js", "navigation.js"), navigationScriptContent);
        result.put(Paths.get("_js", "sorting.js"), sortingScriptContent);
        result.put(Paths.get("_js", "tooltip.js"), tooltipScriptContent);
        return result;
    }
}
