package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>UnknownScopeValueWarning</code> class.
 */
public final class UnknownScopeValueWarningTest {
    /**
     * A unknown scope value warning for a on line 1.
     */
    private static final UnknownScopeValueWarning WARNING_1_A = new UnknownScopeValueWarning(1, "a");

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Unknown scope value (“a”) detected in line 1.", WARNING_1_A.toString());
    }

    /**
     * Verifies that an unknown scope value warning is not equal to null.
     */
    @Test
    public void anUnknownScopeValueWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1_A.equals(null));
    }

    /**
     * Verifies that an unknown scope value warning is not equal to an object of another class, like a string.
     */
    @Test
    public void anUnknownScopeValueWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1_A.equals(""));
    }

    /**
     * Verifies that an unknown scope value warning is equal to itself.
     */
    @Test
    public void anUnknownScopeValueWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1_A.equals(WARNING_1_A));
    }

    /**
     * Verifies that calling hashCode twice on an unknown scope value warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnUnknownScopeValueWarningReturnsTheSameResult() {
        assertEquals(WARNING_1_A.hashCode(), WARNING_1_A.hashCode());
    }

    /**
     * Verifies that two unknown scope value warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoUnknownScopeValueWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1_A, new UnknownScopeValueWarning(1, "a"));
    }

    /**
     * Verifies that two unknown scope value warnings constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoUnknownScopeValueWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1_A.hashCode(), new UnknownScopeValueWarning(1, "a").hashCode());
    }

    /**
     * Verifies that two different unknown scope value warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentUnknownScopeValueWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new UnknownScopeValueWarning(2, "a")));
    }

    /**
     * Verifies that two different unknown scope value warnings with different line numbers have different hash codes.
     */
    @Test
    public void twoDifferentUnknownScopeValueWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new UnknownScopeValueWarning(2, "a").hashCode());
    }

    /**
     * Verifies that two different unknown scope value warnings with different scope values are not equal.
     */
    @Test
    public void twoDifferentUnknownScopeValueWarningsWithDifferentScopeValuesShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new UnknownScopeValueWarning(1, "b")));
    }

    /**
     * Verifies that two different unknown scope value warnings with different scope values have different hash codes.
     */
    @Test
    public void twoDifferentUnknownScopeValueWarningsWithDifferentScopeValuesShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new UnknownScopeValueWarning(1, "b").hashCode());
    }
}
