package net.filipvanlaenen.asapop.model;

import net.filipvanlaenen.kolektoj.ModifiableMap;

/**
 * Class representing a candidate.
 */
public final class Candidate {
    /**
     * A map with all the instances.
     */
    private static ModifiableMap<String, Candidate> instances = ModifiableMap.<String, Candidate>empty();

    /**
     * The abbreviation for the candidate.
     */
    private String abbreviation;
    /**
     * The ID of the candidate.
     */
    private final String id;
    /**
     * The name for the candidate.
     */
    private String name;
    /**
     * The romanized for the candidate.
     */
    private String romanizedName;

    /**
     * Constructor using the candidate's ID as the parameter.
     *
     * @param id The ID for the candidate.
     */
    private Candidate(final String id) {
        this.id = id;
    }

    /**
     * Returns the candidate with the given ID if it already exists, or creates a new one otherwise.
     *
     * @param id ID of the candidate.
     * @return The candidate with that ID, or a new instance.
     */
    public static Candidate get(final String id) {
        if (instances.containsKey(id)) {
            return instances.get(id);
        }
        Candidate newInstance = new Candidate(id);
        instances.put(id, newInstance);
        return newInstance;
    }

    /**
     * Returns the abbreviation for the candidate.
     *
     * @return The abbreviation for the candidate.
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Returns the ID for the candidate.
     *
     * @return The ID for the candidate.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name for the candidate.
     *
     * @return The name for the candidate.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the romanized name for the candidate.
     *
     * @return The romanized name for the candidate.
     */
    public String getRomanizedName() {
        return romanizedName;
    }

    /**
     * Sets the abbreviation for the candidate.
     *
     * @param abbreviation The abbreviation for the candidate.
     */
    public void setAbbreviation(final String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Sets the name for the candidate.
     *
     * @param name The name for the candidate.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the romanized name for the candidate.
     *
     * @param romanizedName The romanized name for the candidate.
     */
    public void setRomanizedName(final String romanizedName) {
        this.romanizedName = romanizedName;
    }
}
