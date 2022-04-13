package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.analysis.SampledMultivariateHypergeometricDistribution.WinnersRegister;

/**
 * Unit tests on the <code>SampledMultivariateHypergeometricDistribution</code> class.
 */
public class SampledMultivariateHypergeometricDistributionTest {
    /**
     * The number of iterations in the multivariate hypergeometric distribution.
     */
    private static final long NUMBER_OF_ITERATIONS = 20_000L;
    /**
     * The number of samples in the hypergeometric distributions.
     */
    private static final long NUMBER_OF_SAMPLES = 2_000L;
    /**
     * The sample size for the polls.
     */
    private static final long SAMPLE_SIZE = 1000L;
    /**
     * The population size of the polls.
     */
    private static final long POPULATION_SIZE = 1_000_000L;
    /**
     * Precision for floating point assertions where high precision can be expected.
     */
    private static final double SMALL_DELTA = 1E-8;
    /**
     * Precision for floating point assertions where moderate precision can be expected.
     */
    private static final double MODERATE_DELTA = 1E-4;
    /**
     * Precision for floating point assertions where low precision can be expected.
     */
    private static final double LARGE_DELTA = 0.05;
    /**
     * The magic number 0.10.
     */
    private static final double DOUBLE_0_10 = 0.10D;
    /**
     * The magic number one quarter.
     */
    private static final double ONE_QUARTER = 0.25D;
    /**
     * The magic number one third.
     */
    private static final double ONE_THIRD = 0.333333333D;
    /**
     * The magic number 0.36.
     */
    private static final double DOUBLE_0_36 = 0.36D;
    /**
     * The magic number 0.45.
     */
    private static final double DOUBLE_0_45 = 0.45D;
    /**
     * The magic number one half.
     */
    private static final double ONE_HALF = 0.5D;
    /**
     * The magic number one three.
     */
    private static final long THREE = 3L;
    /**
     * The magic number one hundred.
     */
    private static final long ONE_HUNDRED = 100L;
    /**
     * The magic number two hundred ninety-nine.
     */
    private static final long TWO_HUNDRED_NINETY_NINE = 299L;
    /**
     * The magic number three hundred.
     */
    private static final long THREE_HUNDRED = 300L;
    /**
     * The magic number three hundred one.
     */
    private static final long THREE_HUNDRED_ONE = 301L;
    /**
     * The magic number four hundred ninety-nine.
     */
    private static final long FOUR_HUNDRED_NINETY_NINE = 499L;
    /**
     * The magic number five hundred.
     */
    private static final long FIVE_HUNDRED = 500L;
    /**
     * The magic number seven hundred.
     */
    private static final long SEVEN_HUNDRED = 700L;
    /**
     * Confidence interval to test the utility methods on.
     */
    private static final ConfidenceInterval<Range> CONFIDENCE_INTERVAL = new ConfidenceInterval<Range>(
            Range.get(ONE_HUNDRED, TWO_HUNDRED_NINETY_NINE), Range.get(FOUR_HUNDRED_NINETY_NINE, FIVE_HUNDRED));
    /**
     * Multivariate distribution to run the equality tests on.
     */
    private static final SampledMultivariateHypergeometricDistribution MULTIVARIATE_DISTRIBUTION =
            new SampledMultivariateHypergeometricDistribution(
                    createProbabilityMassFunctions(FIVE_HUNDRED, THREE_HUNDRED, ONE_HUNDRED), POPULATION_SIZE,
                    SAMPLE_SIZE, NUMBER_OF_ITERATIONS);

    /**
     * Verifies that when there's only one candidate, and the candidate's support is well above fifty percent, the
     * candidate is certain to win the first round.
     */
    @Test
    public void singleCandidateWithSupportAboveFiftyPercentShouldBeCertainToWin() {
        assertSingleWinnerProbabilityEquals(1D, SMALL_DELTA, 0, SEVEN_HUNDRED);
    }

