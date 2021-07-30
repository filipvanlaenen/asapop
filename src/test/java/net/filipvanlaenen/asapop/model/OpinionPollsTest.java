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
    public void shouldParseTwoLinesWithSimpleOpinionPolls() {
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

    /**
     * Verifies that upper case letters with diacritics and similar can be used as keys for the electoral lists.
     */
    @Test
    public void shouldHandleElectoralListKeysWithDiacritics() {
        String content = "•PF: ACME •PD: 2021-07-27 Ä:55 Æ:45";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("Ä", "55").addResult("Æ", "45").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies that upper case Greek and Cyrillic letters can be used as keys for the electoral lists.
     */
    @Test
    public void shouldHandleElectoralListKeysWithGreekAndCyrillicLetters() {
        String content = "•PF: ACME •PD: 2021-07-27 Б:55 Ω:45";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("Б", "55").addResult("Ω", "45").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies that an opinion poll with a commissioner can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithACommissioner() {
        String content = "•PF: ACME •C: The Times •PD: 2021-07-27 A:55 B:45";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addCommissioner("The Times").addResult("A", "55")
                                                    .addResult("B", "45").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies that an opinion poll with two commissioners can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithTwoCommissioners() {
        String content = "•PF: ACME •C: The Times •C: The Post •PD: 2021-07-27 A:55 B:45";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addCommissioner("The Times").addCommissioner("The Post")
                                                    .addResult("A", "55").addResult("B", "45").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies that an opinion poll with a sample size can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithASampleSize() {
        String content = "•PF: ACME •PD: 2021-07-27 •SS: 1000 A:55 B:45";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .setSampleSize("1000").addResult("A", "55")
                                                    .addResult("B", "45").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies that an opinion poll with a result for other can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAResultForOther() {
        String content = "•PF: ACME •PD: 2021-07-27 •O: 2 A:55 B:43";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "43").setOther("2").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }
}
