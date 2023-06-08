package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class HighestAveragesAllocationTest {
    /**
     * Verifies that two seats are allocated to a list with 100 votes and none to a list with 49 votes.
     */
    @Test
    public void shouldAllocateSecondSeatToPartyWithMoreThanDoubleTheVotesOfTheOther() {
        assertEquals(List.of(2, 0), HighestAveragesAllocation.allocate(2, List.of(100L, 49L)));
    }

    /**
     * Verifies that each party receives one seat when the largest party doesn't have twice as many votes as the other
     * party.
     */
    @Test
    public void shouldAllocateOneSeatToEachPartyWhenLargestDoesNotHaveTwiceTheVotesOfTheOther() {
        assertEquals(List.of(1, 1), HighestAveragesAllocation.allocate(2, List.of(100L, 51L)));
    }
}
