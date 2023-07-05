package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>HighestAveragesAllocation</code> class.
 */
public class HighestAveragesAllocationTest {
    /**
     * The magic number three.
     */
    private static final long THREE = 3L;

    /**
     * Verifies that the first seat is allocated to the largest number of votes.
     */
    @Test
    public void shouldAllocateTheFirstSeatToTheLargestNumberOfVotes() {
        assertEquals(List.of(1, 0), HighestAveragesAllocation.allocate(1, List.of(1L, 0L)));
    }

    /**
     * Verifies that the second seat is also allocated to the largest number of votes if it's more than double of the
     * next number of votes.
     */
    @Test
    public void shouldAllocateTheSecondSeatToTheLargestNumberOfVotesIfMoreThanDoubleOfTheNext() {
        assertEquals(List.of(2, 0), HighestAveragesAllocation.allocate(2, List.of(THREE, 1L)));
    }

    /**
     * Verifies that the second seat is also allocated to the second largest number of votes if it's more than half of
     * the largest number of votes.
     */
    @Test
    public void shouldAllocateTheSecondSeatToTheSecondLargestNumberOfVotesIfMoreThanHalfOfTheLargest() {
        assertEquals(List.of(1, 1), HighestAveragesAllocation.allocate(2, List.of(THREE, 2L)));
    }
}
