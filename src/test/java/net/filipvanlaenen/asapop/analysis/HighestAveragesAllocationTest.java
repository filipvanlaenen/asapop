package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        assertEquals(Map.of(1L, List.of(0), 2L, List.of(1)), HighestAveragesAllocation.allocate(1, List.of(2L, 1L)));
    }

    /**
     * Verifies that if there are two equal largest numbers of votes, both get the two first seats.
     */
    @Test
    public void shouldAllocateTheFirstTwoSeatsToTheTwoEqualLargestNumberOfVotes() {
        assertEquals(Map.of(1L, List.of(0), 2L, List.of(1, 1)),
                HighestAveragesAllocation.allocate(2, List.of(2L, 2L, 1L)));
    }

    /**
     * Verifies that if there are two equal largest numbers of votes, one of them gets the first seat.
     */
    @Test
    public void shouldAllocateTheFirstSeatToOneOfTheTwoEqualLargestNumberOfVotes() {
        List<Integer> actual = HighestAveragesAllocation.allocate(1, List.of(2L, 2L)).get(2L);
        Collections.sort(actual);
        assertEquals(List.of(0, 1), actual);
    }

    /**
     * Verifies that the second seat is also allocated to the largest number of votes if it's more than double of the
     * next number of votes.
     */
    @Test
    public void shouldAllocateTheSecondSeatToTheLargestNumberOfVotesIfMoreThanDoubleOfTheNext() {
        assertEquals(Map.of(1L, List.of(0), THREE, List.of(2)),
                HighestAveragesAllocation.allocate(2, List.of(THREE, 1L)));
    }

    /**
     * Verifies that the second seat is also allocated to the second largest number of votes if it's more than half of
     * the largest number of votes.
     */
    @Test
    public void shouldAllocateTheSecondSeatToTheSecondLargestNumberOfVotesIfMoreThanHalfOfTheLargest() {
        assertEquals(Map.of(2L, List.of(1), THREE, List.of(1)),
                HighestAveragesAllocation.allocate(2, List.of(THREE, 2L)));
    }

    /**
     * Verifies that in case of a toss-up, the second seat is also allocated to smallest number of seats.
     */
    @Test
    public void shouldAllocateTheSecondSeatToTheSmallestNumberOfVotesInCaseOfATossUp() {
        assertEquals(Map.of(1L, List.of(1), 2L, List.of(1)), HighestAveragesAllocation.allocate(2, List.of(1L, 2L)));
    }
}
