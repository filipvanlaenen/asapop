package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPoll;

/**
 * Unit tests on the <code>OpinionPollLine</code> class.
 */
public final class OpinionPollLineTest {
    /**
     * Simple opinion poll line.
     */
    private static final String SIMPLE_OPINION_POLL_LINE = "•PF: ACME •PD: 2021-07-27 A:55 B:45";

    /**
     * Verifies that the <code>isOpinionPollLine</code> method can detect an opinion poll line.
     */
    @Test
    public void isOpinionPollLineShouldDetectOpinionPollLine() {
        assertTrue(OpinionPollLine.isOpinionPollLine(SIMPLE_OPINION_POLL_LINE));
    }

    /**
     * Verifies that the <code>isOpinionPollLine</code> method can detect a line that isn't an opinion poll line.
     */
    @Test
    public void isOpinionPollLineShouldDetectNonOpinionPollLine() {
        assertFalse(OpinionPollLine.isOpinionPollLine("Foo"));
    }

    /**
     * Verifies that String with a single line containing a simple opinion poll can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithASimpleOpinionPoll() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse(SIMPLE_OPINION_POLL_LINE);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                        .addWellformedResult("A", "55").addWellformedResult("B", "45")
                                                        .build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that upper case letters with diacritics and similar can be used as keys for the electoral lists.
     */
    @Test
    public void shouldAllowDiacriticsInTheKeysForTheElectoralLists() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 Ä:55 Æ:45");
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                        .addWellformedResult("Ä", "55").addWellformedResult("Æ", "45")
                                                        .build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that upper case Greek and Cyrillic letters can be used as keys for the electoral lists.
     */
    @Test
    public void shouldAllowGreekAndCyrillicLettersInTheKeysForTheElectoralLists() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 Б:55 Ω:45");
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                        .addWellformedResult("Б", "55").addWellformedResult("Ω", "45")
                                                        .build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with an area can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAnArea() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •A: AB A:55 B:45");
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                        .setArea("AB").addWellformedResult("A", "55")
                                                        .addWellformedResult("B", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a commissioner can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithACommissioner() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •C: The Times •PD: 2021-07-27 A:55 B:45");
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                        .addCommissioner("The Times").addWellformedResult("A", "55")
                                                        .addWellformedResult("B", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with two commissioners can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithTwoCommissioners() {
        String line = "•PF: ACME •C: The Times •C: The Post •PD: 2021-07-27 A:55 B:45";
        OpinionPollLine opinionPollLine = OpinionPollLine.parse(line);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                        .addCommissioner("The Times").addCommissioner("The Post")
                                                        .addWellformedResult("A", "55").addWellformedResult("B", "45")
                                                        .build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a sample size can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithASampleSize() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SS: 1000 A:55 B:45");
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                        .setSampleSize("1000").addWellformedResult("A", "55")
                                                        .addWellformedResult("B", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a result for other can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAResultForOther() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 A:55 B:43 •O:2");
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                        .addWellformedResult("A", "55").addWellformedResult("B", "43")
                                                        .setWellformedOther("2").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a fieldwork start can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAFieldworkStart() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •FS: 2021-07-27 A:55 B:43");
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-07-27")
                                                        .addWellformedResult("A", "55").addWellformedResult("B", "43")
                                                        .build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a year-month fieldwork start can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAYearMonthFieldworkStart() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •FS: 2021-07 A:55 B:43");
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-07")
                                                        .addWellformedResult("A", "55").addWellformedResult("B", "43")
                                                        .build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a fieldwork end can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAFieldworkEnd() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •FE: 2021-07-27 A:55 B:43");
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2021-07-27")
                                                        .addWellformedResult("A", "55").addWellformedResult("B", "43")
                                                        .build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a year-month fieldwork end can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAYearMonthFieldworkEnd() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •FE: 2021-07 A:55 B:43");
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2021-07")
                                                        .addWellformedResult("A", "55").addWellformedResult("B", "43")
                                                        .build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a scope can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAScope() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43");
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-07-27")
                                                        .setScope("N").addWellformedResult("A", "55")
                                                        .addWellformedResult("B", "43").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }
}
