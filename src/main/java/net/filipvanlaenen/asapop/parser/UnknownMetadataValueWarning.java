package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about an unknown metadata value.
 */
final class UnknownMetadataValueWarning extends ParserWarning {
    /**
     * The metadata field name.
     */
    private final String metadataFieldName;
    /**
     * The unknown metadata value.
     */
    private final String metadataValue;

    /**
     * Constructor taking the line number and the metadata value as its parameters.
     *
     * @param lineNumber        The number of the line where the warning occurred.
     * @param metadataFieldName The name of the metadata field.
     * @param metadataValue     The unknown metadata value.
     */
    UnknownMetadataValueWarning(final int lineNumber, final String metadataFieldName, final String metadataValue) {
        super(lineNumber);
        this.metadataFieldName = metadataFieldName;
        this.metadataValue = metadataValue;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof UnknownMetadataValueWarning) {
            UnknownMetadataValueWarning otherWarning = (UnknownMetadataValueWarning) obj;
            return otherWarning.getLineNumber() == getLineNumber()
                    && otherWarning.metadataFieldName.equals(metadataFieldName)
                    && otherWarning.metadataValue.equals(metadataValue);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineNumber(), metadataFieldName, metadataValue);
    }

    @Override
    public String toString() {
        return "Unknown " + metadataFieldName + " (“" + metadataValue + "”) detected in line " + getLineNumber() + ".";
    }
}
