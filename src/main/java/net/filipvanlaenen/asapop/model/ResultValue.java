package net.filipvanlaenen.asapop.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

/**
 * Class modeling a result value.
 */
public final class ResultValue {
    /**
     * The decimal format symbols for the US.
     */
    private static final DecimalFormatSymbols US_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.US);
    /**
     * The decimal format for integers, i.e. no decimals.
     */
    private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("0", US_FORMAT_SYMBOLS);
    /**
     * The decimal format for one decimal digit.
     */
    private static final DecimalFormat ONE_DECIMAL_FORMAT = new DecimalFormat("0.0", US_FORMAT_SYMBOLS);

    /**
     * Enumeration with the precisions for result values.
     */
    public enum Precision {
        /**
         * A precision of 1.
         */
        ONE("1", 1D, INTEGER_FORMAT),
        /**
         * A precision of 0.5.
         */
        HALF("0.5", 0.5D, ONE_DECIMAL_FORMAT),
        /**
         * A precision of 0.1.
         */
        TENTH("0.1", 0.1D, ONE_DECIMAL_FORMAT);

        /**
         * The decimal format of the precision.
         */
        private final DecimalFormat format;
        /**
         * The string representation of the precision.
         */
        private final String stringValue;
        /**
         * The numerical value of the precision.
         */
        private final double value;

        /**
         * Constructor taking the string representation as its parameter.
         *
         * @param stringValue The string representation of the precision.
         * @param value       The numerical value of the precision.
         * @param format      The decimal format of the precision.
         */
        Precision(final String stringValue, final double value, final DecimalFormat format) {
            this.stringValue = stringValue;
            this.value = value;
            this.format = format;
        }

        /**
         * Returns the decimal format of the precision.
         *
         * @return The decimal format of the precision.
         */
        public DecimalFormat getFormat() {
            return format;
        }

        /**
         * Returns the numerical value of the precision.
         *
         * @return The numerical value of the precision.
         */
        public double getValue() {
            return value;
        }

        /**
         * Returns the highest precision.
         *
         * @param p1 The first precision.
         * @param p2 The second precision.
         * @return The highest precision.
         */
        static Precision highest(final Precision p1, final Precision p2) {
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            return p1.ordinal() < p2.ordinal() ? p2 : p1;
        }

        /**
         * Returns the highest precision in a collection of result values.
         *
         * @param resultValues The result values.
         * @return The highest precision in the collection of result values.
         */
        public static Precision getHighestPrecision(final Collection<ResultValue> resultValues) {
            Precision result = ONE;
            for (ResultValue resultValue : resultValues) {
                result = highest(result, resultValue.getPrecision());
            }
            return result;
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
            return 0D;
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
