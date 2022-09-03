package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>DatesMissingWarning</code> class.
 */
public final class DatesMissingWarningTest {
    /**
     * A dates missing warning for a on line 1.
     */
    private static final DatesMissingWarning WARNING_1 = new DatesMissingWarning(1);

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Dates missing in line 1.", WARNING_1.toString());
    }

    /**
     * Verifies that a dates missing warning is not equal to null.
     */
    @Test
    public void aDatesMissingWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1.equals(null));
    }

    /**
     * Verifies that a dates missing warning is not equal to an object of another class, like a string.
     */
    @Test
    public void aDatesMissingWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1.equals(""));
    }

    /**
     * Verifies that a dates missing warning is equal to itself.
     */
    @Test
    public void aDatesMissingWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1.equals(WARNING_1));
    }

    /**
     * Verifies that calling hashCode twice on a dates missing warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnDatesMissingWarningReturnsTheSameResult() {
        assertEquals(WARNING_1.hashCode(), WARNING_1.hashCode());
    }

    /**
     * Verifies that two dates missing warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoDatesMissingWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1, new DatesMissingWarning(1));
    }

    /**
     * Verifies that two dates missing warnings constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoDatesMissingWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1.hashCode(), new DatesMissingWarning(1).hashCode());
    }

    /**
     * Verifies that two different dates missing warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentDatesMissingWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1.equals(new DatesMissingWarning(2)));
    }

    /**
     * Verifies that two different dates missing warnings with different line numbers have different hash codes.
     */
    @Test
    public void twoDifferentDatesMissingWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1.hashCode() == new DatesMissingWarning(2).hashCode());
    }
}
