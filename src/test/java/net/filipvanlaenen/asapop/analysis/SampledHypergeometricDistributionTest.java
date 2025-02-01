package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.math.MathContext;

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
     * Another hypergeometric distribution to run the tests on.
     */
    private static final SampledHypergeometricDistribution DISTRIBUTION_1_4_5_9 =
            new SampledHypergeometricDistribution(1L, FOUR, FIVE, NINE);

    /**
     * Verifies that the number of samples is correct, i.e. when five samples are requested, five samples are
     * calculated.
     *
     * Note: in order to kill a mutant, a new distribution has to be created when running this unit test.
     */
    @Test
    public void numberOfSamplesShouldBeCorrect() {
        assertEquals(FIVE, new SampledHypergeometricDistribution(1L, FOUR, FIVE, NINE).getNumberOfSamples());
    }

    /**
     * Verifies that the key weight is calculated correctly.
     */
    @Test
    public void keyWeightShouldBeLengthOfRange() {
        assertEquals(new BigDecimal(2), DISTRIBUTION_1_4_5_9.getKeyWeight(new Range(0, 1)));
    }

    /**
     * Verifies that the probability mass is calculated correctly.
     */
    @Test
    public void probabilityMassShouldBeCalculatedCorrectly() {
        assertEquals(new BigDecimal(SEVENTY), DISTRIBUTION_1_4_5_9.getProbabilityMass(new Range(2, THREE)));
        assertEquals(new BigDecimal(ONE_HUNDRED_FIVE),
                new SampledHypergeometricDistribution(1L, FOUR, FOUR, TEN).getProbabilityMass(new Range(2, FOUR)));
    }

    /**
     * Verifies that the ranges are distributed correctly over the population size.
     */
    @Test
    public void rangesShouldBeDistributedCorrectlyOverThePopulationSize() {
        assertNotNull(
                new SampledHypergeometricDistribution(1L, FOUR, FOUR, NINE).getProbabilityMass(new Range(SEVEN, NINE)));
        assertNotNull(
                new SampledHypergeometricDistribution(1L, FOUR, FIVE, TEN).getProbabilityMass(new Range(EIGHT, TEN)));
    }

    /**
     * Verifies that when the threshold is set to zero, the probability mass fraction above it is 1 if the sample value
     * is larger than zero.
     */
    @Test
    public void probabilityMassFractionShouldBeOneAboveZeroForNonZeroSample() {
        assertEquals(BigDecimal.ONE, DISTRIBUTION_1_4_5_9.getProbabilityMassFractionAbove(0L));
    }

    /**
     * Verifies that when the threshold is equal to the lower bound of the lowest range with non-zero probability mass,
     * the probability mass fraction is correctly reduced.
     */
    @Test
    public void probabilityMassFractionShouldBeReducedForPartOfLowestRange() {
        BigDecimal sum = DISTRIBUTION_1_4_5_9.getProbabilityMassSum();
        BigDecimal expected =
                sum.subtract(DISTRIBUTION_1_4_5_9.getProbabilityMass(new Range(2, THREE)), MathContext.DECIMAL128)
                        .divide(sum, MathContext.DECIMAL128);
        assertEquals(expected, DISTRIBUTION_1_4_5_9.getProbabilityMassFractionAbove(2L));
    }

    /**
     * Verifies that when the threshold is equal to the upper bound of the lowest range with non-zero probability mass,
     * the probability mass fraction is correctly reduced.
     */
    @Test
    public void probabilityMassFractionShouldBeReducedForEntireLowestRange() {
        BigDecimal sum = DISTRIBUTION_1_4_5_9.getProbabilityMassSum();
        BigDecimal expected =
                sum.subtract(DISTRIBUTION_1_4_5_9.getProbabilityMass(new Range(2, THREE)).multiply(new BigDecimal("2"),
                        MathContext.DECIMAL128), MathContext.DECIMAL128).divide(sum, MathContext.DECIMAL128);
        assertEquals(expected, DISTRIBUTION_1_4_5_9.getProbabilityMassFractionAbove(THREE));
    }
}
