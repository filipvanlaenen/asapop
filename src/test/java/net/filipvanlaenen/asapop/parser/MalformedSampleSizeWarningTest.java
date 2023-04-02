package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>MalformedSampleSizeWarning</code> class.
 */
public class MalformedSampleSizeWarningTest {
    /**
     * A malformed sample size warning for a on line 1.
     */
    private static final MalformedSampleSizeWarning WARNING_1_A = new MalformedSampleSizeWarning(1, "a");

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Malformed sample size (“a”) detected in line 1.", WARNING_1_A.toString());
    }

    /**
     * Verifies that a malformed sample size warning is not equal to null.
     */
    @Test
    public void aMalformedSampleSizeWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1_A.equals(null));
    }

    /**
     * Verifies that a malformed sample size warning is not equal to an object of another class, like a string.
     */
    @Test
    public void aMalformedSampleSizeWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1_A.equals(""));
    }

    /**
     * Verifies that a malformed sample size warning is equal to itself.
     */
    @Test
    public void aMalformedSampleSizeWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1_A.equals(WARNING_1_A));
    }

    /**
     * Verifies that calling hashCode twice on a malformed sample size warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnMalformedSampleSizeWarningReturnsTheSameResult() {
        assertEquals(WARNING_1_A.hashCode(), WARNING_1_A.hashCode());
    }

    /**
     * Verifies that two malformed sample size warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoMalformedSampleSizeWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1_A, new MalformedSampleSizeWarning(1, "a"));
    }

    /**
     * Verifies that two malformed sample size warnings constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoMalformedSampleSizeWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1_A.hashCode(), new MalformedSampleSizeWarning(1, "a").hashCode());
    }

    /**
     * Verifies that two different malformed sample size warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentMalformedSampleSizeWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new MalformedSampleSizeWarning(2, "a")));
    }

    /**
     * Verifies that two different malformed sample size warnings with different line numbers have different hash codes.
     */
    @Test
    public void twoDifferentMalformedSampleSizeWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new MalformedSampleSizeWarning(2, "a").hashCode());
    }

    /**
     * Verifies that two different malformed sample size warnings with different result values are not equal.
     */
    @Test
    public void twoDifferentMalformedSampleSizeWarningsWithDifferentResultValuesShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new MalformedSampleSizeWarning(1, "b")));
    }

    /**
     * Verifies that two different malformed sample size warnings with different result values have different hash
     * codes.
     */
    @Test
    public void twoDifferentMalformedSampleSizeWarningsWithDifferentResultValuesShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new MalformedSampleSizeWarning(1, "b").hashCode());
    }
}
