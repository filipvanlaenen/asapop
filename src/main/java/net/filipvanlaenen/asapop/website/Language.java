package net.filipvanlaenen.asapop.website;

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
}
