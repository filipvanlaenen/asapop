package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;

/**
 * Unit tests on the <code>HighestAveragesAllocation</code> class.
 */
public class HighestAveragesAllocationTest {
    /**
     * The magic long number three.
     */
    private static final long THREE = 3L;
    /**
     * The magic integer number three.
     */
    private static final int INTEGER_THREE = 3;
    /**
     * The magic number ten.
     */
    private static final int TEN = 10;

    /**
     * Verifies that the first seat is allocated to the largest number of votes.
     */
    @Test
    public void shouldAllocateTheFirstSeatToTheLargestNumberOfVotes() {
        HighestAveragesAllocation allocation = new HighestAveragesAllocation(1, 0D, Collection.of(2L, 1L));
        assertTrue(allocation.getNumberOfSeats(1L).containsSame(Collection.of(0)));
        assertTrue(allocation.getNumberOfSeats(2L).containsSame(Collection.of(1)));
    }

    /**
     * Verifies that if there are two equal largest numbers of votes, both get the two first seats.
     */
    @Test
    public void shouldAllocateTheFirstTwoSeatsToTheTwoEqualLargestNumberOfVotes() {
        HighestAveragesAllocation allocation = new HighestAveragesAllocation(2, 0D, Collection.of(2L, 2L, 1L));
        assertTrue(allocation.getNumberOfSeats(1L).containsSame(Collection.of(0)));
        assertTrue(allocation.getNumberOfSeats(2L).containsSame(Collection.of(1, 1)));
    }

    /**
     * Verifies that if there are two equal largest numbers of votes, one of them gets the first seat.
     */
    @Test
    public void shouldAllocateTheFirstSeatToOneOfTheTwoEqualLargestNumberOfVotes() {
        HighestAveragesAllocation allocation = new HighestAveragesAllocation(1, 0D, Collection.of(2L, 2L));
        assertTrue(allocation.getNumberOfSeats(2L).containsSame(Collection.of(0, 1)));
    }

    /**
     * Verifies that the second seat is also allocated to the largest number of votes if it's more than double of the
     * next number of votes.
     */
    @Test
    public void shouldAllocateTheSecondSeatToTheLargestNumberOfVotesIfMoreThanDoubleOfTheNext() {
        HighestAveragesAllocation allocation = new HighestAveragesAllocation(2, 0D, Collection.of(THREE, 1L));
        assertTrue(allocation.getNumberOfSeats(1L).containsSame(Collection.of(0)));
        assertTrue(allocation.getNumberOfSeats(THREE).containsSame(Collection.of(2)));
    }

    /**
     * Verifies that the second seat is also allocated to the second largest number of votes if it's more than half of
     * the largest number of votes.
     */
    @Test
    public void shouldAllocateTheSecondSeatToTheSecondLargestNumberOfVotesIfMoreThanHalfOfTheLargest() {
        HighestAveragesAllocation allocation = new HighestAveragesAllocation(2, 0D, Collection.of(THREE, 2L));
        assertTrue(allocation.getNumberOfSeats(2L).containsSame(Collection.of(1)));
        assertTrue(allocation.getNumberOfSeats(THREE).containsSame(Collection.of(1)));
    }

    /**
     * Verifies that in case of a toss-up, the second seat is also allocated to the smallest number of seats.
     */
    @Test
    public void shouldAllocateTheSecondSeatToTheSmallestNumberOfVotesInCaseOfATossUp() {
        HighestAveragesAllocation allocation = new HighestAveragesAllocation(2, 0D, Collection.of(1L, 2L));
        assertTrue(allocation.getNumberOfSeats(1L).containsSame(Collection.of(1)));
        assertTrue(allocation.getNumberOfSeats(2L).containsSame(Collection.of(1)));
    }

    /**
     * Verifies that getNumberOfSeatsString returns a simple number string for a unique number of votes.
     */
    @Test
    public void getNumberOfSeatsStringShouldReturnASimpleNumberStringForUniqueNumberOfVotes() {
        HighestAveragesAllocation allocation = new HighestAveragesAllocation(1, 0D, Collection.of(2L, 1L));
        assertEquals("0", allocation.getNumberOfSeatsString(1L));
    }

    /**
     * Verifies that getNumberOfSeatsString returns a simple number string for duplicate number of votes with the same
     * number of seats.
     */
    @Test
    public void getNumberOfSeatsStringShouldReturnASimpleNumberStringForDuplicateNumberOfVotesWithEqualResult() {
        HighestAveragesAllocation allocation = new HighestAveragesAllocation(2, 0D, Collection.of(2L, 2L, 1L));
        assertEquals("1", allocation.getNumberOfSeatsString(2L));
    }

    /**
     * Verifies that getNumberOfSeatsString returns a vulgar fraction for a duplicate number of votes with different
     * number of seats.
     */
    @Test
    public void getNumberOfSeatsStringShouldReturnAVulgarFractionForDuplicateNumberOfVotesWithDifferentResults() {
        HighestAveragesAllocation allocation = new HighestAveragesAllocation(1, 0D, Collection.of(2L, 2L, 1L));
        assertEquals("½", allocation.getNumberOfSeatsString(2L));
    }

    /**
     * Verifies that getNumberOfSeatsString returns an integer and vulgar fraction for a duplicate number of votes with
     * different number of seats.
     */
    @Test
    public void getNumberOfSeatsStringShouldReturnAnIntegerAndVulgarFraction() {
        HighestAveragesAllocation allocation = new HighestAveragesAllocation(INTEGER_THREE, 0D, Collection.of(2L, 2L));
        assertEquals("1½", allocation.getNumberOfSeatsString(2L));
    }

    /**
     * Verifies that getNumberOfSeatsString returns an integer and non-Unicode vulgar fraction for a duplicate number of
     * votes with different number of seats.
     */
    @Test
    public void getNumberOfSeatsStringShouldReturnAnIntegerAndNonUnicodeVulgarFraction() {
        HighestAveragesAllocation allocation =
                new HighestAveragesAllocation(TEN, 0D, Collection.of(2L, 2L, 2L, 2L, 2L, 2L, 2L));
        assertEquals("1 3/7", allocation.getNumberOfSeatsString(2L));
    }
}
