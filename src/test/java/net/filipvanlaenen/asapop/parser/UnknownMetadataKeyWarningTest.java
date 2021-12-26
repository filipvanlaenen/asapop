package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>UnknownMetadataKeyWarning</code> class.
 */
public class UnknownMetadataKeyWarningTest {
    /**
     * An unknown metadata key warning for a on line 1.
     */
    private static final UnknownMetadataKeyWarning WARNING_1_A = new UnknownMetadataKeyWarning(1, "a");

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Unknown metadata key (“a”) detected in line 1.", WARNING_1_A.toString());
    }

    /**
     * Verifies that an unknown metadata key warning is not equal to null.
     */
    @Test
    public void anUnknownMetadataKeyWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1_A.equals(null));
    }

    /**
     * Verifies that an unknown metadata key warning is not equal to an object of another class, like a string.
     */
    @Test
    public void anUnknownMetadataKeyWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1_A.equals(""));
    }

    /**
     * Verifies that an unknown metadata key warning is equal to itself.
     */
    @Test
    public void anUnknownMetadataKeyWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1_A.equals(WARNING_1_A));
    }

    /**
     * Verifies that calling hashCode twice on an unknown metadata key warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnUnknownMetadataKeyWarningReturnsTheSameResult() {
        assertEquals(WARNING_1_A.hashCode(), WARNING_1_A.hashCode());
    }

    /**
     * Verifies that two unknown metadata key warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoUnknownMetadataKeyWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1_A, new UnknownMetadataKeyWarning(1, "a"));
    }

    /**
     * Verifies that two unknown metadata key warnings constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoUnknownMetadataKeyWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1_A.hashCode(), new UnknownMetadataKeyWarning(1, "a").hashCode());
    }

    /**
     * Verifies that two different unknown metadata key warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentUnknownMetadataKeyWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new UnknownMetadataKeyWarning(2, "a")));
    }

    /**
     * Verifies that two different unknown metadata key warnings with different line numbers have different hash
     * codes.
     */
    @Test
    public void twoDifferentUnknownMetadataKeyWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new UnknownMetadataKeyWarning(2, "a").hashCode());
    }

    /**
     * Verifies that two different unknown metadata key warnings with different metadata keys are not equal.
     */
    @Test
    public void twoDifferentUnknownMetadataKeyWarningsWithDifferentMetadataKeysShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new UnknownMetadataKeyWarning(1, "b")));
    }

    /**
     * Verifies that two different unknown metadata key warnings with different metadata keys have different hash
     * codes.
     */
    @Test
    public void twoDifferentUnknownMetadataKeyWarningsWithDifferentMetadataKeysShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new UnknownMetadataKeyWarning(1, "b").hashCode());
    }

}
