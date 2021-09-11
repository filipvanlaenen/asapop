package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;

/**
 * Unit tests on the class <code>EopaodCsvExporter</code>.
 */
public class EopaodCsvExporterTest {
    /**
     * Verifies the correct export of a minimal opinion poll.
     */
    @Test
    public void shouldExportMinimalOpinionPoll() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45",
                                                               "A: •A:AP", "B: •A:BL").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP,BL,Other\n");
        expected.append("ACME,,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, null, "A", "B"));
    }

    /**
     * Verifies that opinion polls are sorted.
     */
    @Test
    public void shouldSortOpinionPolls() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45",
                                                               "•PF: ACME •PD: 2021-08-15 A:55 B:45",
                                                               "•PF: ACME •PD: 2021-07-28 A:55 B:45",
                                                               "A: •A:AP", "B: •A:BL").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP,BL,Other\n");
        expected.append("ACME,,2021-08-15,2021-08-15,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available\n");
        expected.append("ACME,,2021-07-28,2021-07-28,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available\n");
        expected.append("ACME,,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, null, "A", "B"));
    }

    /**
     * Verifies the correct export of an opinion poll with an alternative response scenario.
     */
    @Test
    public void shouldExportOpinionPollWithAlternativeResponseScenario() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45",
                                                               "& A:50 B:40 C:10", "A: •A:AP", "B: •A:BL", "C: •A:C")
                                                        .getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP,BL,C,Other\n");
        expected.append("ACME,,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,55%,45%,Not Available,Not Available\n");
        expected.append("ACME,,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1%,50%,40%,10%,Not Available");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, null, "A", "B", "C"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with only a publication date.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithPublicationDateOnlyCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55").addResult("B", "45").build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a fieldwork period.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithFieldworkPeriodCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                                                    .setFieldworkEnd("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "45").build();
        String expected = "ACME,,2021-08-01,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenatio for an opinion poll with the same scope.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithSameScopeCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .setScope("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .build();
        String expected = "ACME,,2021-08-02,2021-08-02,National,Not Available,Not Available"
                          + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario for an opinion poll with a different scope.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithDifferentScopeCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .setScope("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .setScope("E").build();
        String expected = "ACME,,2021-08-02,2021-08-02,European,Not Available,Not Available"
                          + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a sample size.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithSampleSizeCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55").addResult("B", "43").setSampleSize("1000")
                                                    .build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,1000,Provided"
                          + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a result for other.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                   .addResult("A", "55").addResult("B", "43").setOther("2").build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1%,55%,43%,2%";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with one commissioner.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithOneCommissioner() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                                                    .setPublicationDate("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "43").build();
        String expected = "ACME,The Times,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with two commissioners.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithTwoCommissioners() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                                                    .addCommissioner("The Post").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55").addResult("B", "43").build();
        String expected = "ACME,The Post and The Times,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with results having half of a percent for other.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithAResultWithHalfAPercentOther() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55.5").addResult("B", "43").build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,0.5%,55.5%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies that when an area is specified, an opinion poll without an area is not exported.
     */
    @Test
    public void shouldNotExportAnOpinionPollWhenTheAreaDoesNotMatch() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55.5").addResult("B", "43").build();
        assertNull(EopaodCsvExporter.export(poll, "N", "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario for an opinion poll with only a publication date.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithPublicationDateOnlyCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "45")
                                                                          .build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario for an opinion poll with a fieldwork period.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithFieldworkPeriodCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                                                    .setFieldworkEnd("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "45")
                                                                          .build();
        String expected = "ACME,,2021-08-01,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenatio for an opinion poll with a result for other.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .setOther("2").build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1%,55%,43%,2%";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with commas in the polling firm and the commissioner.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithCommasInPollingFirmAndCommissionerCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("AC,ME").addCommissioner("Times, The")
                                                    .setPublicationDate("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "45").build();
        String expected = "\"AC,ME\",\"Times, The\",2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario with commas in the polling firm and the commissioner.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithCommasInPollingFirmAndCommissionerCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("AC,ME").addCommissioner("Times, The")
                                                    .setPublicationDate("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "45").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .build();
        String expected = "\"AC,ME\",\"Times, The\",2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with quotes in the polling firm and the commissioner.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithQuotesInPollingFirmAndCommissionerCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("AC\"ME").addCommissioner("Times\" The")
                                                    .setPublicationDate("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "45").build();
        String expected = "\"AC\"\"ME\",\"Times\"\" The\",2021-08-02,2021-08-02,Not Available,Not Available"
                          + ",Not Available,Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario with quotes in the polling firm and the commissioner.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithQuotesInPollingFirmAndCommissionerCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("AC\"ME").addCommissioner("Times\" The")
                                                    .setPublicationDate("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "45").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .build();
        String expected = "\"AC\"\"ME\",\"Times\"\" The\",2021-08-02,2021-08-02,Not Available,Not Available"
                          + ",Not Available,Not Available,1%,55%,43%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario with a different sample size.
     */
    @Test
    public void shouldExportAResponseScenarioWithADifferentSampleSizeCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .setSampleSize("1000").addResult("A", "55").addResult("B", "45")
                                                    .build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().setSampleSize("999").addResult("A", "55")
                                                                          .addResult("B", "43").build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,999,Provided,Not Available,1%,55%,43%"
                          + ",Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, null, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario when the opinion poll has the correct area, and the
     * response scenario doesn't specify another one.
     */
    @Test
    public void shouldExportSimpleResponseScenarioInheritingTheSpeficiedAreaFromTheOpinionPoll() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .setArea("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "45")
                                                                          .build();
        String expected = "ACME,,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1%,55%,45%,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, "N", "A", "B"));
    }

    /**
     * Verifies that a simple response scenario is not exported when the opinion poll has a different area, but the
     * response scenario's area doesn't match the specified one.
     */
    @Test
    public void shouldNotExportSimpleResponseScenarioWithDifferentAreaNotMatchingTheSpecifiedArea() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .setArea("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "45")
                                                                          .setArea("S").build();
        assertNull(EopaodCsvExporter.export(responseScenario, poll, "N", "A", "B"));
    }
}