    /**
     * Verifies that when there's only one candidate, and the candidate's support is fifty percent, the candidate has a
     * probability of fifty percent to win the first round.
     */
    @Test
    public void singleCandidateWithSupportOfFiftyPercentShouldHaveFiftyPercentProbabilityToWin() {
        assertSingleWinnerProbabilityEquals(ONE_HALF, MODERATE_DELTA, 0, FIVE_HUNDRED);
    }

    /**
     * Verifies that when there's only one candidate, and the candidate's support is fifty percent, the candidate has a
     * probability of fifty percent not to win the first round.
     */
    @Test
    public void singleCandidateWithSupportOfFiftyPercentShouldHaveFiftyPercentProbabilityNotToWin() {
        assertPairProbabilityEquals(ONE_HALF, MODERATE_DELTA, 0, null, FIVE_HUNDRED);
    }

    /**
     * Verifies that when there's only one candidate, and the candidate's support is well below fifty percent, the
     * candidate is certain not to win the first round.
     */
    @Test
    public void singleCandidateWithSupportBelowFiftyPercentShouldBeCertainToNotWin() {
        assertPairProbabilityEquals(1D, SMALL_DELTA, 0, null, THREE_HUNDRED);
    }

    /**
     * Verifies that when there are two candidates, and the largest candidate's support is well above fifty percent,
     * that candidate is certain to win the first round.
     */
    @Test
    public void largestCandidateOfTwoWithSupportAboveFiftyPercentShouldBeCertainToWin() {
        assertSingleWinnerProbabilityEquals(1D, SMALL_DELTA, 0, SEVEN_HUNDRED, ONE_HUNDRED);
    }

    /**
     * Verifies that when there are two candidates, and the largest candidate's support is fifty percent, that candidate
     * has a probability of fifty percent to win the first round.
     */
    @Test
    public void largestCandidateOfTwoWithSupportOfFiftyPercentShouldHaveFiftyPercentProbabilityToWin() {
        assertSingleWinnerProbabilityEquals(ONE_HALF, MODERATE_DELTA, 0, FIVE_HUNDRED, THREE_HUNDRED);
    }

    /**
     * Verifies that when there are two candidates, and the largest candidate's support is fifty percent, there's a
     * fifty percent probability for a second round.
     */
    @Test
    public void largestCandidateOfTwoWithSupportOfFiftyPercentShouldHaveFiftyPercentProbabilityNotToWin() {
        assertPairProbabilityEquals(ONE_HALF, MODERATE_DELTA, 0, 1, FIVE_HUNDRED, THREE_HUNDRED);
    }

    /**
     * Verifies that when there are two candidates both with almost fifty percent support, each has a slightly less than
     * fifty percent probability to win the first round.
     */
    @Test
    public void twoCandidatesWithSupportOfAlmostFiftyPercentShouldHaveSlightlyLessThanOneHalfToWin() {
        assertSingleWinnerProbabilityEquals(DOUBLE_0_45, LARGE_DELTA, 0, FOUR_HUNDRED_NINETY_NINE,
                FOUR_HUNDRED_NINETY_NINE);
    }

    /**
     * Verifies that when there are two candidates both with almost fifty percent support, there's a small probability
     * for a second round.
     */
    @Test
    public void twoCandidatesWithSupportOfAlmostFiftyPercentShouldHaveALowProbabilityNotToWin() {
        assertPairProbabilityEquals(DOUBLE_0_10, LARGE_DELTA, 0, 1, FOUR_HUNDRED_NINETY_NINE, FOUR_HUNDRED_NINETY_NINE);
    }

    /**
     * Verifies that when there are two candidates with the same support, these two candidates are certain to win the
     * first round.
     */
    @Test
    public void twoCandidatesWithTheSameSupportShouldBeCertainToWin() {
        assertPairProbabilityEquals(1, SMALL_DELTA, 0, 1, THREE_HUNDRED, THREE_HUNDRED);
    }

    /**
     * Verifies that when there are three candidates, and the largest candidate's support is well above fifty percent,
     * that candidate is certain to win the first round.
     */
    @Test
    public void largestCandidateOfThreeWithSupportAboveFiftyPercentShouldBeCertainToWin() {
        assertSingleWinnerProbabilityEquals(1D, SMALL_DELTA, 0, SEVEN_HUNDRED, ONE_HUNDRED, ONE_HUNDRED);
    }

