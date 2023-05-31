package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about a malformed verified sum.
 */
public class MalformedVerifiedSumWarning extends ParserWarning {
    /**
     * The malformed verified sum.
     */
    private final String verifiedSum;

    /**
     * Constructor taking the line number and the verified sum as its parameters.
     *
     * @param lineNumber  The number of the line where the warning occurred.
     * @param verifiedSum The verified sum.
     */
    MalformedVerifiedSumWarning(final int lineNumber, final String verifiedSum) {
        super(lineNumber);
        this.verifiedSum = verifiedSum;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MalformedVerifiedSumWarning) {
            MalformedVerifiedSumWarning otherWarning = (MalformedVerifiedSumWarning) obj;
            return otherWarning.getLineNumber() == getLineNumber() && otherWarning.verifiedSum.equals(verifiedSum);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineNumber(), verifiedSum);
    }

    @Override
    public String toString() {
        return "Malformed verified sum (“" + verifiedSum + "”) detected in line " + getLineNumber() + ".";
    }
}
