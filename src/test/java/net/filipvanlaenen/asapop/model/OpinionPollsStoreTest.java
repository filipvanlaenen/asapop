package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.nombrajkolektoj.NumericMap;

/**
 * Unit tests on the class <code>OpinionPollsStore</code>.
 */
public class OpinionPollsStoreTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * The magic number five.
     */
    private static final int FIVE = 5;
    /**
     * The magic number six.
     */
    private static final int SIX = 6;
    /**
     * A date to run the unit tests on.
     */
    private static final LocalDate DATE1 = LocalDate.parse("2022-11-28");
    /**
     * Another date to run the unit tests on.
     */
    private static final LocalDate DATE2 = LocalDate.parse("2022-12-29");

    /**
     * Adds data to the opinion polls store.
     */
    private void populateOpinionPollsStore() {
        OpinionPollsStore.clear();
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55").setSampleSize("1000")
                .setPollingFirm("ACME").addCommissioner("The Times").setPublicationDate(DATE1)
                .setExcluded(DecimalNumber.parse("10")).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "56").build();
        poll1.addAlternativeResponseScenario(responseScenario);
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "57").addResult("B", "56")
                .setPollingFirm("BCME").addCommissioner("The Post").setPublicationDate(DATE2).build();
        OpinionPollsStore.addAll("aa", Collection.of(poll1, poll2));
        OpinionPollsStore.addAll("bb", Collection.of(poll1));
    }

    /**
     * Verifies that the most recent date for an area is the one registered.
     */
    @Test
    public void mostRecentDateShouldBeTheOnlyOneRegisteredIfThereIsOnlyOnePoll() {
        populateOpinionPollsStore();
        assertEquals(DATE1, OpinionPollsStore.getMostRecentDate("bb"));
    }

    /**
     * Verifies that the most recent date for an area is the last one registered.
     */
    @Test
    public void mostRecentDateShouldBeTheLatest() {
        populateOpinionPollsStore();
        assertEquals(DATE2, OpinionPollsStore.getMostRecentDate("aa"));
    }

    /**
     * Verifies that the correct number of opinion polls is returned.
     */
    @Test
    public void getNumberOfOpinionPollsShouldBeCorrect() {
        populateOpinionPollsStore();
        assertEquals(THREE, OpinionPollsStore.getNumberOfOpinionPolls());
    }

    /**
     * Verifies that the correct number of opinion polls by area is returned.
     */
    @Test
    public void getNumberOfOpinionPollsByAreaShouldBeCorrect() {
        populateOpinionPollsStore();
        assertEquals(2, OpinionPollsStore.getNumberOfOpinionPolls("aa"));
    }

    /**
     * Verifies that the correct number of response scenarios is returned.
     */
    @Test
    public void getNumberOfResponseScenariosShouldBeCorrect() {
        populateOpinionPollsStore();
        assertEquals(FIVE, OpinionPollsStore.getNumberOfResponseScenarios());
    }

    /**
     * Verifies that the correct number of response scenarios by area is returned.
     */
    @Test
    public void getNumberOfResponseScenariosByAreaShouldBeCorrect() {
        populateOpinionPollsStore();
        assertEquals(THREE, OpinionPollsStore.getNumberOfResponseScenarios("aa"));
    }

    /**
     * Verifies that the correct number of result values is returned.
     */
    @Test
    public void getNumberOfResultValuesShouldBeCorrect() {
        populateOpinionPollsStore();
        assertEquals(SIX, OpinionPollsStore.getNumberOfResultValues());
    }

    /**
     * Verifies that the correct number of result values by area is returned.
     */
    @Test
    public void getNumberOfResultValuesByAreaShouldBeCorrect() {
        populateOpinionPollsStore();
        assertEquals(FOUR, OpinionPollsStore.getNumberOfResultValues("aa"));
    }
}