    /**
     * Verifies that when there are three candidates, one with half of the support, and two with equal small support,
     * the probability that the large candidate wins the first round is one half.
     */
    @Test
    public void aCandidateWithHalfOfTheSupportAgainstTwoSmallCandidatesShouldHaveProbabilityOfOneHalf() {
        assertSingleWinnerProbabilityEquals(ONE_HALF, LARGE_DELTA, 0, FIVE_HUNDRED, ONE_HUNDRED, ONE_HUNDRED);
    }

    /**
     * Verifies that when there are three candidates, one with half of the support, and two with equal small support,
     * the probability that one of the smaller candidates wins the first round together with the large candidate is one
     * quarter.
     */
    @Test
    public void aCandidateWithHalfOfTheSupportTogetherWithOneOfTwoSmallCandidatesShouldHaveProbabilityOfOneQuarter() {
        assertPairProbabilityEquals(ONE_QUARTER, LARGE_DELTA, 0, 1, FIVE_HUNDRED, ONE_HUNDRED, ONE_HUNDRED);
    }

    /**
     * Verifies that when there are three candidates with the same support, a pair of two of these candidates has a
     * probability of one third to win the first round.
     */
    @Test
    public void threeCandidatesWithTheSameSupportShouldEachHaveAProbabilityOfOneThird() {
        assertPairProbabilityEquals(ONE_THIRD, SMALL_DELTA, 0, 1, THREE_HUNDRED, THREE_HUNDRED, THREE_HUNDRED);
    }

    /**
     * Verifies that when there are three candidates with almost the same support, a pair of two largest candidates has
     * a probability of slightly more than one third to win the first round.
     */
    @Test
    public void twoLargestOfThreeCandidatesWithTheAlmostSameSupportShouldHaveAProbabilityAboveOneThird() {
        assertPairProbabilityEquals(DOUBLE_0_36, LARGE_DELTA, 0, 1, THREE_HUNDRED_ONE, THREE_HUNDRED,
                TWO_HUNDRED_NINETY_NINE);
    }

    /**
     * Verifies that many small candidates do not disturb the calculation for the lagre candidates.
     */
    @Test
    public void manySmallCandidatesShouldNotDisturbTheCalculationForTheLargeCandidates() {
        assertPairProbabilityEquals(DOUBLE_0_36, LARGE_DELTA, 0, 1, THREE_HUNDRED_ONE, THREE_HUNDRED,
                TWO_HUNDRED_NINETY_NINE, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L);
    }

    /**
     * Asserts the probability of a pair of candidates to be equal within a given delta. Asserts also that the number of
     * iterations is set correctly.
     *
     * @param expected The expected probability.
     * @param delta    The margin of error.
     * @param i0       The index of the first candidate in the pair.
     * @param i1       The index of the second candidate in the pair.
     * @param values   The sample values for the candidates.
     */
    private void assertPairProbabilityEquals(final double expected, final double delta, final Integer i0,
            final Integer i1, final long... values) {
        List<SampledHypergeometricDistribution> probabilityMassFunctions = createProbabilityMassFunctions(values);
        SampledMultivariateHypergeometricDistribution multivariateDistribution;
        multivariateDistribution = new SampledMultivariateHypergeometricDistribution(probabilityMassFunctions,
                POPULATION_SIZE, SAMPLE_SIZE, NUMBER_OF_ITERATIONS);
        double actual = multivariateDistribution.getProbabilityMass(probabilityMassFunctions.get(i0),
                i1 == null ? null : probabilityMassFunctions.get(i1));
        assertEquals(expected, actual, delta);
        assertEquals(NUMBER_OF_ITERATIONS, multivariateDistribution.getNumberOfIterations());
    }

