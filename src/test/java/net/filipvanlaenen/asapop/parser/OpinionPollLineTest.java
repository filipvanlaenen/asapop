package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;

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
                OpinionPollLine.parse(SIMPLE_OPINION_POLL_LINE, ELECTORAL_LIST_KEY_MAP, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE2).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that String with a single line containing an opinion poll with combined electoral lists can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAnOpinionPollWithCombinedElectoralLists() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse(OPINION_POLL_LINE_WITH_COMBINED_ELECTORAL_LISTS, ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 A1:55 B:45", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 Ä:55 Æ:45", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 Б:55 Ω:45", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •A: AB A:55 B:45", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                ELECTORAL_LIST_KEY_MAP, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setPollingFirm("ACME").setPublicationDate(DATE1).addCommissioner("The Times").build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a polling firm partner can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAPollingFirmPartner() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PFP: EMCA •PD: 2021-07-27 A:55 B:45", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
        OpinionPollLine opinionPollLine = OpinionPollLine.parse(line, ELECTORAL_LIST_KEY_MAP, TOKEN);
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
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SS: 1000 A:55 B:45", ELECTORAL_LIST_KEY_MAP, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setSampleSize("1000").setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a greather-than-or-equal-to sample size can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithGreaterThanOrEqualToSampleSize() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SS: ≥1000 A:55 B:45", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •EX: 10 A:55 B:45", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 A:55 B:43 •O:2", ELECTORAL_LIST_KEY_MAP, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setOther("2")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a result for no reponses can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAResultForNoResponse() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 A:55 B:43 •N:2", ELECTORAL_LIST_KEY_MAP, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43")
                .setNoResponses("2").setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a result for other and no reponses combined can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAResultForOtherAndNoResponseCombined() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 A:55 B:43 •ON:2", ELECTORAL_LIST_KEY_MAP, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43")
                .setOtherAndNoResponses("2").setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that an opinion poll with a fieldwork start can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithAFieldworkStart() {
        OpinionPollLine opinionPollLine =
                OpinionPollLine.parse("•PF: ACME •FS: 2021-07-28 A:55 B:43", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                OpinionPollLine.parse("•PF: ACME •FS: 2021-07 A:55 B:43", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                OpinionPollLine.parse("•PF: ACME •FE: 2021-07-28 A:55 B:43", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                OpinionPollLine.parse("•PF: ACME •FE: 2021-07 A:55 B:43", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP, TOKEN);
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
                OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •U: S A:15 B:3", ELECTORAL_LIST_KEY_MAP, TOKEN);
        OpinionPoll expected = new OpinionPollTestBuilder().addResult("A", "15").addResult("B", "3")
                .setPollingFirm("ACME").setPublicationDate(DATE1).setUnit(Unit.SEATS).build();
        assertEquals(expected, opinionPollLine.getOpinionPoll());
    }

    /**
     * Verifies that a wellformed line doesn't produce a warning while parsing.
     */
    @Test
    public void shouldNotLogAnErrorWhenParsingAWellformedLine() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldNotLogAnErrorWhenParsingAWellformedLine.");
        OpinionPollLine.parse(SIMPLE_OPINION_POLL_LINE, ELECTORAL_LIST_KEY_MAP, token);
        assertTrue(outputStream.toString().isEmpty());
    }

    /**
     * Verifies that a line with a malformed result value produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedResultValue() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedResultValue.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:Error B:43", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedResultValue.\n"
                + "‡ ⬐ Processing result key A.\n" + "‡ Malformed result value Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed other value produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedOtherValue() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedOtherValue.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:45 •O:Error", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedOtherValue.\n"
                + "‡ ⬐ Processing metadata field O.\n" + "‡ Malformed result value Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed no responses value produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedNoResponsesValue() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedNoResponsesValue.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43 •N:Error", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedNoResponsesValue.\n"
                + "‡ ⬐ Processing metadata field N.\n" + "‡ Malformed result value Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed other and no responses value produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedOtherAndNoResponsesValue() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedOtherAndNoResponsesValue.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:45 •ON:Error", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedOtherAndNoResponsesValue.\n"
                + "‡ ⬐ Processing metadata field ON.\n" + "‡ Malformed result value Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed sample size produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedSampleSize() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedSampleSize.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43 •SS:Error", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedSampleSize.\n"
                + "‡ ⬐ Processing metadata field SS.\n" + "‡ Malformed sample size Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed verified sum produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedVerifiedSum() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedVerifiedSum.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43 •VS:Error", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedVerifiedSum.\n"
                + "‡ ⬐ Processing metadata field VS.\n" + "‡ Malformed decimal number Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed fieldwork start produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedFieldworkStart() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedFieldworkStart.");
        OpinionPollLine.parse("•PF: ACME •FS: Error •FE: 2021-07-27 •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedFieldworkStart.\n"
                + "‡ ⬐ Processing metadata field FS.\n" + "‡ Malformed date, month or year Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed fieldwork end produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedFieldworkEnd() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedFieldworkEnd.");
        OpinionPollLine.parse("•PF: ACME •FE: Error •FS: 2021-07-27 •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedFieldworkEnd.\n"
                + "‡ ⬐ Processing metadata field FE.\n" + "‡ Malformed date, month or year Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed publication date produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedPublicationDate() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedPublicationDate.");
        OpinionPollLine.parse("•PF: ACME •PD: Error •FE: 2021-07-27 •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedPublicationDate.\n"
                + "‡ ⬐ Processing metadata field PD.\n" + "‡ Malformed date Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with an unknown metadata key produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAnUnknownMetadataKey() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAnUnknownMetadataKey.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •XX: X •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAnUnknownMetadataKey.\n"
                + "‡ ⬐ Processing metadata field XX.\n" + "‡ Unknown metadata key XX.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with an unknown scope produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAnUnknownScopeValue() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAnUnknownScopeValue.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: X •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAnUnknownScopeValue.\n"
                + "‡ ⬐ Processing metadata field SC.\n" + "‡ Unknown metadata value X.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with an unknown unit produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAnUnknownUnitValue() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAnUnknownUnitValue.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •U: X •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAnUnknownUnitValue.\n"
                + "‡ ⬐ Processing metadata field U.\n" + "‡ Unknown metadata value X.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed decimal number for excluded produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedDecimalNumberForExcludedResponses() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage(
                "Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedDecimalNumberForExcludedResponses.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •EX: X •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP, token);
        String expected =
                "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAMalformedDecimalNumberForExcludedResponses.\n"
                        + "‡ ⬐ Processing metadata field EX.\n" + "‡ Malformed decimal number X.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line missing results produces a warning.
     */
    @Test
    public void shouldLogAnErrorForMissingResults() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldProduceAWarningForMissingResults.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N", ELECTORAL_LIST_KEY_MAP, token);
        String expected =
                "‡ ⬐ Unit test OpinionPollLineTest.shouldProduceAWarningForMissingResults.\n" + "‡ No results found.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line missing dates produces a warning.
     */
    @Test
    public void shouldLogAnErrorForMissingDates() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldProduceAWarningForMissingDates.");
        OpinionPollLine.parse("•PF: ACME •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP, token);
        String expected =
                "‡ ⬐ Unit test OpinionPollLineTest.shouldProduceAWarningForMissingDates.\n" + "‡ No dates found.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with inconsistent dates produces a warning.
     */
    @Test
    public void shouldLogAnErrorForInconsistentDates() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForInconsistentDates.");
        OpinionPollLine.parse("•PF: ACME •FS: 2021-07-27 •FE: 2021-07-26 •SC: N A:55 B:43", ELECTORAL_LIST_KEY_MAP,
                token);
        String expected = "‡ ⬐ Unit test OpinionPollLineTest.shouldLogAnErrorForInconsistentDates.\n"
                + "‡ Dates aren't consistent (FS ≤ FE ≤ PD).\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line missing both polling firm and commissioner produces a warning.
     */
    @Test
    public void shouldLogAnErrorForMissingPollingFirmsAndCommissioner() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForMissingPollingFirmsAndCommissioner.");
        OpinionPollLine.parse("•PD: 2021-07-27 A:55 B:45", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡ ⬐ Unit test OpinionPollLineTest.shouldLogAnErrorForMissingPollingFirmsAndCommissioner.\n"
                + "‡ No polling firm or commissioner.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with an unknown electoral list key produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAnUnknownElectoralListKey() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorForAnUnknownElectoralListKey.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N A:55 B:43 XX:2", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorForAnUnknownElectoralListKey.\n"
                + "‡ ⬐ Processing result key XX.\n" + "‡ Unknown electoral list key XX.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding the area twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenAreaIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenAreaIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •A: A •A: A A:55 B:45", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenAreaIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field A.\n" + "‡ Single value metadata key A occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding excluded twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenExcludedIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenExcludedIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •EX: 12 •EX: 12 A:55 B:45", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenExcludedIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field EX.\n" + "‡ Single value metadata key EX occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding fieldwork end twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenFieldworkEndIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenFieldworkEndIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •FE: 2021-07-27 •FE: 2021-07-27 A:55 B:45", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenFieldworkEndIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field FE.\n" + "‡ Single value metadata key FE occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding fieldwork start twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenFieldworkStartIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenFieldworkStartIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •FS: 2021-07-27 •FS: 2021-07-27 A:55 B:45", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenFieldworkStartIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field FS.\n" + "‡ Single value metadata key FS occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding no responses twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenNoResponsesIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenNoResponsesIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •N: 12 •N: 12 A:53 B:35", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenNoResponsesIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field N.\n" + "‡ Single value metadata key N occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding other twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenOtherIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenOtherIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •O: 12 •O: 12 A:53 B:35", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenOtherIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field O.\n" + "‡ Single value metadata key O occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding other and no responses twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenOtherAndNoResponsesIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenOtherAndNoResponsesIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •ON: 12 •ON: 12 A:53 B:35", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenOtherAndNoResponsesIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field ON.\n" + "‡ Single value metadata key ON occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding both other and other and no responses combined produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenOtherAndOtherAndNoResponsesIsAdded() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenOtherAndOtherAndNoResponsesIsAdded.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •O: 6 •ON: 6 A:53 B:35", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡ ⬐ Unit test OpinionPollLineTest.shouldLogAnErrorWhenOtherAndOtherAndNoResponsesIsAdded.\n"
                + "‡ Other and no responses (ON) shouldn’t be combined with other (O) and/or no responses (N).\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding both no responses and other and no responses combined produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenNoResponsesAndOtherAndNoResponsesIsAdded() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage(
                "Unit test OpinionPollLineTest.shouldLogAnErrorWhenNoResponsesAndOtherAndNoResponsesIsAdded.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •N: 6 •ON: 6 A:53 B:35", ELECTORAL_LIST_KEY_MAP, token);
        String expected =
                "‡ ⬐ Unit test OpinionPollLineTest.shouldLogAnErrorWhenNoResponsesAndOtherAndNoResponsesIsAdded.\n"
                        + "‡ Other and no responses (ON) shouldn’t be combined with other (O) and/or no responses"
                        + " (N).\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding a verified sum twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenVerifiedSumIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenVerifiedSumIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •VS: 88 •VS: 88 A:53 B:35", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenVerifiedSumIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field VS.\n" + "‡ Single value metadata key VS occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding publication date twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenPublicationDateIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenPublicationDateIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •PD: 2021-07-27 A:55 B:45", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenPublicationDateIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field PD.\n" + "‡ Single value metadata key PD occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding polling firm twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenPollingFirmIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenPollingFirmIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PF: ACME •PD: 2021-07-27 A:55 B:45", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenPollingFirmIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field PF.\n" + "‡ Single value metadata key PF occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding polling firm partner twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenPollingFirmPartnerIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenPollingFirmPartnerIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PFP: BCME •PFP: BCME •PD: 2021-07-27 A:55 B:45", ELECTORAL_LIST_KEY_MAP,
                token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenPollingFirmPartnerIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field PFP.\n" + "‡ Single value metadata key PFP occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding scope twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenScopeIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenScopeIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SC: N •SC: N A:55 B:45", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenScopeIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field SC.\n" + "‡ Single value metadata key SC occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding unit twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenUnitIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenUnitIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •U: S •U: S A:55 B:45", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenUnitIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field U.\n" + "‡ Single value metadata key U occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding sample size twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenSampleSizeIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogAnErrorWhenSampleSizeIsAddedTwice.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SS: 1000 •SS: 1000 A:55 B:45", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogAnErrorWhenSampleSizeIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field SS.\n" + "‡ Single value metadata key SS occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with results that don't add up logs an error message.
     */
    @Test
    public void shouldLogAnLogErrorWhenResultsDoNotAddUp() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test OpinionPollLineTest.shouldLogErrorWhenResultsDoNotAddUp.");
        OpinionPollLine.parse("•PF: ACME •PD: 2021-07-27 •SS: 1000 A:60 B:50", ELECTORAL_LIST_KEY_MAP, token);
        String expected = "‡   Unit test OpinionPollLineTest.shouldLogErrorWhenResultsDoNotAddUp.\n"
                + "‡ ⬐ Total sum is 110.000000.\n" + "‡ Results don’t add up within rounding error interval.\n";
        assertEquals(expected, outputStream.toString());
    }
}
