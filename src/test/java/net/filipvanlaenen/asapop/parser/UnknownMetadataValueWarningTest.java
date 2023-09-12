package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>UnknownMetadataValueWarning</code> class.
 */
public class UnknownMetadataValueWarningTest {
    /**
     * An unknown scope value warning for a on line 1.
     */
    private static final UnknownMetadataValueWarning WARNING_1_A = new UnknownMetadataValueWarning(1, "scope", "a");

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Unknown scope (“a”) detected in line 1.", WARNING_1_A.toString());
    }

    /**
     * Verifies that an unknown metadata value warning is not equal to null.
     */
    @Test
    public void anUnknownMetadataValueWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1_A.equals(null));
    }

    /**
     * Verifies that an unknown metadata value warning is not equal to an object of another class, like a string.
     */
    @Test
    public void anUnknownMetadataValueWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1_A.equals(""));
    }

    /**
     * Verifies that an unknown metadata value warning is equal to itself.
     */
    @Test
    public void anUnknownMetadataValueWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1_A.equals(WARNING_1_A));
    }

    /**
     * Verifies that calling hashCode twice on an unknown metadata value warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnUnknownMetadataValueWarningReturnsTheSameResult() {
        assertEquals(WARNING_1_A.hashCode(), WARNING_1_A.hashCode());
    }

    /**
     * Verifies that two unknown metadata value warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoUnknownMetadataValueWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1_A, new UnknownMetadataValueWarning(1, "scope", "a"));
    }

    /**
     * Verifies that two unknown metadata value warnings constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoUnknownMetadataValueWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1_A.hashCode(), new UnknownMetadataValueWarning(1, "scope", "a").hashCode());
    }

    /**
     * Verifies that two different unknown metadata value warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentUnknownMetadataValueWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new UnknownMetadataValueWarning(2, "scope", "a")));
    }

    /**
     * Verifies that two different unknown metadata value warnings with different line numbers have different hash
     * codes.
     */
    @Test
    public void twoDifferentUnknownMetadataValueWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new UnknownMetadataValueWarning(2, "scope", "a").hashCode());
    }

    /**
     * Verifies that two different unknown metadata value warnings with different metadata field names are not equal.
     */
    @Test
    public void twoDifferentUnknownMetadataValueWarningsWithDifferentMetadataFieldNamesShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new UnknownMetadataValueWarning(1, "unit", "a")));
    }

    /**
     * Verifies that two different unknown metadata value warnings with different metadata field names have different
     * hash codes.
     */
    @Test
    public void twoDifferentUnknownMetadataValueWarningsWithDifferentMetadataFieldNamesShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new UnknownMetadataValueWarning(1, "unit", "a").hashCode());
    }

    /**
     * Verifies that two different unknown metadata value warnings with different metadata values are not equal.
     */
    @Test
    public void twoDifferentUnknownMetadataValueWarningsWithDifferentMetadataValuesShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new UnknownMetadataValueWarning(1, "scope", "b")));
    }

    /**
     * Verifies that two different unknown metadata value warnings with different metadata values have different hash
     * codes.
     */
    @Test
    public void twoDifferentUnknownMetadataValueWarningsWithDifferentMetadataValuesShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1_A.hashCode() == new UnknownMetadataValueWarning(1, "scope", "b").hashCode());
    }
}
