package net.filipvanlaenen.asapop.model;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * The string representation of the scope.
     */
    private String stringValue;

    /**
     * A map mapping string values to their scope.
     */
    private static final Map<String, Scope> VALUE_MAP = new HashMap<String, Scope>();

    static {
        for (Scope scope : values()) {
            VALUE_MAP.put(scope.stringValue, scope);
        }
    }

    /**
     * Constructor taking the string value as its parameter.
     *
     * @param stringValue The string representation of the scope.
     */
    Scope(final String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * Parses a string into a scope.
     *
     * @param string The string to parse.
     * @return The scope represented by the string.
     */
    public static Scope parse(final String string) {
        return VALUE_MAP.get(string);
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
