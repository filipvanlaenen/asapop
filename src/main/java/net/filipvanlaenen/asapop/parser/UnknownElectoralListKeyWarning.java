package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about an unknown electoral list key.
 */
final class UnknownElectoralListKeyWarning extends ParserWarning {
    /**
     * The unknown electoral list key.
     */
    private final String electoralListKey;

    /**
     * Constructor taking the line number and the electoral list key as its parameters.
     *
     * @param lineNumber       The number of the line where the warning occurred.
     * @param electoralListKey The unknown electoral list key.
     */
    UnknownElectoralListKeyWarning(final int lineNumber, final String electoralListKey) {
        super(lineNumber);
        this.electoralListKey = electoralListKey;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof UnknownElectoralListKeyWarning) {
            UnknownElectoralListKeyWarning otherWarning = (UnknownElectoralListKeyWarning) obj;
            return otherWarning.getLineNumber() == getLineNumber()
                    && otherWarning.electoralListKey.equals(electoralListKey);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineNumber(), electoralListKey);
    }

    @Override
    public String toString() {
        return "Unknown electoral list key (“" + electoralListKey + "”) detected in line " + getLineNumber() + ".";
    }
}
