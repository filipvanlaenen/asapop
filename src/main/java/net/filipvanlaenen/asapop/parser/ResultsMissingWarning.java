package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about a line missing results.
 */
class ResultsMissingWarning extends ParserWarning {
    /**
     * Constructor taking the line number as its parameter.
     *
     * @param lineNumber The number of the line where the warning occurred.
     */
    ResultsMissingWarning(final int lineNumber) {
        super(lineNumber);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ResultsMissingWarning) {
            ResultsMissingWarning otherWarning = (ResultsMissingWarning) obj;
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
        return "Results missing in line " + getLineNumber() + ".";
    }
}
