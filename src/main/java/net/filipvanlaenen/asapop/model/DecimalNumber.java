package net.filipvanlaenen.asapop.model;

import java.util.Locale;
import java.util.Objects;

public class DecimalNumber {
    private final float value;
    private final int numberOfDecimals;

    public DecimalNumber(final float value, final int numberOfDecimals) {
        this.value = value;
        this.numberOfDecimals = numberOfDecimals;
    }

    public static DecimalNumber parse(String string) {
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

    public int getNumberOfDecimals() {
        return numberOfDecimals;
    }

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
