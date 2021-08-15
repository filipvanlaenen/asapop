package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
     * Verifies the correct export of a simple opinion poll with no commissioner.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithNoCommissioner() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55").addResult("B", "43").build();
        assertNull(Exporter.exportCommissionners(poll));
    }

    /**
     * Verifies the correct export of a simple opinion poll with one commissioner.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithOneCommissioner() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                                                    .setPublicationDate("2021-08-02").addResult("A", "55")
                                                    .addResult("B", "43").build();
        assertEquals("The Times", Exporter.exportCommissionners(poll));
    }

    /**
     * Verifies the correct export of a simple opinion poll with two commissioners.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithTwoCommissioners() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                                                    .addCommissioner("The Post").setPublicationDate("2021-08-02")
                                                    .addResult("A", "55").addResult("B", "43").build();
        assertEquals("The Post and The Times", Exporter.exportCommissionners(poll));
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
        assertEquals("The Mail, The Post and The Times", Exporter.exportCommissionners(poll));
    }

    /**
     * Verifies the correct export of a simple opinion poll with results having half of a percent for other.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithAResultWithHalfAPercentOther() {
       OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                   .addResult("A", "55.0").addResult("B", "43").setOther("0.5")
                                                   .build();
       assertEquals("0.5", Exporter.calculatePrecision(poll, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with results having a tenth of a percent.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithAResultWithTenthOfAPercent() {
       OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                   .addResult("A", "55.4").addResult("B", "43").build();
       assertEquals("0.1", Exporter.calculatePrecision(poll, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario with results having half of a percent for other.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithAResultWithHalfAPercentOther() {
       ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55.0").addResult("B", "45")
                                                                         .setOther("0.5").build();
       assertEquals("0.5", Exporter.calculatePrecision(responseScenario, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario with results having a tenth of a percent.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithAResultWithTenthOfAPercent() {
       ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55.4").addResult("B", "43")
                                                                         .build();
       assertEquals("0.1", Exporter.calculatePrecision(responseScenario, "A", "B"));
    }
}
