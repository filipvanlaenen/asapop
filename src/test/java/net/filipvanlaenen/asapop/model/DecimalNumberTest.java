package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>DecimalNumber</code> class.
 */
public class DecimalNumberTest {
    /**
     * The decimal number 1.
     */
    private static final DecimalNumber DECIMAL_NUMBER_ONE_WITH_NO_DECIMALS = new DecimalNumber(1F, 0);
    /**
     * The decimal number 1.0.
     */
    private static final DecimalNumber DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL = new DecimalNumber(1F, 1);

    /**
     * Verifies that a decimal number with no decimals is parsed correctly.
     */
    @Test
    public void aDecimalNumberWithNoDecimalsShouldBeParsedCorrectly() {
        assertEquals(DECIMAL_NUMBER_ONE_WITH_NO_DECIMALS, DecimalNumber.parse("1"));
    }

    /**
     * Verifies that a decimal number with one decimal is parsed correctly.
     */
    @Test
    public void aDecimalNumberWithOneDecimalShouldBeParsedCorrectly() {
        assertEquals(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL, DecimalNumber.parse("1.0"));
    }

    /**
     * Verifies that a decimal number with one decimal is parsed correctly even if it includes trailing whitespace.
     */
    @Test
    public void aDecimalNumberWithOneDecimalAndTrailingWhitespaceShouldBeParsedCorrectly() {
        assertEquals(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL, DecimalNumber.parse("1.0 "));
    }

    /**
     * Verifies that a decimal number with no decimals is converted correctly to a string.
     */
    @Test
    public void toStringShouldConvertDecimalNumberWithNoDecimalsCorrectly() {
        assertEquals("1", DECIMAL_NUMBER_ONE_WITH_NO_DECIMALS.toString());
    }

    /**
     * Verifies that a decimal number with 1 decimal is converted correctly to a string.
     */
    @Test
    public void toStringShouldConvertDecimalNumberWithOneDecimalCorrectly() {
        assertEquals("1.0", DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.toString());
    }
}
