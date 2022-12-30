package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about a single value metadata key occurring more than once.
 */
final class SingleValueMetadataKeyOccurringMoreThanOnceWarning extends ParserWarning {
    /**
     * The single value metadata key occurring more than once.
     */
    private final String metadataKey;

    /**
     * Constructor taking the line number and the metadata key as its parameters.
     *
     * @param lineNumber  The number of the line where the warning occurred.
     * @param metadataKey The single value metadata key occuring more than once.
     */
    SingleValueMetadataKeyOccurringMoreThanOnceWarning(final int lineNumber, final String metadataKey) {
        super(lineNumber);
        this.metadataKey = metadataKey;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SingleValueMetadataKeyOccurringMoreThanOnceWarning) {
            SingleValueMetadataKeyOccurringMoreThanOnceWarning otherWarning =
                    (SingleValueMetadataKeyOccurringMoreThanOnceWarning) obj;
            return otherWarning.getLineNumber() == getLineNumber() && otherWarning.metadataKey.equals(metadataKey);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineNumber(), metadataKey);
    }

    @Override
    public String toString() {
        return "Single value metadata key (“" + metadataKey + "”) occurring more than once in line " + getLineNumber()
                + ".";
    }
}
