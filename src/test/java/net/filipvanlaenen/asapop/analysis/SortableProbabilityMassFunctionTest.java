package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SortableProbabilityMassFunction</code> class.
 */
public class SortableProbabilityMassFunctionTest {
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number five.
     */
    private static final long FIVE = 5L;

    /**
     * Verifies that the median is calculated correctly for a binomial distribution of 1 out of 4 in a population of 5.
     */
    @Test
    public void medianShouldBe1ForBinomialDistribution1OutOf4In5() {
        assertEquals(1L, BinomialDistribution.create(1L, FOUR, FIVE).getMedian());
    }

    /**
     * Verifies that the median is calculated correctly for a binomial distribution of 2 out of 4 in a population of 5.
     */
    @Test
    public void medianShouldBe2ForBinomialDistribution2OutOf4In5() {
        assertEquals(2L, BinomialDistribution.create(2L, FOUR, FIVE).getMedian());
    }
}
