package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.SampleSize.ExactSampleSize;

/**
 * Unit tests on the class <code>OpinionPoll</code>.
 */
public class OpinionPollTest {
    /**
     * The magic number 0.88.
     */
    private static final double SCALE088 = 0.88D;
    /**
     * The magic number 1000.
     */
    private static final int ONE_THOUSAND = 1000;
    /**
     * The magic number 1250.
     */
    private static final int ONE_THOUSAND_TWO_HUNDRED_FIFTY = 1250;
    /**
     * A date for the unit tests.
     */
    private static final LocalDate DATE1 = LocalDate.parse("2021-07-29");
    /**
     * Another date for the unit tests.
     */
    private static final LocalDate DATE2 = LocalDate.parse("2021-07-30");
    /**
     * A date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH1 = DateMonthOrYear.parse("2021-07-27");
    /**
     * Another date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH2 = DateMonthOrYear.parse("2021-07-28");
    /**
     * A sample size of 800.
     */
    private static final ExactSampleSize SAMPLE_SIZE_EIGHT_HUNDRED = new ExactSampleSize(800);
    /**
     * A sample size of 1000.
     */
    private static final ExactSampleSize SAMPLE_SIZE_ONE_THOUSAND = new ExactSampleSize(1000);

    /**
     * Verifies that the addCommissioner method in the builder class is wired correctly to the getCommissioners method.
     */
    @Test
    public void addCommissionerInBuilderShouldBeWiredCorrectlyToGetCommissioners() {
        OpinionPoll poll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        Set<String> expected = new HashSet<String>();
        expected.add("The Times");
        assertEquals(expected, poll.getCommissioners());
    }

    /**
     * Verifies that after a commissioner has been added, the builder responds that there is a polling firm or
     * commissioner present.
     */
    @Test
    public void hasPollingFirmOrCommissionerInBuilderShouldReturnTrueAfterCommissionerIsAdded() {
        assertTrue(new OpinionPoll.Builder().addCommissioner("The Times").hasPollingFirmOrCommissioner());
    }

    /**
     * Verifies that the setArea method in the builder class is wired correctly to the getArea method.
     */
    @Test
    public void setAreaInBuilderShouldBeWiredCorrectlyToGetArea() {
        OpinionPoll poll = new OpinionPoll.Builder().setArea("AB").build();
        assertEquals("AB", poll.getArea());
    }

    /**
     * Verifies that before an area has been added, the builder responds that the area is missing.
     */
    @Test
    public void hasAreaInBuilderShouldReturnFalseBeforeAreaIsAdded() {
        assertFalse(new OpinionPoll.Builder().hasArea());
    }

    /**
     * Verifies that after an area has been added, the builder responds that the area is present.
     */
    @Test
    public void hasAreaInBuilderShouldReturnTrueAfterAreaIsAdded() {
        assertTrue(new OpinionPoll.Builder().setArea("A").hasArea());
    }

    /**
     * Verifies that before a date has been added, the builder responds that the dates are missing.
     */
    @Test
    public void hasDatesInBuilderShouldReturnFalseBeforeDatesAreAdded() {
        assertFalse(new OpinionPoll.Builder().hasDates());
    }

    /**
     * Verifies that after instantiation, the builder responds that the polling firm and the commissioner are missing.
     */
    @Test
    public void hasPollingFirmOrCommissionerInBuilderShouldReturnFalseAfterInstantiation() {
        assertFalse(new OpinionPoll.Builder().hasPollingFirmOrCommissioner());
    }

    /**
     * Verifies that the setFieldworkEnd method in the builder class is wired correctly to the getFieldworkEnd method.
     */
    @Test
    public void setFieldworkEndInBuilderShouldBeWiredCorrectlyToGetFieldworkEnd() {
        OpinionPoll poll = new OpinionPoll.Builder().setFieldworkEnd(DATE_OR_MONTH1).build();
        assertEquals(DATE_OR_MONTH1, poll.getFieldworkEnd());
    }

