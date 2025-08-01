package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;

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
        Map<Path, String> map = Map.<Path, String>of(Paths.get("_css", "base.css"), baseStyleSheetContent,
                Paths.get("_css", "skin.css"), customStyleSheetContent);
        StyleSheetsBuilder builder = new StyleSheetsBuilder(baseStyleSheetContent, customStyleSheetContent);
        assertTrue(map.containsSame(builder.build()));
    }
}
