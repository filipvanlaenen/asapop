package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about the sum of results being not within the interval of rounding errors.
 */
class ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning extends ParserWarning {
    /**
     * Constructor taking the line number as its parameter.
     *
     * @param lineNumber The number of the line where the warning occurred.
     */
    ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning(final int lineNumber) {
        super(lineNumber);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning) {
            ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning otherWarning =
                    (ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning) obj;
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
        return "Results in line " + getLineNumber() + " don’t add up within rounding error interval.";
    }
}
