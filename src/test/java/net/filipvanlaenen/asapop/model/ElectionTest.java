package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Election</code> class.
 */
public class ElectionTest {
    /**
     * An election date for an annulled round in the unit tests.
     */
    private static final ElectionDate ANNULLED_ELECTION_DATE = ElectionDate.parse("2023-04-03");
    /**
     * An election date for the unit tests.
     */
    private static final ElectionDate ELECTION_DATE = ElectionDate.parse("2023-04-10");
    /**
     * An election to run the unit tests on.
     */
    private static final Election ELECTION =
            new Election("aa", ElectionType.NATIONAL, List.of(ELECTION_DATE), List.of(List.of(ANNULLED_ELECTION_DATE)));
    /**
     * A date before the election date.
     */
    private static final LocalDate NOW_BEFORE = LocalDate.of(2022, Month.DECEMBER, 7);
    /**
     * A date after the election date.
     */
    private static final LocalDate NOW_AFTER = LocalDate.of(2024, Month.DECEMBER, 7);

    /**
     * Verifies that the getter method <code>areaCode</code> is wired correctly to the constructor.
     */
    @Test
    public void areaCodeShouldBeWiredCorrectlyToTheConstructor() {
        assertEquals("aa", ELECTION.areaCode());
    }

    /**
     * Verifies that the getter method <code>electionType</code> is wired correctly to the constructor.
     */
    @Test
    public void electionTypeShouldBeWiredCorrectlyToTheConstructor() {
        assertEquals(ElectionType.NATIONAL, ELECTION.electionType());
    }

    /**
     * Verifies that the getter method <code>dates</code> is wired correctly to the constructor.
     */
    @Test
    public void datesShouldBeWiredCorrectlyToTheConstructor() {
        assertEquals(List.of(ELECTION_DATE), ELECTION.dates());
    }

    /**
     * Verifies that the getter method <code>datesAnnulled</code> is wired correctly to the constructor.
     */
    @Test
    public void datesAnnulledShouldBeWiredCorrectlyToTheConstructor() {
        assertEquals(List.of(List.of(ANNULLED_ELECTION_DATE)), new Election("aa", ElectionType.NATIONAL,
                List.of(ELECTION_DATE), List.of(List.of(ANNULLED_ELECTION_DATE))).datesAnnulled());
    }

    /**
     * Verifies that the election date is returned when it is in the future.
     */
    @Test
    public void getNextElectionDateShouldReturnDateIfItIsInTheFuture() {
        assertEquals(ELECTION_DATE, ELECTION.getNextElectionDate(NOW_BEFORE));
    }

    /**
     * Verifies that the first election date is returned when they are is in the future.
     */
    @Test
    public void getNextElectionDateShouldReturnFirstDateInTheFuture() {
        assertEquals(ELECTION_DATE,
                new Election("aa", ElectionType.NATIONAL, List.of(ELECTION_DATE, ElectionDate.parse("2023-04-11")),
                        List.of(List.of(ANNULLED_ELECTION_DATE))).getNextElectionDate(NOW_BEFORE));
    }

    /**
     * Verifies that <code>null</code> is returned when the election date is in the past.
     */
    @Test
    public void getNextElectionDateShouldReturnNullWhenItIsInThePast() {
        assertNull(ELECTION.getNextElectionDate(NOW_AFTER));
    }
}
