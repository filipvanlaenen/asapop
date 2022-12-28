package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>MalformedDateOrMonthWarning</code> class.
 */
public final class MalformedDateOrMonthWarningTest {
    /**
     * A malformed date or month warning for a on line 1.
     */
    private static final MalformedDateOrMonthWarning WARNING_1_A_B = new MalformedDateOrMonthWarning(1, "a", "b");

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Malformed date or month (“b”) detected for metadata field “a” in line 1.",
                WARNING_1_A_B.toString());
    }

    /**
     * Verifies that a malformed date or month warning is not equal to null.
     */
    @Test
    public void aMalformedDateOrMonthWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1_A_B.equals(null));
    }

    /**
     * Verifies that a malformed date or month warning is not equal to an object of another class, like a string.
     */
    @Test
    public void aMalformedDateOrMonthWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1_A_B.equals(""));
    }

    /**
     * Verifies that a malformed date or month warning is equal to itself.
     */
    @Test
    public void aMalformedDateOrMonthWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1_A_B.equals(WARNING_1_A_B));
    }

    /**
     * Verifies that calling hashCode twice on a malformed date or month warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnMalformedDateOrMonthWarningReturnsTheSameResult() {
        assertEquals(WARNING_1_A_B.hashCode(), WARNING_1_A_B.hashCode());
    }

    /**
     * Verifies that two malformed date or month warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoMalformedDateOrMonthWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1_A_B, new MalformedDateOrMonthWarning(1, "a", "b"));
    }

    /**
     * Verifies that two malformed date or month warnings constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoMalformedDateOrMonthWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1_A_B.hashCode(), new MalformedDateOrMonthWarning(1, "a", "b").hashCode());
    }

    /**
     * Verifies that two different malformed date or month warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentMalformedDateOrMonthWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDateOrMonthWarning(2, "a", "b")));
    }

    /**
     * Verifies that two different malformed date or month warnings with different line numbers have different hash
     * codes.
     */
    @Test
    public void twoDifferentMalformedDateOrMonthWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDateOrMonthWarning(2, "a", "b").hashCode());
    }

    /**
     * Verifies that two different malformed date or month warnings with different keys are not equal.
     */
    @Test
    public void twoDifferentMalformedDateOrMonthWarningsWithDifferentKeysShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDateOrMonthWarning(1, "x", "b")));
    }

    /**
     * Verifies that two different malformed date or month warnings with different keys have different hash codes.
     */
    @Test
    public void twoDifferentMalformedDateOrMonthWarningsWithDifferentKeysShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDateOrMonthWarning(1, "x", "b").hashCode());
    }

    /**
     * Verifies that two different malformed date or month warnings with different values are not equal.
     */
    @Test
    public void twoDifferentMalformedDateOrMonthWarningsWithDifferentValuesShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDateOrMonthWarning(1, "a", "x")));
    }

    /**
     * Verifies that two different malformed date or month warnings with different values have different hash codes.
     */
    @Test
    public void twoDifferentMalformedDateOrMonthWarningsWithDifferentValuesShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDateOrMonthWarning(1, "a", "x").hashCode());
    }
}
