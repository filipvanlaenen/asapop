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
     * Verifies that two lines containing simple opinion polls can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithSimpleOpinionPolls() {
        String[] content = new String[]{"•PF: ACME •PD: 2021-07-27 A:55 B:45", "•PF: BCME •PD: 2021-07-28 A:56 C:43"};
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
        String content = "•PF: ACME •PD: 2021-07-27 A:55 B:43 •O:2";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "43").setOther("2").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies that an opinion poll with fieldwork start can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAFieldworkStart() {
        String content = "•PF: ACME •FS: 2021-07-27 A:55 B:43";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "43").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies that an opinion poll with fieldwork end can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAFieldworkEnd() {
        String content = "•PF: ACME •FE: 2021-07-27 A:55 B:43";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "43").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies that an opinion poll with a scope can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAScope() {
        String content = "•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43";
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .setScope("N").addResult("A", "55").addResult("B", "43").build();
        polls.add(poll);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies that two lines containing a simple opinion with an alternative response scenario can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithOpinionPollAndAlternativeResponseScenario() {
        String[] content = new String[]{"•PF: ACME •PD: 2021-07-27 A:55 B:45", "& A:50 B:40 C:10"};
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "45").build();
        polls.add(poll);
        ResponseScenario scenario = new ResponseScenario.Builder().addResult("A", "50").addResult("B", "40")
                                                                  .addResult("C", "10").build();
        poll.addResponseScenario(scenario);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies that two lines containing a simple opinion with an alternative response scenario with a result for other
     * can be parsed.
     */
    @Test
    public void shouldParseTwoLinesWithOpinionPollAndAlternativeResponseWithResultForOtherScenario() {
        String[] content = new String[]{"•PF: ACME •PD: 2021-07-27 A:55 B:45", "& A:50 B:40 C:8 •O: 2"};
        List<OpinionPoll> polls = new ArrayList<OpinionPoll>();
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                    .addResult("A", "55").addResult("B", "45").build();
        polls.add(poll);
        ResponseScenario scenario = new ResponseScenario.Builder().addResult("A", "50").addResult("B", "40")
                                                                  .addResult("C", "8").setOther("2").build();
        poll.addResponseScenario(scenario);
        assertEquals(polls, OpinionPolls.parse(content).getOpinionPollsList());
    }

    /**
     * Verifies the correct export of a minimal opinion poll to the PSV file format for EOPAOD.
     */
    @Test
    public void shouldExportMinimalOpinionPollToEopaodPsvFormat() {
        OpinionPolls opinionPolls = OpinionPolls.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45");
        StringBuffer expected = new StringBuffer();
        expected.append("Polling firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | Other\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | N/A | 55 | 45 | N/A");
        assertEquals(expected.toString(), opinionPolls.toEopaodPsvString("A", "B"));
    }

    /**
     * Verifies the correct export of an opinion poll with an alternative response scenario to the PSV file format for
     * EOPAOD.
     */
    @Test
    public void shouldExportOpinionPollWithAlternativeResponseScenarioToEopaodPsvFormat() {
        OpinionPolls opinionPolls = OpinionPolls.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45", "& A:50 B:40 C:10");
        StringBuffer expected = new StringBuffer();
        expected.append("Polling firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | C | Other\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | N/A | 55 | 45 | N/A | N/A\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | N/A | 50 | 40 | 10 | N/A");
        assertEquals(expected.toString(), opinionPolls.toEopaodPsvString("A", "B", "C"));
    }
}
