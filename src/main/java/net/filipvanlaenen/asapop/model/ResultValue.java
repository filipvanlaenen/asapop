package net.filipvanlaenen.asapop.model;

import java.util.Objects;

/**
 * Class modeling a result value.
 */
public final class ResultValue {
    /**
     * Enumeration with the precisions for result values.
     */
    public enum Precision {
        /**
         * A precision of 1.
         */
        ONE("1"),
        /**
         * A precision of 0.5.
         */
        HALF("0.5"),
        /**
         * A precision of 0.1.
         */
        TENTH("0.1");

        /**
         * The string representation of the precision.
         */
        private final String stringValue;

        /**
         * Constructor taking the string representation as its parameter.
         *
         * @param stringValue The string representation of the precision.
         */
        Precision(final String stringValue) {
            this.stringValue = stringValue;
        }

        /**
         * Returns the highest precision.
         *
         * @param p1 The first precision.
         * @param p2 The second precision.
         * @return The highest precision.
         */
        public static Precision highest(final Precision p1, final Precision p2) {
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            return p1.ordinal() < p2.ordinal() ? p2 : p1;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    /**
     * Flag indicating whether this is a less than value.
     */
    private final boolean lessThan;
    /**
     * The nominal value of the result value.
     */
    private final Double nominalValue;
    /**
     * The precision of th result value.
     */
    private final Precision precision;
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
        this.lessThan = text.startsWith("<");
        this.nominalValue = calculateNominalValue();
        this.precision = calculatePrecision();
    }

    /**
     * Calculates the nominal value of the result value.
     *
     * @return The nominal value of the result value.
     */
    private Double calculateNominalValue() {
        try {
            return Double.parseDouble(getPrimitiveText());
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    /**
     * Calculates the precision of the result value.
     *
     * @return The precision of the result value.
     */
    private Precision calculatePrecision() {
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

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ResultValue) {
            ResultValue otherResultValue = (ResultValue) obj;
            return otherResultValue.text.equals(text);
        } else {
            return false;
        }
    }

    /**
     * Returns the nominal value of the result value.
     *
     * @return The nominal value of the result value.
     */
    public Double getNominalValue() {
        return nominalValue;
    }

    /**
     * Returns the precision of the result value.
     *
     * @return The precision of the result value.
     */
    public Precision getPrecision() {
        return precision;
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
    public int hashCode() {
        return Objects.hash(text);
    }
}
