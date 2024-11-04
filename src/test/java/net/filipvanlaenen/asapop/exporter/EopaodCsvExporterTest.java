package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.DateMonthOrYear;
import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPollTestBuilder;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResponseScenarioTestBuilder;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.asapop.model.Unit;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the class <code>EopaodCsvExporter</code>.
 */
public class EopaodCsvExporterTest {
    /**
     * A date for the unit tests.
     */
    private static final LocalDate DATE = LocalDate.parse("2021-08-02");
    /**
     * A date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH1 = DateMonthOrYear.parse("2021-08-01");
    /**
     * Another date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH2 = DateMonthOrYear.parse("2021-08-02");
    /**
     * A list with the keys of the electoral lists A and B.
     */
    private static final List<Set<String>> A_AND_B = List.of(Set.of("A"), Set.of("B"));
    /**
     * A list with the IDs of the electoral lists A and B.
     */
    private static final List<Set<String>> A_AND_B_IDS = List.of(Set.of("AA001"), Set.of("AA002"));
    /**
     * A list with the electoral lists A, B and C.
     */
    private static final List<Set<String>> A_AND_B_AND_C = List.of(Set.of("A"), Set.of("B"), Set.of("C"));
    /**
     * A list with the IDs of the electoral lists A, B and C.
     */
    private static final List<Set<String>> A_AND_B_AND_C_IDS =
            List.of(Set.of("AA001"), Set.of("AA002"), Set.of("AA003"));
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test EopaodCsvExporterTest.");

