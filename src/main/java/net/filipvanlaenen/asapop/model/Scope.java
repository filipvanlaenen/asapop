package net.filipvanlaenen.asapop.model;

/**
 * Enumeration containing opinion poll scopes.
 */
public enum Scope {
    /**
     * Opinion polls for the election of the European Parliament.
     */
    EUROPEAN("E"),
    /**
     * Opinion polls for the election of the national parliament.
     */
    NATIONAL("N"),
    /**
     * Opinion polls for the first round of the presidential election.
     */
    PRESIDENTIAL_FIRST_ROUND("P1");

    private String ropfValue;

    Scope(final String ropfValue) {
        this.ropfValue = ropfValue;
    }

    @Override
    public String toString() {
        return ropfValue;
    }
}
