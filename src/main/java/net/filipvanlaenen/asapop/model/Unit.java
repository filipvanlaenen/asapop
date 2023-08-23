package net.filipvanlaenen.asapop.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration containing opinion poll result value units.
 */
public enum Unit {
    PERCENTAGES("%"), SEATS("S");

    private String stringValue;

    private final static Map<String, Unit> VALUE_MAP = new HashMap<String, Unit>();

    static {
        for (Unit unit : values()) {
            VALUE_MAP.put(unit.stringValue, unit);
        }
    }

    Unit(final String stringValue) {
        this.stringValue = stringValue;
    }

    public static Unit parse(final String string) {
        return VALUE_MAP.get(string);
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
