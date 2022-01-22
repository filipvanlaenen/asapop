package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>BinomialDistributions</code> class.
 */
public class BinomialDistributionsTest {
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number five.
     */
    private static final long FIVE = 5L;

    /**
     * Verifies that it retrieves a binomial distribution that is equal to the correct binomial distribution.
     */
    @Test
    public void shouldRetrieveTheCorrectBinomialDistribution() {
        assertEquals(BinomialDistribution.create(1L, FOUR, FIVE), BinomialDistributions.get(1L, FOUR, FIVE));
    }
}
