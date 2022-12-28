package net.filipvanlaenen.asapop.parser;

import java.util.Objects;

/**
 * A warning about a metadata field containing a malformed date or month.
 */
class MalformedDateOrMonthWarning extends ParserWarning {
    /**
     * The key.
     */
    private final String key;
    /**
     * The malformed value.
     */
    private final String value;

    /**
     * Constructor taking the line number, the metadata field key and value as its parameters.
     *
     * @param lineNumber The line number where the warning was detected.
     * @param key        The key of the metadata field.
     * @param value      The value of the metadata field containing a malformed decimal number.
     */
    MalformedDateOrMonthWarning(final int lineNumber, final String key, final String value) {
        super(lineNumber);
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MalformedDateOrMonthWarning) {
            MalformedDateOrMonthWarning otherWarning = (MalformedDateOrMonthWarning) obj;
            return otherWarning.getLineNumber() == getLineNumber() && otherWarning.key.equals(key)
                    && otherWarning.value.equals(value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineNumber(), key, value);
    }

    @Override
    public String toString() {
        return "Malformed date or month (“" + value + "”) detected for metadata field “" + key + "” in line "
                + getLineNumber() + ".";
    }
}