    /**
     * Asserts of the probability of a single candidate to be equal within a given delta. Asserts also that the number
     * of iterations is set correctly.
     *
     * @param expected The expected probability.
     * @param delta    The margin of error.
     * @param i        The index of the candidate.
     * @param values   The sample values for the candidates.
     */
    private void assertSingleWinnerProbabilityEquals(final double expected, final double delta, final Integer i,
            final long... values) {
        List<SampledHypergeometricDistribution> probabilityMassFunctions = createProbabilityMassFunctions(values);
        SampledMultivariateHypergeometricDistribution multivariateDistribution;
        multivariateDistribution = new SampledMultivariateHypergeometricDistribution(probabilityMassFunctions,
                POPULATION_SIZE, SAMPLE_SIZE, NUMBER_OF_ITERATIONS);
        double actual = multivariateDistribution.getProbabilityMass(probabilityMassFunctions.get(i));
        assertEquals(expected, actual, delta);
        assertEquals(NUMBER_OF_ITERATIONS, multivariateDistribution.getNumberOfIterations());
    }

    /**
     * Creates a list of probability mass functions based on a set of values.
     *
     * @param values The sample values for the candidates.
     * @return A list of probability mass functions based on a set of values.
     */
    private static List<SampledHypergeometricDistribution> createProbabilityMassFunctions(final long... values) {
        List<SampledHypergeometricDistribution> probabilityMassFunctions;
        probabilityMassFunctions = new ArrayList<SampledHypergeometricDistribution>();
        for (long value : values) {
            probabilityMassFunctions.add(
                    SampledHypergeometricDistributions.get(value, SAMPLE_SIZE, NUMBER_OF_SAMPLES, POPULATION_SIZE));
        }
        return probabilityMassFunctions;
    }

    /**
     * Verifies that the test confidence interval is above 1.
     */
    @Test
    public void confidenceIntervalShouldBeAboveOne() {
        assertTrue(SampledMultivariateHypergeometricDistribution.isConfidenceIntervalAbove(CONFIDENCE_INTERVAL, 1L));
    }

    /**
     * Verifies that the test confidence interval is not above 100.
     */
    @Test
    public void confidenceIntervalShouldNotBeAboveOneHundred() {
        assertFalse(SampledMultivariateHypergeometricDistribution.isConfidenceIntervalAbove(CONFIDENCE_INTERVAL,
                ONE_HUNDRED));
    }

    /**
     * Verifies that the test confidence interval is below 1000.
     */
    @Test
    public void confidenceIntervalShouldBeBelowOneThousand() {
        assertTrue(SampledMultivariateHypergeometricDistribution.isConfidenceIntervalBelow(CONFIDENCE_INTERVAL,
                SEVEN_HUNDRED));
    }

    /**
     * Verifies that the test confidence interval is not beow 700.
     */
    @Test
    public void confidenceIntervalShouldNotBeBelowSevenHundred() {
        assertFalse(SampledMultivariateHypergeometricDistribution.isConfidenceIntervalBelow(CONFIDENCE_INTERVAL,
                FIVE_HUNDRED));
    }

    /**
     * Verifies that a range is set to be the largest right after initialization.
     */
    @Test
    public void firstRangeAfterInitializationShouldBeSetAsLargest() {
        WinnersRegister winnersRegister = new WinnersRegister();
        winnersRegister.initialize();
        winnersRegister.update(Range.get(0L, 1L), 0);
        assertEquals(0, winnersRegister.getIndexOfLargestRange());
        assertEquals(Range.get(0L, 1L), winnersRegister.getLargestRange());
    }

    /**
     * Verifies that when the second range is larger than the first, the second range is set as the largest, and the
     * first one as the second largest range.
     */
    @Test
    public void largerSecondRangeShouldBeSetAsLargest() {
        WinnersRegister winnersRegister = new WinnersRegister();
        winnersRegister.initialize();
        winnersRegister.update(Range.get(0L, 1L), 0);
        winnersRegister.update(Range.get(2L, THREE), 1);
        assertEquals(1, winnersRegister.getIndexOfLargestRange());
        assertEquals(Range.get(2L, THREE), winnersRegister.getLargestRange());
        assertEquals(0, winnersRegister.getIndexOfSecondLargestRange());
    }

