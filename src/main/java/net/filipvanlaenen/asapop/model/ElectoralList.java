package net.filipvanlaenen.asapop.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
     * The romanized abbreviation for the electoral list.
     */
    private String romanizedAbbreviation;

    /**
     * Returns the electoral list with the given key if it already exists, or creates a new one otherwise.
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
     * Returns the set of electoral lists with the given keys. New electoral lists will be created for the keys for
     * which an electoral list didn't already exist.
     *
     * @param keys Keys of the electoral lists.
     * @return A set with the electoral list with the keys.
     */
    public static Set<ElectoralList> get(final Set<String> keys) {
        return keys.stream().map(key -> get(key)).collect(Collectors.toSet());
    }

    /**
     * Returns the keys for a set of electoral lists.
     *
     * @param electoralListSet The set of electoral lists.
     * @return The keys for a set of the electoral lists.
     */
    public static Set<String> getKeys(final Set<ElectoralList> electoralListSet) {
        return electoralListSet.stream().map(electoralList -> electoralList.getKey()).collect(Collectors.toSet());
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
     *
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
    public String getKey() {
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
     * Returns the romanized abbreviation for the electoral list.
     *
     * @return The romanized abbreviation for the electoral list.
     */
    public String getRomanizedAbbreviation() {
        return romanizedAbbreviation;
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

    /**
     * Sets the romanized abbreviation for the electoral list.
     *
     * @param romanizedAbbreviation The romanized abbreviation for the electoral list.
     */
    public void setRomanizedAbbreviation(final String romanizedAbbreviation) {
        this.romanizedAbbreviation = romanizedAbbreviation;
    }
}
