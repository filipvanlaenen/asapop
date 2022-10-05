package net.filipvanlaenen.asapop.website;

/**
 * Enumeration containing the languages supported by the website builder.
 */
public enum Language {
    /**
     * The language Dutch.
     */
    Dutch("nl"),
    /**
     * The language English.
     */
    English("en"),
    /**
     * The language Esperanto.
     */
    Esperanto("eo"),
    /**
     * The language French.
     */
    French("fr"),
    /**
     * The language German.
     */
    German("de"),
    /**
     * The language Norwegian.
     */
    Norwegian("no");

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
