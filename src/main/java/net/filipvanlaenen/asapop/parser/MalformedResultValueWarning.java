package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

class MalformedResultValueWarning implements Warning {
    private final int lineNumber;
    private final String value;
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
        return "Malformed result value (“" + value + "”) detected in line " + lineNumber+".";
    }
}
