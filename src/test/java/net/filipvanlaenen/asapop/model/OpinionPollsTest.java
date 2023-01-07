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
        OpinionPoll poll1 = new OpinionPoll.Builder().addCommissioner("The Times").setPublicationDate(DATE1)
                .addWellformedResult("A", "55").build();
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().addWellformedResult("A", "56").build();
        poll1.addAlternativeResponseScenario(responseScenario1);
        OpinionPoll poll2 = new OpinionPoll.Builder().addCommissioner("The Post").setPublicationDate(DATE2)
                .addWellformedResult("A", "57").addWellformedResult("B", "56").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().addWellformedResult("A", "56").build();
        poll2.addAlternativeResponseScenario(responseScenario2);
        OpinionPoll poll3 = new OpinionPoll.Builder().addCommissioner("The Times").setPublicationDate(DATE2)
                .addWellformedResult("A", "55").build();
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
     * Verifies that the number of opinion polls is counted correctly.
     */
    @Test
    public void getNumberOfOpinionPollsShouldBeCorrect() {
        assertEquals(THREE, opinionPolls.getNumberOfOpinionPolls());
    }

    /**
     * Verifies that the number of opinion polls from a date is counted correctly.
     */
    @Test
    public void getNumberOfOpinionPollsFromADateShouldBeCorrect() {
        assertEquals(2, opinionPolls.getNumberOfOpinionPolls(DATE2));
    }

    /**
     * Verifies that the number of response scenarios is counted correctly.
     */
    @Test
    public void getNumberOfResponseScenariosShouldBeCorrect() {
        assertEquals(FIVE, opinionPolls.getNumberOfResponseScenarios());
    }

    /**
     * Verifies that the number of response scenarios from a date is counted correctly.
     */
    @Test
    public void getNumberOfResponseScenariosFromADateShouldBeCorrect() {
        assertEquals(THREE, opinionPolls.getNumberOfResponseScenarios(DATE2));
    }

    /**
     * Verifies that the number of result values is counted correctly.
     */
    @Test
    public void getNumberOfResultValuesShouldBeCorrect() {
        assertEquals(SIX, opinionPolls.getNumberOfResultValues());
    }

    /**
     * Verifies that the number of result values from a date is counted correctly.
     */
    @Test
    public void getNumberOfResultValuesFromADateShouldBeCorrect() {
        assertEquals(FOUR, opinionPolls.getNumberOfResultValues(DATE2));
    }

    /**
     * Verifies that the most recent date is calculated correctly.
     */
    @Test
    public void getMostRecentDateShouldBeCorrect() {
        assertEquals(DATE2, opinionPolls.getMostRecentDate());
    }
}
