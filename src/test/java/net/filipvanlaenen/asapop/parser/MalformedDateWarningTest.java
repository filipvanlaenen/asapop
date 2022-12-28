package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>MalformedDateWarning</code> class.
 */
public final class MalformedDateWarningTest {
    /**
     * A malformed date warning for a on line 1.
     */
    private static final MalformedDateWarning WARNING_1_A_B = new MalformedDateWarning(1, "a", "b");

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Malformed date (“b”) detected for metadata field “a” in line 1.", WARNING_1_A_B.toString());
    }

    /**
     * Verifies that a malformed date warning is not equal to null.
     */
    @Test
    public void aMalformedDateWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1_A_B.equals(null));
    }

    /**
     * Verifies that a malformed date warning is not equal to an object of another class, like a string.
     */
    @Test
    public void aMalformedDateWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1_A_B.equals(""));
    }

    /**
     * Verifies that a malformed date warning is equal to itself.
     */
    @Test
    public void aMalformedDateWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1_A_B.equals(WARNING_1_A_B));
    }

    /**
     * Verifies that calling hashCode twice on a malformed date warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnMalformedDateWarningReturnsTheSameResult() {
        assertEquals(WARNING_1_A_B.hashCode(), WARNING_1_A_B.hashCode());
    }

    /**
     * Verifies that two malformed date warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoMalformedDateWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1_A_B, new MalformedDateWarning(1, "a", "b"));
    }

    /**
     * Verifies that two malformed date warnings constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoMalformedDateWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1_A_B.hashCode(), new MalformedDateWarning(1, "a", "b").hashCode());
    }

    /**
     * Verifies that two different malformed date warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentMalformedDateWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDateWarning(2, "a", "b")));
    }

    /**
     * Verifies that two different malformed date warnings with different line numbers have different hash codes.
     */
    @Test
    public void twoDifferentMalformedDateWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDateWarning(2, "a", "b").hashCode());
    }

    /**
     * Verifies that two different malformed date warnings with different keys are not equal.
     */
    @Test
    public void twoDifferentMalformedDateWarningsWithDifferentKeysShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDateWarning(1, "x", "b")));
    }

    /**
     * Verifies that two different malformed date warnings with different keys have different hash codes.
     */
    @Test
    public void twoDifferentMalformedDateWarningsWithDifferentKeysShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDateWarning(1, "x", "b").hashCode());
    }

    /**
     * Verifies that two different malformed date warnings with different values are not equal.
     */
    @Test
    public void twoDifferentMalformedDateWarningsWithDifferentValuesShouldNotBeEqual() {
        assertFalse(WARNING_1_A_B.equals(new MalformedDateWarning(1, "a", "x")));
    }

    /**
     * Verifies that two different malformed date warnings with different values have different hash codes.
     */
    @Test
    public void twoDifferentMalformedDateWarningsWithDifferentValuesShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A_B.hashCode() == new MalformedDateWarning(1, "a", "x").hashCode());
    }
}