    /**
     * Verifies that when the second range is equal to the first, it is set as the second largest range.
     */
    @Test
    public void equalSecondRangeShouldBeSetAsSecondLargest() {
        WinnersRegister winnersRegister = new WinnersRegister();
        winnersRegister.initialize();
        winnersRegister.update(Range.get(0L, 1L), 0);
        winnersRegister.update(Range.get(0L, 1L), 1);
        assertEquals(0, winnersRegister.getIndexOfLargestRange());
        assertEquals(Range.get(0L, 1L), winnersRegister.getLargestRange());
        assertEquals(1, winnersRegister.getIndexOfSecondLargestRange());
    }

    /**
     * Verifies that when the third range is largest, it is set as the largest, and the formerly largest is set as the
     * second largest.
     */
    @Test
    public void largerThirdRangeShouldBeSetAsLargest() {
        WinnersRegister winnersRegister = new WinnersRegister();
        winnersRegister.initialize();
        winnersRegister.update(Range.get(0L, 1L), 0);
        winnersRegister.update(Range.get(0L, 1L), 1);
        winnersRegister.update(Range.get(2L, THREE), 2);
        assertEquals(2, winnersRegister.getIndexOfLargestRange());
        assertEquals(Range.get(2L, THREE), winnersRegister.getLargestRange());
        assertEquals(0, winnersRegister.getIndexOfSecondLargestRange());
    }

    /**
     * Verifies that when the third range is equal to the largest, but larger to the second largest, it is set as the
     * second largest range.
     */
    @Test
    public void equalThirdRangeLargestThanSecondShouldBeSetAsSecond() {
        WinnersRegister winnersRegister = new WinnersRegister();
        winnersRegister.initialize();
        winnersRegister.update(Range.get(2L, THREE), 0);
        winnersRegister.update(Range.get(0L, 1L), 1);
        winnersRegister.update(Range.get(2L, THREE), 2);
        assertEquals(0, winnersRegister.getIndexOfLargestRange());
        assertEquals(Range.get(2L, THREE), winnersRegister.getLargestRange());
        assertEquals(2, winnersRegister.getIndexOfSecondLargestRange());
    }

    /**
     * Verifies that when the third range is equal to the two first, it isn't retained.
     */
    @Test
    public void equalThirdRangeShouldNotBeRetained() {
        WinnersRegister winnersRegister = new WinnersRegister();
        winnersRegister.initialize();
        winnersRegister.update(Range.get(0L, 1L), 0);
        winnersRegister.update(Range.get(0L, 1L), 1);
        winnersRegister.update(Range.get(0L, 1L), 2);
        assertEquals(0, winnersRegister.getIndexOfLargestRange());
        assertEquals(Range.get(0L, 1L), winnersRegister.getLargestRange());
        assertEquals(1, winnersRegister.getIndexOfSecondLargestRange());
    }

    /**
     * Verifies that a sampled multivariate hypergeometric distribution is not equal to null.
     */
    @Test
    public void aSampledMultivariateHypergeometricDistributionShouldNotBeEqualToNull() {
        assertFalse(MULTIVARIATE_DISTRIBUTION.equals(null));
    }

    /**
     * Verifies that a sampled multivariate hypergeometric distribution is not equal to an object of another class, like
     * a string.
     */
    @Test
    public void aSampledMultivariateHypergeometricDistributionShouldNotBeEqualToAString() {
        assertFalse(MULTIVARIATE_DISTRIBUTION.equals(""));
    }

    /**
     * Verifies that a sampled multivariate hypergeometric distribution is equal to itself.
     */
    @Test
    public void aSampledMultivariateHypergeometricDistributionShouldBeEqualToItself() {
        assertTrue(MULTIVARIATE_DISTRIBUTION.equals(MULTIVARIATE_DISTRIBUTION));
    }

    /**
     * Verifies that calling hashCode twice on a sampled multivariate hypergeometric distribution returns the same
     * result.
     */
    @Test
    public void callingHashCodeTwiceOnASampledMultivariateHypergeometricDistributionReturnsTheSameResult() {
        assertEquals(MULTIVARIATE_DISTRIBUTION.hashCode(), MULTIVARIATE_DISTRIBUTION.hashCode());
    }

