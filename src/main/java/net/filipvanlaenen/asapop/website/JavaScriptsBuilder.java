package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;

/**
 * Class building the JavaScripts files.
 */
class JavaScriptsBuilder {
    /**
     * The internationalization dictionary.
     */
    private final Internationalization internationalization;
    /**
     * The content of the navigation script.
     */
    private final String navigationScriptContent;
    /**
     * The content of the sorting script.
     */
    private final String sortingScriptContent;
    /**
     * The content of the tooltip script.
     */
    private final String tooltipScriptContent;

    /**
     * Constructor taking the internationalization terms as its parameter.
     *
     * @param navigationScriptContent The content of the navigation script.
     * @param sortingScriptContent    The content of the sorting script.
     * @param tooltipScriptContent    The content of the tooltip script.
     * @param internationalization    The internationalization dictionary.
     */
    JavaScriptsBuilder(final String navigationScriptContent, final String sortingScriptContent,
            final String tooltipScriptContent, final Internationalization internationalization) {
        this.navigationScriptContent = navigationScriptContent;
        this.sortingScriptContent = sortingScriptContent;
        this.tooltipScriptContent = tooltipScriptContent;
        this.internationalization = internationalization;
    }

    /**
     * Builds the JavaScripts files.
     *
     * @return A map with the JavaScript files and their paths.
     */
    Map<Path, String> build() {
        ModifiableMap<Path, String> result = ModifiableMap.<Path, String>empty();
        result.put(Paths.get("_js", "internationalization.js"),
                new InternationalizationScriptBuilder(internationalization).build());
        result.put(Paths.get("_js", "navigation.js"), navigationScriptContent);
        result.put(Paths.get("_js", "sorting.js"), sortingScriptContent);
        result.put(Paths.get("_js", "tooltip.js"), tooltipScriptContent);
        return result;
    }
}
