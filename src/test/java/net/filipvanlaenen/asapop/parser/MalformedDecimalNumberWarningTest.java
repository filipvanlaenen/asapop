package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>MalformedDecimalNumberWarning</code> class.
 */
public final class MalformedDecimalNumberWarningTest {
    /**
     * A malformed result value warning for a on line 1.
     */
    private static final MalformedDecimalNumberWarning WARNING_1_A_B = new MalformedDecimalNumberWarning(1, "a", "b");

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Malformed decimal number (“b”) detected for metadata field “a” in line 1.",
                WARNING_1_A_B.toString());
    }

    /**
     * Verifies that a malformed decimal number warning is not equal to null.
     */
    @Test
    public void aMalformedDecimalNumberWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1_A_B.equals(null));
    }

    /**
     * Verifies that a malformed decimal number warning is not equal to an object of another class, like a string.
     */
    @Test
    public void aMalformedDecimalNumberWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1_A_B.equals(""));
    }

    /**
     * Verifies that a malformed rdecimal number warning is equal to itself.
     */
    @Test
    public void aMalformedDecimalNumberWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1_A_B.equals(WARNING_1_A_B));
    }

    /**
     * Verifies that calling hashCode twice on a malformed decimal number warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnMalformedDecimalNumberWarningReturnsTheSameResult() {
        assertEquals(WARNING_1_A_B.hashCode(), WARNING_1_A_B.hashCode());
    }

    /**
     * Verifies that two malformed decimal number warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoMalformedDecimalNumberWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1_A_B, new MalformedDecimalNumberWarning(1, "a", "b"));
    }

    /**
     * Verifies that two malformed decimal number warnings constructed with the same parameters return the same
     * hashCode.
     */
    @Test
    public void twoMalformedDecimalNumberWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1_A_B.hashCode(), new MalformedDecimalNumberWarning(1, "a", "b").hashCode());
    }

    /**
     * Verifies that two different malformed decimal number warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentMalformedDecimalNumberWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDecimalNumberWarning(2, "a", "b")));
    }

    /**
     * Verifies that two different malformed decimal number warnings with different line numbers have different hash
     * codes.
     */
    @Test
    public void twoDifferentMalformedDecimalNumberWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDecimalNumberWarning(2, "a", "b").hashCode());
    }

    /**
     * Verifies that two different malformed decimal number warnings with different keys are not equal.
     */
    @Test
    public void twoDifferentMalformedDecimalNumberWarningsWithDifferentKeysShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDecimalNumberWarning(1, "x", "b")));
    }

    /**
     * Verifies that two different malformed decimal number warnings with different keys have different hash codes.
     */
    @Test
    public void twoDifferentMalformedDecimalNumberWarningsWithDifferentKeysShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDecimalNumberWarning(1, "x", "b").hashCode());
    }

    /**
     * Verifies that two different malformed decimal number warnings with different values are not equal.
     */
    @Test
    public void twoDifferentMalformedDecimalNumberWarningsWithDifferentValuesShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDecimalNumberWarning(1, "a", "x")));
    }

    /**
     * Verifies that two different malformed decimal number warnings with different values have different hash codes.
     */
    @Test
    public void twoDifferentMalformedDecimalNumberWarningsWithDifferentValuesShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDecimalNumberWarning(1, "a", "x").hashCode());
    }
}
