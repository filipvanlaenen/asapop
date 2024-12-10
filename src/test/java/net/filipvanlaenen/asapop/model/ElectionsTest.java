package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.LaconicConfigurator;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the <code>Elections</code> class.
 */
public class ElectionsTest {
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test ElectionsTest.");
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
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
        assertTrue(EMPTY_ELECTIONS.getNextElections(EARLY_NOW, TOKEN).isEmpty());
    }

    /**
     * Creates an elections instance with one election.
     *
     * @return An elections instance with one election.
     */
    private Elections createElectionsInstanceWithOneElection() {
        Elections elections = new Elections();
        elections.addElection("aa", ElectionType.NATIONAL, 1, "2023-04-15", null);
        return elections;
    }

    /**
     * Verifies that when one election with a date in the future is added, it is returned for that area and election
     * type.
     */
    @Test
    public void shouldReturnElectionWithDateInTheFutureForAreaAndType() {
        Elections elections = createElectionsInstanceWithOneElection();
        Election expected = new Election("aa", ElectionType.NATIONAL, 1, List.of(ElectionDate.parse("2023-04-15")),
                List.of(List.of()), null);
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
     * Verifies that when one election with a date in the past is added, getting the next election logs an error.
     */
    @Test
    public void getNextElectionShouldLogAnErrorWhenElectionWithDateInThePastIsAdded() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage(
                "Unit test ElectionsTest.getNextElectionShouldLogAnErrorWhenElectionWithDateInThePastIsAdded.");
        Elections elections = createElectionsInstanceWithOneElection();
        elections.getNextElection("aa", ElectionType.NATIONAL, LATE_NOW, token);
        String expected =
                "‡ ⬐ Unit test ElectionsTest.getNextElectionShouldLogAnErrorWhenElectionWithDateInThePastIsAdded.\n"
                        + "‡ No election dates set in the future.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that when one election with a date in the future is added, it is returned in the set.
     */
    @Test
    public void shouldReturnElectionWithDateInTheFuture() {
        Elections elections = createElectionsInstanceWithOneElection();
        Election expected = new Election("aa", ElectionType.NATIONAL, 1, List.of(ElectionDate.parse("2023-04-15")),
                List.of(List.of()), null);
        assertEquals(Set.of(expected), elections.getNextElections(EARLY_NOW, TOKEN));
    }

    /**
     * Verifies that when one election with a date in the past is added, getting the next elections returns an empty
     * set.
     */
    @Test
    public void shouldReturnEmptySetWhenElectionWithDateInThePastIsAdded() {
        Elections elections = createElectionsInstanceWithOneElection();
        assertTrue(elections.getNextElections(LATE_NOW, TOKEN).isEmpty());
    }

    /**
     * Verifies that when one election with a date in the past is added, getting the next elections logs an error.
     */
    @Test
    public void getNextElectionsShouldLogAnErrorWhenElectionWithDateInThePastIsAdded() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage(
                "Unit test ElectionsTest.getNextElectionsShouldLogAnErrorWhenElectionWithDateInThePastIsAdded.");
        Elections elections = createElectionsInstanceWithOneElection();
        assertTrue(elections.getNextElections(LATE_NOW, token).isEmpty());
        String expected =
                "‡   Unit test ElectionsTest.getNextElectionsShouldLogAnErrorWhenElectionWithDateInThePastIsAdded.\n"
                        + "‡   Calculating the next election dates for area aa.\n"
                        + "‡ ⬐ Calculating the next election date for election type parliament.\n"
                        + "‡ No election dates set in the future.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that when three future elections are added, the first one is returned in the set.
     */
    @Test
    public void shouldReturnFirstOfThreeElectionsInTheFutureForArea() {
        Elections elections = new Elections();
        elections.addElection("aa", ElectionType.NATIONAL, 1, "2023-04-15", null);
        elections.addElection("aa", ElectionType.NATIONAL, 2, "2024-04-15", null);
        elections.addElection("aa", ElectionType.NATIONAL, THREE, "2025-04-15", null);
        Election expected = new Election("aa", ElectionType.NATIONAL, 1, List.of(ElectionDate.parse("2023-04-15")),
                List.of(List.of()), null);
        assertEquals(expected, elections.getNextElection("aa", ElectionType.NATIONAL, EARLY_NOW));
    }

    /**
     * Verifies that when an election has been annuled but the next date is in the future, it is returned.
     */
    @Test
    public void shouldReturnElectionWithAnnuledDateInPastAndNextDateInTheFuture() {
        Elections elections = new Elections();
        elections.addElection("aa", ElectionType.NATIONAL, 1, "(2023-04-13)+2023-04-15", null);
        Election expected = new Election("aa", ElectionType.NATIONAL, 1, List.of(ElectionDate.parse("2023-04-15")),
                List.of(List.of(ElectionDate.parse("2023-04-13"))), null);
        assertEquals(expected, elections.getNextElection("aa", ElectionType.NATIONAL, EARLY_NOW));
    }

    /**
     * Verifies that an elections instance is not equal to null.
     */
    @Test
    public void electionsShouldNotBeEqualToNull() {
        assertFalse(EMPTY_ELECTIONS.equals(null));
    }

    /**
     * Verifies that an elections instance is not equal to an object of another class, like a string.
     */
    @Test
    public void electionsShouldNotBeEqualToAString() {
        assertFalse(EMPTY_ELECTIONS.equals(""));
    }

    /**
     * Verifies that an elections instance is equal to itself.
     */
    @Test
    public void electionsShouldBeEqualToItself() {
        assertTrue(EMPTY_ELECTIONS.equals(EMPTY_ELECTIONS));
    }

    /**
     * Verifies that calling hashCode twice on an elections instance returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnElectionsReturnsTheSameResult() {
        assertEquals(EMPTY_ELECTIONS.hashCode(), EMPTY_ELECTIONS.hashCode());
    }

    /**
     * Verifies that two elections instances constructed with the same parameter are equal.
     */
    @Test
    public void twoElectionsInstancesConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(EMPTY_ELECTIONS, new Elections());
    }

    /**
     * Verifies that two elections instances constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoElectionsInstancesConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(EMPTY_ELECTIONS.hashCode(), new Elections().hashCode());
    }

    /**
     * Verifies that two different elections instances with different values are not equal.
     */
    @Test
    public void twoDifferentElectionsInstancesWithDifferentValuesShouldNotBeEqual() {
        Elections elections = new Elections();
        elections.addElection("aa", ElectionType.NATIONAL, 1, "2023-04-15", null);
        assertFalse(EMPTY_ELECTIONS.equals(elections));
    }

    /**
     * Verifies that two different elections instances with different values have different hash codes.
     */
    @Test
    public void twoDifferentElectionsInstancesWithDifferentValuesShouldHaveDifferentHashCodes() {
        Elections elections = new Elections();
        elections.addElection("aa", ElectionType.NATIONAL, 1, "2023-04-15", null);
        assertFalse(EMPTY_ELECTIONS.hashCode() == elections.hashCode());
    }
}
