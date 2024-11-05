package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.LaconicConfigurator;
import net.filipvanlaenen.asapop.model.DateMonthOrYear;
import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPollTestBuilder;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.asapop.model.Unit;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the <code>OpinionPollLine</code> class.
 */
public final class OpinionPollLineTest {
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.");
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
    private static final DateMonthOrYear DATE_OR_MONTH1 = DateMonthOrYear.parse("2021-07");
    /**
     * Another date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH2 = DateMonthOrYear.parse("2021-07-28");
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
     * A map mapping keys to electoral lists.
     */
    private static final Map<String, ElectoralList> ELECTORAL_LIST_KEY_MAP =
            Map.of("A", ElectoralList.get("A"), "Ä", ElectoralList.get("Ä"), "Æ", ElectoralList.get("Æ"), "A1",
                    ElectoralList.get("A1"), "B", ElectoralList.get("B"), "C", ElectoralList.get("C"), "Б",
                    ElectoralList.get("Б"), "Ω", ElectoralList.get("Ω"));

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
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse(SIMPLE_OPINION_POLL_LINE, ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE2).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that String with a single line containing an opinion poll with combined electoral lists can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAnOpinionPollWithCombinedElectoralLists() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse(OPINION_POLL_LINE_WITH_COMBINED_ELECTORAL_LISTS,
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "C", "55").addResult("B", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that digits can be used in the keys for the electoral lists.
     */
    @Test
    public void shouldAllowDigitsInTheKeysForTheElectoralLists() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 A1:55 B:45", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A1", "55").addResult("B", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that upper case letters with diacritics and similar can be used as keys for the electoral lists.
     */
    @Test
    public void shouldAllowDiacriticsInTheKeysForTheElectoralLists() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 Ä:55 Æ:45", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("Ä", "55").addResult("Æ", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that upper case Greek and Cyrillic letters can be used as keys for the electoral lists.
     */
    @Test
    public void shouldAllowGreekAndCyrillicLettersInTheKeysForTheElectoralLists() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 Б:55 Ω:45", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("Б", "55").addResult("Ω", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with an area can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAnArea() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •A: AB A:55 B:45", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE1).setArea("AB").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a commissioner can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithACommissioner() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •C: The Times •PD: 2021-07-27 A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE1).addCommissioner("The Times").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a polling firm partner can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAPollingFirmPartner() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PFP: EMCA •PD: 2021-07-27 A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE1).setPollingFirmPartner("EMCA").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with two commissioners can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithTwoCommissioners() {
        String line = "•PF: ACME •C: The Times •C: The Post •PD: 2021-07-27 A:55 B:45";
        OpinionPollLine opinionPollLine = OpinionPollLine.parse(line, ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected =
                new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                        .setPublicationDate(DATE1).addCommissioner("The Times").addCommissioner("The Post").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a sample size can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithASampleSize() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SS: 1000 A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setSampleSize("1000").setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a greather-than-or-equal-to sample size can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithGreaterThanOrEqualToSampleSize() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SS: ≥1000 A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setSampleSize("≥1000").setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a sample size range can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithSampleSizeRange() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SS: 600–700 A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setSampleSize("600–700").setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with excluded can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithExcluded() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •EX: 10 A:55 B:45", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE1).setExcluded(DecimalNumber.parse("10")).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a result for other can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAResultForOther() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 A:55 B:43 •O:2", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setOther("2")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a result for no reponse can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAResultForNoResponse() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 A:55 B:43 •N:2", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43")
                .setNoResponses("2").setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a fieldwork start can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAFieldworkStart() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •FS: 2021-07-28 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH2).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a year-month fieldwork start can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAYearMonthFieldworkStart() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •FS: 2021-07 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a fieldwork end can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAFieldworkEnd() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •FE: 2021-07-28 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43")
                .setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a year-month fieldwork end can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAYearMonthFieldworkEnd() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •FE: 2021-07 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43")
                .setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a scope can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAScope() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43")
                .setPollingFirm("ACME").setPublicationDate(DATE1).setScope(Scope.NATIONAL).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a unit can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAUnit() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •U: S A:15 B:3", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "15").addResult("B", "3")
                .setPollingFirm("ACME").setPublicationDate(DATE1).setUnit(Unit.SEATS).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that a wellformed line doesn't produce a warning while parsing.
     */
    @Test
    public void shouldNotProduceAWarningWhenParsingAWellformedLine() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse(SIMPLE_OPINION_POLL_LINE, ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        assertTrue(opinionPollLine.getWarnings().isEmpty());
    }

    /**
     * Verifies that a line with a malformed result value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedResultValue() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:Error B:43",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new MalformedResultValueWarning(1, "Error"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed other value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedOtherValue() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43 •O:Error",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new MalformedResultValueWarning(1, "Error"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed no responses value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedNoResponsesValue() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43 •N:Error",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new MalformedResultValueWarning(1, "Error"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed verified sum produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedVerifiedSum() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43 •VS:Error",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new MalformedDecimalNumberWarning(1, "VS", "Error"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with an unknown metadata key produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownMetadataKey() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •XX: X •SC: N A:55 B:43",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new UnknownMetadataKeyWarning(1, "XX"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with an unknown scope produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownScopeValue() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: X •SC: N A:55 B:43",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new UnknownMetadataValueWarning(1, "scope", "X"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with an unknown unit produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownUnitValue() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •U: X •SC: N A:55 B:43",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new UnknownMetadataValueWarning(1, "unit", "X"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed decimal number for excluded produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedDecimalNumberForExcludedResponses() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •EX: X •SC: N A:55 B:43",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new MalformedDecimalNumberWarning(1, "EX", "X"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line missing results produces a warning.
     */
    @Test
    public void shouldProduceAWarningForMissingResults() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new ResultsMissingWarning(1));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line missing dates produces a warning.
     */
    @Test
    public void shouldProduceAWarningForMissingDates() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldProduceAWarningForMissingDates.");
        OpinionPollLine.parse("•PF: ACME •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected =
                "‡ ⬐ Unit test OpinionPollLineTest.shouldProduceAWarningForMissingDates.\n" + "‡ No dates found.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line missing both polling firm and commissioner produces a warning.
     */
    @Test
    public void shouldProduceAWarningForMissingPollingFirmsAndCommissioner() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PD: 2021-07-27 A:55 B:45", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new PollingFirmAndCommissionerMissingWarning(1));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with an unknown electoral list key produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownElectoralListKey() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43 XX:2",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new UnknownElectoralListKeyWarning(1, "XX"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding the area twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenAreaIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •A: A •A: A A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "A"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding excluded twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenExcludedIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •EX: 12 •EX: 12 A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "EX"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding fieldwork end twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenFieldworkEndIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •FE: 2021-07-27 •FE: 2021-07-27 A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "FE"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding fieldwork start twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenFieldworkStartIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •FS: 2021-07-27 •FS: 2021-07-27 A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "FS"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding no responses twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenNoResponsesIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •N: 12 •N: 12 A:53 B:35",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "N"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding other twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenOtherIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •O: 12 •O: 12 A:53 B:35",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "O"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding a verified sum twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenVerifiedSumIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •VS: 88 •VS: 88 A:53 B:35",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "VS"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding publication date twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenPublicationDateIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •PD: 2021-07-27 A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "PD"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding polling firm twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenPollingFirmIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PF: ACME •PD: 2021-07-27 A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "PF"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding polling firm partner twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenPollingFirmPartnerIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine
                .parse("•PF: ACME •PFP: BCME •PFP: BCME •PD: 2021-07-27 A:55 B:45", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "PFP"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding scope twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenScopeIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N •SC: N A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "SC"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding unit twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenUnitIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •U: S •U: S A:55 B:45",
                ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "U"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line adding sample size twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenSampleSizeIsAddedTwice() {
        OpinionPollLine opinionPollLine = OpinionPollLine
                .parse("•PF: ACME •PD: 2021-07-27 •SS: 1000 •SS: 1000 A:55 B:45", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "SS"));
        assertEquals(expected, opinionPollLine.getWarnings());
    }

    /**
     * Verifies that a line with results that don't add up logs an error message.
     */
    @Test
    public void shouldLogErrorWhenResultsDoNotAddUp() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogErrorWhenResultsDoNotAddUp.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SS: 1000 A:60 B:50", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogErrorWhenResultsDoNotAddUp.\n"
                + "‡ ⬐ Total sum is 110.000000.\n" + "‡ Results don’t add up within rounding error interval.\n";
        assertEquals(expected, outputStream.toString());
    }
}
