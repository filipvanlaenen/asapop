package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about an unknown metadata key.
 */
final class UnknownMetadataKeyWarning extends Warning {
    /**
     * The unknown metadata key.
     */
    private final String metadataKey;

    /**
     * Constructor taking the line number and the metadata key as its parameters.
     *
     * @param lineNumber  The number of the line where the warning occurred.
     * @param metadataKey The unknown metadata key.
     */
    UnknownMetadataKeyWarning(final int lineNumber, final String metadataKey) {
        super(lineNumber);
        this.metadataKey = metadataKey;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof UnknownMetadataKeyWarning) {
            UnknownMetadataKeyWarning otherWarning = (UnknownMetadataKeyWarning) obj;
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
        return "Unknown metadata key (“" + metadataKey + "”) detected in line " + getLineNumber() + ".";
    }
}
