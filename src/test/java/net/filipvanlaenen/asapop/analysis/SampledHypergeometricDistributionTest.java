package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SampledHypergeometricDistribution</code> class.
 */
public class SampledHypergeometricDistributionTest {
    /**
     * The magic number three.
     */
    private static final long THREE = 3L;
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number five.
     */
    private static final long FIVE = 5L;
    /**
     * The magic number seven.
     */
    private static final long SEVEN = 7L;
    /**
     * The magic number eight.
     */
    private static final long EIGHT = 8L;
    /**
     * The magic number nine.
     */
    private static final long NINE = 9L;
    /**
     * The magic number ten.
     */
    private static final long TEN = 10L;
    /**
     * The magic number seventy.
     */
    private static final long SEVENTY = 70L;
    /**
     * The magic number one hundred five.
     */
    private static final long ONE_HUNDRED_FIVE = 105L;
    /**
     * A hypergeometric distribution to run the tests on.
     */
    private static final SampledHypergeometricDistribution DISTRIBUTION_1_4_5_9 = new SampledHypergeometricDistribution(
            1L, FOUR, FIVE, NINE);

    /**
     * Verifies that the number of samples is returned correctly.
     */
    @Test
    public void numberOfSamplesShouldBeReturnedCorrectly() {
        assertEquals(FIVE, DISTRIBUTION_1_4_5_9.getNumberOfSamples());
    }

    /**
     * Verifies that the key weight is calculated correctly.
     */
    @Test
    public void keyWeightShouldBeLengthOfRange() {
        assertEquals(new BigDecimal(2), DISTRIBUTION_1_4_5_9.getKeyWeight(Range.get(0, 1)));
    }

    /**
     * Verifies that the probability mass is calculated correctly.
     */
    @Test
    public void probabilityMassShouldBeCalculatedCorrectly() {
        assertEquals(new BigDecimal(SEVENTY), DISTRIBUTION_1_4_5_9.getProbabilityMass(Range.get(2, THREE)));
        assertEquals(new BigDecimal(ONE_HUNDRED_FIVE),
                new SampledHypergeometricDistribution(1L, FOUR, FOUR, TEN).getProbabilityMass(Range.get(2, FOUR)));
    }

    /**
     * Verifies that the ranges are distributed correctly over the population size.
     */
    @Test
    public void rangesShouldBeDistributedCorrectlyOverThePopulationSize() {
        assertNotNull(
                new SampledHypergeometricDistribution(1L, FOUR, FOUR, NINE).getProbabilityMass(Range.get(SEVEN, NINE)));
        assertNotNull(
                new SampledHypergeometricDistribution(1L, FOUR, FIVE, TEN).getProbabilityMass(Range.get(EIGHT, TEN)));
    }
}
