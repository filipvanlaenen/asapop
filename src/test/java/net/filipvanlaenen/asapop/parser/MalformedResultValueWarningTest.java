package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>MalformedResultValueWarning</code> class.
 */
public final class MalformedResultValueWarningTest {
    /**
     * A malformed result value warning for a on line 1.
     */
    private static final MalformedResultValueWarning WARNING_1_A = new MalformedResultValueWarning(1, "a");

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Malformed result value (“a”) detected in line 1.", WARNING_1_A.toString());
    }

    /**
     * Verifies that a malformed result value warning is not equal to null.
     */
    @Test
    public void aMalformedResultValueWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1_A.equals(null));
    }

    /**
     * Verifies that a malformed result value warning is not equal to an object of another class, like a string.
     */
    @Test
    public void aMalformedResultValueWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1_A.equals(""));
    }

    /**
     * Verifies that a malformed result value warning is equal to itself.
     */
    @Test
    public void aMalformedResultValueWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1_A.equals(WARNING_1_A));
    }

    /**
     * Verifies that calling hashCode twice on a malformed result value warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnMalformedResultValueWarningReturnsTheSameResult() {
        assertEquals(WARNING_1_A.hashCode(), WARNING_1_A.hashCode());
    }

    /**
     * Verifies that two malformed result value warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoMalformedResultValueWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1_A, new MalformedResultValueWarning(1, "a"));
    }

    /**
     * Verifies that two malformed result value warnings constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoMalformedResultValueWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1_A.hashCode(), new MalformedResultValueWarning(1, "a").hashCode());
    }

    /**
     * Verifies that two different malformed result value warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentMalformedResultValueWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new MalformedResultValueWarning(2, "a")));
    }

    /**
     * Verifies that two different malformed result value warnings with different line numbers have different hash
     * codes.
     */
    @Test
    public void twoDifferentMalformedResultValueWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new MalformedResultValueWarning(2, "a").hashCode());
    }

    /**
     * Verifies that two different malformed result value warnings with different result values are not equal.
     */
    @Test
    public void twoDifferentMalformedResultValueWarningsWithDifferentResultValuesShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new MalformedResultValueWarning(1, "b")));
    }

    /**
     * Verifies that two different malformed result value warnings with different result values have different hash
     * codes.
     */
    @Test
    public void twoDifferentMalformedResultValueWarningsWithDifferentResultValuesShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new MalformedResultValueWarning(1, "b").hashCode());
    }
}
