package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SampledMultivariateHypergeometricDistribution</code> class.
 */
public class SampledMultivariateHypergeometricDistributionTest {
    /**
     * The number of iterations in the multivariate hypergoemetric distribution.
     */
    private static final long NUMBER_OF_ITERATIONS = 20_000L;
    /**
     * The number of samples in the hypergeometric distributions.
     */
    private static final long NUMBER_OF_SAMPLES = 10_000L;
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
    private static final double SMALL_DELTA = 1E-6;
    /**
     * Precision for floating point assertions where low precision can be expected.
     */
    private static final double LARGE_DELTA = 0.05;
    /**
     * The magic number one quarter.
     */
    private static final double ONE_QUARTER = 0.25D;
    /**
     * The magic number one third.
     */
    private static final double ONE_THIRD = 0.3333333D;
    /**
     * The magic number 0.36.
     */
    private static final double DOUBLE_0_36 = 0.36D;
    /**
     * The magic number one half.
     */
    private static final double ONE_HALF = 0.5D;
    /**
     * The magic number one hundred.
     */
    private static final long ONE_HUNDRED = 100L;
    /**
     * The magic numbet two hundred ninety-nine.
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
     * The magic number five hundred.
     */
    private static final long FIVE_HUNDRED = 500L;

    /**
     * Verifies that the requested number of iterations are performed.
     */
    @Test
    public void shouldPerformTheRequestedNumberOfIterations() {
        List<SampledHypergeometricDistribution> probabilityMassFunctions = createProbabilityMassFunctions(THREE_HUNDRED,
                THREE_HUNDRED, THREE_HUNDRED);
        SampledMultivariateHypergeometricDistribution multivariateDistribution = new SampledMultivariateHypergeometricDistribution(
                probabilityMassFunctions, POPULATION_SIZE, SAMPLE_SIZE, NUMBER_OF_ITERATIONS);
        assertEquals(NUMBER_OF_ITERATIONS, multivariateDistribution.getNumberOfIterations());
    }

    /**
     * Verifies that when there are two candidates with the same support, these two candidates are certain to win the
     * first round.
     */
    @Test
    public void twoCandidatesWithTheSameSupportShouldBeCertainToWin() {
        assertProbabilityEquals(1, SMALL_DELTA, 0, 1, THREE_HUNDRED, THREE_HUNDRED);
    }

    /**
     * Verifies that when there are three candidates with the same support, a pair of two of these candidates has a
     * probability of one third to win the first round.
     */
    @Test
    public void threeCandidatesWithTheSameSupportShouldEachHaveAProbabilityOfOneThird() {
        assertProbabilityEquals(ONE_THIRD, SMALL_DELTA, 0, 1, THREE_HUNDRED, THREE_HUNDRED, THREE_HUNDRED);
    }

    /**
     * Verifies that when there are three candidates with almost the same support, a pair of two largest candidates has
     * a probability of slightly more than one third to win the first round.
     */
    @Test
    public void twoLargestOfThreeCandidatesWithTheAlmostSameSupportShouldHaveAProbabilityAboveOneThird() {
        assertProbabilityEquals(DOUBLE_0_36, LARGE_DELTA, 0, 1, THREE_HUNDRED_ONE, THREE_HUNDRED,
                TWO_HUNDRED_NINETY_NINE);
    }

    /**
     * Verifies that many small candidates do not disturb the calculation for the lagre candidates.
     */
    @Test
    public void manySmallCandidatesShouldNotDisturbTheCalculationForTheLargeCandidates() {
        assertProbabilityEquals(DOUBLE_0_36, LARGE_DELTA, 0, 1, THREE_HUNDRED_ONE, THREE_HUNDRED,
                TWO_HUNDRED_NINETY_NINE, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L);
    }

    /**
     * Verifies that when there are three candidates, one with half of the support, and two with equal small support,
     * the probability that the large candidate wins the first round is one half.
     */
    @Test
    public void aCandidateWithHalfOfTheSupportAgainstTwoSmallCandidatesShouldHaveProbabilityOfOneHalf() {
        assertProbabilityEquals(ONE_HALF, LARGE_DELTA, 0, FIVE_HUNDRED, ONE_HUNDRED, ONE_HUNDRED);
    }

    /**
     * Verifies that when there are three candidates, one with half of the support, and two with equal small support,
     * the probability that one of the smaller candidates wins the first round together with the large candidate is one
     * quarter.
     */
    @Test
    public void aCandidateWithHalfOfTheSupportTogetherWithOneOfTwoSmallCandidatesShouldHaveProbabilityOfOneQuarter() {
        assertProbabilityEquals(ONE_QUARTER, LARGE_DELTA, 0, 1, FIVE_HUNDRED, ONE_HUNDRED, ONE_HUNDRED);
    }

    /**
     * Asserts the probability of a pair of candidates to be equal within a given delta.
     *
     * @param expected The expected probability.
     * @param delta    The margin of error.
     * @param i0       The index of the first candidate in the pair.
     * @param i1       The index of the second candidate in the pair.
     * @param values   The sample values for the candidates.
     */
    private void assertProbabilityEquals(double expected, double delta, int i0, int i1, long... values) {
        List<SampledHypergeometricDistribution> probabilityMassFunctions = createProbabilityMassFunctions(values);
        SampledMultivariateHypergeometricDistribution multivariateDistribution = new SampledMultivariateHypergeometricDistribution(
                probabilityMassFunctions, POPULATION_SIZE, SAMPLE_SIZE, NUMBER_OF_ITERATIONS);
        double actual = multivariateDistribution.getProbabilityMass(probabilityMassFunctions.get(i0),
                probabilityMassFunctions.get(i1));
        assertEquals(expected, actual, delta);
    }

    /**
     * Asserts of the probability of a single candidate to be equal within a given delta.
     *
     * @param expected The expected probability.
     * @param delta    The margin of error.
     * @param i        The index of the candidate.
     * @param values   The sample values for the candidates.
     */
    private void assertProbabilityEquals(double expected, double delta, int i, long... values) {
        List<SampledHypergeometricDistribution> probabilityMassFunctions = createProbabilityMassFunctions(values);
        SampledMultivariateHypergeometricDistribution multivariateDistribution = new SampledMultivariateHypergeometricDistribution(
                probabilityMassFunctions, POPULATION_SIZE, SAMPLE_SIZE, NUMBER_OF_ITERATIONS);
        double actual = multivariateDistribution.getProbabilityMass(probabilityMassFunctions.get(i));
        assertEquals(expected, actual, delta);
    }

    /**
     * Creates a list of probability mass functions based on a set of values.
     *
     * @param values The sample values for the candidates.
     * @return A list of probability mass functions based on a set of values.
     */
    private List<SampledHypergeometricDistribution> createProbabilityMassFunctions(long... values) {
        List<SampledHypergeometricDistribution> probabilityMassFunctions = new ArrayList<SampledHypergeometricDistribution>();
        for (long value : values) {
            probabilityMassFunctions.add(
                    SampledHypergeometricDistributions.get(value, SAMPLE_SIZE, NUMBER_OF_SAMPLES, POPULATION_SIZE));
        }
        return probabilityMassFunctions;
    }
}
