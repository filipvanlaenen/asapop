package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Language</code> class.
 */
public class LanguageTest {
    /**
     * Verifies that getter for the identifier is wired correctly to the constructor.
     */
    @Test
    public void getIdShouldBeWiredCorrectlyToConstructor() {
        assertEquals("en", Language.ENGLISH.getId());
    }

    /**
     * Verifies that <code>parse</code> can parse an ID into a language.
     */
    @Test
    public void parseShouldParseAnIdIntoALanguage() {
        assertEquals(Language.ENGLISH, Language.parse("en"));
    }
}
