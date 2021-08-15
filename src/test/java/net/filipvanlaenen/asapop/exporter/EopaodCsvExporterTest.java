package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void shouldExportMinimalOpinionPollToEopaodCsvFormat() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45",
                                                               "A: •A:AP", "B: •A:BL").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP,BL,Other\n");
        expected.append("ACME,Not Available,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1,55,45,Not Available");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, "A", "B"));
    }

    /**
     * Verifies the correct export of an opinion poll with an alternative response scenario.
     */
    @Test
    public void shouldExportOpinionPollWithAlternativeResponseScenarioToEopaodPsvFormat() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45",
                                                               "& A:50 B:40 C:10", "A: •A:AP", "B: •A:BL", "C: •A:C")
                                                        .getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP,BL,C,Other\n");
        expected.append("ACME,Not Available,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1,55,45,Not Available,Not Available\n");
        expected.append("ACME,Not Available,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1,50,40,10,Not Available");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, "A", "B", "C"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with only a publication date.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithPublicationDateOnlyCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55").addResult("B", "45").build();
        String expected = "ACME,Not Available,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1,55,45,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a fieldwork period.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithFieldworkPeriodCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                                                    .setFieldworkEnd("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "45").build();
        String expected = "ACME,Not Available,2021-08-01,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1,55,45,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(poll, "A", "B"));
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
        String expected = "ACME,Not Available,2021-08-02,2021-08-02,National,Not Available,Not Available"
                          + ",Not Available,1,55,43,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenatio for an opinion poll with a different scope.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithDifferentScopeCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .setScope("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .setScope("E").build();
        String expected = "ACME,Not Available,2021-08-02,2021-08-02,European,Not Available,Not Available"
                          + ",Not Available,1,55,43,Not Available";
        assertEquals(expected, EopaodCsvExporter.export(responseScenario, poll, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a result for other.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithOtherResultCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                   .addResult("A", "55").addResult("B", "43").setOther("2").build();
        String expected = "ACME,Not Available,2021-08-02,2021-08-02,Not Available,Not Available,Not Available"
                          + ",Not Available,1,55,43,2";
        assertEquals(expected, EopaodCsvExporter.export(poll, "A", "B"));
    }
}
