package net.filipvanlaenen.asapop.analysis;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>HypergeometricDistributions</code> class.
 */
public class HypergeometricDistributionsTest {
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number five.
     */
    private static final long FIVE = 5L;
    /**
     * The magic number 997.
     */
    private static final long L997 = 997L;

    /**
     * Verifies that it retrieves a hypergeometric distribution that is equal to the correct hypergeometric
     * distribution.
     */
    @Test
    public void shouldRetrieveTheCorrectHypergeometricDistribution() {
        assertEquals(new HypergeometricDistribution(1L, FOUR, FIVE), HypergeometricDistributions.get(1L, FOUR, FIVE));
    }

    /**
     * Verifies that it retrieves the same instance when asking for the same hypergeometric distribution.
     */
    @Test
    public void shouldRetrieveTheSameObjectWhenAskingForTheSameHypergeometricDistribution() {
        assertSame(HypergeometricDistributions.get(1L, FOUR, FIVE), HypergeometricDistributions.get(1L, FOUR, FIVE));
    }

    /**
     * Verifies that it calculates a hypergeometric distribution if it hasn't been retrieved yet.
     */
    @Test
    public void shouldRetrieveANewHypergeometricDistribution() {
        long populationSize = Instant.now().toEpochMilli() % L997;
        assertEquals(new HypergeometricDistribution(1L, FOUR, populationSize),
                HypergeometricDistributions.get(1L, FOUR, populationSize));
    }
}
