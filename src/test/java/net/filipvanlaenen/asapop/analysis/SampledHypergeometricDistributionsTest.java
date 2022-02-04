package net.filipvanlaenen.asapop.analysis;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SampledHypergeometricDistributions</code> class.
 */
public class SampledHypergeometricDistributionsTest {
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number five.
     */
    private static final long FIVE = 5L;
    /**
     * The magic number 10.
     */
    private static final long TEN = 10L;

    /**
     * Verifies that it retrieves a sampled hypergeometric distribution that is equal to the correct sampled
     * hypergeometric distribution.
     */
    @Test
    public void shouldRetrieveTheCorrectSampledHypergeometricDistribution() {
        assertEquals(SampledHypergeometricDistribution.create(1L, FOUR, FIVE, TEN),
                SampledHypergeometricDistributions.get(1L, FOUR, FIVE, TEN));
    }

    /**
     * Verifies that it retrieves the same instance when asking for the same sampled hypergeometric distribution.
     */
    @Test
    public void shouldRetrieveTheSameObjectWhenAskingForTheSameSampledHypergeometricDistribution() {
        assertSame(SampledHypergeometricDistributions.get(1L, FOUR, FIVE, TEN),
                SampledHypergeometricDistributions.get(1L, FOUR, FIVE, TEN));
    }

    /**
     * Verifies that it retrieves a sampled hypergeometric distribution with more samples than what was cached when
     * requested.
     */
    @Test
    public void shouldRetrieveAHypergeometricDistributionWithMoreSamplesWhenRequested() {
        SampledHypergeometricDistributions.get(1L, FOUR, FOUR, TEN);
        assertEquals(FIVE, SampledHypergeometricDistributions.get(1L, FOUR, FIVE, TEN).getNumberOfSamples());
    }

    /**
     * Verifies that it retrieves a sampled hypergeometric distribution with more samples than what was requested if
     * cached.
     */
    @Test
    public void shouldRetrieveAHypergeometricDistributionWithMoreSamplesWhenAvailable() {
        SampledHypergeometricDistributions.get(1L, FOUR, FIVE, TEN);
        assertEquals(FIVE, SampledHypergeometricDistributions.get(1L, FOUR, FOUR, TEN).getNumberOfSamples());
    }
}