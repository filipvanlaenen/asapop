package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Elections</code> class.
 */
public class ElectionsTest {
    /**
     * An early date for the unit tests.
     */
    private static final LocalDate EARLY_NOW = LocalDate.of(2023, Month.APRIL, 14);
    /**
     * A late date for the unit tests.
     */
    private static final LocalDate LATE_NOW = LocalDate.of(2023, Month.APRIL, 16);
    /**
     * An empty elections instance to run some of the unit tests on.
     */
    private static final Elections EMPTY_ELECTIONS = new Elections();

    /**
     * Verifies that an empty instance returns <code>null</code> as the next election of a type for an area.
     */
    @Test
    public void anEmptyInstanceShouldReturnNullForTheNextElection() {
        assertNull(EMPTY_ELECTIONS.getNextElection("aa", ElectionType.NATIONAL, EARLY_NOW));
    }

    /**
     * Verifies that an empty instance returns an empty set as the next elections.
     */
    @Test
    public void anEmptyInstanceShouldReturnEmptySetForTheNextElections() {
        assertTrue(EMPTY_ELECTIONS.getNextElections(EARLY_NOW).isEmpty());
    }

    /**
     * Creates an elections instance with one election.
     *
     * @return An elections instance with one election.
     */
    private Elections createElectionsInstanceWithOneElection() {
        Elections elections = new Elections();
        elections.addElection("aa", ElectionType.NATIONAL, "2023-04-15");
        return elections;
    }

    /**
     * Verifies that when one election with a date in the future is added, it is returned for that area and election
     * type.
     */
    @Test
    public void shouldReturnElectionWithDateInTheFutureForAreaAndType() {
        Elections elections = createElectionsInstanceWithOneElection();
        Election expected = new Election("aa", ElectionType.NATIONAL, List.of(ElectionDate.parse("2023-04-15")),
                List.of(List.of()));
        assertEquals(expected, elections.getNextElection("aa", ElectionType.NATIONAL, EARLY_NOW));
    }

    /**
     * Verifies that when an election of one type is added, getting the next election of another type returns
     * <code>null</code>.
     */
    @Test
    public void shouldReturnNullForNextElectionOfAnotherType() {
        Elections elections = createElectionsInstanceWithOneElection();
        assertNull(elections.getNextElection("aa", ElectionType.EUROPEAN, EARLY_NOW));
    }

    /**
     * Verifies that when one election with a date in the past is added, getting the next election returns
     * <code>null</code>.
     */
    @Test
    public void shouldReturnNullWhenElectionWithDateInThePastIsAdded() {
        Elections elections = createElectionsInstanceWithOneElection();
        assertNull(elections.getNextElection("aa", ElectionType.NATIONAL, LATE_NOW));
    }

    /**
     * Verifies that when one election with a date in the future is added, it is returned in the set.
     */
    @Test
    public void shouldReturnElectionWithDateInTheFuture() {
        Elections elections = createElectionsInstanceWithOneElection();
        Election expected = new Election("aa", ElectionType.NATIONAL, List.of(ElectionDate.parse("2023-04-15")),
                List.of(List.of()));
        assertEquals(Set.of(expected), elections.getNextElections(EARLY_NOW));
    }

    /**
     * Verifies that when one election with a date in the past is added, getting the next elections returns an empty
     * set.
     */
    @Test
    public void shouldReturnEmptySetWhenElectionWithDateInThePastIsAdded() {
        Elections elections = createElectionsInstanceWithOneElection();
        assertTrue(elections.getNextElections(LATE_NOW).isEmpty());
    }

    /**
     * Verifies that when three future elections are added, the first one is returned in the set.
     */
    @Test
    public void shouldReturnFirstOfThreeElectionsInTheFutureForArea() {
        Elections elections = new Elections();
        elections.addElection("aa", ElectionType.NATIONAL, "2023-04-15");
        elections.addElection("aa", ElectionType.NATIONAL, "2024-04-15");
        elections.addElection("aa", ElectionType.NATIONAL, "2025-04-15");
        Election expected = new Election("aa", ElectionType.NATIONAL, List.of(ElectionDate.parse("2023-04-15")),
                List.of(List.of()));
        assertEquals(expected, elections.getNextElection("aa", ElectionType.NATIONAL, EARLY_NOW));
    }

    /**
     * Verifies that when an election has been annuled but the next date is in the future, it is returned.
     */
    @Test
    public void shouldReturnElectionWithAnnuledDateInPastAndNextDateInTheFuture() {
        Elections elections = new Elections();
        elections.addElection("aa", ElectionType.NATIONAL, "(2023-04-13)+2023-04-15");
        Election expected = new Election("aa", ElectionType.NATIONAL, List.of(ElectionDate.parse("2023-04-15")),
                List.of(List.of(ElectionDate.parse("2023-04-13"))));
        assertEquals(expected, elections.getNextElection("aa", ElectionType.NATIONAL, EARLY_NOW));
    }
}
