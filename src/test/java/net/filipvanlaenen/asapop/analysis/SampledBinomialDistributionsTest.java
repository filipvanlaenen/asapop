package net.filipvanlaenen.asapop.analysis;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SampledBinomialDistributions</code> class.
 */
public class SampledBinomialDistributionsTest {
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
     * Verifies that it retrieves a sampled binomial distribution that is equal to the correct sampled binomial
     * distribution.
     */
    @Test
    public void shouldRetrieveTheCorrectSampledBinomialDistribution() {
        assertEquals(SampledBinomialDistribution.create(1L, FOUR, FIVE, TEN),
                SampledBinomialDistributions.get(1L, FOUR, FIVE, TEN));
    }

    /**
     * Verifies that it retrieves the same instance when asking for the same sampled binomial distribution.
     */
    @Test
    public void shouldRetrieveTheSameObjectWhenAskingForTheSameSampledBinomialDistribution() {
        assertSame(SampledBinomialDistributions.get(1L, FOUR, FIVE, TEN),
                SampledBinomialDistributions.get(1L, FOUR, FIVE, TEN));
    }
}
