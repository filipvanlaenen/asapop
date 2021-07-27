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
    public void shouldParseSingleLineWithASimplePoll() {
        String content = "•PF: ACME •PD:2021-07-27 A:55 B:45";
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "45").build();
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }
}
