package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;
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
        assertTrue(new Website().asMap().isEmpty());
    }

    /**
     * A website with a single page should return a map with the content of that page.
     */
    @Test
    public void aWebsiteWithASinglePageShouldReturnAMapWithTheContentOfThatPage() {
        Website website = new Website();
        website.put("foo", new Html());
        Map<Path, String> expected = Map.of(FOO_PATH, new Html().asString());
        assertTrue(expected.containsSame(website.asMap()));
    }

    /**
     * A website with a single page added as a map should return a map with the content of that page.
     */
    @Test
    public void aWebsiteWithASinglePageAddedAsAMapShouldReturnAMapWithTheContentOfThatPage() {
        Website website = new Website();
        website.putAll(Map.of(FOO_PATH, new Html().asString()));
        Map<Path, String> expected = Map.of(FOO_PATH, new Html().asString());
        assertTrue(expected.containsSame(website.asMap()));
    }
}
