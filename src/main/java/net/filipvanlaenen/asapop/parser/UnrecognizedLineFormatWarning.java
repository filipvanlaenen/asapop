package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about a line with an unrecognized format.
 */
class UnrecognizedLineFormatWarning extends ParserWarning {
    /**
     * Constructor taking the line number as its parameter.
     *
     * @param lineNumber The number of the line where the warning occurred.
     */
    UnrecognizedLineFormatWarning(final int lineNumber) {
        super(lineNumber);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof UnrecognizedLineFormatWarning) {
            UnrecognizedLineFormatWarning otherWarning = (UnrecognizedLineFormatWarning) obj;
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
        return "Unrecognized line format on line " + getLineNumber() + ".";
    }
}
