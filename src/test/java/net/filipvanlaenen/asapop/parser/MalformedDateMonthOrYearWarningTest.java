package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>MalformedDateMonthOrYearWarning</code> class.
 */
public final class MalformedDateMonthOrYearWarningTest {
    /**
     * A malformed date, month or year warning for a on line 1.
     */
    private static final MalformedDateMonthOrYearWarning WARNING_1_A_B =
            new MalformedDateMonthOrYearWarning(1, "a", "b");

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Malformed date, month or year (“b”) detected for metadata field “a” in line 1.",
                WARNING_1_A_B.toString());
    }

    /**
     * Verifies that a malformed date, month or year warning is not equal to null.
     */
    @Test
    public void aMalformedDateMonthOrYearWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1_A_B.equals(null));
    }

    /**
     * Verifies that a malformed date, month or year warning is not equal to an object of another class, like a string.
     */
    @Test
    public void aMalformedDateMonthOrYearWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1_A_B.equals(""));
    }

    /**
     * Verifies that a malformed date, month or year warning is equal to itself.
     */
    @Test
    public void aMalformedDateMonthOrYearWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1_A_B.equals(WARNING_1_A_B));
    }

    /**
     * Verifies that calling hashCode twice on a malformed date, month or year warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnMalformedDateMonthOrYearWarningReturnsTheSameResult() {
        assertEquals(WARNING_1_A_B.hashCode(), WARNING_1_A_B.hashCode());
    }

    /**
     * Verifies that two malformed date, month or year warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoMalformedDateMonthOrYearWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1_A_B, new MalformedDateMonthOrYearWarning(1, "a", "b"));
    }

    /**
     * Verifies that two malformed date, month or year warnings constructed with the same parameters return the same
     * hashCode.
     */
    @Test
    public void twoMalformedDateMonthOrYearWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1_A_B.hashCode(), new MalformedDateMonthOrYearWarning(1, "a", "b").hashCode());
    }

    /**
     * Verifies that two different malformed date, month or year warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentMalformedDateMonthOrYearWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDateMonthOrYearWarning(2, "a", "b")));
    }

    /**
     * Verifies that two different malformed date, month or year warnings with different line numbers have different
     * hash codes.
     */
    @Test
    public void twoDifferentMalformedDateMonthOrYearWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDateMonthOrYearWarning(2, "a", "b").hashCode());
    }

    /**
     * Verifies that two different malformed date, month or year warnings with different keys are not equal.
     */
    @Test
    public void twoDifferentMalformedDateMonthOrYearWarningsWithDifferentKeysShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDateMonthOrYearWarning(1, "x", "b")));
    }

    /**
     * Verifies that two different malformed date, month or year warnings with different keys have different hash codes.
     */
    @Test
    public void twoDifferentMalformedDateMonthOrYearWarningsWithDifferentKeysShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDateMonthOrYearWarning(1, "x", "b").hashCode());
    }

    /**
     * Verifies that two different malformed date, month or year warnings with different values are not equal.
     */
    @Test
    public void twoDifferentMalformedDateMonthOrYearWarningsWithDifferentValuesShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDateMonthOrYearWarning(1, "a", "x")));
    }

    /**
     * Verifies that two different malformed date, month or year warnings with different values have different hash
     * codes.
     */
    @Test
    public void twoDifferentMalformedDateMonthOrYearWarningsWithDifferentValuesShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDateMonthOrYearWarning(1, "a", "x").hashCode());
    }
}
