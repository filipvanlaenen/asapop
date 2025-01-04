package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;

/**
 * Unit tests on the <code>Translations</code> class.
 */
public class InternationalizationTest {
    /**
     * Creates an Internationalization object to run unit tests on.
     *
     * @return An Internationalization object to run unit tests on.
     */
    private Internationalization createInternationalization() {
        Internationalization internationalization = new Internationalization();
        internationalization.addTranslations("foo", Map.of("en", "Foo", "de", "Bar"));
        return internationalization;
    }

    /**
     * Verifies that <code>containsKey</code> returns <code>true</code> for a key that's present.
     */
    @Test
    public void containsKeyShouldReturnTrueForAPresentKey() {
        assertTrue(createInternationalization().containsKey("foo"));
    }

    /**
     * Verifies that <code>containsKey</code> returns <code>false</code> for a key that's absent.
     */
    @Test
    public void containsKeyShouldReturnFalseForAnAbsentKey() {
        assertFalse(createInternationalization().containsKey("bar"));
    }

    /**
     * Verifies that <code>getTranslation</code> returns <code>null</code> for a key that's absent.
     */
    @Test
    public void getTranslationShouldReturnNullForAnAbsentKey() {
        assertNull(createInternationalization().getTranslation("bar", Language.ENGLISH));
    }

    /**
     * Verifies that <code>getTranslation</code> returns <code>null</code> for a language that's absent for a key.
     */
    @Test
    public void getTranslationShouldReturnNullForAnAbsentLanguageOnAKey() {
        assertNull(createInternationalization().getTranslation("foo", Language.FRENCH));
    }

    /**
     * Verifies that <code>getTranslation</code> returns the correct translation for a key and a language.
     */
    @Test
    public void getTranslationShouldReturnCorrectTranslationForKeyAndLanguage() {
        assertEquals("Foo", createInternationalization().getTranslation("foo", Language.ENGLISH));
    }

    /**
     * Verifies that the constructor using <code>Terms</code> processes them correctly.
     */
    @Test
    public void constructorWithTermsShouldProcessCorrectly() {
        Terms terms = new Terms();
        Term term = new Term();
        term.setKey("foo");
        term.setTranslations(Map.of("en", "Foo", "de", "Bar"));
        terms.setTerms(new Term[] {term});
        Internationalization internationalization = new Internationalization(terms);
        assertEquals("Foo", internationalization.getTranslation("foo", Language.ENGLISH));
        assertEquals("foo", internationalization.getTranslations("foo").getKey());
    }

    /**
     * Verifies that <code>getTranslations</code> for a key returns the <code>Translations</code> object for that key.
     */
    @Test
    public void getTranslationsShouldReturnTranslationsObjectForAKey() {
        Translations translations = createInternationalization().getTranslations("foo");
        assertEquals("Foo", translations.getTranslation(Language.ENGLISH));
        assertEquals("foo", translations.getKey());
    }

    /**
     * Verifies that <code>getTranslations</code> returns an array with the <code>Translations</code> objects.
     */
    @Test
    public void getTranslationsShouldReturnTranslationsArray() {
        Translations[] translations = createInternationalization().getTranslations();
        assertEquals(1, translations.length);
        assertEquals("Foo", translations[0].getTranslation(Language.ENGLISH));
        assertEquals("foo", translations[0].getKey());
    }
}
