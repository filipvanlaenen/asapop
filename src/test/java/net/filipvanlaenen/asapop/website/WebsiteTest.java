package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.txhtmlj.Html;

/**
 * Unit tests on the <code>Website</code> class.
 */
public class WebsiteTest {
    /**
     * Path <code>foo</code>.
     */
    private static final Path FOO_PATH = Paths.get("foo");

    /**
     * An empty website returns an empty map.
     */
    @Test
    public void emptyWebsiteShouldReturnAnEmptyContentMap() {
        assertEquals(Collections.EMPTY_MAP, new Website().asMap());
    }

    /**
     * A website with a single page should return a map with the content of that page.
     */
    @Test
    public void aWebsiteWithASinglePageShouldReturnAMapWithTheContentOfThatPage() {
        Website website = new Website();
        website.put("foo", new Html());
        Map<Path, String> expected = new HashMap<Path, String>();
        expected.put(FOO_PATH, new Html().asString());
        assertEquals(expected, website.asMap());
    }

    /**
     * A website with a single page added as a map should return a map with the content of that page.
     */
    @Test
    public void aWebsiteWithASinglePageAddedAsAMapShouldReturnAMapWithTheContentOfThatPage() {
        Website website = new Website();
        website.putAll(Map.of(FOO_PATH, new Html().asString()));
        Map<Path, String> expected = new HashMap<Path, String>();
        expected.put(FOO_PATH, new Html().asString());
        assertEquals(expected, website.asMap());
    }

    /**
     * Verifies that <code>asMap</code> returns an UnmodifiableMap.
     */
    @Test
    public void asMapReturnsAnUnmodifiableMap() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new Website().asMap().put(FOO_PATH, new Html().asString());
        });
    }
}
