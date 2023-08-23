package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about an unknown unit value.
 */
final class UnknownUnitValueWarning extends ParserWarning {
    /**
     * The unknown unit value.
     */
    private final String unitValue;

    /**
     * Constructor taking the line number and the unit value as its parameters.
     *
     * @param lineNumber The number of the line where the warning occurred.
     * @param unitValue  The unknown unit value.
     */
    UnknownUnitValueWarning(final int lineNumber, final String unitValue) {
        super(lineNumber);
        this.unitValue = unitValue;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof UnknownUnitValueWarning) {
            UnknownUnitValueWarning otherWarning = (UnknownUnitValueWarning) obj;
            return otherWarning.getLineNumber() == getLineNumber() && otherWarning.unitValue.equals(unitValue);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineNumber(), unitValue);
    }

    @Override
    public String toString() {
        return "Unknown unit value (“" + unitValue + "”) detected in line " + getLineNumber() + ".";
    }
}
