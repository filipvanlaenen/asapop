package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     * A hypergeometric distribution to run the tests on.
     */
    private static final SampledHypergeometricDistribution DISTRIBUTION_1_4_4_9 = new SampledHypergeometricDistribution(
            1L, FOUR, FOUR, NINE);
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
    }

    /**
     * Verifies that the ranges are distributed correctly over the population size.
     */
    @Test
    public void rangesShouldBeDistributedCorrectlyOverThePopulationSize() {
        assertNotNull(DISTRIBUTION_1_4_4_9.getProbabilityMass(Range.get(SEVEN, NINE)));
        assertNotNull(
                new SampledHypergeometricDistribution(1L, FOUR, FIVE, TEN).getProbabilityMass(Range.get(EIGHT, TEN)));
    }

    /**
     * Verifies that a sampled hypergeometric distribution is not equal to null.
     */
    @Test
    public void aSampledHypergeometricDistributionShouldNotBeEqualToNull() {
        assertFalse(DISTRIBUTION_1_4_5_9.equals(null));
    }

    /**
     * Verifies that a sampled hypergeometric distribution is not equal to an object of another class, like a string.
     */
    @Test
    public void aSampledHypergeometricDistributionShouldNotBeEqualToAString() {
        assertFalse(DISTRIBUTION_1_4_5_9.equals(""));
    }

    /**
     * Verifies that a sampled hypergeometric distribution is equal to itself.
     */
    @Test
    public void aSampledHypergeometricDistributionShouldBeEqualToItself() {
        assertTrue(DISTRIBUTION_1_4_5_9.equals(DISTRIBUTION_1_4_5_9));
    }

    /**
     * Verifies that calling hashCode twice on a sampled hypergeometric distribution returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnASampledHypergeometricDistributionReturnsTheSameResult() {
        assertEquals(DISTRIBUTION_1_4_5_9.hashCode(), DISTRIBUTION_1_4_5_9.hashCode());
    }

    /**
     * Verifies that two sampled hypergeometric distributions constructed with the same parameter are equal.
     */
    @Test
    public void twoSampledHypergeometricDistributionsConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(DISTRIBUTION_1_4_5_9, new SampledHypergeometricDistribution(1L, FOUR, FIVE, NINE));
    }

    /**
     * Verifies that two sampled hypergeometric distributions constructed with the same parameters return the same
     * hashCode.
     */
    @Test
    public void twoSampledHypergeometricDistributionsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(DISTRIBUTION_1_4_5_9.hashCode(),
                new SampledHypergeometricDistribution(1L, FOUR, FIVE, NINE).hashCode());
    }

    /**
     * Verifies that two different sampled hypergeometric distribution with different values are not equal.
     */
    @Test
    public void twoDifferentSampledHypergeometricDistributionsWithDifferentValuesShouldNotBeEqual() {
        assertFalse(DISTRIBUTION_1_4_5_9.equals(new SampledHypergeometricDistribution(2L, FOUR, FIVE, TEN)));
    }

    /**
     * Verifies that two different sampled hypergeometric distribution with different values have different hash codes.
     */
    @Test
    public void twoDifferentSampledHypergeometricDistributionsWithDifferentValuesShouldHaveDifferentHashCodes() {
        assertFalse(DISTRIBUTION_1_4_5_9.hashCode() == new SampledHypergeometricDistribution(2L, FOUR, FIVE, TEN)
                .hashCode());
    }
}
