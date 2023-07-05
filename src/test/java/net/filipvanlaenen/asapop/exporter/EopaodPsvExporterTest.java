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
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;

/**
 * Unit tests on the class <code>EopaodPsvExporter</code>.
 */
public class EopaodPsvExporterTest {
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
     * A list with the electoral lists A and B.
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
     * Verifies the correct export of a minimal opinion poll.
     */
    @Test
    public void shouldExportMinimalOpinionPoll() {
        OpinionPolls opinionPolls = RichOpinionPollsFile
                .parse("•PF: ACME •PD: 2021-07-27 A:55 B:45", "A: AA001 •A:A", "B: AA002 •A:B").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | Other\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 55 | 45 | N/A");
        assertEquals(expected.toString(), EopaodPsvExporter.export(opinionPolls, null, A_AND_B_IDS));
    }

    /**
     * Verifies the correct export of an opinion poll with combined electoral lists.
     */
    @Test
    public void shouldExportOpinionPollWithCombinedElectoralLists() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A+C+E:55 B+D:45",
                "A: AA001 •A:A", "B: AA002 •A:B", "C: AA003 •A:C", "D: AA004 •A:D", "E: AA005 •A:E").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A+C+E | B+D | Other\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 55 | 45 | N/A");
        assertEquals(expected.toString(), EopaodPsvExporter.export(opinionPolls, null,
                List.of(Set.of("AA001", "AA003", "AA005"), Set.of("AA002", "AA004"))));
    }

    /**
     * Verifies that opinion polls are sorted.
     */
    @Test
    public void shouldSortOpinionPolls() {
        OpinionPolls opinionPolls =
                RichOpinionPollsFile
                        .parse("•PF: ACME •PD: 2021-07-27 A:55 B:45", "•PF: ACME •PD: 2021-08-15 A:55 B:45",
                                "•PF: ACME •PD: 2021-07-28 A:55 B:45", "A: AA001 •A:A", "B: AA002 •A:B")
                        .getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | Other\n");
        expected.append("ACME | N/A | 2021-08-15 | 2021-08-15 | N/A | N/A | N/A | 1 | 55 | 45 | N/A\n");
        expected.append("ACME | N/A | 2021-07-28 | 2021-07-28 | N/A | N/A | N/A | 1 | 55 | 45 | N/A\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 55 | 45 | N/A");
        assertEquals(expected.toString(), EopaodPsvExporter.export(opinionPolls, null, A_AND_B_IDS));
    }

    /**
     * Verifies the correct export of an opinion poll with an alternative response scenario.
     */
    @Test
    public void shouldExportOpinionPollWithAlternativeResponseScenario() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45",
                "& A:50 B:40 C:10", "A: AA001 •A:A", "B: AA002 •A:B", "C: AA003 •A:C").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | C | Other\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 55 | 45 | N/A | N/A\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 50 | 40 | 10 | N/A");
        assertEquals(expected.toString(), EopaodPsvExporter.export(opinionPolls, null, A_AND_B_AND_C_IDS));
    }

    /**
     * Verifies the correct export of a opinion poll with only a publication date.
     */
    @Test
    public void shouldExportOpinionPollWithPublicationDateOnlyCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .setPublicationDate(DATE).build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with a fieldwork period.
     */
    @Test
    public void shouldExportOpinionPollWithFieldworkPeriodCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        String expected = "ACME | N/A | 2021-08-01 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, A_AND_B));
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
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, A_AND_B));
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
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | 90 | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, A_AND_B));
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
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | E | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with a sample size.
     */
    @Test
    public void shouldExportOpinionPollWithSampleSizeCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setPublicationDate(DATE).build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | 1000 | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with excluded responses.
     */
    @Test
    public void shouldExportOpinionPollWithExcludedResponsesCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setPublicationDate(DATE).setExcluded(DecimalNumber.parse("10")).build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | 90 | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with a result for other.
     */
    @Test
    public void shouldExportOpinionPollWithOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setOther("2")
                .setPollingFirm("ACME").setPublicationDate(DATE).build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | 2";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with no responses and a result for other.
     */
    @Test
    public void shouldExportOpinionPollWithNoResponsesAndOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "50").addResult("B", "40").setNoResponses("8")
                .setOther("2").setPollingFirm("ACME").setPublicationDate(DATE).build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | 92 | 1 | 54 | 43 | 2";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with one commissioner.
     */
    @Test
    public void shouldExportOpinionPollWithOneCommissioner() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .addCommissioner("The Times").setPublicationDate(DATE).build();
        String expected = "ACME | The Times | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with two commissioners.
     */
    @Test
    public void shouldExportOpinionPollWithTwoCommissioners() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .addCommissioner("The Times").addCommissioner("The Post").setPublicationDate(DATE).build();
        String expected =
                "ACME | The Post and The Times | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43" + " | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with a polling firm and a polling firm partner.
     */
    @Test
    public void shouldExportOpinionPollWithPollingFirmAndPollingFirmPartner() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setPollingFirmPartner("EMCA").setPublicationDate(DATE).build();
        String expected = "ACME and EMCA | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with results having half of a percent.
     */
    @Test
    public void shouldExportOpinionPollWithAResultWithHalfAPercent() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55.5").addResult("B", "43")
                .setPollingFirm("ACME").setPublicationDate(DATE).build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 0.5 | 55.5 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with less than result.
     */
    @Test
    public void shouldExportOpinionPollWithALessThanResult() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").addResult("C", "<1")
                .setPollingFirm("ACME").setPublicationDate(DATE).build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | 0 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, A_AND_B_AND_C));
    }

    /**
     * Verifies that an opinion poll with a different area than the one specified isn't exported.
     */
    @Test
    public void shouldNotExportOpinionPollWithOtherAreaThanTheOneSpecified() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55.5").addResult("B", "43")
                .setPollingFirm("ACME").setPublicationDate(DATE).setArea("N").build();
        assertNull(EopaodPsvExporter.export(poll, "S", A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario for an opinion poll with only a publication date.
     */
    @Test
    public void shouldExportResponseScenarioWithPublicationDateOnlyCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE).build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "45").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, A_AND_B));
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
        String expected = "ACME | N/A | 2021-08-01 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario for an opinion poll with a result for other.
     */
    @Test
    public void shouldExportResponseScenarioWithOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE).build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").setOther("2").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | 2";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, A_AND_B));
    }

    /**
     * Verifies the correct export of a response scenario for an opinion poll with no responses and a result for other.
     */
    @Test
    public void shouldExportResponseScenarioWithNoResponsesAndOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "50").addResult("B", "40")
                .setNoResponses("8").setOther("2").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | 92 | 1 | 54 | 43 | 2";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, A_AND_B));
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
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | 999 | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, A_AND_B));
    }

    /**
     * Verifies that a response scenario inheriting the specified area is exported.
     */
    @Test
    public void shouldExportResponseScenarioInheritingTheSpecifiedArea() {
        OpinionPoll poll =
                new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE).setArea("N").build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "N", A_AND_B));
    }

    /**
     * Verifies that a response scenario overriding the area with another one than the one specified is not exported.
     */
    @Test
    public void shouldNotExportSimpleResponseScenarioOverridingWithANonSpecifiedArea() {
        OpinionPoll poll =
                new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE).setArea("N").build();
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").setArea("S").build();
        assertNull(EopaodPsvExporter.export(responseScenario, poll, "N", A_AND_B));
    }
}
