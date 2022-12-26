package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about a line missing dates.
 */
class DatesMissingWarning extends ParserWarning {
    /**
     * Constructor taking the line number as its parameter.
     *
     * @param lineNumber The number of the line where the warning occurred.
     */
    DatesMissingWarning(final int lineNumber) {
        super(lineNumber);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DatesMissingWarning) {
            DatesMissingWarning otherWarning = (DatesMissingWarning) obj;
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
        return "Dates missing in line " + getLineNumber() + ".";
    }
}
