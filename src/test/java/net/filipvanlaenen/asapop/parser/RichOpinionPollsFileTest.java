package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.LaconicConfigurator;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPollTestBuilder;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResponseScenarioTestBuilder;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

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
     * Sample candidate poll line.
     */
    private static final String SAMPLE_CANDIDATE_POLL_LINE = "•PF: ACME •PD: 2021-07-27 P:55 Q:45";
    /**
     * Sample poll corresponding to the sample poll line.
     */
    private static final OpinionPoll SAMPLE_POLL = new OpinionPollTestBuilder().addResult("AA202501", "55")
            .addResult("AA202502", "45").setPollingFirm("ACME").setPublicationDate(DATE1).build();
    /**
     * Sample poll corresponding to the sample poll line.
     */
    private static final OpinionPoll SAMPLE_CANDIDATE_POLL =
            new OpinionPollTestBuilder().addCandidateResult("AA2025P", "55").addCandidateResult("AA2025Q", "45")
                    .setPollingFirm("ACME").setPublicationDate(DATE1).build();
    /**
     * Other sample poll line.
     */
    private static final String OTHER_SAMPLE_POLL_LINE = "•PF: BCME •PD: 2021-07-28 A:56 C:43";
    /**
     * Sample poll corresponding to the other sample poll line.
     */
    private static final OpinionPoll OTHER_SAMPLE_POLL = new OpinionPollTestBuilder().addResult("AA202501", "56")
            .addResult("AA202503", "43").setPollingFirm("BCME").setPublicationDate(DATE2).build();
    /**
     * Line for electoral list A.
     */
    private static final String ELECTORAL_LIST_A_LINE = "A: AA202501 •A: A";
    /**
     * Line for electoral list B.
     */
    private static final String ELECTORAL_LIST_B_LINE = "B: AA202502 •A: B";
    /**
     * Line for electoral list C.
     */
    private static final String ELECTORAL_LIST_C_LINE = "C: AA202503 •A: C";
    /**
     * Comment line.
     */
    private static final String COMMENT_LINE = "‡ Foo";
    /**
     * Line for candidate P.
     */
    private static final String CANDIDATE_P_LINE = "P: AA2025P •A: P";
    /**
     * Line for candidate Q.
     */
    private static final String CANDIDATE_Q_LINE = "Q: AA2025Q •A: Q";
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test RichOpinionPollsFileTest.");

    /**
     * Verifies that a single line containing a simple opinion poll can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithASimpleOpinionPoll() {
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        polls.add(SAMPLE_POLL);
        assertEquals(polls,
                RichOpinionPollsFile.parse(TOKEN, SAMPLE_POLL_LINE, ELECTORAL_LIST_A_LINE, ELECTORAL_LIST_B_LINE)
                        .getOpinionPollsDeprecated().getOpinionPolls());
    }

    /**
     * Verifies that a single line containing a simple opinion poll with candidates can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithASimpleCandidateOpinionPoll() {
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        polls.add(SAMPLE_CANDIDATE_POLL);
        assertEquals(polls,
                RichOpinionPollsFile.parse(TOKEN, SAMPLE_CANDIDATE_POLL_LINE, CANDIDATE_P_LINE, CANDIDATE_Q_LINE)
                        .getOpinionPollsDeprecated().getOpinionPolls());
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
        assertEquals(polls, RichOpinionPollsFile.parse(TOKEN, content).getOpinionPollsDeprecated().getOpinionPolls());
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
        assertEquals(polls, RichOpinionPollsFile.parse(TOKEN, content).getOpinionPollsDeprecated().getOpinionPolls());
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
        assertEquals(polls, RichOpinionPollsFile.parse(TOKEN, content).getOpinionPollsDeprecated().getOpinionPolls());
    }

    /**
     * Verifies that two lines containing a simple opinion with an alternative response scenario can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithOpinionPollAndAlternativeResponseScenario() {
        String[] content = new String[] {SAMPLE_POLL_LINE, "& A:50 B:40 C:10", ELECTORAL_LIST_A_LINE,
                ELECTORAL_LIST_B_LINE, ELECTORAL_LIST_C_LINE};
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("AA202501", "55").addResult("AA202502", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        ResponseScenario scenario = new ResponseScenarioTestBuilder().addResult("AA202501", "50")
                .addResult("AA202502", "40").addResult("AA202503", "10").build();
        poll.addAlternativeResponseScenario(scenario);
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        polls.add(poll);
        assertEquals(polls, RichOpinionPollsFile.parse(TOKEN, content).getOpinionPollsDeprecated().getOpinionPolls());
    }

    /**
     * Verifies that a line containing a definition of an electoral list can be parsed and the abbreviation is updated.
     */
    @Test
    public void shouldParseALineWithAnElectoralListDefinitionAndAddAbbreviation() {
        RichOpinionPollsFile.parse(TOKEN, "A: AA001 •A: AP •EN: Apple Party");
        assertEquals("AP", ElectoralList.get("AA001").getAbbreviation());
    }

    /**
     * Verifies that a single line containing a simple opinion poll doesn't produce warnings.
     */
    @Test
    public void shouldParseSingleLineWithASimpleOpinionPollWithoutErrors() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test RichOpinionPollsFileTest.shouldLogAnErrorForALineWithAMalformedResultValue.");
        RichOpinionPollsFile.parse(token, SAMPLE_POLL_LINE, ELECTORAL_LIST_A_LINE, ELECTORAL_LIST_B_LINE);
        assertTrue(outputStream.toString().isEmpty());
    }

    /**
     * Verifies that a line with a malformed result value produces a warning.
     */
    @Test
    public void shouldLogAnErrorForALineWithAMalformedResultValue() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test RichOpinionPollsFileTest.shouldLogAnErrorForALineWithAMalformedResultValue.");
        RichOpinionPollsFile.parse(token, "•PF: ACME •PD: 2021-07-27 A:x B:45", ELECTORAL_LIST_A_LINE,
                ELECTORAL_LIST_B_LINE);
        String expected = "‡   Unit test RichOpinionPollsFileTest.shouldLogAnErrorForALineWithAMalformedResultValue.\n"
                + "‡   Parsing line number 1.\n" + "‡   Line is recognized as an opinion poll line.\n"
                + "‡ ⬐ Processing result key A.\n" + "‡ Malformed result value x.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed other result value produces a warning.
     */
    @Test
    public void shouldLogAnErrorForALineWithAMalformedOtherResultValue() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage(
                "Unit test RichOpinionPollsFileTest.shouldLogAnErrorForALineWithAMalformedOtherResultValue.");
        RichOpinionPollsFile.parse(token, "•PF: ACME •PD: 2021-07-27 A:56 B:45 •O:x", ELECTORAL_LIST_A_LINE,
                ELECTORAL_LIST_B_LINE);
        String expected =
                "‡   Unit test RichOpinionPollsFileTest.shouldLogAnErrorForALineWithAMalformedOtherResultValue.\n"
                        + "‡   Parsing line number 1.\n" + "‡   Line is recognized as an opinion poll line.\n"
                        + "‡ ⬐ Processing metadata field O.\n" + "‡ Malformed result value x.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with non-permanent ID for an electoral list produces a warning.
     */
    @Test
    public void shouldLogAnErrorForALineWithANonpermanentId() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test RichOpinionPollsFileTest.shouldLogAnErrorForALineWithANonpermanentId.");
        RichOpinionPollsFile.parse(token, SAMPLE_POLL_LINE, "A: AA001 •A: A", ELECTORAL_LIST_B_LINE);
        String expected = "‡   Unit test RichOpinionPollsFileTest.shouldLogAnErrorForALineWithANonpermanentId.\n"
                + "‡   Parsing line number 2.\n" + "‡ ⬐ Line is recognized as an electoral list line.\n"
                + "‡ Electoral list ID AA001 is a non-permanent electoral list ID.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with an unrecognized line format produces a warning.
     */
    @Test
    public void shouldLogAnErrorForALineWithAnUnrecognizedFormat() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        RichOpinionPollsFile.parse(TOKEN, "Foo");
        String expected = "‡   Unit test RichOpinionPollsFileTest.\n" + "‡ ⬐ Parsing line number 1.\n"
                + "‡ Line doesn't have a recognized line format.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a single line containing a comment can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAComment() {
        List<CommentLine> commentLines = RichOpinionPollsFile.parse(TOKEN, COMMENT_LINE).getCommentLines();
        assertEquals(1, commentLines.size());
        assertEquals("Foo", commentLines.get(0).getContent());
    }
}
