package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;

/**
 * Unit tests on the <code>Term</code> class.
 */
public class TermTest {
    /**
     * Verifies that the getter method <code>getKey</code> is wired correctly to the setter method <code>setKey</code>.
     */
    @Test
    public void getKeyShouldBeWiredCorrectlyToSetKey() {
        Term term = new Term();
        term.setKey("foo");
        assertEquals("foo", term.getKey());
    }

    /**
     * Verifies that the getter method <code>getTranslations</code> is wired correctly to the setter method
     * <code>setTranslations</code>.
     */
    @Test
    public void getTranslationsShouldBeWiredCorrectlyToSetTranslations() {
        Term term = new Term();
        Map<String, String> translations = Map.of("en", "Foo");
        term.setTranslations(translations);
        assertEquals(translations, term.getTranslations());
    }
}
