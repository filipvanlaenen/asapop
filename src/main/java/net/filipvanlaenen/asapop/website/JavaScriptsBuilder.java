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
     * The internationalization terms.
     */
    private final Terms terms;

    /**
     * Constructor taking the internationalization terms as its parameter.
     *
     * @param terms The internationalization terms.
     */
    JavaScriptsBuilder(final Terms terms) {
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
        return result;
    }
}
