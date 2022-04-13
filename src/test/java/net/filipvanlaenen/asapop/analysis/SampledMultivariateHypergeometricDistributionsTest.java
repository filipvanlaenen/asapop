package net.filipvanlaenen.asapop.analysis;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SampledMultivariateHypergeometricDistributions</code> class.
 */
public class SampledMultivariateHypergeometricDistributionsTest {
    /**
     * The number of iterations in the multivariate hypergeometric distribution.
     */
    private static final long NUMBER_OF_ITERATIONS = 200L;
    /**
     * The number of samples in the hypergeometric distributions.
     */
    private static final long NUMBER_OF_SAMPLES = 200L;
    /**
     * The sample size for the polls.
     */
    private static final long SAMPLE_SIZE = 1000L;
    /**
     * The population size of the polls.
     */
    private static final long POPULATION_SIZE = 1_000_000L;
    /**
     * A set of probability mass functions to test on.
     */
    private static final List<SampledHypergeometricDistribution> TEST_PMFS = createProbabilityMassFunctions(300L, 200L,
            100L);

    /**
     * Verifies that it retrieves a sampled multivariate hypergeometric distribution that is equal to the correct
     * sampled multivariate hypergeometric distribution.
     */
    @Test
    public void shouldRetrieveTheCorrectSampledMultivariateHypergeometricDistribution() {
        SampledMultivariateHypergeometricDistribution expected;
        expected = new SampledMultivariateHypergeometricDistribution(TEST_PMFS, POPULATION_SIZE, SAMPLE_SIZE,
                NUMBER_OF_ITERATIONS);
        assertEquals(expected, SampledMultivariateHypergeometricDistributions.get(TEST_PMFS, POPULATION_SIZE,
                SAMPLE_SIZE, NUMBER_OF_ITERATIONS));
    }

    /**
     * Verifies that it retrieves the same instance when asking for the same sampled multivariate hypergeometric
     * distribution.
     */
    @Test
    public void shouldRetrieveTheSameObjectWhenAskingForTheSameSampledMultivariateHypergeometricDistribution() {
        assertSame(
                SampledMultivariateHypergeometricDistributions.get(TEST_PMFS, POPULATION_SIZE, SAMPLE_SIZE,
                        NUMBER_OF_ITERATIONS),
                SampledMultivariateHypergeometricDistributions.get(TEST_PMFS, POPULATION_SIZE, SAMPLE_SIZE,
                        NUMBER_OF_ITERATIONS));
    }

    /**
     * Verifies that it retrieves a sampled multivariate hypergeometric distribution with more samples than what was
     * cached when requested.
     */
    @Test
    public void shouldRetrieveASampledMultivariateHypergeometricDistributionWithMoreSamplesWhenRequested() {
        List<SampledHypergeometricDistribution> pmfs = createProbabilityMassFunctions(300L, 200L, 100L, 50L);
        SampledMultivariateHypergeometricDistributions.get(pmfs, POPULATION_SIZE, SAMPLE_SIZE, NUMBER_OF_ITERATIONS);
        assertEquals(NUMBER_OF_ITERATIONS + 1L, SampledMultivariateHypergeometricDistributions
                .get(pmfs, POPULATION_SIZE, SAMPLE_SIZE, NUMBER_OF_ITERATIONS + 1L).getNumberOfIterations());
    }

    /**
     * Verifies that it retrieves a sampled multivariate hypergeometric distribution with more samples than what was
     * requested if cached.
     */
    @Test
    public void shouldRetrieveASampledMultivariateHypergeometricDistributionWithMoreSamplesWhenAvailable() {
        List<SampledHypergeometricDistribution> pmfs = createProbabilityMassFunctions(300L, 200L, 100L, 50L, 50L);
        SampledMultivariateHypergeometricDistributions.get(pmfs, POPULATION_SIZE, SAMPLE_SIZE, NUMBER_OF_ITERATIONS);
        assertEquals(NUMBER_OF_ITERATIONS, SampledMultivariateHypergeometricDistributions
                .get(pmfs, POPULATION_SIZE, SAMPLE_SIZE, NUMBER_OF_ITERATIONS - 1L).getNumberOfIterations());
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
}
