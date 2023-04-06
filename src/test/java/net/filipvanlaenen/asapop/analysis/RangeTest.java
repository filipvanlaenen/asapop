package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
     * Verifiers that the cache returns the same object when the same parameters are provided.
     */
    @Test
    public void cacheShouldReturnTheSameObjectForTheSameParameters() {
        assertSame(RANGE_0_1, Range.get(0, 1));
    }
}
