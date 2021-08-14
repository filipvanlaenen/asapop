package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.List;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;

/**
 * Unit tests on the class <code>Exporter</code>.
 */
public class ExporterTest {
    /**
     * Verifies that when only a publication date is provided, it is used as the fieldwork period.
     */
    @Test
    public void shouldUsePublicationDateAsFieldworkPeriodWhenOtherDatesAbsent() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55").addResult("B", "45").build();
        List<String> expected = List.of("2021-08-02", "2021-08-02");
        assertEquals(expected, Exporter.exportDates(poll));
    }

    /**
     * Verifies that when fieldwork start and end are provided, they are used as the fieldwork period.
     */
    @Test
    public void shouldUseFieldworkStartAndEndAsFieldworkPeriod() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                                                    .setFieldworkEnd("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "45").build();
        List<String> expected = List.of("2021-08-01", "2021-08-02");
        assertEquals(expected, Exporter.exportDates(poll));
    }

    /**
     * Verifies that when only a fieldwork end date is provided, it is used as the fieldwork period.
     */
    @Test
    public void shouldUseFieldworkEndDateAsFieldworkPeriodWhenOtherDatesAbsent() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2021-08-02")
                                                    .addResult("A", "55").addResult("B", "45").build();
        List<String> expected = List.of("2021-08-02", "2021-08-02");
        assertEquals(expected, Exporter.exportDates(poll));
    }

    /**
     * Verifies that when fieldwork start and a publication date are provided, they are used as the fieldwork period.
     */
    @Test
    public void shouldUseFieldworkStartAndPublicationDateAsFieldworkPeriodWhenFieldworkEndIsAbsent() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                                                    .setPublicationDate("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "45").build();
        List<String> expected = List.of("2021-08-01", "2021-08-02");
        assertEquals(expected, Exporter.exportDates(poll));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a result for other.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithOtherResultCorrectly() {
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
     * Verifies the correct export of a simple response scenatio for an opinion poll with only a publication date.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithPublicationDateOnlyCorrectly() {
       OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
       ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "45")
                                                                         .build();
       String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
       assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "A", "B"));
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
       String expected = "ACME | N/A | 2021-08-01 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 45 | N/A";
       assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenatio for an opinion poll with a result for other.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithOtherResultCorrectly() {
       OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
       ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                         .setOther("2").build();
       String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | 1 | 55 | 43 | 2";
       assertEquals(expected, EopaodPsvExporter.export(responseScenario, poll, "A", "B"));
    }
}
