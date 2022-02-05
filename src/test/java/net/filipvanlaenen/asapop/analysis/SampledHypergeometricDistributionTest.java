package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

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
     * The magic number six.
     */
    private static final long SIX = 6L;
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
     * The magic number eleven.
     */
    private static final long ELEVEN = 11L;
    /**
     * The magic number one hundred twelve.
     */
    private static final long ONE_HUNDRED_TWELVE = 112L;
    /**
     * The magic number four hundred thirty-two.
     */
    private static final long FOUR_HUNDRED_THIRTY_TWO = 432L;
    /**
     * A hypergeometric distribution to run the tests on.
     */
    private static final SampledHypergeometricDistribution SAMPLED_HYPERGEOMETRIC_DISTRIBUTION = new SampledHypergeometricDistribution(
            1L, FOUR, FIVE, TEN);

    /**
     * Verifies that the number of samples is returned correctly.
     */
    @Test
    public void numberOfSamplesShouldBeReturnedCorrectly() {
        assertEquals(FIVE, SAMPLED_HYPERGEOMETRIC_DISTRIBUTION.getNumberOfSamples());
    }

    /**
     * Verifies that the key weight is calculated correctly.
     */
    @Test
    public void keyWeightShouldBeLengthOfRange() {
        assertEquals(new BigDecimal(2), SAMPLED_HYPERGEOMETRIC_DISTRIBUTION.getKeyWeight(Range.get(0, 1)));
    }

    /**
     * Verifies that the probability mass is calculated correctly.
     */
    @Test
    public void probabilityMassIsCalculatedCorrectly() {
        assertEquals(new BigDecimal(ONE_HUNDRED_TWELVE),
                SAMPLED_HYPERGEOMETRIC_DISTRIBUTION.getProbabilityMass(Range.get(2, THREE)));
    }

    /**
     * Verifies that a sampled hypergeometric distribution is not equal to null.
     */
    @Test
    public void aSampledHypergeometricDistributionShouldNotBeEqualToNull() {
        assertFalse(SAMPLED_HYPERGEOMETRIC_DISTRIBUTION.equals(null));
    }

    /**
     * Verifies that a sampled hypergeometric distribution is not equal to an object of another class, like a string.
     */
    @Test
    public void aSampledHypergeometricDistributionShouldNotBeEqualToAString() {
        assertFalse(SAMPLED_HYPERGEOMETRIC_DISTRIBUTION.equals(""));
    }

    /**
     * Verifies that a sampled hypergeometric distribution is equal to itself.
     */
    @Test
    public void aSampledHypergeometricDistributionShouldBeEqualToItself() {
        assertTrue(SAMPLED_HYPERGEOMETRIC_DISTRIBUTION.equals(SAMPLED_HYPERGEOMETRIC_DISTRIBUTION));
    }

    /**
     * Verifies that calling hashCode twice on a sampled hypergeometric distribution returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnASampledHypergeometricDistributionReturnsTheSameResult() {
        assertEquals(SAMPLED_HYPERGEOMETRIC_DISTRIBUTION.hashCode(), SAMPLED_HYPERGEOMETRIC_DISTRIBUTION.hashCode());
    }

    /**
     * Verifies that two sampled hypergeometric distributions constructed with the same parameter are equal.
     */
    @Test
    public void twoSampledHypergeometricDistributionsConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(SAMPLED_HYPERGEOMETRIC_DISTRIBUTION, new SampledHypergeometricDistribution(1L, FOUR, FIVE, TEN));
    }

    /**
     * Verifies that two sampled hypergeometric distributions constructed with the same parameters return the same
     * hashCode.
     */
    @Test
    public void twoSampledHypergeometricDistributionsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(SAMPLED_HYPERGEOMETRIC_DISTRIBUTION.hashCode(),
                new SampledHypergeometricDistribution(1L, FOUR, FIVE, TEN).hashCode());
    }

    /**
     * Verifies that two different sampled hypergeometric distribution with different values are not equal.
     */
    @Test
    public void twoDifferentSampledHypergeometricDistributionsWithDifferentValuesShouldNotBeEqual() {
        assertFalse(
                SAMPLED_HYPERGEOMETRIC_DISTRIBUTION.equals(new SampledHypergeometricDistribution(2L, FOUR, FIVE, TEN)));
    }

    /**
     * Verifies that two different sampled hypergeometric distribution with different values have different hash codes.
     */
    @Test
    public void twoDifferentSampledHypergeometricDistributionsWithDifferentValuesShouldHaveDifferentHashCodes() {
        assertFalse(SAMPLED_HYPERGEOMETRIC_DISTRIBUTION
                .hashCode() == new SampledHypergeometricDistribution(2L, FOUR, FIVE, TEN).hashCode());
    }
}
