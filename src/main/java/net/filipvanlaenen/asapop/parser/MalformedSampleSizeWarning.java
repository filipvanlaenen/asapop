package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about a malformed sample size.
 */
class MalformedSampleSizeWarning extends ParserWarning {
    /**
     * The malformed sample size.
     */
    private final String sampleSize;

    /**
     * Constructor taking the line number and the sample size as its parameters.
     *
     * @param lineNumber The number of the line where the warning occurred.
     * @param sampleSize The malformed sample size.
     */
    MalformedSampleSizeWarning(final int lineNumber, final String sampleSize) {
        super(lineNumber);
        this.sampleSize = sampleSize;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MalformedSampleSizeWarning) {
            MalformedSampleSizeWarning otherWarning = (MalformedSampleSizeWarning) obj;
            return otherWarning.getLineNumber() == getLineNumber() && otherWarning.sampleSize.equals(sampleSize);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineNumber(), sampleSize);
    }

    @Override
    public String toString() {
        return "Malformed sample size (“" + sampleSize + "”) detected in line " + getLineNumber() + ".";
    }
}
