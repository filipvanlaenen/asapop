package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;

/**
 * Unit tests on the class <code>EopaodPsvExporter</code>.
 */
public class EopaodPsvExporterTest {
    /**
     * Verifies the correct export of a minimal opinion poll.
     */
    @Test
    public void shouldExportMinimalOpinionPoll() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | Other\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 55 | 45 | N/A");
        assertEquals(expected.toString(), EopaodPsvExporter.export(opinionPolls, null, "A", "B"));
    }

    /**
     * Verifies that opinion polls are sorted.
     */
    @Test
    public void shouldSortOpinionPolls() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45",
                "•PF: ACME •PD: 2021-08-15 A:55 B:45", "•PF: ACME •PD: 2021-07-28 A:55 B:45").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | Other\n");
        expected.append("ACME | N/A | 2021-08-15 | 2021-08-15 | N/A | N/A | N/A | 1 | 55 | 45 | N/A\n");
        expected.append("ACME | N/A | 2021-07-28 | 2021-07-28 | N/A | N/A | N/A | 1 | 55 | 45 | N/A\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 55 | 45 | N/A");
        assertEquals(expected.toString(), EopaodPsvExporter.export(opinionPolls, null, "A", "B"));
    }

    /**
     * Verifies the correct export of an opinion poll with an alternative response scenario.
     */
    @Test
    public void shouldExportOpinionPollWithAlternativeResponseScenario() {
        OpinionPolls opinionPolls = RichOpinionPollsFile
                .parse("•PF: ACME •PD: 2021-07-27 A:55 B:45", "& A:50 B:40 C:10").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | C | Other\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 55 | 45 | N/A | N/A\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 50 | 40 | 10 | N/A");
        assertEquals(expected.toString(), EopaodPsvExporter.export(opinionPolls, null, "A", "B", "C"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with only a publication date.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithPublicationDateOnlyCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a fieldwork period.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithFieldworkPeriodCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                .setFieldworkEnd("2021-08-02").addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        String expected = "ACME | N/A | 2021-08-01 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenatio for an opinion poll with the same scope.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithSameScopeCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .setScope("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "43").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenatio for an opinion poll with a different scope.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithDifferentScopeCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .setScope("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "43").setScope("E").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | E | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a sample size.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithSampleSizeCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55").addWellformedResult("B", "43").setSampleSize("1000").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | 1000 | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a result for other.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55").addWellformedResult("B", "43").setWellformedOther("2").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | 2";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with one commissioner.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithOneCommissioner() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                .setPublicationDate("2021-08-02").addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        String expected = "ACME | The Times | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with two commissioners.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithTwoCommissioners() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                .addCommissioner("The Post").setPublicationDate("2021-08-02").addWellformedResult("A", "55")
                .addWellformedResult("B", "43").build();
        String expected = "ACME | The Post and The Times | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43"
                + " | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with results having half of a percent.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithAResultWithHalfAPercent() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55.5").addWellformedResult("B", "43").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 0.5 | 55.5 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with less than result.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithALessThanResult() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55").addWellformedResult("B", "43").addWellformedResult("C", "<1").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | 0 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, null, "A", "B", "C"));
    }

    /**
     * Verifies that an opinion poll with a different area than the one specified isn't exported.
     */
    @Test
    public void shouldNotExportSimpleOpinionPollWithOtherAreaThanTheOneSpecified() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55.5").addWellformedResult("B", "43").setArea("N").build();
        assertNull(EopaodPsvExporter.export(poll, "S", "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario for an opinion poll with only a publication date.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithPublicationDateOnlyCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "45").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario for an opinion poll with a fieldwork period.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithFieldworkPeriodCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                .setFieldworkEnd("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "45").build();
        String expected = "ACME | N/A | 2021-08-01 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenatio for an opinion poll with a result for other.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "43").setWellformedOther("2").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | 2";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario with a different sample size.
     */
    @Test
    public void shouldExportAResponseScenarioWithADifferentSampleSizeCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .setSampleSize("1000").addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().setSampleSize("999")
                .addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | 999 | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies that a response scenario inheriting the specified area is exported.
     */
    @Test
    public void shouldExportSimpleResponseScenarioInheritingTheSpecifiedArea() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .setArea("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "43").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "N", "A", "B"));
    }

    /**
     * Verifies that a response scenario overriding the area with another one than the one specified is not exported.
     */
    @Test
    public void shouldNotExportSimpleResponseScenarioOverridingWithANonSpecifiedArea() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .setArea("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "43").setArea("S").build();
        assertNull(EopaodPsvExporter.export(responseScenario, poll, "N", "A", "B"));
    }
}
