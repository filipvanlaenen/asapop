package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>StyleSheetsBuilder</code> class.
 */
public class StyleSheetsBuilderTest {
    /**
     * Verifies that the style sheets are built correctly.
     */
    @Test
    public void styleSheetsShouldBeBuiltCorrectly() {
        String baseStyleSheetContent = "header { display: block; width: 100%; }";
        String customStyleSheetContent = "body { font-family: serif; background: #FFFFFF; color: #0E3651; }";
        Map<Path, String> map = new HashMap<Path, String>();
        map.put(Paths.get("_css", "base.css"), baseStyleSheetContent);
        map.put(Paths.get("_css", "skin.css"), customStyleSheetContent);
        StyleSheetsBuilder builder = new StyleSheetsBuilder(baseStyleSheetContent, customStyleSheetContent);
        assertEquals(map, builder.build());
    }
}
