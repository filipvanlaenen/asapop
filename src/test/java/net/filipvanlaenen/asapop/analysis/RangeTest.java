package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Range</code> class.
 */
public class RangeTest {
    /**
     * The magic number three.
     */
    private static final long THREE = 3L;
    /**
     * A range to run tests on, 0 to 1.
     */
    private static final Range RANGE_0_1 = Range.get(0, 1);
    /**
     * A range to run tests on, 1 to 1.
     */
    private static final Range RANGE_1_1 = Range.get(1, 1);
    /**
     * A range to run tests on, 1 to 2.
     */
    private static final Range RANGE_1_2 = Range.get(1, 2);

    /**
     * Verifies that the getter for the lower bound is wired correctly to the constructor.
     */
    @Test
    public void getLowerBoundShouldBeWiredCorrectlyToTheConstructor() {
        assertEquals(1, RANGE_1_2.getLowerBound());
    }

    /**
     * Verifies that the getter for the upper bound is wired correctly to the constructor.
     */
    @Test
    public void getUpperBoundShouldBeWiredCorrectlyToTheConstructor() {
        assertEquals(2, RANGE_1_2.getUpperBound());
    }

    /**
     * Verifies that a range with a lower lower bound is lower than the other range.
     */
    @Test
    public void aRangeWithALowerLowerBoundShouldCompareLower() {
        assertTrue(RANGE_0_1.compareTo(RANGE_1_1) < 0);
    }

    /**
     * Verifies that a range compares equal to itself.
     */
    @Test
    public void aRangeShouldCompareEqualToItself() {
        assertTrue(RANGE_0_1.compareTo(RANGE_0_1) == 0);
    }

    /**
     * Verifies the midpoint for a range with an uneven length to be in the middle.
     */
    @Test
    public void theMidpointOfARangeWithUnevenLengthShouldBeInTheMiddle() {
        assertEquals(2, Range.get(1, THREE).getMidpoint());
    }

    /**
     * Verifies the midpoint for a range with an even length to be just below the middle.
     */
    @Test
    public void theMidpointOfARangeWithEvenLengthShouldBeJustBelowTheMiddle() {
        assertEquals(1, RANGE_1_2.getMidpoint());
    }

    /**
     * Verifies the length of a range.
     */
    @Test
    public void theLengthOfARangeShouldBe2ForARangeFrom1To2() {
        assertEquals(2, RANGE_1_2.getLength());
    }

    /**
     * Verifies that a range is not equal to null.
     */
    @Test
    public void aRangeShouldNotBeEqualToNull() {
        assertFalse(RANGE_0_1.equals(null));
    }

    /**
     * Verifies that a range is not equal to an object of another class, like a string.
     */
    @Test
    public void aRangeShouldNotBeEqualToAString() {
        assertFalse(RANGE_0_1.equals(""));
    }

    /**
     * Verifies that a range is equal to itself.
     */
    @Test
    public void aRangeShouldBeEqualToItself() {
        assertTrue(RANGE_0_1.equals(RANGE_0_1));
    }

    /**
     * Verifiers that the cache returns the same object when the same parameters are provided.
     */
    @Test
    public void cacheShouldReturnTheSameObjectForTheSameParameters() {
        assertSame(RANGE_0_1, Range.get(0, 1));
    }

    /**
     * Verifies that calling hashCode twice on a range returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnARangeReturnsTheSameResult() {
        assertEquals(RANGE_0_1.hashCode(), RANGE_0_1.hashCode());
    }

    /**
     * Verifies that two ranges with different lower bounds are not equal.
     */
    @Test
    public void twoRangesWithDifferentLowerBoundsShouldNotBeEqual() {
        assertFalse(RANGE_0_1.equals(RANGE_1_1));
    }

    /**
     * Verifies that two ranges with different lower bounds have different hash codes.
     */
    @Test
    public void twoRangesWithDifferentLowerBoundsShouldHaveDifferentHashCodes() {
        assertFalse(RANGE_0_1.hashCode() == RANGE_1_1.hashCode());
    }

    /**
     * Verifies that two ranges with different upper bounds are not equal.
     */
    @Test
    public void twoRangesWithDifferentUpperBoundsShouldNotBeEqual() {
        assertFalse(RANGE_0_1.equals(Range.get(0, 2)));
    }

    /**
     * Verifies that two ranges with different upper bounds have different hash codes.
     */
    @Test
    public void twoRangesWithDifferentUpperBoundsShouldHaveDifferentHashCodes() {
        assertFalse(RANGE_0_1.hashCode() == Range.get(0, 2).hashCode());
    }
}
