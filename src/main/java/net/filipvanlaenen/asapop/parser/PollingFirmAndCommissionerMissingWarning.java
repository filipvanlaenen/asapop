package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about a line missing both polling firm and commissioner.
 */
class PollingFirmAndCommissionerMissingWarning extends ParserWarning {
    /**
     * Constructor taking the line number as its parameter.
     *
     * @param lineNumber The number of the line where the warning occurred.
     */
    PollingFirmAndCommissionerMissingWarning(final int lineNumber) {
        super(lineNumber);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PollingFirmAndCommissionerMissingWarning) {
            PollingFirmAndCommissionerMissingWarning otherWarning = (PollingFirmAndCommissionerMissingWarning) obj;
            return otherWarning.getLineNumber() == getLineNumber();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineNumber());
    }

    @Override
    public String toString() {
        return "Polling firm and commissioner missing in line " + getLineNumber() + ".";
    }
}
