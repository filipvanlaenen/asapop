package net.filipvanlaenen.asapop.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration containing opinion poll result value units.
 */
public enum Unit {
    /**
     * Percentages as the units for the result values.
     */
    PERCENTAGES("%"),
    /**
     * Seats as the units for the result values.
     */
    SEATS("S");

    /**
     * The string representation of the unit.
     */
    private String stringValue;
    /**
     * A map mapping string values to their unit.
     */
    private static final Map<String, Unit> VALUE_MAP = new HashMap<String, Unit>();

    static {
        for (Unit unit : values()) {
            VALUE_MAP.put(unit.stringValue, unit);
        }
    }

    /**
     * Constructor taking the string value as its parameter.
     *
     * @param stringValue The string representation of the unit.
     */
    Unit(final String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * Parses a string into a unit.
     *
     * @param string The string to parse.
     * @return The unit represented by the string.
     */
    public static Unit parse(final String string) {
        return VALUE_MAP.get(string);
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
