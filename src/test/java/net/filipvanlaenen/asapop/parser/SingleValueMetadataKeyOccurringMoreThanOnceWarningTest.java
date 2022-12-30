package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SingleValueMetadataKeyOccurringMoreThanOnce</code> class.
 */
public class SingleValueMetadataKeyOccurringMoreThanOnceWarningTest {
    /**
     * A warning that a single value metadata key a occurred more than once on line 1.
     */
    private static final SingleValueMetadataKeyOccurringMoreThanOnceWarning WARNING_1_A =
            new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "a");

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Single value metadata key (“a”) occurring more than once in line 1.", WARNING_1_A.toString());
    }

    /**
     * Verifies that a warning that a single value metadata key a occurred more than once is not equal to null.
     */
    @Test
    public void aWarningThatASingleValueMetadataKeyOccurredMoreThanOnceShouldNotBeEqualToNull() {
        assertFalse(WARNING_1_A.equals(null));
    }

    /**
     * Verifies that a warning that a single value metadata key a occurred more than once is not equal to an object of
     * another class, like a string.
     */
    @Test
    public void aWarningThatASingleValueMetadataKeyOccurredMoreThanOnceShouldNotBeEqualToAString() {
        assertFalse(WARNING_1_A.equals(""));
    }

    /**
     * Verifies that a warning that a single value metadata key a occurred more than once is equal to itself.
     */
    @Test
    public void aWarningThatASingleValueMetadataKeyOccurredMoreThanOnceShouldBeEqualToItself() {
        assertTrue(WARNING_1_A.equals(WARNING_1_A));
    }

    /**
     * Verifies that calling hashCode twice on a warning that a single value metadata key a occurred more than once
     * returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnWarningThatASingleValueMetadataKeyOccurredMoreThanOnceReturnsTheSameResult() {
        assertEquals(WARNING_1_A.hashCode(), WARNING_1_A.hashCode());
    }

    /**
     * Verifies that two warnings that a single value metadata key a occurred more than once constructed with the same
     * parameters are equal.
     */
    @Test
    public void warningsThatASingleValueMetadataKeyOccurredMoreThanOnceConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1_A, new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "a"));
    }

    /**
     * Verifies that two warnings that a single value metadata key a occurred more than once constructed with the same
     * parameters return the same hashCode.
     */
    @Test
    public void warningsThatSingleValueMetadataKeyOccurredMoreThanOnceFromTheSameParametersShouldHaveSameHashCode() {
        assertEquals(WARNING_1_A.hashCode(), new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "a").hashCode());
    }

    /**
     * Verifies that two different warnings that a single value metadata key a occurred more than once with different
     * line numbers are not equal.
     */
    @Test
    public void warningsThatASingleValueMetadataKeyOccurredMoreThanOnceWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(2, "a")));
    }

    /**
     * Verifies that two different warnings that a single value metadata key a occurred more than once with different
     * line numbers have different hash codes.
     */
    @Test
    public void singleValueMetadataKeyOccurredMoreThanOnceWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(
                WARNING_1_A.hashCode() == new SingleValueMetadataKeyOccurringMoreThanOnceWarning(2, "a").hashCode());
    }

    /**
     * Verifies that two different warnings that a single value metadata key a occurred more than once with different
     * metadata keys are not equal.
     */
    @Test
    public void warningsThatASingleValueMetadataKeyOccurredMoreThanOnceWithDifferentMetadataKeysShouldNotBeEqual() {
        assertFalse(WARNING_1_A.equals(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "b")));
    }

    /**
     * Verifies that two different warnings that a single value metadata key a occurred more than once with different
     * metadata keys have different hash codes.
     */
    @Test
    public void singleValueMetadataKeyOccurredMoreThanOnceWithDifferentMetadataKeysShouldHaveDifferentHashCodes() {
        assertFalse(
                WARNING_1_A.hashCode() == new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "b").hashCode());
    }
}
