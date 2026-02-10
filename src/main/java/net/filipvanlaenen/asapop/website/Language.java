package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;

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
     * A map mapping IDs to the language.
     */
    private static final Map<String, Language> VALUE_MAP = createValueMap();

    /**
     * Creates the value map, mapping IDs to languages.
     *
     * @return A map mapping the IDs to the languages.
     */
    private static Map<String, Language> createValueMap() {
        ModifiableMap<String, Language> map = ModifiableMap.empty();
        for (Language language : values()) {
            map.put(language.id, language);
        }
        return Map.of(map);
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
     * Parses an ID into a language.
     *
     * @param id The string with the ID to parse.
     * @return The language represented by the string.
     */
    public static Language parse(final String id) {
        return VALUE_MAP.get(id);
    }
}
