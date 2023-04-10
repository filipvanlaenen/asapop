package net.filipvanlaenen.asapop.model;

/**
 * Enumeration containing the election types.
 */
public enum ElectionType {
    /**
     * Presidential election.
     */
    PRESIDENTIAL("president"),
    /**
     * National parliamentary election.
     */
    NATIONAL("parliament"),
    /**
     * European election.
     */
    EUROPEAN("european-parliament");

    /**
     * The key of the term for this election type.
     */
    private final String termKey;

    /**
     * Constructor taking the key of the term for this election type as its parameter.
     *
     * @param termKey The key of the term for this election type.
     */
    ElectionType(final String termKey) {
        this.termKey = termKey;
    }

    /**
     * Returns the key of the term for this election type.
     *
     * @return The key of the term for this election type.
     */
    public String getTermKey() {
        return termKey;
    }
}
