package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>UnknownElectoralListKeyWarning</code> class.
 */
public class UnknownElectoralListKeyWarningTest {
    /**
     * An unknown electoral list key warning for a on line 1.
     */
    private static final UnknownElectoralListKeyWarning WARNING_1_A = new UnknownElectoralListKeyWarning(1, "A");

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Unknown electoral list key (“A”) detected in line 1.", WARNING_1_A.toString());
    }

    /**
     * Verifies that an unknown electoral list key warning is not equal to null.
     */
    @Test
    public void anUnknownElectoralListKeyWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1_A.equals(null));
    }

    /**
     * Verifies that an unknown electoral list key warning is not equal to an object of another class, like a string.
     */
    @Test
    public void anUnknownElectoralListKeyWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1_A.equals(""));
    }

    /**
     * Verifies that an unknown electoral list key warning is equal to itself.
     */
    @Test
    public void anUnknownElectoralListKeyWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1_A.equals(WARNING_1_A));
    }

    /**
     * Verifies that calling hashCode twice on an unknown electoral list key warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnUnknownElectoralListKeyWarningReturnsTheSameResult() {
        assertEquals(WARNING_1_A.hashCode(), WARNING_1_A.hashCode());
    }

    /**
     * Verifies that two unknown electoral list key warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoUnknownElectoralListKeyWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1_A, new UnknownElectoralListKeyWarning(1, "A"));
    }

    /**
     * Verifies that two unknown electoral list key warnings constructed with the same parameters return the same
     * hashCode.
     */
    @Test
    public void twoUnknownElectoralListKeyWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1_A.hashCode(), new UnknownElectoralListKeyWarning(1, "A").hashCode());
    }

    /**
     * Verifies that two different unknown electoral list key warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentUnknownElectoralListKeyWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new UnknownElectoralListKeyWarning(2, "A")));
    }

    /**
     * Verifies that two different unknown electoral list key warnings with different line numbers have different hash
     * codes.
     */
    @Test
    public void twoDifferentUnknownElectoralListKeyWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new UnknownElectoralListKeyWarning(2, "A").hashCode());
    }

    /**
     * Verifies that two different unknown electoral list key warnings with different metadata keys are not equal.
     */
    @Test
    public void twoDifferentUnknownElectoralListKeyWarningsWithDifferentElectoralListKeysShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new UnknownElectoralListKeyWarning(1, "B")));
    }

    /**
     * Verifies that two different unknown electoral list key warnings with different metadata keys have different hash
     * codes.
     */
    @Test
    public void twoDifferentUnknownElectoralListKeyWarningsWithDifferentKeysShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new UnknownElectoralListKeyWarning(1, "B").hashCode());
    }
}
