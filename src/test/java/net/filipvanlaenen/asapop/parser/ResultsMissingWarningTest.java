package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ResultsMissingWarning</code> class.
 */
public final class ResultsMissingWarningTest {
    /**
     * A results missing warning for a on line 1.
     */
    private static final ResultsMissingWarning WARNING_1 = new ResultsMissingWarning(1);

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Results missing in line 1.", WARNING_1.toString());
    }

    /**
     * Verifies that a results missing warning is not equal to null.
     */
    @Test
    public void aResultsMissingWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1.equals(null));
    }

    /**
     * Verifies that a results missing warning is not equal to an object of another class, like a string.
     */
    @Test
    public void aResultsMissingWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1.equals(""));
    }

    /**
     * Verifies that a results missing warning is equal to itself.
     */
    @Test
    public void aResultsMissingWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1.equals(WARNING_1));
    }

    /**
     * Verifies that calling hashCode twice on a results missing warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnResultsMissingWarningReturnsTheSameResult() {
        assertEquals(WARNING_1.hashCode(), WARNING_1.hashCode());
    }

    /**
     * Verifies that two results missing warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoResultsMissingWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1, new ResultsMissingWarning(1));
    }

    /**
     * Verifies that two results missing warnings constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoResultsMissingWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1.hashCode(), new ResultsMissingWarning(1).hashCode());
    }

    /**
     * Verifies that two different results missing warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentResultsMissingWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1.equals(new ResultsMissingWarning(2)));
    }

    /**
     * Verifies that two different results missing warnings with different line numbers have different hash codes.
     */
    @Test
    public void twoDifferentResultsMissingWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1.hashCode() == new ResultsMissingWarning(2).hashCode());
    }
}
