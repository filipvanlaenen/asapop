package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.ResponseScenario;

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
                .addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        List<String> expected = List.of("2021-08-02", "2021-08-02");
        assertEquals(expected, Exporter.exportDates(poll));
    }

    /**
     * Verifies that when fieldwork start and end are provided, they are used as the fieldwork period.
     */
    @Test
    public void shouldUseFieldworkStartAndEndAsFieldworkPeriod() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                .setFieldworkEnd("2021-08-02").addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        List<String> expected = List.of("2021-08-01", "2021-08-02");
        assertEquals(expected, Exporter.exportDates(poll));
    }

    /**
     * Verifies that when only a fieldwork end date is provided, it is used as the fieldwork period.
     */
    @Test
    public void shouldUseFieldworkEndDateAsFieldworkPeriodWhenOtherDatesAbsent() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2021-08-02")
                .addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        List<String> expected = List.of("2021-08-02", "2021-08-02");
        assertEquals(expected, Exporter.exportDates(poll));
    }

    /**
     * Verifies that when fieldwork start and a publication date are provided, they are used as the fieldwork period.
     */
    @Test
    public void shouldUseFieldworkStartAndPublicationDateAsFieldworkPeriodWhenFieldworkEndIsAbsent() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                .setPublicationDate("2021-08-02").addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        List<String> expected = List.of("2021-08-01", "2021-08-02");
        assertEquals(expected, Exporter.exportDates(poll));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a polling firm without a polling firm partner.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithPollingFirmWithoutPollingFirmPartner() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        assertEquals("ACME", Exporter.exportPollingFirms(poll));
    }

    /**
     * Verifies the correct export of a simple opinion poll with a polling firm and a polling firm partner.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithPollingFirmAndPollingFirmPartner() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPollingFirmPartner("EMCA")
                .setPublicationDate("2021-08-02").addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        assertEquals("ACME and EMCA", Exporter.exportPollingFirms(poll));
    }

    /**
     * Verifies the correct export of a simple opinion poll with no commissioner.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithNoCommissioner() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        assertNull(Exporter.exportCommissioners(poll));
    }

    /**
     * Verifies the correct export of a simple opinion poll with one commissioner.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithOneCommissioner() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                .setPublicationDate("2021-08-02").addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        assertEquals("The Times", Exporter.exportCommissioners(poll));
    }

    /**
     * Verifies the correct export of a simple opinion poll with two commissioners.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithTwoCommissioners() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                .addCommissioner("The Post").setPublicationDate("2021-08-02").addWellformedResult("A", "55")
                .addWellformedResult("B", "43").build();
        assertEquals("The Post and The Times", Exporter.exportCommissioners(poll));
    }

    /**
     * Verifies the correct export of a simple opinion poll with three commissioners.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithThreeCommissioners() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                .addCommissioner("The Post").addCommissioner("The Mail").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55").addWellformedResult("B", "43").build();
        assertEquals("The Mail, The Post and The Times", Exporter.exportCommissioners(poll));
    }

    /**
     * Verifies the correct export of a simple opinion poll with results having half of a percent for other.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithAResultWithHalfAPercentOther() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55.0").addWellformedResult("B", "43").setWellformedOther("0.5").build();
        assertEquals("0.5", Exporter.calculatePrecision(poll, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple opinion poll with results having a tenth of a percent.
     */
    @Test
    public void shouldExportSimpleOpinionPollWithAResultWithTenthOfAPercent() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        assertEquals("0.1", Exporter.calculatePrecision(poll, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario with results having half of a percent for other.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithAResultWithHalfAPercentOther() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().addWellformedResult("A", "55.0")
                .addWellformedResult("B", "45").setWellformedOther("0.5").build();
        assertEquals("0.5", Exporter.calculatePrecision(responseScenario, "A", "B"));
    }

    /**
     * Verifies the correct export of a simple response scenario with results having a tenth of a percent.
     */
    @Test
    public void shouldExportSimpleResponseScenarioWithAResultWithTenthOfAPercent() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().addWellformedResult("A", "55.4")
                .addWellformedResult("B", "43").build();
        assertEquals("0.1", Exporter.calculatePrecision(responseScenario, "A", "B"));
    }

    /**
     * Verifies that two opinion polls with publication dates are compared correctly.
     */
    @Test
    public void shouldCompareOpinionPollsWithPublicationDatesOnlyCorrectly() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-03")
                .addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        assertTrue(Exporter.compareOpinionPolls(poll1, poll2) > 0);
    }

    /**
     * Verifies that two opinion polls with fieldwork end dates are compared correctly.
     */
    @Test
    public void shouldCompareOpinionPollsWithFieldworkEndDatesOnlyCorrectly() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2021-08-02")
                .addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2021-08-03")
                .addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        assertTrue(Exporter.compareOpinionPolls(poll1, poll2) > 0);
    }

    /**
     * Verifies that when two opinion polls have the same fieldwork end dates, they are compared using the start dates.
     */
    @Test
    public void shouldCompareOpinionPollsWithEqualFieldworkEndDatesByFieldworkStartDatesCorrectly() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-07-20")
                .setFieldworkEnd("2021-08-02").addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-07-21")
                .setFieldworkEnd("2021-08-02").addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        assertTrue(Exporter.compareOpinionPolls(poll1, poll2) > 0);
    }

    /**
     * Verifies that when two opinion polls have the same fieldwork end dates, the one missing the fieldwork start date
     * reuses the fieldwork end date.
     */
    @Test
    public void shouldUseEndDateAsStartDateWhenComparingOpinionPollsWithEqualFieldworkEndDatesIfFieldworkStartAbsent() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2021-08-02")
                .addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-07-21")
                .setFieldworkEnd("2021-08-02").addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        assertTrue(Exporter.compareOpinionPolls(poll1, poll2) < 0);
    }

    /**
     * Verifies that when two opinion polls have the same start and end dates, they are compared for sorting based on
     * the sample size value.
     */
    @Test
    public void shouldUseSampleSizeValueForComparisonOfOpinionPollsWhenStartAndEndDatesAreEqual() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2021-08-02")
                .setSampleSize("1000").addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2021-08-02")
                .setSampleSize("999").addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        assertTrue(Exporter.compareOpinionPolls(poll1, poll2) < 0);
    }

    /**
     * Verifies that the exporter can sort a set of opinion polls into a sorted list.
     */
    @Test
    public void shouldSortASetOfOpinionPollsIntoAList() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-03")
                .addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        OpinionPoll poll3 = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-04")
                .addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        OpinionPoll poll4 = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-05")
                .addWellformedResult("A", "55.4").addWellformedResult("B", "43").build();
        List<OpinionPoll> expected = List.of(poll4, poll3, poll2, poll1);
        assertEquals(expected, Exporter.sortOpinionPolls(Set.of(poll1, poll2, poll3, poll4)));
    }

    /**
     * Verifies that if the specified area is <code>null</code>, any area matches.
     */
    @Test
    public void areaShouldMatchWhenSpecifiedAreaIsNull() {
        assertTrue(Exporter.areaMatches(null, "N"));
    }

    /**
     * Verifies that if the specified area is <code>--</code>, <code>null</code> as area matches.
     */
    @Test
    public void areaShouldMatchWhenSpecifiedAreaIsTwoDashesAndProvidedAreaIsNull() {
        assertTrue(Exporter.areaMatches("--", null));
    }

    /**
     * Verifies that if the specified area is <code>--</code>, any area doesn't match.
     */
    @Test
    public void areaShouldNotMatchWhenSpecifiedAreaIsTwoDashesAndProvidedAreaIsNotNull() {
        assertFalse(Exporter.areaMatches("--", "N"));
    }

    /**
     * Verifies that the area matches if it is equal to the specified area.
     */
    @Test
    public void areaShouldMatchWhenEqualToSpecifiedArea() {
        assertTrue(Exporter.areaMatches("N", "N"));
    }

    /**
     * Verifies that the area doesn't match is isn't equal to the specified area.
     */
    @Test
    public void areaShouldNotMatchWhenNotEqualToSpecifiedArea() {
        assertFalse(Exporter.areaMatches("S", "N"));
    }

    /**
     * Verifies that <code>secondIfFirstNull</code> returns the first string if it isn't <code>null</code>.
     */
    @Test
    public void shouldReturnFirstStringIfItIsNotNull() {
        assertEquals("A", Exporter.secondIfFirstNull("A", "B"));
    }

    /**
     * Verifies that <code>secondIfFirstNull</code> returns the second string if the first string is <code>null</code>.
     */
    @Test
    public void shouldReturnSecondStringIfTheFirstStringIsNotNull() {
        assertEquals("B", Exporter.secondIfFirstNull(null, "B"));
    }
}