    /**
     * Verifies that after a fieldwork end date has been added, the builder responds that there are dates present.
     */
    @Test
    public void hasDatesInBuilderShouldReturnTrueAfterFieldworkEndIsAdded() {
        assertTrue(new OpinionPoll.Builder().setFieldworkEnd(DATE_OR_MONTH1).hasDates());
    }

    /**
     * Verifies that the setFieldworkStart method in the builder class is wired correctly to the getFieldworkStart
     * method.
     */
    @Test
    public void setFieldworkStartInBuilderShouldBeWiredCorrectlyToGetFieldworkStart() {
        OpinionPoll poll = new OpinionPoll.Builder().setFieldworkStart(DATE_OR_MONTH1).build();
        assertEquals(DATE_OR_MONTH1, poll.getFieldworkStart());
    }

    /**
     * Verifies that after a fieldwork start date has been added, the builder responds that there are dates present.
     */
    @Test
    public void hasDatesInBuilderShouldReturnTrueAfterFieldworkStartIsAdded() {
        assertTrue(new OpinionPoll.Builder().setFieldworkStart(DATE_OR_MONTH1).hasDates());
    }

    /**
     * Verifies that getEndDate returns the end date of the fieldword end date if it has been set.
     */
    @Test
    public void getEndDateShouldReturnEndDateOfFieldworkEnd() {
        OpinionPoll poll = new OpinionPoll.Builder().setFieldworkEnd(DATE_OR_MONTH1).build();
        assertEquals("2021-07-27", poll.getEndDate().toString());
    }

    /**
     * Verifies that getEndDate returns the end date of the fieldword end date even if the publication date is set.
     */
    @Test
    public void getEndDateShouldReturnEndDateOfFieldworkEndEvenIfThereIsAPublicationDate() {
        OpinionPoll poll = new OpinionPoll.Builder().setFieldworkEnd(DATE_OR_MONTH1).setPublicationDate(DATE1).build();
        assertEquals("2021-07-27", poll.getEndDate().toString());
    }

    /**
     * Verifies that getEndDate returns the publication date if the fieldwork end date is absent.
     */
    @Test
    public void getEndDateShouldReturnPublicationDateIfFieldworkEndIsAbsent() {
        OpinionPoll poll = new OpinionPoll.Builder().setPublicationDate(DATE1).build();
        assertEquals(DATE1, poll.getEndDate());
    }

    /**
     * Verifies that the setOther method in the builder class is wired correctly to the getOther method.
     */
    @Test
    public void setOtherInBuilderShouldBeWiredCorrectlyToGetOther() {
        OpinionPoll poll = new OpinionPollTestBuilder().setOther(new ResultValue("2")).build();
        assertEquals("2", poll.getOther().getText());
    }

    /**
     * Verifies that before other has been added, the builder responds that other is missing.
     */
    @Test
    public void hasOtherInBuilderShouldReturnFalseBeforeOtherIsAdded() {
        assertFalse(new OpinionPoll.Builder().hasOther());
    }

    /**
     * Verifies that after other has been added, the builder responds that other is present.
     */
    @Test
    public void hasOtherInBuilderShouldReturnTrueAfterOtherIsAdded() {
        assertTrue(new OpinionPollTestBuilder().setOther("12").hasOther());
    }

