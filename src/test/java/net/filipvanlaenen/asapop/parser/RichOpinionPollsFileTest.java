package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPollTestBuilder;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResponseScenarioTestBuilder;

/**
 * Unit tests on the <code>RichOpinionPollsFile</code> class.
 */
public final class RichOpinionPollsFileTest {
    /**
     * A date for the unit tests.
     */
    private static final LocalDate DATE1 = LocalDate.parse("2021-07-27");
    /**
     * Another date for the unit tests.
     */
    private static final LocalDate DATE2 = LocalDate.parse("2021-07-28");
    /**
     * Sample poll line.
     */
    private static final String SAMPLE_POLL_LINE = "•PF: ACME •PD: 2021-07-27 A:55 B:45";
    /**
     * Sample poll corresponding to the sample poll line.
     */
    private static final OpinionPoll SAMPLE_POLL = new OpinionPollTestBuilder().addResult("AA001", "55")
            .addResult("AA002", "45").setPollingFirm("ACME").setPublicationDate(DATE1).build();
    /**
     * Other sample poll line.
     */
    private static final String OTHER_SAMPLE_POLL_LINE = "•PF: BCME •PD: 2021-07-28 A:56 C:43";
    /**
     * Sample poll corresponding to the other sample poll line.
     */
    private static final OpinionPoll OTHER_SAMPLE_POLL = new OpinionPollTestBuilder().addResult("AA001", "56")
            .addResult("AA003", "43").setPollingFirm("BCME").setPublicationDate(DATE2).build();
    /**
     * Line for electoral list A.
     */
    private static final String ELECTORAL_LIST_A_LINE = "A: AA001 •A: A";
    /**
     * Line for electoral list B.
     */
    private static final String ELECTORAL_LIST_B_LINE = "B: AA002 •A: B";
    /**
     * Line for electoral list C.
     */
    private static final String ELECTORAL_LIST_C_LINE = "C: AA003 •A: C";
    /**
     * Comment line.
     */
    private static final String COMMENT_LINE = "‡ Foo";

    /**
     * Verifies that a single line containing a simple opinion poll can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithASimpleOpinionPoll() {
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        polls.add(SAMPLE_POLL);
        assertEquals(polls, RichOpinionPollsFile.parse(SAMPLE_POLL_LINE, ELECTORAL_LIST_A_LINE, ELECTORAL_LIST_B_LINE)
                .getOpinionPolls().getOpinionPolls());
    }

    /**
     * Verifies that two lines containing simple opinion polls can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithSimpleOpinionPolls() {
        String[] content = new String[] {SAMPLE_POLL_LINE, OTHER_SAMPLE_POLL_LINE, ELECTORAL_LIST_A_LINE,
                ELECTORAL_LIST_B_LINE, ELECTORAL_LIST_C_LINE};
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        polls.add(SAMPLE_POLL);
        polls.add(OTHER_SAMPLE_POLL);
        assertEquals(polls, RichOpinionPollsFile.parse(content).getOpinionPolls().getOpinionPolls());
    }

    /**
     * Verifies that two lines containing simple opinion polls with an empty line in between can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithSimpleOpinionPollsWithEmptyLineInBetween() {
        String[] content = new String[] {SAMPLE_POLL_LINE, "", OTHER_SAMPLE_POLL_LINE, ELECTORAL_LIST_A_LINE,
                ELECTORAL_LIST_B_LINE, ELECTORAL_LIST_C_LINE};
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        polls.add(SAMPLE_POLL);
        polls.add(OTHER_SAMPLE_POLL);
        assertEquals(polls, RichOpinionPollsFile.parse(content).getOpinionPolls().getOpinionPolls());
    }

    /**
     * Verifies that two lines containing simple opinion polls with a comment line in between can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithSimpleOpinionPollsWithCommentLineInBetween() {
        String[] content = new String[] {SAMPLE_POLL_LINE, "‡", OTHER_SAMPLE_POLL_LINE, ELECTORAL_LIST_A_LINE,
                ELECTORAL_LIST_B_LINE, ELECTORAL_LIST_C_LINE};
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        polls.add(SAMPLE_POLL);
        polls.add(OTHER_SAMPLE_POLL);
        assertEquals(polls, RichOpinionPollsFile.parse(content).getOpinionPolls().getOpinionPolls());
    }

    /**
     * Verifies that two lines containing a simple opinion with an alternative response scenario can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithOpinionPollAndAlternativeResponseScenario() {
        String[] content = new String[] {SAMPLE_POLL_LINE, "& A:50 B:40 C:10", ELECTORAL_LIST_A_LINE,
                ELECTORAL_LIST_B_LINE, ELECTORAL_LIST_C_LINE};
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("AA001", "55").addResult("AA002", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        ResponseScenario scenario = new ResponseScenarioTestBuilder().addResult("AA001", "50").addResult("AA002", "40")
                .addResult("AA003", "10").build();
        poll.addAlternativeResponseScenario(scenario);
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        polls.add(poll);
        assertEquals(polls, RichOpinionPollsFile.parse(content).getOpinionPolls().getOpinionPolls());
    }

    /**
     * Verifies that a line containing a definition of an electoral list can be parsed and the abbreviation is updated.
     */
    @Test
    public void shouldParseALineWithAnElectoralListDefinitionAndAddAbbreviation() {
        RichOpinionPollsFile.parse("A: AA001 •A: AP •EN: Apple Party");
        assertEquals("AP", ElectoralList.get("AA001").getAbbreviation());
    }

    /**
     * Verifies that a single line containing a simple opinion poll doesn't produce warnings.
     */
    @Test
    public void shouldParseSingleLineWithASimpleOpinionPollWithoutWarnings() {
        assertTrue(RichOpinionPollsFile.parse(SAMPLE_POLL_LINE, ELECTORAL_LIST_A_LINE, ELECTORAL_LIST_B_LINE)
                .getWarnings().isEmpty());
    }

    /**
     * Verifies that a line with a malformed result value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForALineWithAMalformedResultValue() {
        assertEquals(Set.of(new MalformedResultValueWarning(1, "x")),
                RichOpinionPollsFile
                        .parse("•PF: ACME •PD: 2021-07-27 A:x B:45", ELECTORAL_LIST_A_LINE, ELECTORAL_LIST_B_LINE)
                        .getWarnings());
    }

    /**
     * Verifies that a line with a malformed other result value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForALineWithAMalformedOtherResultValue() {
        assertEquals(Set.of(new MalformedResultValueWarning(1, "x")),
                RichOpinionPollsFile
                        .parse("•PF: ACME •PD: 2021-07-27 A:46 B:45 •O:x", ELECTORAL_LIST_A_LINE, ELECTORAL_LIST_B_LINE)
                        .getWarnings());
    }

    /**
     * Verifies that a line with an unrecognized line format produces a warning.
     */
    @Test
    public void shouldProduceAWarningForALineWithAnRecognizedFormat() {
        assertEquals(Set.of(new UnrecognizedLineFormatWarning(1)), RichOpinionPollsFile.parse("Foo").getWarnings());
    }

    /**
     * Verifies that a single line containing a comment can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAComment() {
        List<CommentLine> commentLines = RichOpinionPollsFile.parse(COMMENT_LINE).getCommentLines();
        assertEquals(1, commentLines.size());
        assertEquals("Foo", commentLines.get(0).getContent());
    }
}
