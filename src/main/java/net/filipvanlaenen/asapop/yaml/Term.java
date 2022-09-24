package net.filipvanlaenen.asapop.yaml;

import java.util.Map;

/**
 * Class representing a term with a key and the translation in a number of languages.
 */
public class Term {
    /**
     * The key for the term.
     */
    private String key;
    /**
     * A map with the translation of the term in a number of languages.
     */
    private Map<String, String> translations;

    /**
     * Returns the key of the term.
     *
     * @return The key of the term.
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns a map with the translation of the term in a number of languages.
     *
     * @return A map with the translation of the term in a number of languages.
     */
    public Map<String, String> getTranslations() {
        return translations;
    }

    /**
     * Sets the key of the term.
     *
     * @param key The key of the term.
     */
    public void setKey(final String key) {
        this.key = key;
    }

    /**
     * Sets the map with the translations of the term in a number of languages.
     *
     * @param translations The map with the translations of the term in a number of languages.
     */
    public void setTranslations(final Map<String, String> translations) {
        this.translations = translations;
    }
}
