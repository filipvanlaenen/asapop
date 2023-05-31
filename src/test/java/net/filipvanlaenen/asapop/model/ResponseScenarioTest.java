package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.SampleSize.ExactSampleSize;

/**
 * Unit tests on the class <code>ResponseScenario</code>.
 */
public class ResponseScenarioTest {
    /**
     * The magic number 0.88.
     */
    private static final double SCALE088 = 0.88D;
    /**
     * The magic number 1000.
     */
    private static final int ONE_THOUSAND = 1000;
    /**
     * The sample size 1000.
     */
    private static final ExactSampleSize SAMPLE_SIZE_ONE_THOUSAND = new ExactSampleSize(1000);
    /**
     * The sample size 1250.
     */
    private static final ExactSampleSize SAMPLE_SIZE_ONE_THOUSAND_TWO_HUNDRED_FIFTY = new ExactSampleSize(1250);

    /**
     * Verifies that getElectoralLists returns the electoral lists of the response scenario.
     */
    @Test
    public void getElectoralListsReturnsTheElectoralLists() {
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "45").build();
        assertEquals(Set.of(ElectoralList.get(Set.of("A")), ElectoralList.get(Set.of("B"))),
                responseScenario.getElectoralListSets());
    }

    /**
     * Verifies that getResults returns the result values of the response scenario.
     */
    @Test
    public void getResultsReturnsTheResults() {
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "45").build();
        assertEquals(Set.of(new ResultValue("55"), new ResultValue("45")),
                new HashSet<ResultValue>(responseScenario.getResults()));
    }

    /**
     * Verifies that the addResult method in the builder class is wired correctly to the getResult method.
     */
    @Test
    public void addResultInBuilderShouldBeWiredCorrectlyToGetResult() {
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder()
                .addResult(Set.of(ElectoralList.get("A")), new ResultValue("55")).build();
        assertEquals("55", responseScenario.getResult(Set.of("A")).getText());
    }

    /**
     * Verifies that before a result has been added, the builder responds that the results are missing.
     */
    @Test
    public void hasResultsInBuilderShouldReturnFalseBeforeResultsAreAdded() {
        assertFalse(new ResponseScenario.Builder().hasResults());
    }

    /**
     * Verifies that after a result has been added, the builder responds that there are results present.
     */
    @Test
    public void hasResultsInBuilderShouldReturnTrueAfterResultsAreAdded() {
        assertTrue(new ResponseScenarioTestBuilder().addResult("A", "55").hasResults());
    }

    /**
     * Verifies that the setOther method in the builder class is wired correctly to the getOther method.
     */
    @Test
    public void setOtherInBuilderShouldBeWiredCorrectlyToGetOther() {
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().setOther(new ResultValue("5")).build();
        assertEquals("5", responseScenario.getOther().getText());
    }

    /**
     * Verifies that before other has been added, the builder responds that other is missing.
     */
    @Test
    public void hasOtherInBuilderShouldReturnFalseBeforeOtherIsAdded() {
        assertFalse(new ResponseScenario.Builder().hasOther());
    }

    /**
     * Verifies that after other has been added, the builder responds that other is present.
     */
    @Test
    public void hasOtherInBuilderShouldReturnTrueAfterOtherIsAdded() {
        assertTrue(new ResponseScenarioTestBuilder().setOther("12").hasOther());
    }

    /**
     * Verifies that the setNoResponses method in the builder class is wired correctly to the getNoResponses method.
     */
    @Test
    public void setNoResponsesInBuilderShouldBeWiredCorrectlyToGetNoResponses() {
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().setNoResponses(new ResultValue("5")).build();
        assertEquals("5", responseScenario.getNoResponses().getText());
    }

    /**
     * Verifies that before no responses has been added, the builder responds that no responses is missing.
     */
    @Test
    public void hasNoResponsesInBuilderShouldReturnFalseBeforeNoResponsesIsAdded() {
        assertFalse(new ResponseScenario.Builder().hasNoResponses());
    }

    /**
     * Verifies that after no responses has been added, the builder responds that no responses is present.
     */
    @Test
    public void hasNoResponsesInBuilderShouldReturnTrueAfterNoResponsesIsAdded() {
        assertTrue(new ResponseScenarioTestBuilder().setNoResponses("12").hasNoResponses());
    }

    /**
     * Verifies that before verified sum has been added, the builder responds that verified sum is missing.
     */
    @Test
    public void hasVerifiedSumInBuilderShouldReturnFalseBeforeVerifiedSumIsAdded() {
        assertFalse(new ResponseScenario.Builder().hasVerifiedSum());
    }

    /**
     * Verifies that after verified sum has been added, the builder responds that a verified sum is present.
     */
    @Test
    public void hasVerifiedSumInBuilderShouldReturnTrueAfterVerifiedSumIsAdded() {
        assertTrue(new ResponseScenarioTestBuilder().setVerifiedSum(80D).hasVerifiedSum());
    }

    /**
     * Verifies that the scale is set to 1D when there are no other or no responses.
     */
    @Test
    public void getScaleReturnsOneWhenNoOtherOrNoResponsesAreRegistered() {
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "55").build();
        assertEquals(1D, responseScenario.getScale());
    }

    /**
     * Verifies that the scale is calculated correctly when there are no responses.
     */
    @Test
    public void getScaleReturnsMoreThanOneWhenNoResponsesAreRegistered() {
        ResponseScenario responseScenario =
                new ResponseScenarioTestBuilder().addResult("A", "55").setNoResponses("12").build();
        assertEquals(SCALE088, responseScenario.getScale());
    }

    /**
     * Verifies that the scale is calculated correctly when the results don't add up.
     */
    @Test
    public void getScaleReturnsMoreThanOneWhenResultsDoNotAddUp() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").setOther("33").setNoResponses("2").build();
        assertEquals(SCALE088, poll.getScale());
    }

    /**
     * Verifies that the setArea method in the builder class is wired correctly to the getArea method.
     */
    @Test
    public void setAreaInBuilderShouldBeWiredCorrectlyToGetArea() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().setArea("AB").build();
        assertEquals("AB", responseScenario.getArea());
    }

    /**
     * Verifies that before an area has been added, the builder responds that the area is missing.
     */
    @Test
    public void hasAreaInBuilderShouldReturnFalseBeforeAreaIsAdded() {
        assertFalse(new ResponseScenario.Builder().hasArea());
    }

    /**
     * Verifies that after an area has been added, the builder responds that the area is present.
     */
    @Test
    public void hasAreaInBuilderShouldReturnTrueAfterAreaIsAdded() {
        assertTrue(new ResponseScenario.Builder().setArea("A").hasArea());
    }

    /**
     * Verifies that the setScope method in the builder class is wired correctly to the getScope method.
     */
    @Test
    public void setScopeInBuilderShouldBeWiredCorrectlyToGetScope() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().setScope(Scope.NATIONAL).build();
        assertEquals(Scope.NATIONAL, responseScenario.getScope());
    }

    /**
     * Verifies that before a scope has been added, the builder responds that a scope is missing.
     */
    @Test
    public void hasScopeInBuilderShouldReturnFalseBeforeScopeIsAdded() {
        assertFalse(new ResponseScenario.Builder().hasScope());
    }

    /**
     * Verifies that after a scope has been added, the builder responds that a scope is present.
     */
    @Test
    public void hasScopeInBuilderShouldReturnTrueAfterScopeIsAdded() {
        assertTrue(new ResponseScenario.Builder().setScope(Scope.NATIONAL).hasScope());
    }

    /**
     * Verifies that the setExcluded method in the builder class is wired correctly to the getExcluded method.
     */
    @Test
    public void setExcludedInBuilderShouldBeWiredCorrectlyToGetExcluded() {
        DecimalNumber expected = DecimalNumber.parse("10");
        ResponseScenario responseScenario = new ResponseScenario.Builder().setExcluded(expected).build();
        assertEquals(expected, responseScenario.getExcluded());
    }

    /**
     * Verifies that before excluded has been added, the builder responds that excluded is missing.
     */
    @Test
    public void hasExcludedInBuilderShouldReturnFalseBeforeExcludedIsAdded() {
        assertFalse(new ResponseScenario.Builder().hasExcluded());
    }

    /**
     * Verifies that after excluded has been added, the builder responds that excluded is present.
     */
    @Test
    public void hasExcludedInBuilderShouldReturnTrueAfterExcludedIsAdded() {
        assertTrue(new ResponseScenario.Builder().setExcluded(DecimalNumber.parse("12")).hasExcluded());
    }

    /**
     * Verifies that the setSampleSize method in the builder class is wired correctly to the getSampleSize method.
     */
    @Test
    public void setSampleSizeInBuilderShouldBeWiredCorrectlyToGetSampleSize() {
        ResponseScenario responseScenario =
                new ResponseScenario.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        assertEquals(SAMPLE_SIZE_ONE_THOUSAND, responseScenario.getSampleSize());
    }

    /**
     * Verifies that the setSampleSize method in the builder class is wired correctly to the getSampleSizeValue method.
     */
    @Test
    public void setSampleSizeInBuilderShouldBeWiredCorrectlyToGetSampleSizeValue() {
        ResponseScenario setSampleSizeInBuilderShouldBeWiredCorrectlyToGetSampleSize =
                new ResponseScenario.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        assertEquals(ONE_THOUSAND, setSampleSizeInBuilderShouldBeWiredCorrectlyToGetSampleSize.getSampleSizeValue());
    }

    /**
     * Verifies that before a sample size has been added, the builder responds that a sample size is missing.
     */
    @Test
    public void hasSampleSizeInBuilderShouldReturnFalseBeforeSampleSizeIsAdded() {
        assertFalse(new ResponseScenario.Builder().hasSampleSize());
    }

    /**
     * Verifies that after a sample size has been added, the builder responds that a sample size is present.
     */
    @Test
    public void hasSampleSizeInBuilderShouldReturnTrueAfterSampleSizeIsAdded() {
        assertTrue(new ResponseScenario.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).hasSampleSize());
    }

    /**
     * Verifies that the getEffectiveSampleSize returns <code>null</code> when no sample size has been specified.
     */
    @Test
    public void getEffectiveSampleSizeShouldBeNullWhenNoSampleSizeHasBeenSpecified() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().build();
        assertNull(responseScenario.getEffectiveSampleSize());
    }

    /**
     * Verifies that the getEffectiveSampleSize returns the specified sample size when there are no excluded responses.
     */
    @Test
    public void getEffectiveSampleSizeShouldReturnTheSampleSizeWhenThereAreNoExcludedResponses() {
        ResponseScenario responseScenario =
                new ResponseScenario.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        assertEquals(ONE_THOUSAND, responseScenario.getEffectiveSampleSize());
    }

    /**
     * Verifies that the getEffectiveSampleSize returns the specified sample size minus the excluded responses.
     */
    @Test
    public void getEffectiveSampleSizeShouldReturnTheSampleSizeMinusTheExcludedResponses() {
        ResponseScenario responseScenario =
                new ResponseScenario.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND_TWO_HUNDRED_FIFTY)
                        .setExcluded(DecimalNumber.parse("20")).build();
        assertEquals(ONE_THOUSAND, responseScenario.getEffectiveSampleSize());
    }

    /**
     * Verifies that a response scenario is not equal to null.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToNull() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().build();
        assertFalse(responseScenario.equals(null));
    }

    /**
     * Verifies that a response scenario is not equal to an object of another class, like a string.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToString() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().build();
        assertFalse(responseScenario.equals(""));
    }

    /**
     * Verifies that a response scenario is equal to itself.
     */
    @Test
    public void aResponseScenarioShouldBeEqualToItself() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().build();
        assertEquals(responseScenario, responseScenario);
    }

    /**
     * Verifies that calling hashCode twice returns the same result.
     */
    @Test
    void callingHashCodeTwiceShouldReturnSameResult() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().build();
        assertEquals(responseScenario.hashCode(), responseScenario.hashCode());
    }

    /**
     * Verifies that a response scenario is equal to another response scenario built from the same builder.
     */
    @Test
    public void aResponseScenarioShouldBeEqualToAnotherInstanceBuiltFromSameBuilder() {
        ResponseScenario.Builder builder = new ResponseScenario.Builder();
        assertEquals(builder.build(), builder.build());
    }

    /**
     * Verifies that calling hashCode on response scenarios built from the same builder returns the same result.
     */
    @Test
    void hashCodeShouldBeEqualForResponseScenariosBuiltFromSameBuilder() {
        ResponseScenario.Builder builder = new ResponseScenario.Builder();
        assertEquals(builder.build().hashCode(), builder.build().hashCode());
    }

    /**
     * Verifies that a response scenario is not equal to another response scenario with a different result.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioWithADifferentResult() {
        ResponseScenario responseScenario1 = new ResponseScenarioTestBuilder().addResult("A", "55").build();
        ResponseScenario responseScenario2 = new ResponseScenarioTestBuilder().addResult("A", "56").build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that response scenarios have different hash codes if they have different results.
     */
    @Test
    public void aResponseScenarioShouldNotHaveSameHashCodeAsAnotherResponseScenarioWithADifferentResult() {
        ResponseScenario responseScenario1 = new ResponseScenarioTestBuilder().addResult("A", "55").build();
        ResponseScenario responseScenario2 = new ResponseScenarioTestBuilder().addResult("A", "56").build();
        assertFalse(responseScenario1.hashCode() == responseScenario2.hashCode());
    }

    /**
     * Verifies that a response scenario is not equal to another response scenario with a different result for others.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioWithADifferentOtherResult() {
        ResponseScenario responseScenario1 = new ResponseScenarioTestBuilder().setOther("5").build();
        ResponseScenario responseScenario2 = new ResponseScenarioTestBuilder().setOther("6").build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that a response scenario is not equal to another response scenario with a different result for no
     * responses.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioWithADifferentNoResponsesResult() {
        ResponseScenario responseScenario1 = new ResponseScenarioTestBuilder().setNoResponses("5").build();
        ResponseScenario responseScenario2 = new ResponseScenarioTestBuilder().setNoResponses("6").build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that response scenarios have different hash codes if they have different results for other.
     */
    @Test
    public void aResponseScenarioShouldNotHaveSameHashCodeAsAnotherResponseScenarioWithADifferentOtherResult() {
        ResponseScenario responseScenario1 = new ResponseScenarioTestBuilder().setOther("5").build();
        ResponseScenario responseScenario2 = new ResponseScenarioTestBuilder().setOther("6").build();
        assertFalse(responseScenario1.hashCode() == responseScenario2.hashCode());
    }

    /**
     * Verifies that response scenarios have different hash codes if they have different results for no responses.
     */
    @Test
    public void aResponseScenarioShouldNotHaveSameHashCodeAsAnotherResponseScenarioWithADifferentNoResponsesResult() {
        ResponseScenario responseScenario1 = new ResponseScenarioTestBuilder().setNoResponses("5").build();
        ResponseScenario responseScenario2 = new ResponseScenarioTestBuilder().setNoResponses("6").build();
        assertFalse(responseScenario1.hashCode() == responseScenario2.hashCode());
    }

    /**
     * Verifies that a response scenario is not equal to another response scenario missing the result for others.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioMissingTheOtherResult() {
        ResponseScenario responseScenario1 = new ResponseScenarioTestBuilder().setOther("5").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that a response scenario is not equal to another response scenario missing the result for no responses.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioMissingTheNoResponsesResult() {
        ResponseScenario responseScenario1 = new ResponseScenarioTestBuilder().setNoResponses("5").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that a response scenario without a result for others is not equal to another response scenario with a
     * result for others.
     */
    @Test
    public void aResponseScenarioMissingOtherResultShouldNotBeEqualToAnotherResponseScenarioWithOtherResult() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().build();
        ResponseScenario responseScenario2 = new ResponseScenarioTestBuilder().setOther("5").build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that a response scenario without a result for no responses is not equal to another response scenario
     * with a result for no responses.
     */
    @Test
    public void aResponseScenarioMissingNoResponsesShouldNotBeEqualToAnotherResponseScenarioWithNoResponses() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().build();
        ResponseScenario responseScenario2 = new ResponseScenarioTestBuilder().setNoResponses("5").build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that a response scenario is not equal to another response scenario with a different area.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioWithADifferentArea() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setArea("AB").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().setArea("YZ").build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that a response scenario is not equal to another response scenario missing the area.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioMissingTheArea() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setArea("AB").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that response scenarios have different hash codes if they have different areas.
     */
    @Test
    public void aResponseScenarioShouldNotHaveSameHashCodeAsAnotherResponseScenarioWithADifferentArea() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setArea("AB").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().setArea("YZ").build();
        assertFalse(responseScenario1.hashCode() == responseScenario2.hashCode());
    }

    /**
     * Verifies that a response scenario is not equal to another response scenario with a different sample size.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioWithADifferentSampleSize() {
        ResponseScenario responseScenario1 =
                new ResponseScenario.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        ResponseScenario responseScenario2 =
                new ResponseScenario.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND_TWO_HUNDRED_FIFTY).build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that a response scenario is not equal to another response scenario missing the sample size.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioMissingTheSampleSize() {
        ResponseScenario responseScenario1 =
                new ResponseScenario.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that response scenarios have different hash codes if they have different sample sizes.
     */
    @Test
    public void aResponseScenarioShouldNotHaveSameHashCodeAsAnotherResponseScenarioWithADifferentSampleSize() {
        ResponseScenario responseScenario1 =
                new ResponseScenario.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND).build();
        ResponseScenario responseScenario2 =
                new ResponseScenario.Builder().setSampleSize(SAMPLE_SIZE_ONE_THOUSAND_TWO_HUNDRED_FIFTY).build();
        assertFalse(responseScenario1.hashCode() == responseScenario2.hashCode());
    }

    /**
     * Verifies that a response scenario is not equal to another response scenario with a different scope.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioWithADifferentScope() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setScope(Scope.NATIONAL).build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().setScope(Scope.EUROPEAN).build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that a response scenario is not equal to another response scenario missing the scope.
     */
    @Test
    public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioMissingTheScope() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setScope(Scope.NATIONAL).build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().build();
        assertFalse(responseScenario1.equals(responseScenario2));
    }

    /**
     * Verifies that response scenarios have different hash codes if they have different scopes.
     */
    @Test
    public void aResponseScenarioShouldNotHaveSameHashCodeAsAnotherResponseScenarioWithADifferentScope() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setScope(Scope.NATIONAL).build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().setScope(Scope.EUROPEAN).build();
        assertFalse(responseScenario1.hashCode() == responseScenario2.hashCode());
    }

    /**
     * Verifies the lower bound of results adding strictly up.
     */
    @Test
    public void shouldBeStrictlyWithinRoundingErrorForLowerBound() {
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "98").addResult("B", "0")
                .addResult("C", "0").setOther("0").setNoResponses("0").build();
        assertTrue(responseScenario.isStrictlyWithinRoundingError());
    }

    /**
     * Verifies the lower bound of results not adding strictly up.
     */
    @Test
    public void shouldNotBeStrictlyWithinRoundingErrorBelowLowerBound() {
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "97").addResult("B", "0")
                .addResult("C", "0").setOther("0").setNoResponses("0").build();
        assertFalse(responseScenario.isStrictlyWithinRoundingError());
    }

    /**
     * Verifies that the lower bound for the results to add up doesn't apply when no responses have been registered.
     */
    @Test
    public void lowerBoundForResultsToAddDoesNotApplyWhenNoNoResponses() {
        ResponseScenario.Builder responseScenarioBuilder = new ResponseScenarioTestBuilder().addResult("A", "50")
                .addResult("B", "0").addResult("C", "0").setOther("0");
        assertTrue(responseScenarioBuilder.resultsAddUp());
    }

    /**
     * Verifies that the lower bound for the results to add up doesn't apply when no other have been registered.
     */
    @Test
    public void lowerBoundForResultsToAddDoesNotApplyWhenNoOther() {
        ResponseScenario.Builder responseScenarioBuilder = new ResponseScenarioTestBuilder().addResult("A", "50")
                .addResult("B", "0").addResult("C", "0").setOther("0");
        assertTrue(responseScenarioBuilder.resultsAddUp());
    }

    /**
     * Verifies the lower bound of results adding up.
     */
    @Test
    public void resultsShouldAddUpForLowerBound() {
        ResponseScenario.Builder responseScenarioBuilder = new ResponseScenarioTestBuilder().addResult("A", "98")
                .addResult("B", "0").addResult("C", "0").setOther("0").setNoResponses("0");
        assertTrue(responseScenarioBuilder.resultsAddUp());
    }

    /**
     * Verifies that the results don't add up below the lower bound.
     */
    @Test
    public void resultsShouldNotAddUpBelowLowerBound() {
        ResponseScenario.Builder responseScenarioBuilder = new ResponseScenarioTestBuilder().addResult("A", "97")
                .addResult("B", "0").addResult("C", "0").setOther("0").setNoResponses("0");
        assertFalse(responseScenarioBuilder.resultsAddUp());
    }

    /**
     * Verifies the upper bound of results adding up.
     */
    @Test
    public void resultsShouldAddUpForUpperBound() {
        ResponseScenario.Builder responseScenarioBuilder = new ResponseScenarioTestBuilder().addResult("A", "100")
                .addResult("B", "1").addResult("C", "1").setOther("0").setNoResponses("0");
        assertTrue(responseScenarioBuilder.resultsAddUp());
    }

    /**
     * Verifies that the results don't add up above the upper bound.
     */
    @Test
    public void resultsShouldNotAddUpAboveUpperBound() {
        ResponseScenario.Builder responseScenarioBuilder = new ResponseScenarioTestBuilder().addResult("A", "100")
                .addResult("B", "0.1").addResult("C", "0").setOther("0.1").setNoResponses("0.1");
        assertFalse(responseScenarioBuilder.resultsAddUp());
    }

    /**
     * Verifies that the results add up if the sum matches the verified sum.
     */
    @Test
    public void resultShouldAddUpIfSumEqualsVerifiedSum() {
        ResponseScenario.Builder responseScenarioBuilder =
                new ResponseScenarioTestBuilder().addResult("A", "80").addResult("B", "30").setVerifiedSum(110D);
        assertTrue(responseScenarioBuilder.resultsAddUp());
    }

    /**
     * Verifies that the results don't add up if the sum doesn't match the verified sum.
     */
    @Test
    public void resultsShouldNotAddUpIfSumDoesNotEqualVerifiedSum() {
        ResponseScenario.Builder responseScenarioBuilder =
                new ResponseScenarioTestBuilder().addResult("A", "50").addResult("B", "30").setVerifiedSum(110D);
        assertFalse(responseScenarioBuilder.resultsAddUp());
    }
}
