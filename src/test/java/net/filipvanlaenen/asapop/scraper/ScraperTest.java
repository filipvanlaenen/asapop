package net.filipvanlaenen.asapop.scraper;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ScraperTest {
    @Test
    public void foo() {
        assertTrue(Scraper.containsTagWithText("<foo>bar</foo>", "foo", "bar"));
    }
}
