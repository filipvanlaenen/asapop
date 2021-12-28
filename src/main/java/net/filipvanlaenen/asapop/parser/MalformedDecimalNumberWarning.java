package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

public class MalformedDecimalNumberWarning extends Warning {
    /**
     * The key.
     */
    private final String key;
    /**
     * The malformed value.
     */
    private final String value;

    public MalformedDecimalNumberWarning(int lineNumber, String key, String value) {
        super(lineNumber);
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MalformedDecimalNumberWarning) {
            MalformedDecimalNumberWarning otherWarning = (MalformedDecimalNumberWarning) obj;
            return otherWarning.getLineNumber() == getLineNumber() && otherWarning.key.equals(key)
                    && otherWarning.value.equals(value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineNumber(), key, value);
    }

    @Override
    public String toString() {
        return "Malformed decimal number (“" + value + "”) detected for metadata field “" + key + "” in line "
                + getLineNumber() + ".";
    }
}
