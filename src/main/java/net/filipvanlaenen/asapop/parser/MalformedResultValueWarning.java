package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about a malformed result value.
 */
class MalformedResultValueWarning implements Warning {
    /**
     * The number of the line where the warning occurred.
     */
    private final int lineNumber;
    /**
     * The malformed value.
     */
    private final String value;

    /**
     * Constructor taking the line number and the value as its parameters.
     *
     * @param lineNumber The number of the line where the warning occured.
     * @param value The malformed value.
     */
    MalformedResultValueWarning(final int lineNumber, final String value) {
        this.lineNumber = lineNumber;
        this.value = value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MalformedResultValueWarning) {
            MalformedResultValueWarning otherWarning = (MalformedResultValueWarning) obj;
            return otherWarning.lineNumber == lineNumber && otherWarning.value.equals(value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumber, value);
    }

    @Override
    public String toString() {
        return "Malformed result value (“" + value + "”) detected in line " + lineNumber + ".";
    }
}
