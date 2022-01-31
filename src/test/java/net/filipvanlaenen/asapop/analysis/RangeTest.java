package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Range</code> class.
 */
public class RangeTest {
    /**
     * A range to run tests on.
     */
    private static final Range RANGE = new Range(0, 1);

    /**
     * Verifies that a range is not equal to null.
     */
    @Test
    public void aRangeShouldNotBeEqualToNull() {
        assertFalse(RANGE.equals(null));
    }

    /**
     * Verifies that a range is not equal to an object of another class, like a string.
     */
    @Test
    public void aRangeShouldNotBeEqualToAString() {
        assertFalse(RANGE.equals(""));
    }

    /**
     * Verifies that a range is equal to itself.
     */
    @Test
    public void aRangeShouldBeEqualToItself() {
        assertTrue(RANGE.equals(RANGE));
    }

    /**
     * Verifies that calling hashCode twice on a range returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnARangeReturnsTheSameResult() {
        assertEquals(RANGE.hashCode(), RANGE.hashCode());
    }

    /**
     * Verifies that two ranges constructed with the same parameter are equal.
     */
    @Test
    public void twoRangesConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(RANGE, new Range(0, 1));
    }

    /**
     * Verifies that two ranges constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoRangesConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(RANGE.hashCode(), new Range(0, 1).hashCode());
    }

    /**
     * Verifies that two ranges with different lower bounds are not equal.
     */
    @Test
    public void twoRangesWithDifferentLowerBoundsShouldNotBeEqual() {
        assertFalse(RANGE.equals(new Range(1, 1)));
    }

    /**
     * Verifies that two ranges with different lower bounds have different hash codes.
     */
    @Test
    public void twoRangesWithDifferentLowerBoundsShouldHaveDifferentHashCodes() {
        assertFalse(RANGE.hashCode() == new Range(1, 1).hashCode());
    }

    /**
     * Verifies that two ranges with different upper bounds are not equal.
     */
    @Test
    public void twoRangesWithDifferentUpperBoundsShouldNotBeEqual() {
        assertFalse(RANGE.equals(new Range(0, 2)));
    }

    /**
     * Verifies that two ranges with different upper bounds have different hash codes.
     */
    @Test
    public void twoRangesWithDifferentUpperBoundsShouldHaveDifferentHashCodes() {
        assertFalse(RANGE.hashCode() == new Range(0, 2).hashCode());
    }
}
