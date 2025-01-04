package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Translations</code> class.
 */
public class TranslationsTest {
    /**
     * Creates a Translations object to run unit tests on.
     *
     * @return A Translations object to run unit tests on.
     */
    private Translations createTranslations() {
        return new Translations("foo", Map.of("en", "Foo", "de", "Bar"));
    }

    /**
     * Verifies that the getter for the key is wired correctly to the constructor.
     */
    @Test
    public void getKeyShouldBeWiredCorrectlyToTheConstructor() {
        assertEquals("foo", createTranslations().getKey());
    }

    /**
     * Verifies that <code>containsLanguage</code> returns <code>true</code> for a language that's present.
     */
    @Test
    public void containsLanguageShouldReturnTrueForPresentLanguage() {
        assertTrue(createTranslations().containsLanguage(Language.ENGLISH));
    }

    /**
     * Verifies that <code>containsLanguage</code> returns <code>false</code> for a language that's absent.
     */
    @Test
    public void containsLanguageShouldReturnFalseForAbsentLanguage() {
        assertFalse(createTranslations().containsLanguage(Language.FRENCH));
    }

    /**
     * Verifies that <code>getTranslation</code> returns the correct translation.
     */
    @Test
    public void getTranslationShouldReturnTheCorrectTranslation() {
        assertEquals("Foo", createTranslations().getTranslation(Language.ENGLISH));
    }
}