    /**
     * Verifies that two sampled multivariate hypergeometric distributions constructed with the same parameter are
     * equal.
     */
    @Test
    public void twoSampledMultivariateHypergeometricDistributionsConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(MULTIVARIATE_DISTRIBUTION,
                new SampledMultivariateHypergeometricDistribution(
                        createProbabilityMassFunctions(FIVE_HUNDRED, THREE_HUNDRED, ONE_HUNDRED), POPULATION_SIZE,
                        SAMPLE_SIZE, NUMBER_OF_ITERATIONS));
    }

    /**
     * Verifies that two sampled multivariate hypergeometric distributions constructed with the same parameters return
     * the same hashCode.
     */
    @Test
    public void twoSampledMultivariateHypergeometricDistributionsConstructedWithSameParametersShouldHaveSameHashCode() {
        assertEquals(MULTIVARIATE_DISTRIBUTION.hashCode(),
                new SampledMultivariateHypergeometricDistribution(
                        createProbabilityMassFunctions(FIVE_HUNDRED, THREE_HUNDRED, ONE_HUNDRED), POPULATION_SIZE,
                        SAMPLE_SIZE, NUMBER_OF_ITERATIONS).hashCode());
    }

    /**
     * Verifies that two sampled multivariate hypergeometric distributions with different probability mass functions are
     * not equal.
     */
    @Test
    public void sampledMultivariateHypergeometricDistributionsWithDifferentProbabilityMassFunctionsShouldNotBeEqual() {
        assertFalse(MULTIVARIATE_DISTRIBUTION.equals(new SampledMultivariateHypergeometricDistribution(
                createProbabilityMassFunctions(FIVE_HUNDRED, THREE_HUNDRED, ONE_HUNDRED, ONE_HUNDRED), POPULATION_SIZE,
                SAMPLE_SIZE, NUMBER_OF_ITERATIONS)));
    }

    /**
     * Verifies that two sampled multivariate hypergeometric distributions with different probability mass functions
     * have different hash codes.
     */
    @Test
    public void sampledMultivariateHypergeometricDistributionsWithDifferentPMFsShouldHaveDifferentHashCodes() {
        assertFalse(MULTIVARIATE_DISTRIBUTION.hashCode() == new SampledMultivariateHypergeometricDistribution(
                createProbabilityMassFunctions(FIVE_HUNDRED, THREE_HUNDRED, ONE_HUNDRED, ONE_HUNDRED), POPULATION_SIZE,
                SAMPLE_SIZE, NUMBER_OF_ITERATIONS).hashCode());
    }

    /**
     * Verifies that two sampled multivariate hypergeometric distributions with different number of iterations are not
     * equal.
     */
    @Test
    public void twoSampledMultivariateHypergeometricDistributionsWithDifferentNumberOfIterationsShouldNotBeEqual() {
        assertFalse(MULTIVARIATE_DISTRIBUTION.equals(new SampledMultivariateHypergeometricDistribution(
                createProbabilityMassFunctions(FIVE_HUNDRED, THREE_HUNDRED, ONE_HUNDRED), POPULATION_SIZE, SAMPLE_SIZE,
                NUMBER_OF_ITERATIONS + 1L)));
    }

    /**
     * Verifies that two sampled multivariate hypergeometric distributions with different number of iterations have
     * different hash codes.
     */
    @Test
    public void sampledMultivariateHypergeometricDistributionsWithDifferentNoOfIterationsShouldHaveDifferentHashCode() {
        assertFalse(MULTIVARIATE_DISTRIBUTION.hashCode() == new SampledMultivariateHypergeometricDistribution(
                createProbabilityMassFunctions(FIVE_HUNDRED, THREE_HUNDRED, ONE_HUNDRED), POPULATION_SIZE, SAMPLE_SIZE,
                NUMBER_OF_ITERATIONS + 1L).hashCode());
    }
}
