package net.filipvanlaenen.asapop.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing an electoral list.
 */
public final class ElectoralList {
    /**
     * A map with all the instances.
     */
    private static Map<String, ElectoralList> instances = new HashMap<String, ElectoralList>();
    /**
     * The abbreviation for the electoral list.
     */
    private String abbreviation;
    /**
     * The key of the electoral list.
     */
    private final String key;
    /**
     * The map with the names for the electoral list.
     */
    private Map<String, String> names = new HashMap<String, String>();

    /**
     * Returns the electoral list with the given key if it already exists, or
     * creates a new one otherwise.
     *
     * @param key Key of the electoral list.
     * @return The electoral list with that key, or a new instance.
     */
    public static ElectoralList get(final String key) {
        if (instances.containsKey(key)) {
            return instances.get(key);
        }
        ElectoralList newInstance = new ElectoralList(key);
        instances.put(key, newInstance);
        return newInstance;
    }

    /**
     * Constructor using the electoral list's key as the parameter.
     *
     * @param key The key for the electoral list.
     */
    private ElectoralList(final String key) {
        this.key = key;
    }

    /**
     * Returns the abbreviation for the electoral list.
     * @return The abbreviation for the electoral list.
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Returns the key of the electoral list.
     *
     * @return The key of the electoral list.
     */
    String getKey() {
        return key;
    }

    /**
     * Returns the name for electoral list for the given language code.
     *
     * @param languageCode The language code.
     * @return The name for the electoral list for the given language code.
     */
    public String getName(final String languageCode) {
        return names.get(languageCode);
    }

    /**
     * Sets the abbreviation for the electoral list.
     *
     * @param abbreviation The abbreviation for the electoral list.
     */
    public void setAbbreviation(final String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Sets the names for the electoral list.
     *
     * @param names The names for the electoral list.
     */
    public void setNames(final Map<String, String> names) {
        this.names = new HashMap<String, String>(names);
    }
}
