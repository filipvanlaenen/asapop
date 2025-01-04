package net.filipvanlaenen.asapop.website;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration containing the languages supported by the website builder.
 */
public enum Language {
    /**
     * The language Dutch.
     */
    DUTCH("nl"),
    /**
     * The language English.
     */
    ENGLISH("en"),
    /**
     * The language Esperanto.
     */
    ESPERANTO("eo"),
    /**
     * The language French.
     */
    FRENCH("fr"),
    /**
     * The language German.
     */
    GERMAN("de"),
    /**
     * The language Norwegian.
     */
    NORWEGIAN("no");

    /**
     * The identifier for the language.
     */
    private final String id;
    /**
     * A map mapping string values to their language.
     */
    private static final Map<String, Language> VALUE_MAP = new HashMap<String, Language>();

    static {
        for (Language language : values()) {
            VALUE_MAP.put(language.id, language);
        }
    }

    /**
     * Constructor using the identifier of the language as its parameter.
     *
     * @param id The identifier for the language.
     */
    Language(final String id) {
        this.id = id;
    }

    /**
     * Returns the identifier for the language.
     *
     * @return The identifier for the language.
     */
    public String getId() {
        return id;
    }

    /**
     * Parses a string into a language.
     *
     * @param id The string to parse.
     * @return The language represented by the string.
     */
    public static Language parse(final String id) {
        return VALUE_MAP.get(id);
    }
}
