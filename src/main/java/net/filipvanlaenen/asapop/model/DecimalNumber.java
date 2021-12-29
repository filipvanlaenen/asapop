package net.filipvanlaenen.asapop.model;

import java.util.Locale;
import java.util.Objects;

/**
 * Class representing a decimal number. A decimal number has a value and a number of decimals.
 */
public final class DecimalNumber {
    /**
     * The value of the decimal number.
     */
    private final float value;
    /**
     * The number of decimals for the decimal number.
     */
    private final int numberOfDecimals;

    /**
     * Constructor using the value and the number of decimals as its parameters.
     *
     * @param value            The value of the decimal number.
     * @param numberOfDecimals The number of decimals for the decimal number.
     */
    public DecimalNumber(final float value, final int numberOfDecimals) {
        this.value = value;
        this.numberOfDecimals = numberOfDecimals;
    }

    /**
     * Parses a string into a decimal number.
     *
     * @param string The string to be parsed into a decimal number.
     * @return A decimal number parsed from the string.
     */
    public static DecimalNumber parse(final String string) {
        float value = Float.parseFloat(string);
        if (string.contains(".")) {
            String strippedString = string.strip();
            int indexOfDecimalPoint = strippedString.indexOf(".");
            return new DecimalNumber(value, strippedString.length() - indexOfDecimalPoint - 1);
        } else {
            return new DecimalNumber(value, 0);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DecimalNumber) {
            DecimalNumber other = (DecimalNumber) obj;
            return numberOfDecimals == other.numberOfDecimals && value == other.value;
        } else {
            return false;
        }
    }

    /**
     * Returns the number of decimals for the decimal number.
     *
     * @return The number of decimals for the decimal number.
     */
    public int getNumberOfDecimals() {
        return numberOfDecimals;
    }

    /**
     * Returns the value of the decimal number.
     *
     * @return The value of the decimal number.
     */
    public float getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfDecimals, value);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%." + numberOfDecimals + "f", value);
    }
}
