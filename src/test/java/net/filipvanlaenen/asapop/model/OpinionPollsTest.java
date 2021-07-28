package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>OpinionPolls</code>.
 */
public class OpinionPollsTest {
    /**
     * Verifies that String with a single line containing a simple opinion poll can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithASimpleOpinionPoll() {
        String content = "•PF: ACME •PD: 2021-07-27 A:55 B:45";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "45").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies that String with two lines containing a simple opinion polls can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithASimpleOpinionPolls() {
        String content = "•PF: ACME •PD: 2021-07-27 A:55 B:45\n•PF: BCME •PD: 2021-07-28 A:56 C:43";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "45").build();
        polls.add(poll);
        poll = new OpinionPoll.Builder().setPollingFirm("BCME").setPublicationDate("2021-07-28")
                                        .addResult("A", "56").addResult("C", "43").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }
}
