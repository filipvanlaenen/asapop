package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>UnrecognizedLineFormatWarning</code> class.
 */
public final class UnrecognizedLineFormatWarningTest {
    /**
     * An unrecognized line format warning for a on line 1.
     */
    private static final UnrecognizedLineFormatWarning WARNING_1 = new UnrecognizedLineFormatWarning(1);

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Unrecognized line format on line 1.", WARNING_1.toString());
    }

    /**
     * Verifies that an unrecognized line format warning is not equal to null.
     */
    @Test
    public void anUnrecognizedLineFormatWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1.equals(null));
    }

    /**
     * Verifies that an unrecognized line format warning is not equal to an object of another class, like a string.
     */
    @Test
    public void anUnrecognizedLineFormatWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1.equals(""));
    }

    /**
     * Verifies that an unrecognized line format warning is equal to itself.
     */
    @Test
    public void anUnrecognizedLineFormatWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1.equals(WARNING_1));
    }

    /**
     * Verifies that calling hashCode twice on an unrecognized line format warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnUnrecognizedLineFormatWarningReturnsTheSameResult() {
        assertEquals(WARNING_1.hashCode(), WARNING_1.hashCode());
    }

    /**
     * Verifies that two unrecognized line format warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoUnrecognizedLineFormatWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1, new UnrecognizedLineFormatWarning(1));
    }

    /**
     * Verifies that two unrecognized line format warnings constructed with the same parameters return the same
     * hashCode.
     */
    @Test
    public void twoUnrecognizedLineFormatWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1.hashCode(), new UnrecognizedLineFormatWarning(1).hashCode());
    }

    /**
     * Verifies that two different unrecognized line format warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentUnrecognizedLineFormatWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1.equals(new UnrecognizedLineFormatWarning(2)));
    }

    /**
     * Verifies that two different unrecognized line format warnings with different line numbers have different hash
     * codes.
     */
    @Test
    public void twoDifferentUnrecognizedLineFormatWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1.hashCode() == new UnrecognizedLineFormatWarning(2).hashCode());
    }
}
