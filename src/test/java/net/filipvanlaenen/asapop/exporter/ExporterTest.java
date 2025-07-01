package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.DateMonthOrYear;
import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPollTestBuilder;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResponseScenarioTestBuilder;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Unit tests on the class <code>Exporter</code>.
 */
public class ExporterTest {
    /**
     * A date for the unit tests.
     */
    private static final LocalDate DATE1 = LocalDate.parse("2021-08-02");
    /**
     * Another date for the unit tests.
     */
    private static final LocalDate DATE2 = LocalDate.parse("2021-08-03");
    /**
     * A third date for the unit tests.
     */
    private static final LocalDate DATE3 = LocalDate.parse("2021-08-04");
    /**
     * A date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH1 = DateMonthOrYear.parse("2021-08-01");
    /**
     * Another date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH2 = DateMonthOrYear.parse("2021-08-02");
    /**
     * A third date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH3 = DateMonthOrYear.parse("2021-08-03");
    /**
     * A fourth date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH4 = DateMonthOrYear.parse("2021-08-05");
    /**
     * A fifth date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH5 = DateMonthOrYear.parse("2021-08-06");
    /**
     * A list with the electoral lists A and B.
     */
    private static final OrderedCollection<Set<String>> A_AND_B = OrderedCollection.of(Set.of("A"), Set.of("B"));

