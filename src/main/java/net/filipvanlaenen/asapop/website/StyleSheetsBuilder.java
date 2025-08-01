package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;

/**
 * Classing building the style sheets.
 */
class StyleSheetsBuilder {
    /**
     * The content of the base style sheet.
     */
    private final String baseStyleSheetContent;
    /**
     * The content of the custom style sheet.
     */
    private final String customStyleSheetContent;

    /**
     * Constructor taking the base and custom style sheet contents as its parameters.
     *
     * @param baseStyleSheetContent   The base style sheet content.
     * @param customStyleSheetContent The custom style sheet content.
     */
    StyleSheetsBuilder(final String baseStyleSheetContent, final String customStyleSheetContent) {
        this.baseStyleSheetContent = baseStyleSheetContent;
        this.customStyleSheetContent = customStyleSheetContent;
    }

    /**
     * Builds the style sheets files.
     *
     * @return A map with the style sheets files and their paths.
     */
    Map<Path, String> build() {
        ModifiableMap<Path, String> result = ModifiableMap.<Path, String>empty();
        result.put(Paths.get("_css", "base.css"), baseStyleSheetContent);
        result.put(Paths.get("_css", "skin.css"), customStyleSheetContent);
        return result;
    }
}