    /**
     * Verifies that the setNoResponses method in the builder class is wired correctly to the getNoResponses method.
     */
    @Test
    public void setNoResponsesInBuilderShouldBeWiredCorrectlyToGetNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().setNoResponses(new ResultValue("2")).build();
        assertEquals("2", poll.getNoResponses().getText());
    }

    /**
     * Verifies that before no responses has been added, the builder responds that no responses is missing.
     */
    @Test
    public void hasNoResponsesInBuilderShouldReturnFalseBeforeNoResponsesIsAdded() {
        assertFalse(new OpinionPoll.Builder().hasNoResponses());
    }

    /**
     * Verifies that after no responses has been added, the builder responds that no responses is present.
     */
    @Test
    public void hasNoResponsesInBuilderShouldReturnTrueAfterNoResponsesIsAdded() {
        assertTrue(new OpinionPollTestBuilder().setNoResponses("12").hasNoResponses());
    }

    /**
     * Verifies that the setVerifiedSum method in the builder class is wired correctly to the getVerifiedSum method.
     */
    @Test
    public void setVerifiedSumInBuilderShouldBeWiredCorrectlyToGetVerifiedSum() {
        OpinionPoll poll = new OpinionPollTestBuilder().setVerifiedSum(DecimalNumber.parse("105")).build();
        assertEquals("105", poll.getVerifiedSum().toString());
    }

    /**
     * Verifies that before verified sum has been added, the builder responds that verified sum is missing.
     */
    @Test
    public void hasVerifiedSumInBuilderShouldReturnFalseBeforeVerifiedSumIsAdded() {
        assertFalse(new OpinionPoll.Builder().hasVerifiedSum());
    }

    /**
     * Verifies that after verified sum has been added, the builder responds that a verified sum is present.
     */
    @Test
    public void hasVerifiedSumInBuilderShouldReturnTrueAfterVerifiedSumIsAdded() {
        assertTrue(new OpinionPollTestBuilder().setVerifiedSum(DecimalNumber.parse("80")).hasVerifiedSum());
    }

    /**
     * Verifies that the scale is set to 1D when there are no other or no responses.
     */
    @Test
    public void getScaleReturnsOneWhenNoOtherOrNoResponsesAreRegistered() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").build();
        assertEquals(1D, poll.getScale());
    }

    /**
     * Verifies that the scale is calculated correctly when there are no responses.
     */
    @Test
    public void getScaleReturnsMoreThanOneWhenNoResponsesAreRegistered() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").setNoResponses("12").build();
        assertEquals(SCALE088, poll.getScale());
    }

    /**
     * Verifies that the setPollingFirm method in the builder class is wired correctly to the getPollingFirm method.
     */
    @Test
    public void setPollingFirmInBuilderShouldBeWiredCorrectlyToGetPollingFirm() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").build();
        assertEquals("ACME", poll.getPollingFirm());
    }

    /**
     * Verifies that after a polling firm has been added, the builder responds that there is a polling firm or
     * commissioner present.
     */
    @Test
    public void hasPollingFirmOrCommissionerInBuilderShouldReturnTrueAfterPollingFirmIsAdded() {
        assertTrue(new OpinionPoll.Builder().setPollingFirm("ACME").hasPollingFirmOrCommissioner());
    }

    /**
     * Verifies that the setPollingFirmPartner method in the builder class is wired correctly to the
     * getPollingFirmPartner method.
     */
    @Test
    public void setPollingFirmPartnerInBuilderShouldBeWiredCorrectlyToGetPollingFirmPartner() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirmPartner("ACME").build();
        assertEquals("ACME", poll.getPollingFirmPartner());
    }

    /**
     * Verifies that before a polling firm partner has been added, the builder responds that a polling firm partner is
     * missing.
     */
    @Test
    public void hasPollingFirmPartnerInBuilderShouldReturnFalseBeforePollingFirmPartnerIsAdded() {
        assertFalse(new OpinionPoll.Builder().hasPollingFirmPartner());
    }

    /**
     * Verifies that after a polling firm partner has been added, the builder responds that a polling firm partner is
     * present.
     */
    @Test
    public void hasPollingFirmPartnerInBuilderShouldReturnTrueAfterPollingFirmPartnerIsAdded() {
        assertTrue(new OpinionPoll.Builder().setPollingFirmPartner("ACME").hasPollingFirmPartner());
    }

    /**
     * Verifies that the setPublicationDate method in the builder class is wired correctly to the getPublicationDate
     * method.
     */
    @Test
    public void setPublicationDateInBuilderShouldBeWiredCorrectlyToGetPublicationDate() {
        OpinionPoll poll = new OpinionPoll.Builder().setPublicationDate(DATE1).build();
        assertEquals(DATE1, poll.getPublicationDate());
    }

    /**
     * Verifies that after a publication date has been added, the builder responds that there are dates present.
     */
    @Test
    public void hasDatesInBuilderShouldReturnTrueAfterPublicationDateIsAdded() {
        assertTrue(new OpinionPoll.Builder().setPublicationDate(DATE1).hasDates());
    }

    /**
     * Verifies that the addResult method in the builder class is wired correctly to the getResult method.
     */
    @Test
    public void addResultInBuilderShouldBeWiredCorrectlyToGetResult() {
        OpinionPoll poll =
                new OpinionPollTestBuilder().addResult(Set.of(ElectoralList.get("A")), new ResultValue("55")).build();
        assertEquals("55", poll.getResult(Set.of("A")).getText());
    }

    /**
     * Verifies that before a result has been added, the builder responds that the results are missing.
     */
    @Test
    public void hasResultsInBuilderShouldReturnFalseBeforeResultsAreAdded() {
        assertFalse(new OpinionPoll.Builder().hasResults());
    }

    /**
     * Verifies that after a result has been added, the builder responds that there are results present.
     */
    @Test
    public void hasResultsInBuilderShouldReturnTrueAfterResultsAreAdded() {
        assertTrue(new OpinionPollTestBuilder().addResult("A", "55").hasResults());
    }

    /**
     * Verifies that the setSampleSize method in the builder class is wired correctly to the getSampleSize method.
     */
    @Test
    public void setSampleSizeInBuilderShouldBeWiredCorrectlyToGetSampleSize() {
        OpinionPoll poll = new OpinionPoll.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        assertEquals(SAMPLE_SIZE_ONE_THOUSAND, poll.getSampleSize());
    }

    /**
     * Verifies that the setSampleSize method in the builder class is wired correctly to the getSampleSizeValue method.
     */
    @Test
    public void setSampleSizeInBuilderShouldBeWiredCorrectlyToGetSampleSizeValue() {
        OpinionPoll poll = new OpinionPoll.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        assertEquals(ONE_THOUSAND, poll.getSampleSizeValue());
    }

    /**
     * Verifies that before a sample size has been added, the builder responds that a sample size is missing.
     */
    @Test
    public void hasSampleSizeInBuilderShouldReturnFalseBeforeSampleSizeIsAdded() {
        assertFalse(new OpinionPoll.Builder().hasSampleSize());
    }

    /**
     * Verifies that after a sample size has been added, the builder responds that a sample size is present.
     */
    @Test
    public void hasSampleSizeInBuilderShouldReturnTrueAfterSampleSizeIsAdded() {
        assertTrue(new OpinionPoll.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).hasSampleSize());
    }

    /**
     * Verifies that the getEffectiveSampleSize returns <code>null</code> when no sample size has been specified.
     */
    @Test
    public void getEffectiveSampleSizeShouldBeNullWhenNoSampleSizeHasBeenSpecified() {
        OpinionPoll poll = new OpinionPoll.Builder().build();
        assertNull(poll.getEffectiveSampleSize());
    }

    /**
     * Verifies that the getEffectiveSampleSize returns the specified sample size when there are no excluded responses.
     */
    @Test
    public void getEffectiveSampleSizeShouldReturnTheSampleSizeWhenThereAreNoExcludedResponses() {
        OpinionPoll poll = new OpinionPoll.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        assertEquals(ONE_THOUSAND, poll.getEffectiveSampleSize());
    }

    /**
     * Verifies that the getEffectiveSampleSize returns the specified sample size minus the excluded responses.
     */
    @Test
    public void getEffectiveSampleSizeShouldReturnTheSampleSizeMinusTheExcludedResponses() {
        OpinionPoll poll = new OpinionPoll.Builder().setSampleSize(new ExactSampleSize(ONE_THOUSAND_TWO_HUNDRED_FIFTY))
                .setExcluded(DecimalNumber.parse("20")).build();
        assertEquals(ONE_THOUSAND, poll.getEffectiveSampleSize());
    }

    /**
     * Verifies that the setScope method in the builder class is wired correctly to the getScope method.
     */
    @Test
    public void setScopeInBuilderShouldBeWiredCorrectlyToGetScope() {
        OpinionPoll poll = new OpinionPoll.Builder().setScope(Scope.NATIONAL).build();
        assertEquals(Scope.NATIONAL, poll.getScope());
    }

    /**
     * Verifies that before a scope has been added, the builder responds that a scope is missing.
     */
    @Test
    public void hasScopeInBuilderShouldReturnFalseBeforeScopeIsAdded() {
        assertFalse(new OpinionPoll.Builder().hasScope());
    }

    /**
     * Verifies that after a scope has been added, the builder responds that a scope is present.
     */
    @Test
    public void hasScopeInBuilderShouldReturnTrueAfterScopeIsAdded() {
        assertTrue(new OpinionPoll.Builder().setScope(Scope.NATIONAL).hasScope());
    }

    /**
     * Verifies that the setExcluded method in the builder class is wired correctly to the getExcluded method.
     */
    @Test
    public void setExcludedInBuilderShouldBeWiredCorrectlyToGetExcluded() {
        DecimalNumber expected = DecimalNumber.parse("10");
        OpinionPoll poll = new OpinionPoll.Builder().setExcluded(expected).build();
        assertEquals(expected, poll.getExcluded());
    }

    /**
     * Verifies that before excluded has been added, the builder responds that excluded is missing.
     */
    @Test
    public void hasExcludedInBuilderShouldReturnFalseBeforeExcludedIsAdded() {
        assertFalse(new OpinionPoll.Builder().hasExcluded());
    }

    /**
     * Verifies that after excluded has been added, the builder responds that excluded is present.
     */
    @Test
    public void hasExcludedInBuilderShouldReturnTrueAfterExcludedIsAdded() {
        assertTrue(new OpinionPoll.Builder().setExcluded(DecimalNumber.parse("12")).hasExcluded());
    }

    /**
     * Verifies that by default, no alternative response scenarios are returned.
     */
    @Test
    public void byDefaultNoAlternativeResponseScenariosShouldBePresent() {
        OpinionPoll poll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        assertTrue(poll.getAlternativeResponseScenarios().isEmpty());
    }

    /**
     * Verifies that by default, the number of response scenarios is one.
     */
    @Test
    public void byDefaultGetNumberOfResponseScenariosReturnsOne() {
        OpinionPoll poll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        assertEquals(1, poll.getNumberOfResponseScenarios());
    }

    /**
     * Verifies the number of result values when there is no alternative response scenario.
     */
    @Test
    public void getNumberOfResultValuesShouldBeCorrect() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "54").addCommissioner("The Times").build();
        assertEquals(1, poll.getNumberOfResultValues());
    }

    /**
     * Verifies that the main response scenario is correctly when there are no alternative response scenarios.
     */
    @Test
    public void getMainResponseScenarioReturnsTheSingleResponseScenario() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").build();
        ResponseScenario expected = new ResponseScenarioTestBuilder().addResult("A", "55").build();
        assertEquals(expected, poll.getMainResponseScenario());
    }

    /**
     * Verifies that the main response scenario is correctly when there is an alternative response scenarios.
     */
    @Test
    public void getMainResponseScenarioReturnsTheMainResponseScenario() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "56").build();
        poll.addAlternativeResponseScenario(responseScenario);
        ResponseScenario expected = new ResponseScenarioTestBuilder().addResult("A", "55").build();
        assertEquals(expected, poll.getMainResponseScenario());
    }

    /**
     * Verifies that when an alternative response scenario is added, it is also returned by the get method.
     */
    @Test
    public void getAlternativeResponseScenariosShouldBeWiredCorrectlyToAddResponseScenario() {
        OpinionPoll poll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "55").build();
        poll.addAlternativeResponseScenario(responseScenario);
        List<ResponseScenario> expected = new ArrayList<ResponseScenario>();
        expected.add(responseScenario);
        assertEquals(expected, poll.getAlternativeResponseScenarios());
    }

    /**
     * Verifies that when an alternative response scenario is added, the number of response scenarios is two.
     */
    @Test
    public void getNumberOfResponseScenariosShouldReturnTwoWhenAlternativeResponseScenarioIsAdded() {
        OpinionPoll poll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "55").build();
        poll.addAlternativeResponseScenario(responseScenario);
        assertEquals(2, poll.getNumberOfResponseScenarios());
    }

    /**
     * Verifies the number of result values includes the ones from the alternative response scenario.
     */
    @Test
    public void getNumberOfResultValuesShouldIncludeThoseFromTheAlternativeResponseScenario() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "54").addCommissioner("The Times").build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "55").build();
        poll.addAlternativeResponseScenario(responseScenario);
        assertEquals(2, poll.getNumberOfResultValues());
    }

    /**
     * Verifies getElectoralLists returns the electoral lists of the main response scenario.
     */
    @Test
    public void getElectoralListsReturnsTheElectoralListsOfTheMainResponseScenario() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").build();
        assertEquals(Set.of(ElectoralList.get(Set.of("A")), ElectoralList.get(Set.of("B"))),
                poll.getElectoralListSets());
    }

    /**
     * Verifies that an opinion poll is not equal to null.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToNull() {
        OpinionPoll poll = new OpinionPoll.Builder().build();
        assertFalse(poll.equals(null));
    }

    /**
     * Verifies that an opinion poll is not equal to an object of another class, like a string.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToString() {
        OpinionPoll poll = new OpinionPoll.Builder().build();
        assertFalse(poll.equals(""));
    }

    /**
     * Verifies that an opinion poll is equal to itself.
     */
    @Test
    public void anOpinionPollShouldBeEqualToItself() {
        OpinionPoll poll = new OpinionPoll.Builder().build();
        assertEquals(poll, poll);
    }

    /**
     * Verifies that calling hashCode twice returns the same result.
     */
    @Test
    void callingHashCodeTwiceShouldReturnSameResult() {
        OpinionPoll poll = new OpinionPoll.Builder().build();
        assertEquals(poll.hashCode(), poll.hashCode());
    }

    /**
     * Verifies that an opinion poll is equal to another opinion poll built from the same builder.
     */
    @Test
    public void anOpinionPollShouldBeEqualToAnotherInstanceBuiltFromSameBuilder() {
        OpinionPoll.Builder builder = new OpinionPoll.Builder();
        assertEquals(builder.build(), builder.build());
    }

    /**
     * Verifies that calling hashCode on opinion polls built from the same builder returns the same result.
     */
    @Test
    public void hashCodeShouldBeEqualForOpinionPollsBuiltFromSameBuilder() {
        OpinionPoll.Builder builder = new OpinionPoll.Builder();
        assertEquals(builder.build().hashCode(), builder.build().hashCode());
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different commissioner.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentCommissioner() {
        OpinionPoll poll1 = new OpinionPoll.Builder().addCommissioner("The Times").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().addCommissioner("The Post").build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different commissioners.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentCommissioner() {
        OpinionPoll poll1 = new OpinionPoll.Builder().addCommissioner("The Times").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().addCommissioner("The Post").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different fieldwork end.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentFieldworkEnd() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkEnd(DATE_OR_MONTH1).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setFieldworkEnd(DATE_OR_MONTH2).build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll missing the fieldwork end.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheFieldworkEnd() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkEnd(DATE_OR_MONTH1).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll missing the fieldwork end is not equal to another opinion poll having one.
     */
    @Test
    public void anOpinionPollMissingFieldworkEndShouldNotBeEqualToAnotherOpinionPollHavingAFieldworkEnd() {
        OpinionPoll poll1 = new OpinionPoll.Builder().build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setFieldworkEnd(DATE_OR_MONTH1).build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different fieldwork end.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentFieldworkEnd() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkEnd(DATE_OR_MONTH1).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setFieldworkEnd(DATE_OR_MONTH2).build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different fieldwork start.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentFieldworkStart() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkStart(DATE_OR_MONTH1).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setFieldworkStart(DATE_OR_MONTH2).build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll missing the fieldwork start.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheFieldworkStart() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkStart(DATE_OR_MONTH1).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different fieldwork start.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentFieldworkStart() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkStart(DATE_OR_MONTH1).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setFieldworkStart(DATE_OR_MONTH2).build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different result for other.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentResultForOther() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().setOther("2").build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().setOther("3").build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different result for no responses.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentResultForNoResponses() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().setNoResponses("2").build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().setNoResponses("3").build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different result for other.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentResultForOther() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().setOther("2").build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().setOther("3").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different result for no responses.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentResultForNoResponses() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().setNoResponses("2").build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().setNoResponses("3").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll missing the result for other.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheResultForOther() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().setOther("2").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll missing the result for no responses.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheResultForNoResponses() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().setNoResponses("2").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different polling firm.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentPollingFirm() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("BCME").build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different polling firm partner.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentPollingFirmPartner() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirmPartner("ACME").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirmPartner("BCME").build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different polling firms.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentPollingFirm() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("BCME").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different polling firm partners.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentPollingFirmPartner() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirmPartner("ACME").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirmPartner("BCME").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll missing the polling firm.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingThePollingFirm() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll missing the polling firm partner.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingThePollingFirmPartner() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirmPartner("ACME").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different publication date.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentPublicationDate() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPublicationDate(DATE1).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPublicationDate(DATE2).build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll missing the publication date.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingThePublicationDate() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPublicationDate(DATE1).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different publication dates.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentPublicationDate() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPublicationDate(DATE1).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPublicationDate(DATE2).build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different result.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentResult() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55").build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "56").build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different results.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentResult() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55").build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "56").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different sample size.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentSampleSize() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setSampleSize(SAMPLE_SIZE_EIGHT_HUNDRED).build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll missing the sample size.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheSampleSize() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different sample sizes.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentSampleSize() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setSampleSize(SAMPLE_SIZE_EIGHT_HUNDRED).build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different scope.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentScope() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setScope(Scope.NATIONAL).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setScope(Scope.EUROPEAN).build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll missing the scope.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheScope() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setScope(Scope.NATIONAL).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different scopes.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentScope() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setScope(Scope.NATIONAL).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setScope(Scope.EUROPEAN).build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different share of excluded responses.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentShareOfExcludedResponses() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setExcluded(DecimalNumber.parse("10")).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setExcluded(DecimalNumber.parse("11")).build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll missing the share of excluded responses.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheShareOfExcludedResponses() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setExcluded(DecimalNumber.parse("10")).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different shares of excluded responses.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentShareOfExcludedResponses() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setExcluded(DecimalNumber.parse("10")).build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setExcluded(DecimalNumber.parse("11")).build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll with a different area.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentArea() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setArea("AB").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setArea("YZ").build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that an opinion poll is not equal to another opinion poll missing the area.
     */
    @Test
    public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheArea() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setArea("AB").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
    }

    /**
     * Verifies that opinion polls have different hash codes if they have different areas.
     */
    @Test
    public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentArea() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setArea("AB").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setArea("YZ").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
    }

    /**
     * Verifies the upper bound of results adding up.
     */
    @Test
    public void resultsShouldAddUpForUpperBound() {
        OpinionPoll.Builder opinionPollBuilder = new OpinionPollTestBuilder().addResult("A", "100").addResult("B", "1")
                .addResult("C", "1").setOther("0").setNoResponses("0");
        assertTrue(opinionPollBuilder.resultsAddUp());
    }

    /**
     * Verifies that the results don't add up above the upper bound.
     */
    @Test
    public void resultsShouldNotAddUpAboveUpperBound() {
        OpinionPoll.Builder opinionPollBuilder = new OpinionPollTestBuilder().addResult("A", "100")
                .addResult("B", "0.1").addResult("C", "0").setOther("0.1").setNoResponses("0.1");
        assertFalse(opinionPollBuilder.resultsAddUp());
    }
}
