package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about an unknown scope value.
 */
final class UnknownScopeValueWarning extends ParserWarning {
    /**
     * The unknown scope value.
     */
    private final String scopeValue;

    /**
     * Constructor taking the line number and the scope value as its parameters.
     *
     * @param lineNumber The number of the line where the warning occurred.
     * @param scopeValue The unknown scope value.
     */
    UnknownScopeValueWarning(final int lineNumber, final String scopeValue) {
        super(lineNumber);
        this.scopeValue = scopeValue;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof UnknownScopeValueWarning) {
            UnknownScopeValueWarning otherWarning = (UnknownScopeValueWarning) obj;
            return otherWarning.getLineNumber() == getLineNumber() && otherWarning.scopeValue.equals(scopeValue);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineNumber(), scopeValue);
    }

    @Override
    public String toString() {
        return "Unknown scope value (“" + scopeValue + "”) detected in line " + getLineNumber() + ".";
    }
}
