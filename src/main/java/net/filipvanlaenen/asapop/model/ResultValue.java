package net.filipvanlaenen.asapop.model;

import java.util.Objects;

/**
 * Class modeling a result value.
 */
public final class ResultValue {
    /**
     * The text representing the result value.
     */
    private final String text;

    /**
     * Constructor taking the text representing the result value as its parameter.
     *
     * @param text The text representing the result value.
     */
    public ResultValue(final String text) {
        this.text = text;
    }

    /**
     * Returns the text representing the result value.
     *
     * @return The text representing the result value.
     */
    public String getText() {
        return text;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ResultValue) {
            ResultValue otherResultValue = (ResultValue) obj;
            return otherResultValue.text.equals(text);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
