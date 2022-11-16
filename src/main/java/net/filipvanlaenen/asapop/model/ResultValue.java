package net.filipvanlaenen.asapop.model;

import java.util.Objects;

/**
 * Class modeling a result value.
 */
public final class ResultValue {

    public enum Precision {

        ONE("1"), HALF("0.5"), TENTH("0.1");

        private final String stringValue;

        Precision(String stringValue) {
            this.stringValue = stringValue;
        }

        public static Precision highest(Precision p1, Precision p2) {
            return p1.ordinal() < p2.ordinal() ? p2 : p1;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    /**
     * The text representing the result value.
     */
    private final String text;
    /**
     * Flag indicating whether this is a less than value.
     */
    private boolean lessThan;

    /**
     * Constructor taking the text representing the result value as its parameter.
     *
     * @param text The text representing the result value.
     */
    public ResultValue(final String text) {
        this.text = text;
        this.lessThan = text.startsWith("<");
    }

    public Double getNominalValue() {
        return Double.parseDouble(getPrimitiveText());
    }

    public Precision getPrecision() {
        String number = getPrimitiveText();
        if (number.contains(".")) {
            if (number.endsWith(".5")) {
                return Precision.HALF;
            } else if (!number.endsWith(".0")) {
                return Precision.TENTH;
            }
        }
        return Precision.ONE;
    }

    /**
     * Returns a primitive text representing the result value.
     *
     * @return A primitive text representing the result value.
     */
    public String getPrimitiveText() {
        return lessThan ? "0" : text;
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
