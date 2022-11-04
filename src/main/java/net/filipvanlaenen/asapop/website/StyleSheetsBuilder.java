package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

class StyleSheetsBuilder {
    private final String baseStyleSheetContent;

    /**
     * Constructor taking the base style sheet content as its parameter.
     *
     * @param baseStyleSheetContent The base style sheet content.
     */
    StyleSheetsBuilder(final String baseStyleSheetContent) {
        this.baseStyleSheetContent = baseStyleSheetContent;
    }

    /**
     * Builds the style sheets files.
     *
     * @return A map with the style sheets files and their paths.
     */
    Map<Path, String> build() {
        Map<Path, String> result = new HashMap<Path, String>();
        result.put(Paths.get("_css", "base.css"), baseStyleSheetContent);
        return result;
    }
}
