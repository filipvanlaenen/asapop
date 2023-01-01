package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning</code> class.
 */
public final class ResultValuesNotAddingUpWithinRoundingErrorIntervalWarningTest {
    /**
     * A result values not adding up within rounding error interval warning for a on line 1.
     */
    private static final ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning WARNING_1 =
            new ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning(1);

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Results in line 1 don’t add up within rounding error interval.", WARNING_1.toString());
    }

    /**
     * Verifies that a result values not adding up within rounding error interval warning is not equal to null.
     */
    @Test
    public void aWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1.equals(null));
    }

    /**
     * Verifies that a result values not adding up within rounding error interval warning is not equal to an object of
     * another class, like a string.
     */
    @Test
    public void aWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1.equals(""));
    }

    /**
     * Verifies that a result values not adding up within rounding error interval warning is equal to itself.
     */
    @Test
    public void aWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1.equals(WARNING_1));
    }

    /**
     * Verifies that calling hashCode twice on a result values not adding up within rounding error interval warning
     * returns the same result.
     */
    @Test
    public void callingHashCodeTwiceShouldReturnTheSameResult() {
        assertEquals(WARNING_1.hashCode(), WARNING_1.hashCode());
    }

    /**
     * Verifies that two result values not adding up within rounding error interval warnings constructed with the same
     * parameters are equal.
     */
    @Test
    public void twoWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1, new ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning(1));
    }

    /**
     * Verifies that two result values not adding up within rounding error interval warnings constructed with the same
     * parameters return the same hashCode.
     */
    @Test
    public void twoWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1.hashCode(), new ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning(1).hashCode());
    }

    /**
     * Verifies that two different result values not adding up within rounding error interval warnings with different
     * line numbers are not equal.
     */
    @Test
    public void twoDifferentWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1.equals(new ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning(2)));
    }

    /**
     * Verifies that two different result values not adding up within rounding error interval warnings with different
     * line numbers have different hash codes.
     */
    @Test
    public void twoDifferentWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(
                WARNING_1.hashCode() == new ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning(2).hashCode());
    }
}
