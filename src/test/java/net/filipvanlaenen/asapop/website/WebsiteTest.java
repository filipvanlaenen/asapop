package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        website.add("foo", new Html());
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("foo", new Html().asString());
        assertEquals(expected, website.asMap());
    }

    /**
     * Verifies that <code>asMap</code> returns an UnmodifiableMap.
     */
    @Test
    public void asMapReturnsAnUnmodifiableMap() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new Website().asMap().put("foo", new Html().asString());
        });
    }
}
