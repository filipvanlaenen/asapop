package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import net.filipvanlaenen.asapop.model.DateOrMonth;
import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.Scope;

/**
 * Unit tests on the <code>OpinionPollLine</code> class.
 */
public final class OpinionPollLineTest {
    /**
     * A date for the unit tests.
     */
    private static final LocalDate DATE1 = LocalDate.parse("2021-07-27");
    /**
     * Another date for the unit tests.
     */
    private static final LocalDate DATE2 = LocalDate.parse("2021-07-29");
    /**
     * A date or month for the unit tests.
     */
    private static final DateOrMonth DATE_OR_MONTH1 = DateOrMonth.parse("2021-07");
    /**
     * Another date or month for the unit tests.
     */
    private static final DateOrMonth DATE_OR_MONTH2 = DateOrMonth.parse("2021-07-28");
    /**
     * Simple opinion poll line.
     */
    private static final String SIMPLE_OPINION_POLL_LINE = "•PF: ACME •PD: 2021-07-29 A:55 B:45";
    /**
     * Opinion poll line with a combination of electoral lists.
     */
    private static final String OPINION_POLL_LINE_WITH_COMBINED_ELECTORAL_LISTS =
            "•PF: ACME •PD: 2021-07-27 A+C:55 B:45";

    /**
     * Verifies that the <code>isOpinionPollLine</code> method can detect an opinion poll line.
     */
    @Test
    public void isOpinionPollLineShouldDetectOpinionPollLine() {
        assertTrue(OpinionPollLine.isOpinionPollLine(SIMPLE_OPINION_POLL_LINE));
    }