    /**
     * Verifies that when only a publication date is provided, it is used as the fieldwork period.
     */
    @Test
    public void shouldUsePublicationDateAsFieldworkPeriodWhenOtherDatesAbsent() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .setPublicationDate(DATE1).build();
        OrderedCollection<String> expected = OrderedCollection.of("2021-08-02", "2021-08-02");
        assertTrue(Exporter.exportDates(poll).containsSame(expected));
    }

    /**
     * Verifies that when fieldwork start and end are provided, they are used as the fieldwork period.
     */
    @Test
    public void shouldUseFieldworkStartAndEndAsFieldworkPeriod() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        OrderedCollection<String> expected = OrderedCollection.of("2021-08-01", "2021-08-02");
        assertTrue(Exporter.exportDates(poll).containsSame(expected));
    }

    /**
     * Verifies that when only a fieldwork end date is provided, it is used as the fieldwork period.
     */
    @Test
    public void shouldUseFieldworkEndDateAsFieldworkPeriodWhenOtherDatesAbsent() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .setFieldworkEnd(DATE_OR_MONTH2).build();
        OrderedCollection<String> expected = OrderedCollection.of("2021-08-02", "2021-08-02");
        assertTrue(Exporter.exportDates(poll).containsSame(expected));
    }

    /**
     * Verifies that when fieldwork start and a publication date are provided, they are used as the fieldwork period.
     */
    @Test
    public void shouldUseFieldworkStartAndPublicationDateAsFieldworkPeriodWhenFieldworkEndIsAbsent() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setPublicationDate(DATE1).build();
        OrderedCollection<String> expected = OrderedCollection.of("2021-08-01", "2021-08-02");
        assertTrue(Exporter.exportDates(poll).containsSame(expected));
    }

    /**
     * Verifies the correct export of a opinion poll with a polling firm without a polling firm partner.
     */
    @Test
    public void shouldExportOpinionPollWithPollingFirmWithoutPollingFirmPartner() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setPublicationDate(DATE1).build();
        assertEquals("ACME", Exporter.exportPollingFirms(poll));
    }

    /**
     * Verifies the correct export of a opinion poll with a polling firm and a polling firm partner.
     */
    @Test
    public void shouldExportOpinionPollWithPollingFirmAndPollingFirmPartner() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setPollingFirmPartner("EMCA").setPublicationDate(DATE1).build();
        assertEquals("ACME and EMCA", Exporter.exportPollingFirms(poll));
    }

    /**
     * Verifies the correct export of a opinion poll with no commissioner.
     */
    @Test
    public void shouldExportOpinionPollWithNoCommissioner() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setPublicationDate(DATE1).build();
        assertNull(Exporter.exportCommissioners(poll));
    }

    /**
     * Verifies the correct export of a opinion poll with one commissioner.
     */
    @Test
    public void shouldExportOpinionPollWithOneCommissioner() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .addCommissioner("The Times").setPublicationDate(DATE1).build();
        assertEquals("The Times", Exporter.exportCommissioners(poll));
    }

    /**
     * Verifies the correct export of a opinion poll with two commissioners.
     */
    @Test
    public void shouldExportOpinionPollWithTwoCommissioners() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .addCommissioner("The Times").addCommissioner("The Post").setPublicationDate(DATE1).build();
        assertEquals("The Post and The Times", Exporter.exportCommissioners(poll));
    }

    /**
     * Verifies the correct export of a opinion poll with three commissioners.
     */
    @Test
    public void shouldExportOpinionPollWithThreeCommissioners() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .addCommissioner("The Times").addCommissioner("The Post").addCommissioner("The Mail")
                .setPublicationDate(DATE1).build();
        assertEquals("The Mail, The Post and The Times", Exporter.exportCommissioners(poll));
    }

    /**
     * Verifies the correct export of a opinion poll with results having half of a percent for other.
     */
    @Test
    public void shouldExportOpinionPollWithAResultWithHalfAPercentOther() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55.0").addResult("B", "43").setOther("0.5")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(ResultValue.Precision.HALF, Exporter.calculatePrecision(poll, A_AND_B));
    }

    /**
     * Verifies the correct export of a opinion poll with results having a tenth of a percent.
     */
    @Test
    public void shouldExportOpinionPollWithAResultWithTenthOfAPercent() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals(ResultValue.Precision.TENTH, Exporter.calculatePrecision(poll, A_AND_B));
    }

    /**
     * Verifies the calculation of a precision of half a percent for a selection of results in a response scenario.
     */
    @Test
    public void shouldCalculatePrecisionOfHalfAPercentForSelectedResultsInAResponseScenario() {
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55.0").addResult("B", "45").setOther("0.5").build();
        assertEquals(ResultValue.Precision.HALF, Exporter.calculatePrecision(responseScenario, A_AND_B));
    }

    /**
     * Verifies the calculation of a precision of half a percent for a response scenario.
     */
    @Test
    public void shouldCalculatePrecisionOfHalfAPercentForAResponseScenario() {
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55.0").addResult("B", "45").setOther("0.5").build();
        assertEquals(ResultValue.Precision.HALF, Exporter.calculatePrecision(responseScenario));
    }

    /**
     * Verifies the correct export of a response scenario with results having a tenth of a percent.
     */
    @Test
    public void shouldExportResponseScenarioWithAResultWithTenthOfAPercent() {
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55.4").addResult("B", "43").build();
        assertEquals(ResultValue.Precision.TENTH, Exporter.calculatePrecision(responseScenario, A_AND_B));
    }

    /**
     * Verifies that two opinion polls with publication dates are compared correctly.
     */
    @Test
    public void shouldCompareOpinionPollsWithPublicationDatesOnlyCorrectly() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setPublicationDate(DATE2).build();
        assertTrue(Exporter.compareOpinionPolls(poll1, poll2) > 0);
    }

    /**
     * Verifies that two opinion polls with fieldwork end dates are compared correctly.
     */
    @Test
    public void shouldCompareOpinionPollsWithFieldworkEndDatesOnlyCorrectly() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH3).build();
        assertTrue(Exporter.compareOpinionPolls(poll1, poll2) > 0);
    }

    /**
     * Verifies that when two opinion polls have the same fieldwork end dates, they are compared using the start dates.
     */
    @Test
    public void shouldCompareOpinionPollsWithEqualFieldworkEndDatesByFieldworkStartDatesCorrectly() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH3).build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH2).setFieldworkEnd(DATE_OR_MONTH3).build();
        assertTrue(Exporter.compareOpinionPolls(poll1, poll2) > 0);
    }

    /**
     * Verifies that when two opinion polls have the same fieldwork end dates, the one missing the fieldwork start date
     * reuses the fieldwork end date.
     */
    @Test
    public void shouldUseEndDateAsStartDateWhenComparingOpinionPollsWithEqualFieldworkEndDatesIfFieldworkStartAbsent() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        assertTrue(Exporter.compareOpinionPolls(poll1, poll2) < 0);
    }

    /**
     * Verifies that when two opinion polls have the same start and end dates, they are compared for sorting based on
     * the sample size value.
     */
    @Test
    public void shouldUseSampleSizeValueForComparisonOfOpinionPollsWhenStartAndEndDatesAreEqual() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setSampleSize("1000").setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setSampleSize("999").setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        assertTrue(Exporter.compareOpinionPolls(poll1, poll2) < 0);
    }

    /**
     * Verifies that when two opinion polls have the same start and end dates and sample size, they are compared for
     * sorting based on the polling firm name.
     */
    @Test
    public void shouldUsePollingFirmForComparisonOfOpinionPollsWhenStartAndEndDatesAndSampleSizeAreEqual() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setSampleSize("1000").setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setSampleSize("1000").setPollingFirm("BCME").setFieldworkEnd(DATE_OR_MONTH2).build();
        OpinionPoll poll3 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setSampleSize("1000").addCommissioner("The Times").setFieldworkEnd(DATE_OR_MONTH2).build();
        assertTrue(Exporter.compareOpinionPolls(poll1, poll2) < 0);
        assertTrue(Exporter.compareOpinionPolls(poll1, poll3) < 0);
        assertTrue(Exporter.compareOpinionPolls(poll3, poll1) > 0);
        assertTrue(Exporter.compareOpinionPolls(poll3, poll3) == 0);
    }

    /**
     * Verifies that the exporter can sort a set of opinion polls into a sorted list.
     */
    @Test
    public void shouldSortASetOfOpinionPollsIntoAList() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH3).build();
        OpinionPoll poll3 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setPollingFirm("ACME").setPublicationDate(DATE3).build();
        OpinionPoll poll4 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setSampleSize("800").setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH4).build();
        OpinionPoll poll5 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setSampleSize("1000").setPollingFirm("BCME").setFieldworkEnd(DATE_OR_MONTH4).build();
        OpinionPoll poll6 = new OpinionPollTestBuilder().addResult("A", "55.4").addResult("B", "43")
                .setSampleSize("1000").setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH5).build();
        List<OpinionPoll> expected = List.of(poll6, poll5, poll4, poll3, poll2, poll1);
        assertEquals(expected, Exporter.sortOpinionPolls(Set.of(poll1, poll2, poll3, poll4, poll5, poll6)));
    }

    /**
     * Verifies that if the specified area is <code>null</code>, any area matches.
     */
    @Test
    public void areaShouldMatchWhenSpecifiedAreaIsNull() {
        assertTrue(Exporter.areaMatches(null, null, "N"));
    }

    /**
     * Verifies that if the specified area is <code>--</code>, <code>null</code> as area matches.
     */
    @Test
    public void areaShouldMatchWhenSpecifiedAreaIsTwoDashesAndProvidedAreaIsNull() {
        assertTrue(Exporter.areaMatches("--", null, null));
    }

    /**
     * Verifies that if the specified area is <code>--</code>, any area doesn't match.
     */
    @Test
    public void areaShouldNotMatchWhenSpecifiedAreaIsTwoDashesAndProvidedAreaIsNotNull() {
        assertFalse(Exporter.areaMatches("--", null, "N"));
    }

    /**
     * Verifies that the area matches if it is equal to the specified area.
     */
    @Test
    public void areaShouldMatchWhenEqualToSpecifiedArea() {
        assertTrue(Exporter.areaMatches("N", null, "N"));
    }

    /**
     * Verifies that the area doesn't match is isn't equal to the specified area.
     */
    @Test
    public void areaShouldNotMatchWhenNotEqualToSpecifiedArea() {
        assertFalse(Exporter.areaMatches("S", null, "N"));
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

    /**
     * Verifies that no participation rate is exported when excluded is missing.
     */
    @Test
    public void shouldExportNoParticipationRateWhenExcludedIsMissing() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setPublicationDate(DATE1).build();
        assertNull(Exporter.exportParticipationRate(poll.getMainResponseScenario(), poll));
    }

    /**
     * Verifies that correct participation rate is exported according to excluded.
     */
    @Test
    public void shouldExportParticipationRateCorrectlyForExcluded() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setPublicationDate(DATE1).setExcluded(DecimalNumber.parse("10")).build();
        assertEquals("90", Exporter.exportParticipationRate(poll.getMainResponseScenario(), poll));
    }

    /**
     * Verifies that participation rate from the opinion poll is exported if the response scenario doesn't have
     * excluded.
     */
    @Test
    public void shouldExportParticipationRateFromOpinionPollForExcluded() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setPublicationDate(DATE1).setExcluded(DecimalNumber.parse("10")).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "45").build();
        poll.addAlternativeResponseScenario(responseScenario);
        assertEquals("90", Exporter.exportParticipationRate(responseScenario, poll));
    }

    /**
     * Verifies that correct participation rate is exported according to no responses.
     */
    @Test
    public void shouldExportParticipationRateCorrectlyForNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setNoResponses("10")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        assertEquals("90", Exporter.exportParticipationRate(poll.getMainResponseScenario(), poll));
    }

    /**
     * Verifies that correct participation rate is exported according to no responses from the opinion poll.
     */
    @Test
    public void shouldExportParticipationRateFromOpinionPollForNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setNoResponses("10")
                .setPollingFirm("ACME").setPublicationDate(DATE1).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "45").build();
        poll.addAlternativeResponseScenario(responseScenario);
        assertEquals("90", Exporter.exportParticipationRate(responseScenario, poll));
    }

    /**
     * Verifies that correct participation rate is exported according to no responses even if excluded is also present.
     */
    @Test
    public void shouldExportParticipationRateCorrectlyForNoResponsesEvenIfExcludedIsPresent() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setNoResponses("10")
                .setPollingFirm("ACME").setPublicationDate(DATE1).setExcluded(DecimalNumber.parse("15")).build();
        assertEquals("90", Exporter.exportParticipationRate(poll.getMainResponseScenario(), poll));
    }

    /**
     * Verifies that an ID for an electoral list is converted to its abbreviation.
     */
    @Test
    public void shouldConvertElectoralListIdToAbbreviation() {
        ElectoralList.get("AA011").setAbbreviation("A");
        assertEquals("A", Exporter.electoralListIdsToAbbreviations(Set.of("AA011")));
    }

    /**
     * Verifies that an ID for an electoral list is converted to its romanized abbreviation.
     */
    @Test
    public void shouldConvertElectoralListIdToRomanizedAbbreviation() {
        ElectoralList.get("AA009").setRomanizedAbbreviation("B");
        assertEquals("B", Exporter.electoralListIdsToAbbreviations(Set.of("AA009")));
    }

    /**
     * Verifies that IDs for electoral lists are converted to their abbreviations.
     */
    @Test
    public void shouldConvertElectoralListIdsToAbbreviations() {
        ElectoralList.get("AA021").setAbbreviation("A");
        ElectoralList.get("AA022").setAbbreviation("B");
        ElectoralList.get("AA023").setAbbreviation("C");
        ElectoralList.get("AA024").setAbbreviation("D");
        ElectoralList.get("AA025").setAbbreviation("E");
        ElectoralList.get("AA026").setAbbreviation("F");
        assertEquals("A+B+C+D+E+F",
                Exporter.electoralListIdsToAbbreviations(Set.of("AA021", "AA022", "AA023", "AA024", "AA025", "AA026")));
    }
}
