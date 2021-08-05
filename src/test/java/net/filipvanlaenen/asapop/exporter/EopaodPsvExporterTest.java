package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;

/**
 * Unit tests on the class <code>EopaodPsvExporter</code>.
 */
public class EopaodPsvExporterTest {
    /**
     * Verifies the correct export of a minimal opinion poll.
     */
    @Test
    public void shouldExportMinimalOpinionPollToEopaodPsvFormat() {
        OpinionPolls opinionPolls = OpinionPolls.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45");
        StringBuffer expected = new StringBuffer();
        expected.append("Polling firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | Other\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 55 | 45 | N/A");
        assertEquals(expected.toString(), EopaodPsvExporter.export(opinionPolls, "A", "B"));
    }

    /**
     * Verifies the correct export of an opinion poll with an alternative response scenario.
     */
    @Test
    public void shouldExportOpinionPollWithAlternativeResponseScenarioToEopaodPsvFormat() {
        OpinionPolls opinionPolls = OpinionPolls.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45", "& A:50 B:40 C:10");
        StringBuffer expected = new StringBuffer();
        expected.append("Polling firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | C | Other\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 55 | 45 | N/A | N/A\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | 1 | 50 | 40 | 10 | N/A");
        assertEquals(expected.toString(), EopaodPsvExporter.export(opinionPolls, "A", "B", "C"));
    }

     /**
      * Verifies the correct export of a simple opinion poll with only a publication date to the EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleOpinionPollWithPublicationDateOnlyCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55").addResult("B", "45").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple opinion poll with a fieldwork period to the EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleOpinionPollWithFieldworkPeriodCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                                                    .setFieldworkEnd("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "45").build();
        String expected = "ACME | N/A | 2021-08-01 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple opinion poll with a result for other to the EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleOpinionPollWithOtherResultCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55").addResult("B", "43").setOther("2").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | 2";
        assertEquals(expected, EopaodPsvExporter.export(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple opinion poll with one commissioner.
      */
     @Test
     public void shouldExportSimpleOpinionPollWithOneCommissioner() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                                                    .setPublicationDate("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "43").build();
        String expected = "ACME | The Times | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple opinion poll with two commissioners.
      */
     @Test
     public void shouldExportSimpleOpinionPollWithTwoCommissioners() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                                                    .addCommissioner("The Post").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55").addResult("B", "43").build();
        String expected = "ACME | The Post and The Times | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43"
                          + " | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple opinion poll with three commissioners.
      */
     @Test
     public void shouldExportSimpleOpinionPollWithThreeCommissioners() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                                                    .addCommissioner("The Post").addCommissioner("The Mail")
                                                    .setPublicationDate("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "43").build();
        String expected = "ACME | The Mail, The Post and The Times | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1"
                          + " | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple opinion poll with results having half of a percent for other.
      */
     @Test
     public void shouldExportSimpleOpinionPollWithAResultWithHalfAPercentOther() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55.0").addResult("B", "43").setOther("0.5")
                                                    .build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 0.5 | 55.0 | 43 | 0.5";
        assertEquals(expected, EopaodPsvExporter.export(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple opinion poll with results having a tenth of a percent.
      */
     @Test
     public void shouldExportSimpleOpinionPollWithAResultWithTenthOfAPercent() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55.4").addResult("B", "43").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 0.1 | 55.4 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple response scenario with results having half of a percent for other.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithAResultWithHalfAPercentOther() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55.0").addResult("B", "45")
                                                                          .setOther("0.5").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 0.5 | 55.0 | 45 | 0.5";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple response scenario with results having a tenth of a percent.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithAResultWithTenthOfAPercent() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55.4").addResult("B", "43")
                                                                          .build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 0.1 | 55.4 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple response scenatio for an opinion poll with only a publication date to
      * the EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithPublicationDateOnlyCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "45")
                                                                          .build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple response scenario for an opinion poll with a fieldwork period to the
      * EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithFieldworkPeriodCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                                                    .setFieldworkEnd("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "45")
                                                                          .build();
        String expected = "ACME | N/A | 2021-08-01 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple response scenatio for an opinion poll with a result for other to
      * the EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithOtherResultCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .setOther("2").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | 2";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple response scenatio for an opinion poll with the same scope to
      * the EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithSameScopeCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .setScope("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple response scenatio for an opinion poll with a different scope to
      * the EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithDifferentScopeCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .setScope("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .setScope("E").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | E | N/A | N/A | 1 | 55 | 43 | N/A";
        assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "A", "B"));
     }
}