    /**
     * Verifies that the <code>isOpinionPollLine</code> method can detect an opinion poll line with combined electoral
     * lists.
     */
    @Test
    public void isOpinionPollLineShouldDetectOpinionPollLineWithCombinedElectoralLists() {
        assertTrue(OpinionPollLine.isOpinionPollLine(OPINION_POLL_LINE_WITH_COMBINED_ELECTORAL_LISTS));
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
        OpinionPollLine opinionPollLine = OpinionPollLine.parse(SIMPLE_OPINION_POLL_LINE, 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE2)
                .addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that String with a single line containing an opinion poll with combined electoral lists can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAnOpinionPollWithCombinedElectoralLists() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse(OPINION_POLL_LINE_WITH_COMBINED_ELECTORAL_LISTS, 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1)
                .addWellformedResult(Set.of("A", "C"), "55").addWellformedResult("B", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that upper case letters with diacritics and similar can be used as keys for the electoral lists.
     */
    @Test
    public void shouldAllowDiacriticsInTheKeysForTheElectoralLists() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 Ä:55 Æ:45", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1)
                .addWellformedResult("Ä", "55").addWellformedResult("Æ", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that upper case Greek and Cyrillic letters can be used as keys for the electoral lists.
     */
    @Test
    public void shouldAllowGreekAndCyrillicLettersInTheKeysForTheElectoralLists() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 Б:55 Ω:45", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1)
                .addWellformedResult("Б", "55").addWellformedResult("Ω", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with an area can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAnArea() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •A: AB A:55 B:45", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1).setArea("AB")
                .addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a commissioner can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithACommissioner() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •C: The Times •PD: 2021-07-27 A:55 B:45", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1)
                .addCommissioner("The Times").addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a polling firm partner can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAPollingFirmPartner() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PFP: EMCA •PD: 2021-07-27 A:55 B:45", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1)
                .setPollingFirmPartner("EMCA").addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with two commissioners can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithTwoCommissioners() {
        String line = "•PF: ACME •C: The Times •C: The Post •PD: 2021-07-27 A:55 B:45";
        OpinionPollLine opinionPollLine = OpinionPollLine.parse(line, 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1)
                .addCommissioner("The Times").addCommissioner("The Post").addWellformedResult("A", "55")
                .addWellformedResult("B", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a sample size can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithASampleSize() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SS: 1000 A:55 B:45", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1)
                .setSampleSize("1000").addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with excluded can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithExcluded() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •EX: 10 A:55 B:45", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1)
                .setExcluded(DecimalNumber.parse("10")).addWellformedResult("A", "55").addWellformedResult("B", "45")
                .build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a result for other can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAResultForOther() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 A:55 B:43 •O:2", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1)
                .addWellformedResult("A", "55").addWellformedResult("B", "43").setWellformedOther("2").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a result for no reponse can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAResultForNoResponse() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 A:55 B:43 •N:2", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1)
                .addWellformedResult("A", "55").addWellformedResult("B", "43").setWellformedNoResponses("2").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a fieldwork start can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAFieldworkStart() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •FS: 2021-07-28 A:55 B:43", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH2)
                .addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a year-month fieldwork start can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAYearMonthFieldworkStart() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •FS: 2021-07 A:55 B:43", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a fieldwork end can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAFieldworkEnd() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •FE: 2021-07-28 A:55 B:43", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2)
                .addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a year-month fieldwork end can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAYearMonthFieldworkEnd() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •FE: 2021-07 A:55 B:43", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH1)
                .addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a scope can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAScope() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43", 1);
        OpinionPoll expected = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE1)
                .setScope(Scope.National).addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that a wellformed line doesn't produce a warning while parsing.
     */
    @Test
    public void shouldNotProduceAWarningWhenParsingAWellformedLine() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse(SIMPLE_OPINION_POLL_LINE, 1);
        assertTrue(opinionPollLine.getWarnings().isEmpty());
    }

    /**
     * Verifies that a line with a malformed result value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedResultValue() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:Error B:43", 1);
        Set<ParserWarning> expected = Set.of(new MalformedResultValueWarning(1, "Error"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed other value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedOtherValue() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43 •O:Error", 1);
        Set<ParserWarning> expected = Set.of(new MalformedResultValueWarning(1, "Error"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed no responses value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedNoResponsesValue() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43 •N:Error", 1);
        Set<ParserWarning> expected = Set.of(new MalformedResultValueWarning(1, "Error"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with an unknown metadata key produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownMetadataKey() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •XX: X •SC: N A:55 B:43", 1);
        Set<ParserWarning> expected = Set.of(new UnknownMetadataKeyWarning(1, "XX"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with an unknown scope produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownScopeValue() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: X •SC: N A:55 B:43", 1);
        Set<ParserWarning> expected = Set.of(new UnknownScopeValueWarning(1, "X"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed decimal number for excluded produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedDecimalNumberForExcludedResponses() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •EX: X •SC: N A:55 B:43", 1);
        Set<ParserWarning> expected = Set.of(new MalformedDecimalNumberWarning(1, "EX", "X"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line missing results produces a warning.
     */
    @Test
    public void shouldProduceAWarningForMissingResults() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N", 1);
        Set<ParserWarning> expected = Set.of(new ResultsMissingWarning(1));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line missing dates produces a warning.
     */
    @Test
    public void shouldProduceAWarningForMissingDates() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •SC: N A:55 B:43", 1);
        Set<ParserWarning> expected = Set.of(new DatesMissingWarning(1));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line missing both polling firm and commissioner produces a warning.
     */
    @Test
    public void shouldProduceAWarningForMissingPollingFirmsAndCommissioner() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PD: 2021-07-27 A:55 B:45", 1);
        Set<ParserWarning> expected = Set.of(new PollingFirmAndCommissionerMissingWarning(1));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding the area twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenAreaIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •A: A •A: A A:55 B:45", 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "A"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding excluded twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenExcludedIsAddedTwice() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •EX: 12 •EX: 12 A:55 B:45", 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "EX"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding fieldwork end twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenFieldworkEndIsAddedTwice() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •FE: 2021-07-27 •FE: 2021-07-27 A:55 B:45", 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "FE"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding fieldwork start twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenFieldworkStartIsAddedTwice() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •FS: 2021-07-27 •FS: 2021-07-27 A:55 B:45", 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "FS"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding no responses twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenNoResponsesIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •N: 12 •N: 12 A:55 B:45", 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "N"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding other twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenOtherIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •O: 12 •O: 12 A:53 B:35", 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "O"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding publication date twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenPublicationDateIsAddedTwice() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •PD: 2021-07-27 A:55 B:45", 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "PD"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding polling firm twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenPollingFirmIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PF: ACME •PD: 2021-07-27 A:55 B:45", 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "PF"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding polling firm partner twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenPollingFirmPartnerIsAddedTwice() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PFP: BCME •PFP: BCME •PD: 2021-07-27 A:55 B:45", 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "PFP"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding scope twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenScopeIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N •SC: N A:55 B:45", 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "SC"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding sample size twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenSampleSizeIsAddedTwice() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SS: 1000 •SS: 1000 A:55 B:45", 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "SS"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }
}
