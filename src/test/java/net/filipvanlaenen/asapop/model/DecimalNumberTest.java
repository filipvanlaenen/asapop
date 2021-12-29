package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     * The decimal number 1.00.
     */
    private static final DecimalNumber DECIMAL_NUMBER_ONE_WITH_TWO_DECIMALS = new DecimalNumber(1F, 2);
    /**
     * The decimal number 2.0.
     */
    private static final DecimalNumber DECIMAL_NUMBER_TWO_WITH_ONE_DECIMAL = new DecimalNumber(2F, 1);

    /**
     * Verifies that the getter method <code>getValue</code> is wired correctly to the constructor.
     */
    @Test
    public void getValueShouldBeWiredCorrectlyToTheConstructor() {
        assertEquals(1.0F, DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.getValue());
    }

    /**
     * Verifies that the getter method <code>getNumberOfDecimals</code> is wired correctly to the constructor.
     */
    @Test
    public void getNumberOfDecimalsShouldBeWiredCorrectlyToTheConstructor() {
        assertEquals(1, DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.getNumberOfDecimals());
    }

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

    /**
     * Verifies that a decimal number is not equal to null.
     */
    @Test
    public void aDecimalNumberShouldNotBeEqualToNull() {
        assertFalse(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.equals(null));
    }

    /**
     * Verifies that a decimal number is not equal to an object of another class, like a string.
     */
    @Test
    public void aDecimalNumberShouldNotBeEqualToAString() {
        assertFalse(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.equals(""));
    }

    /**
     * Verifies that a decimal number is equal to itself.
     */
    @Test
    public void aDecimalNumberShouldBeEqualToItself() {
        assertTrue(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.equals(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL));
    }

    /**
     * Verifies that calling hashCode twice on a decimal number returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnDecimalNumberReturnsTheSameResult() {
        assertEquals(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.hashCode(), DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.hashCode());
    }

    /**
     * Verifies that two decimal numbers constructed with the same parameter are equal.
     */
    @Test
    public void twoDecimalNumbersConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL, new DecimalNumber(1.0F, 1));
    }

    /**
     * Verifies that two decimal numbers constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoDecimalNumbersConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.hashCode(), new DecimalNumber(1.0F, 1).hashCode());
    }

    /**
     * Verifies that two different decimal numbers with different values are not equal.
     */
    @Test
    public void twoDifferentDecimalNumbersWithDifferentValuesShouldNotBeEqual() {
        assertFalse(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.equals(DECIMAL_NUMBER_TWO_WITH_ONE_DECIMAL));
    }

    /**
     * Verifies that two different decimal numbers with different values have different hash codes.
     */
    @Test
    public void twoDifferentDecimalNumersWithDifferentValuesShouldHaveDifferentHashCodes() {
        assertFalse(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.hashCode() == DECIMAL_NUMBER_TWO_WITH_ONE_DECIMAL.hashCode());
    }

    /**
     * Verifies that two different decimal numbers with different number of decimals are not equal.
     */
    @Test
    public void twoDifferentDecimalNumbersWithDifferentNumberOfDecimalsShouldNotBeEqual() {
        assertFalse(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.equals(DECIMAL_NUMBER_ONE_WITH_TWO_DECIMALS));
    }

    /**
     * Verifies that two different decimal numbers with different number of decimals have different hash codes.
     */
    @Test
    public void twoDifferentDecimalNumersWithDifferentNumberOfDecimalsShouldHaveDifferentHashCodes() {
        assertFalse(DECIMAL_NUMBER_ONE_WITH_ONE_DECIMAL.hashCode() == DECIMAL_NUMBER_ONE_WITH_TWO_DECIMALS.hashCode());
    }
}
