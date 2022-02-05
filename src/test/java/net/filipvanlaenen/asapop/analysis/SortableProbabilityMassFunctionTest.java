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
     * The magic number eight.
     */
    private static final long EIGHT = 8L;
    /**
     * The magic number nine.
     */
    private static final long NINE = 9L;
    /**
     * The magic number 0.81.
     */
    private static final double EIGHTY_ONE_PERCENT = 0.81;
    /**
     * The magic number 0.79.
     */
    private static final double SEVENTY_NINE_PERCENT = 0.79;

    /**
     * Verifies that the median is calculated correctly for a hypergeometric distribution of 1 out of 4 in a population
     * of 5.
     */
    @Test
    public void medianShouldBe1ForHypergeometricDistribution1OutOf4In5() {
        assertEquals(1L, new HypergeometricDistribution(1L, FOUR, FIVE).getMedian());
    }

    /**
     * Verifies that the median is calculated correctly for a hypergeometric distribution of 2 out of 4 in a population
     * of 5.
     */
    @Test
    public void medianShouldBe2ForHypergeometricDistribution2OutOf4In5() {
        assertEquals(2L, new HypergeometricDistribution(2L, FOUR, FIVE).getMedian());
    }

    /**
     * Verifies that the 80% confidence interval [0, 0] is calculated correctly for a hypergeometric distribution of 0
     * out of 8 in a population of 9.
     */
    @Test
    public void confidenceInterval81ShouldBe0To0ForHypergeometricDistribution0OutOf8In9() {
        HypergeometricDistribution binomialDistribution = new HypergeometricDistribution(0L, EIGHT, NINE);
        assertEquals(new ConfidenceInterval<Long>(0L, 2L),
                binomialDistribution.getConfidenceInterval(EIGHTY_ONE_PERCENT));
    }

    /**
     * Verifies that the 91% confidence interval [0, 1] is calculated correctly for a hypergeometric distribution of 0
     * out of 8 in a population of 9.
     */
    @Test
    public void confidenceInterval79ShouldBe0To1ForHypergeometricDistribution0OutOf8In9() {
        HypergeometricDistribution binomialDistribution = new HypergeometricDistribution(0L, EIGHT, NINE);
        assertEquals(new ConfidenceInterval<Long>(0L, 1L),
                binomialDistribution.getConfidenceInterval(SEVENTY_NINE_PERCENT));
    }
}
