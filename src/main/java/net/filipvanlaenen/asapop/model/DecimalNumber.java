package net.filipvanlaenen.asapop.model;

import java.util.Locale;

/**
 * Class representing a decimal number. A decimal number has a value and a number of decimals.
 *
 * @param value            The value of the decimal number.
 * @param numberOfDecimals The number of decimals for the decimal number.
 */
public record DecimalNumber(float value, int numberOfDecimals) {
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
    public String toString() {
        return String.format(Locale.US, "%." + numberOfDecimals + "f", value);
    }
}