    /**
     * Verifies the correct export of a minimal opinion poll.
     */
    @Test
    public void shouldExportMinimalOpinionPoll() {
        OpinionPolls opinionPolls = RichOpinionPollsFile
                .parse(TOKEN, "•PF: ACME •PD: 2021-07-27 A:55 B:45", "A: AA001 •A:AP", "B: AA002 •A:BL")
                .getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP,BL,Other\n");
        expected.append("ACME,,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available\n");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, null, null, A_AND_B_IDS));
    }

    /**
     * Verifies that the correct export of combined electoral lists.
     */
    @Test
    public void shouldExportWithCombinedElectoralListsAbbreviation() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse(TOKEN, "•PF: ACME •PD: 2021-07-27 A+C:55 B+D:45",
                "A: AA001 •A:AP", "B: AA002 •A:BL", "C: AA003 •A:CC", "D: AA004 •A:DE").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP+CC,BL+DE,Other\n");
        expected.append("ACME,,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available\n");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, null, null,
                List.of(Set.of("AA001", "AA003"), Set.of("AA002", "AA004"))));
    }

    /**
     * Verifies that the romanized abbreviation is used when available.
     */
    @Test
    public void shouldExportWithRomanizedAbbreviation() {
        OpinionPolls opinionPolls = RichOpinionPollsFile
                .parse(TOKEN, "•PF: ACME •PD: 2021-07-27 A:55 B:45", "A: AA001 •A:ΑΠ •R:AP", "B: AA002 •A:BL")
                .getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP,BL,Other\n");
        expected.append("ACME,,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available\n");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, null, null, A_AND_B_IDS));
    }

    /**
     * Verifies that opinion polls are sorted.
     */
    @Test
    public void shouldSortOpinionPolls() {
        OpinionPolls opinionPolls =
                RichOpinionPollsFile
                        .parse(TOKEN, "•PF: ACME •PD: 2021-07-27 A:55 B:45", "•PF: ACME •PD: 2021-08-15 A:55 B:45",
                                "•PF: ACME •PD: 2021-07-28 A:55 B:45", "A: AA001 •A:AP", "B: AA002 •A:BL")
                        .getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP,BL,Other\n");
        expected.append("ACME,,2021-08-15,2021-08-15,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available\n");
        expected.append("ACME,,2021-07-28,2021-07-28,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available\n");
        expected.append("ACME,,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available\n");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, null, null, A_AND_B_IDS));
    }

    /**
     * Verifies the correct export of an opinion poll with an alternative response scenario.
     */
    @Test
    public void shouldExportOpinionPollWithAlternativeResponseScenario() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse(TOKEN, "•PF: ACME •PD: 2021-07-27 A:55 B:45",
                "& A:50 B:40 C:10", "A: AA001 •A:AP", "B: AA002 •A:BL", "C: AA003 •A:C").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP,BL,C,Other\n");
        expected.append("ACME,,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available,Not Available\n");
        expected.append("ACME,,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,50%,40%,10%,Not Available\n");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, null, null, A_AND_B_AND_C_IDS));
    }

    /**
     * Verifies the correct export of a opinion poll with only a publication date.
     */
    @Test
    public void shouldExportOpinionPollWithPublicationDateOnlyCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .setPublicationDate(DATE).build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with a fieldwork period.
     */
    @Test
    public void shouldExportOpinionPollWithFieldworkPeriodCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        String expected = "ACME,,2021-08-01,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario for an opinion poll with the same scope.
     */
    @Test
    public void shouldExportResponseScenarioWithSameScopeCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE)
                .setScope(Scope.NATIONAL).build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").build();
        String expected = "ACME,,2021-08-02,2021-08-02,National,Not Available,Not Available"
                + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario for an opinion poll with a different scope.
     */
    @Test
    public void shouldExportResponseScenarioWithDifferentScopeCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE)
                .setScope(Scope.NATIONAL).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43")
                .setScope(Scope.EUROPEAN).build();
        String expected = "ACME,,2021-08-02,2021-08-02,European,Not Available,Not Available"
                + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a sample size.
     */
    @Test
    public void shouldExportOpinionPollWithSampleSizeCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setPublicationDate(DATE).build();
        String expected =
                "ACME,,2021-08-02,2021-08-02,Not Available,1000,Provided" + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a simple opinion poll with excluded responses.
     */
    @Test
    public void shouldExportOpinionPollWithExcludedResponsesCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setPublicationDate(DATE).setExcluded(DecimalNumber.parse("10")).build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available,90%,1%,55%,43%"
                + ",Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a result for other.
     */
    @Test
    public void shouldExportOpinionPollWithOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setOther("2")
                .setPollingFirm("ACME").setPublicationDate(DATE).build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,43%,2%";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a result for no responses and other.
     */
    @Test
    public void shouldExportOpinionPollWithNoResponsesAndOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "50").addResult("B", "40").setNoResponses("8")
                .setOther("2").setPollingFirm("ACME").setPublicationDate(DATE).build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available,92%,1%,54%,43%,2%";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of an opinion poll given in numbers of seats when the sample size and result values
     * are given.
     *
     * <code>•U: S •SS: 1000 A: 40 B: 30 C: 20</code>
     */
    @Test
    public void exportOfOpinionPollCaseU01ShouldHandleSampleSizeAndResultValues() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setSampleSize("1000").setUnit(Unit.SEATS).setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).build();
        String expected =
                "ACME,,2021-08-01,2021-08-02,Not Available,1000,Provided,Not Available,1.1%,44.4%,33.3%,22.2%,Not"
                        + " Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of a response scenario given in numbers of seats when the sample size and result
     * values are given.
     *
     * <code>•U: S •SS: 1000 A: 40 B: 30 C: 20</code>
     */
    @Test
    public void exportOfResponseScenarioCaseU01ShouldHandleSampleSizeAndResultValues() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setUnit(Unit.SEATS).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "40").addResult("B", "30")
                .addResult("C", "20").setSampleSize("1000").build();
        String expected =
                "ACME,,2021-08-01,2021-08-02,Not Available,1000,Provided,Not Available,1.1%,44.4%,33.3%,22.2%,Not"
                        + " Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of an opinion poll given in numbers of seats when the sample size, the excluded and
     * result values are given.
     *
     * <code>•U: S •SS: 1250 •EX: 20 A: 40 B: 30 C: 20</code>
     */
    @Test
    public void exportOfOpinionPollCaseU02ShouldHandleSampleSizeExcludedAndResultValues() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setSampleSize("1250").setUnit(Unit.SEATS).setExcluded(DecimalNumber.parse("20")).setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        String expected =
                "ACME,,2021-08-01,2021-08-02,Not Available,1250,Provided,80%,1.1%,44.4%,33.3%,22.2%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of response scenario given in numbers of seats when the sample size, the excluded and
     * result values are given.
     *
     * <code>•U: S •SS: 1250 •EX: 20 A: 40 B: 30 C: 20</code>
     */
    @Test
    public void exportOfResponseScenarioCaseU02ShouldHandleSampleSizeExcludedAndResultValues() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setUnit(Unit.SEATS).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "40").addResult("B", "30")
                .addResult("C", "20").setSampleSize("1250").setExcluded(DecimalNumber.parse("20")).build();
        String expected =
                "ACME,,2021-08-01,2021-08-02,Not Available,1250,Provided,80%,1.1%,44.4%,33.3%,22.2%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of an opinion poll given in numbers of seats when the sample size, result values and
     * others are given.
     *
     * <code>•U: S •SS: 1000 A: 40 B: 30 C: 20 •O: 10</code>
     */
    @Test
    public void exportOfOpinionPollCaseU03ShouldHandleSampleSizeResultValuesAndOthers() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setOther("10").setSampleSize("1000").setUnit(Unit.SEATS).setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        String expected = "ACME,,2021-08-01,2021-08-02,Not Available,1000,Provided,Not Available,1.0%,40%,30%,20%,10%";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of a response scenario given in numbers of seats when the sample size, result values
     * and others are given.
     *
     * <code>•U: S •SS: 1000 A: 40 B: 30 C: 20 •O: 10</code>
     */
    @Test
    public void exportOfResponseScenarioCaseU03ShouldHandleSampleSizeResultValuesAndOthers() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setUnit(Unit.SEATS).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "40").addResult("B", "30")
                .addResult("C", "20").setOther("10").setSampleSize("1000").build();
        String expected = "ACME,,2021-08-01,2021-08-02,Not Available,1000,Provided,Not Available,1.0%,40%,30%,20%,10%";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of an opinion poll given in numbers of seats when the sample size, the excluded,
     * result values and others are given.
     *
     * <code>•U: S •SS: 1250 •EX: 20 A: 40 B: 30 C: 20 •O: 10</code>
     */
    @Test
    public void exportOfOpinionPollCaseU04ShouldHandleSampleSizeExcludedResultValuesAndOthers() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setOther("10").setSampleSize("1250").setUnit(Unit.SEATS).setExcluded(DecimalNumber.parse("20"))
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        String expected = "ACME,,2021-08-01,2021-08-02,Not Available,1250,Provided,80%,1.0%,40%,30%,20%,10%";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of a response scenario given in numbers of seats when the sample size, the excluded,
     * result values and others are given.
     *
     * <code>•U: S •SS: 1250 •EX: 20 A: 40 B: 30 C: 20 •O: 10</code>
     */
    @Test
    public void exportOfResponseScenarioCaseU04ShouldHandleSampleSizeExcludedResultValuesAndOthers() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setUnit(Unit.SEATS).build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                        .setOther("10").setSampleSize("1250").setExcluded(DecimalNumber.parse("20")).build();
        String expected = "ACME,,2021-08-01,2021-08-02,Not Available,1250,Provided,80%,1.0%,40%,30%,20%,10%";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of an opinion poll given in numbers of seats when the sample size, result values and
     * others are less than one hundred.
     *
     * <code>•U: S •SS: 1250 A: 32 B: 24 C: 16 •O: 8</code>
     */
    @Test
    public void exportOfOpinionPollCaseU05ShouldHandleSampleSizeResultValuesAndOthersLessThanOneHundred() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "32").addResult("B", "24").addResult("C", "16")
                .setOther("8").setSampleSize("1250").setUnit(Unit.SEATS).setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        String expected =
                "ACME,,2021-08-01,2021-08-02,Not Available,1250,Provided,Not Available,1.2%,40.0%,30.0%,20.0%,10.0%";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of a response scenario given in numbers of seats when the sample size, result values
     * and others are less than one hundred.
     *
     * <code>•U: S •SS: 1250 A: 32 B: 24 C: 16 •O: 8</code>
     */
    @Test
    public void exportOfResponseScenarioCaseU05ShouldHandleSampleSizeResultValuesAndOthersLessThanOneHundred() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setUnit(Unit.SEATS).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "32").addResult("B", "24")
                .addResult("C", "16").setOther("8").setSampleSize("1250").build();
        String expected =
                "ACME,,2021-08-01,2021-08-02,Not Available,1250,Provided,Not Available,1.2%,40.0%,30.0%,20.0%,10.0%";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of an opinion poll in numbers of seats when the sample size, the excluded, result
     * values and others are less than one hundred.
     *
     * <code>•U: S •SS: 1250 •EX: 30 A: 32 B: 24 C: 16 •O: 8</code>
     */
    @Test
    public void exportOfOpinionPollCaseU06ShouldHandleSampleSizeExcludedResultValuesAndOthersLessThanOneHundred() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "32").addResult("B", "24").addResult("C", "16")
                .setOther("8").setSampleSize("1250").setUnit(Unit.SEATS).setExcluded(DecimalNumber.parse("30"))
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        String expected = "ACME,,2021-08-01,2021-08-02,Not Available,1250,Provided,70%,1.2%,40.0%,30.0%,20.0%,10.0%";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of a response scenario in numbers of seats when the sample size, the excluded, result
     * values and others are less than one hundred.
     *
     * <code>•U: S •SS: 1250 •EX: 30 A: 32 B: 24 C: 16 •O: 8</code>
     */
    @Test
    public void exportOfResponseScenarioCaseU06ShouldHandleSampleSizeExcludedResultValuesAndOthersLessThanOneHundred() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setUnit(Unit.SEATS).build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "32").addResult("B", "24").addResult("C", "16")
                        .setOther("8").setSampleSize("1250").setExcluded(DecimalNumber.parse("30")).build();
        String expected = "ACME,,2021-08-01,2021-08-02,Not Available,1250,Provided,70%,1.2%,40.0%,30.0%,20.0%,10.0%";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of an opinion poll given in number of seats when the sample size, result values and
     * others are greater than one hundred.
     *
     * <code>•U: S •SS: 1000 A: 80 B: 60 C: 40 •O: 20</code>
     */
    @Test
    public void exportOfOpinionPollCaseU15ShouldHandleSampleSizeResultValuesAndOthersGreaterThanOneHundred() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "80").addResult("B", "60").addResult("C", "40")
                .setOther("20").setSampleSize("1000").setUnit(Unit.SEATS).setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        String expected =
                "ACME,,2021-08-01,2021-08-02,Not Available,1000,Provided,Not Available,0.5%,40.0%,30.0%,20.0%,10.0%";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of a response scenario given in number of seats when the sample size, result values
     * and others are greater than one hundred.
     *
     * <code>•U: S •SS: 1000 A: 80 B: 60 C: 40 •O: 20</code>
     */
    @Test
    public void exportOfResponseScenarioCaseU15ShouldHandleSampleSizeResultValuesAndOthersGreaterThanOneHundred() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setUnit(Unit.SEATS).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "80").addResult("B", "60")
                .addResult("C", "40").setOther("20").setSampleSize("1000").build();
        String expected =
                "ACME,,2021-08-01,2021-08-02,Not Available,1000,Provided,Not Available,0.5%,40.0%,30.0%,20.0%,10.0%";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies the correct export of a simple opinion poll with one commissioner.
     */
    @Test
    public void shouldExportOpinionPollWithOneCommissioner() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .addCommissioner("The Times").setPublicationDate(DATE).build();
        String expected = "ACME,The Times,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a simple opinion poll with two commissioners.
     */
    @Test
    public void shouldExporOpinionPollWithTwoCommissioners() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .addCommissioner("The Times").addCommissioner("The Post").setPublicationDate(DATE).build();
        String expected = "ACME,The Post and The Times,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a polling firm and a polling firm partner.
     */
    @Test
    public void shouldExportOpinionPollWithPollingFirmAndPollingFirmPartner() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setPollingFirmPartner("EMCA").setPublicationDate(DATE).build();
        String expected = "ACME and EMCA,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with results having half of a percent.
     */
    @Test
    public void shouldExportOpinionPollWithAResultWithHalfAPercent() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55.5").addResult("B", "43")
                .setPollingFirm("ACME").setPublicationDate(DATE).build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,0.5%,55.5%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with a less than result.
     */
    @Test
    public void shouldExportOpinionPollWithALessThanResult() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").addResult("C", "<1")
                .setPollingFirm("ACME").setPublicationDate(DATE).build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,43%,0%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B_AND_C));
    }

    /**
     * Verifies that when an area is specified, an opinion poll without an area is not exported.
     */
    @Test
    public void shouldNotExportAnOpinionPollWhenTheAreaDoesNotMatch() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55.5").addResult("B", "43")
                .setPollingFirm("ACME").setPublicationDate(DATE).build();
        assertNull(EopaodCsvExporter.export(poll, "N", null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario for an opinion poll with only a publication date.
     */
    @Test
    public void shouldExportResponseScenarioWithPublicationDateOnlyCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE).build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "45").build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario for an opinion poll with a fieldwork period.
     */
    @Test
    public void shouldExportResponseScenarioWithFieldworkPeriodCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "45").build();
        String expected = "ACME,,2021-08-01,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario for an opinion poll with a result for other.
     */
    @Test
    public void shouldExportResponseScenarioWithOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE).build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").setOther("2").build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,43%,2%";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario with a result for no responses and other.
     */
    @Test
    public void shouldExportResponseScenarioWithNoResponsesAndOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "50").addResult("B", "40")
                .setNoResponses("8").setOther("2").build();
        String expected =
                "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available" + ",92%,1%,54%,43%,2%";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario for an opinion poll with excluded responses.
     */
    @Test
    public void shouldExportResponseScenarioWithExcludedResponsesCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE)
                .setExcluded(DecimalNumber.parse("10")).build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",90%,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with commas in the polling firm and the commissioner.
     */
    @Test
    public void shouldExportOpinionPollWithCommasInPollingFirmAndCommissionerCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setPollingFirm("AC,ME").addCommissioner("Times, The").setPublicationDate(DATE).build();
        String expected = "\"AC,ME\",\"Times, The\",2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a simple response scenario with commas in the polling firm and the commissioner.
     */
    @Test
    public void shouldExportResponseScenarioWithCommasInPollingFirmAndCommissionerCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setPollingFirm("AC,ME").addCommissioner("Times, The").setPublicationDate(DATE).build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").build();
        String expected = "\"AC,ME\",\"Times, The\",2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with quotes in the polling firm and the commissioner.
     */
    @Test
    public void shouldExportOpinionPollWithQuotesInPollingFirmAndCommissionerCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setPollingFirm("AC\"ME").addCommissioner("Times\" The").setPublicationDate(DATE).build();
        String expected = "\"AC\"\"ME\",\"Times\"\" The\",2021-08-02,2021-08-02,Not Available,Not Available"
                + ",Not Available,Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario with quotes in the polling firm and the commissioner.
     */
    @Test
    public void shouldExportResponseScenarioWithQuotesInPollingFirmAndCommissionerCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45")
                .setPollingFirm("AC\"ME").addCommissioner("Times\" The").setPublicationDate(DATE).build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").build();
        String expected = "\"AC\"\"ME\",\"Times\"\" The\",2021-08-02,2021-08-02,Not Available,Not Available"
                + ",Not Available,Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario with a different sample size.
     */
    @Test
    public void shouldExportAResponseScenarioWithADifferentSampleSizeCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setSampleSize("1000")
                .setPollingFirm("ACME").setPublicationDate(DATE).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43")
                .setSampleSize("999").build();
        String expected =
                "ACME,,2021-08-02,2021-08-02,Not Available,999,Provided,Not Available,1%,55%,43%" + ",Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario when the opinion poll has the correct area, and the response
     * scenario doesn't specify another one.
     */
    @Test
    public void shouldExportResponseScenarioInheritingTheSpeficiedAreaFromTheOpinionPoll() {
        OpinionPoll poll =
                new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE).setArea("N").build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "45").build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, "N", null, A_AND_B));
    }

    /**
     * Verifies that a response scenario is not exported when the opinion poll has a different area, but the response
     * scenario's area doesn't match the specified one.
     */
    @Test
    public void shouldNotExportResponseScenarioWithDifferentAreaNotMatchingTheSpecifiedArea() {
        OpinionPoll poll =
                new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE).setArea("N").build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "45").setArea("S").build();
        assertNull(EopaodCsvExporter.export(responseScenario, poll, "N", null, A_AND_B));
    }
}
