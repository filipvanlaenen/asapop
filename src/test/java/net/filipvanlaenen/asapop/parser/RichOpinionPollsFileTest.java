package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.ResponseScenario;

/**
 * Unit tests on the <code>RichOpinionPollsFile</code> class.
 */
public final class RichOpinionPollsFileTest {
    /**
     * Sample poll line.
     */
    private static final String SAMPLE_POLL_LINE = "•PF: ACME •PD: 2021-07-27 A:55 B:45";
    /**
     * Sample poll corresponding to the sample poll line.
     */
    private static final OpinionPoll SAMPLE_POLL = new OpinionPoll.Builder().setPollingFirm("ACME")
                                                                            .setPublicationDate("2021-07-27")
                                                                            .addResult("A", "55").addResult("B", "45")
                                                                            .build();
    /**
     * Other sample poll line.
     */
    private static final String OTHER_SAMPLE_POLL_LINE = "•PF: BCME •PD: 2021-07-28 A:56 C:43";
    /**
     * Sample poll corresponding to the other sample poll line.
     */
    private static final OpinionPoll OTHER_SAMPLE_POLL = new OpinionPoll.Builder().setPollingFirm("BCME")
                                                                                  .setPublicationDate("2021-07-28")
                                                                                  .addResult("A", "56")
                                                                                  .addResult("C", "43")
                                                                                  .build();

    /**
     * Verifies that String with a single line containing a simple opinion poll can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithASimpleOpinionPoll() {
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        polls.add(SAMPLE_POLL);
        assertEquals(polls, RichOpinionPollsFile.parse(SAMPLE_POLL_LINE).getOpinionPolls().getOpinionPolls());
    }

    /**
     * Verifies that two lines containing simple opinion polls can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithSimpleOpinionPolls() {
        String[] content = new String[]{SAMPLE_POLL_LINE, OTHER_SAMPLE_POLL_LINE};
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
        String[] content = new String[]{SAMPLE_POLL_LINE, "", OTHER_SAMPLE_POLL_LINE};
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
        String[] content = new String[]{SAMPLE_POLL_LINE, "& A:50 B:40 C:10"};
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "45").build();
        ResponseScenario scenario = new ResponseScenario.Builder().addResult("A", "50").addResult("B", "40")
                                                                  .addResult("C", "10").build();
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
        RichOpinionPollsFile.parse("A: •A: AP •EN: Apple Party");
        assertEquals("AP", ElectoralList.get("A").getAbbreviation());
    }
}
