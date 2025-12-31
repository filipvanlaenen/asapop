package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>OpinionPolls</code>.
 */
public class OpinionPollsTest {
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
     * The magic number eight hundred.
     */
    private static final int EIGHT_HUNDRED = 800;
    /**
     * The magic number eight hundred seventy.
     */
    private static final int EIGHT_HUNDRED_SEVENTY = 870;
    /**
     * The magic number nine hundred.
     */
    private static final int NINE_HUNDRED = 900;
    /**
     * The magic number thousand.
     */
    private static final int THOUSAND = 1000;
    /**
     * A date to run the unit tests on.
     */
    private static final LocalDate DATE1 = LocalDate.parse("2022-12-28");
    /**
     * Another date to run the unit tests on.
     */
    private static final LocalDate DATE2 = LocalDate.parse("2022-12-29");
    /**
     * Instance to run the unit tests on.
     */
    private static OpinionPolls opinionPolls;

    /**
     * Creates an instance of <code>OpinionPolls</code> to run some of the unit tests on.
     */
    @BeforeAll
    public static void createOpinionPollsInstance() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55").setSampleSize("1000")
                .setPollingFirm("ACME").addCommissioner("The Times").setPublicationDate(DATE1)
                .setExcluded(DecimalNumber.parse("10")).build();
        ResponseScenario responseScenario1 = new ResponseScenarioTestBuilder().addResult("A", "56").build();
        poll1.addAlternativeResponseScenario(responseScenario1);
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "57").addResult("B", "56")
                .setPollingFirm("BCME").addCommissioner("The Post").setPublicationDate(DATE2).build();
        ResponseScenario responseScenario2 = new ResponseScenarioTestBuilder().addResult("A", "56").build();
        poll2.addAlternativeResponseScenario(responseScenario2);
        OpinionPoll poll3 = new OpinionPollTestBuilder().addResult("A", "55").setSampleSize("870")
                .addCommissioner("The Times").setPublicationDate(DATE2).setExcluded(DecimalNumber.parse("8")).build();
        opinionPolls = new OpinionPolls(Set.of(poll1, poll2, poll3));
    }

    /**
     * Verifies that the getter method <code>getOpinionPolls</code> is wired correctly to the constructor.
     */
    @Test
    public void getOpinionPollsShouldBeWiredCorrectlyToTheConstructor() {
        OpinionPoll poll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        Set<OpinionPoll> expected = new HashSet<OpinionPoll>();
        expected.add(poll);
        OpinionPolls op = new OpinionPolls(expected);
        assertEquals(expected, op.getOpinionPolls());
    }

    /**
     * Verifies that <code>getOpinionPolls</code> returns an UnmodifiableSet.
     */
    @Test
    public void getOpinionPollsReturnsAnUnmodifiableSet() {
        OpinionPoll otherPoll = new OpinionPoll.Builder().addCommissioner("The Post").build();
        assertThrows(UnsupportedOperationException.class, () -> {
            opinionPolls.getOpinionPolls().add(otherPoll);
        });
    }

    /**
     * Verifies that the number of opinion polls from a date is counted correctly.
     */
    @Test
    public void getNumberOfOpinionPollsFromADateShouldBeCorrect() {
        assertEquals(2, opinionPolls.getNumberOfOpinionPolls(DATE2));
    }

    /**
     * Verifies that the most recent date is calculated correctly.
     */
    @Test
    public void getMostRecentDateShouldBeCorrect() {
        assertEquals(DATE2, opinionPolls.getMostRecentDate());
    }

    /**
     * Verifies that the lowest sample size for a polling firm is calculated correctly.
     */
    @Test
    public void lowestSampleSizeIsCalculatedCorrectlyForAPollingFirm() {
        assertEquals(THOUSAND, opinionPolls.getLowestSampleSize("ACME"));
    }

    /**
     * Verifies that the lowest effective sample size for a polling firm is calculated correctly.
     */
    @Test
    public void lowestEffectiveSampleSizeIsCalculatedCorrectlyForAPollingFirm() {
        assertEquals(NINE_HUNDRED, opinionPolls.getLowestEffectiveSampleSize("ACME"));
    }

    /**
     * Verifies that the lowest sample size for a polling firm that has no sample sizes at all is the overall lowest
     * sample size.
     */
    @Test
    public void lowestSampleSizeIsCalculatedCorrectlyForAPollingFirmWithoutSampleSize() {
        assertEquals(EIGHT_HUNDRED_SEVENTY, opinionPolls.getLowestSampleSize("BCME"));
    }

    /**
     * Verifies that the lowest effective sample size for a polling firm that has no sample sizes at all is the overall
     * lowest effective sample size.
     */
    @Test
    public void lowestEffectiveSampleSizeIsCalculatedCorrectlyForAPollingFirmWithoutSampleSize() {
        assertEquals(EIGHT_HUNDRED, opinionPolls.getLowestEffectiveSampleSize("BCME"));
    }
}
