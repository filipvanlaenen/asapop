package net.filipvanlaenen.asapop.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.filipvanlaenen.kolektoj.Collection;

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
     * The ID of the electoral list.
     */
    private final String id;
    /**
     * The map with the names for the electoral list.
     */
    private Map<String, String> names = new HashMap<String, String>();
    /**
     * The romanized abbreviation for the electoral list.
     */
    private String romanizedAbbreviation;

    /**
     * Returns the electoral list with the given ID if it already exists, or creates a new one otherwise.
     *
     * @param id ID of the electoral list.
     * @return The electoral list with that ID, or a new instance.
     */
    public static ElectoralList get(final String id) {
        if (instances.containsKey(id)) {
            return instances.get(id);
        }
        ElectoralList newInstance = new ElectoralList(id);
        instances.put(id, newInstance);
        return newInstance;
    }

    /**
     * Returns the set of electoral lists with the given IDs. New electoral lists will be created for the IDs for which
     * an electoral list didn't already exist.
     *
     * @param ids IDs of the electoral lists.
     * @return A set with the electoral list with the IDs.
     */
    public static Set<ElectoralList> get(final Collection<String> ids) {
        return ids.stream().map(id -> get(id)).collect(Collectors.toSet());
    }

    /**
     * Returns the set of electoral lists with the given IDs. New electoral lists will be created for the IDs for which
     * an electoral list didn't already exist.
     *
     * @param ids IDs of the electoral lists.
     * @return A set with the electoral list with the IDs.
     */
    public static Set<ElectoralList> get(final Set<String> ids) {
        return ids.stream().map(id -> get(id)).collect(Collectors.toSet());
    }

    /**
     * Returns the IDs for a set of electoral lists.
     *
     * @param electoralListSet The set of electoral lists.
     * @return The IDs for a set of the electoral lists.
     */
    public static Set<String> getIds(final Set<ElectoralList> electoralListSet) {
        return electoralListSet.stream().map(electoralList -> electoralList.getId()).collect(Collectors.toSet());
    }

    /**
     * Constructor using the electoral list's ID as the parameter.
     *
     * @param id The ID for the electoral list.
     */
    private ElectoralList(final String id) {
        this.id = id;
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
     * Returns the ID of the electoral list.
     *
     * @return The ID of the electoral list.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the language codes for which names are provided for this electoral list.
     *
     * @return A set with the language codes for which names are provided for this electoral list.
     */
    public Set<String> getLanguageCodes() {
        return names.keySet();
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
