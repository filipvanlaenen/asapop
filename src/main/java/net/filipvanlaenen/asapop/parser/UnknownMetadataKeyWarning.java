package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about an unknown metadata key.
 */
class UnknownMetadataKeyWarning implements Warning {
    /**
     * The number of the line where the warning occurred.
     */
    private final int lineNumber;
    /**
     * The unknown metadata key.
     */
    private final String metadataKey;

    /**
     * Constructor taking the line number and the metadata key as its parameters.
     *
     * @param lineNumber  The number of the line where the warning occured.
     * @param metadataKey The unknown metadata key.
     */
    UnknownMetadataKeyWarning(final int lineNumber, final String metadataKey) {
        this.lineNumber = lineNumber;
        this.metadataKey = metadataKey;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof UnknownMetadataKeyWarning) {
            UnknownMetadataKeyWarning otherWarning = (UnknownMetadataKeyWarning) obj;
            return otherWarning.lineNumber == lineNumber && otherWarning.metadataKey.equals(metadataKey);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumber, metadataKey);
    }

    @Override
    public String toString() {
        return "Unknown metadata key (“" + metadataKey + "”) detected in line " + lineNumber + ".";
    }
}
