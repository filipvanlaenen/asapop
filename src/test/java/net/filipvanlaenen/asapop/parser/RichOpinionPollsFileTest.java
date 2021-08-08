package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.ResponseScenario;

/**
 * Unit tests on the <code>RichOpinionPollsFile</code> class.
 */
public final class RichOpinionPollsFileTest {
    /**
     * Verifies that String with a single line containing a simple opinion poll can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithASimpleOpinionPoll() {
        String content = "•PF: ACME •PD: 2021-07-27 A:55 B:45";
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "45").build();
        polls.add(poll);
        assertEquals(polls, RichOpinionPollsFile.parse(content).getOpinionPolls().getOpinionPolls());
    }

    /**
     * Verifies that two lines containing simple opinion polls can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithSimpleOpinionPolls() {
        String[] content = new String[]{"•PF: ACME •PD: 2021-07-27 A:55 B:45", "•PF: BCME •PD: 2021-07-28 A:56 C:43"};
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "45").build();
        polls.add(poll);
        poll = new OpinionPoll.Builder().setPollingFirm("BCME").setPublicationDate("2021-07-28")
                                        .addResult("A", "56").addResult("C", "43").build();
        polls.add(poll);
        assertEquals(polls, RichOpinionPollsFile.parse(content).getOpinionPolls().getOpinionPolls());
    }

    /**
     * Verifies that two lines containing a simple opinion with an alternative response scenario can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithOpinionPollAndAlternativeResponseScenario() {
        String[] content = new String[]{"•PF: ACME •PD: 2021-07-27 A:55 B:45", "& A:50 B:40 C:10"};
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
     * Verifies that two lines containing a simple opinion with an alternative response scenario with a result for other
     * can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithOpinionPollAndAlternativeResponseScenarioWithResultForOther() {
        String[] content = new String[]{"•PF: ACME •PD: 2021-07-27 A:55 B:45", "& A:50 B:40 C:8 •O: 2"};
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "45").build();
        ResponseScenario scenario = new ResponseScenario.Builder().addResult("A", "50").addResult("B", "40")
                                                                  .addResult("C", "8").setOther("2").build();
        poll.addAlternativeResponseScenario(scenario);
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        polls.add(poll);
        assertEquals(polls, RichOpinionPollsFile.parse(content).getOpinionPolls().getOpinionPolls());
    }

    /**
     * Verifies that two lines containing a simple opinion with an alternative response scenario with a different scope
     * can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithOpinionPollAndAlternativeResponseScenarioWithDifferentScope() {
        String[] content = new String[]{"•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:45", "& •SC: E A:60 B:40"};
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .setScope("N").addResult("A", "55").addResult("B", "45").build();
        ResponseScenario scenario = new ResponseScenario.Builder().addResult("A", "60").addResult("B", "40")
                                                                  .setScope("E").build();
        poll.addAlternativeResponseScenario(scenario);
        Set<OpinionPoll> polls = new HashSet<OpinionPoll>();
        polls.add(poll);
        assertEquals(polls, RichOpinionPollsFile.parse(content).getOpinionPolls().getOpinionPolls());
    }
}
