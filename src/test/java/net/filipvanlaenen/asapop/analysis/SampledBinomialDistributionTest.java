package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SampledBinomialDistribution</code> class.
 */
public class SampledBinomialDistributionTest {
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number five.
     */
    private static final long FIVE = 5L;
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
     * A binomial distribution to run the tests on.
     */
    private static final SampledBinomialDistribution SAMPLED_BINOMIAL_DISTRIBUTION = SampledBinomialDistribution
            .create(1L, FOUR, FIVE, TEN);

    /**
     * Verifies that the number of samples is returned correctly.
     */
    @Test
    public void numberOfSamplesShouldBeReturnedCorrectly() {
        assertEquals(FIVE, SAMPLED_BINOMIAL_DISTRIBUTION.getNumberOfSamples());
    }

    /**
     * Verifies that the probability mass is calculated correctly.
     */
    @Test
    public void probabilityMassIsCalculatedCorrectly() {
        assertEquals(new BigDecimal(112), SAMPLED_BINOMIAL_DISTRIBUTION.getProbabilityMass(Range.get(2, 3)));
    }

    /**
     * Verifies that the probability mass sum is calculated correctly.
     */
    @Test
    public void probabilityMassSumIsCalculatedCorrectly() {
        assertEquals(new BigDecimal(432), SAMPLED_BINOMIAL_DISTRIBUTION.getProbabilityMassSum());
    }

    /**
     * Verifies that the keys, i.e. the ranges, are returned correctly for five samples in a population size of ten.
     */
    @Test
    public void sortedKeysAreReturnedCorrectlyForFiveSamplesInAPopulationSizeOfTen() {
        assertEquals(
                List.of(Range.get(0, 1), Range.get(2, 3), Range.get(FOUR, FIVE), Range.get(6, 7), Range.get(8, TEN)),
                SAMPLED_BINOMIAL_DISTRIBUTION.getSortedKeys());
    }

    /**
     * Verifies that the keys, i.e. the ranges, are returned correctly for five samples in a population size of nine.
     */
    @Test
    public void sortedKeysAreReturnedCorrectlyForFiveSamplesInAPopulationSizeOfNine() {
        assertEquals(
                List.of(Range.get(0, 1), Range.get(2, 3), Range.get(FOUR, FIVE), Range.get(6, 7), Range.get(8, NINE)),
                SampledBinomialDistribution.create(1L, FOUR, FIVE, NINE).getSortedKeys());
    }

    /**
     * Verifies that the keys, i.e. the ranges, are returned correctly for five samples in a population size of eleven.
     */
    @Test
    public void sortedKeysAreReturnedCorrectlyForFiveSamplesInAPopulationSizeOfEleven() {
        assertEquals(
                List.of(Range.get(0, 1), Range.get(2, 3), Range.get(FOUR, FIVE), Range.get(6, 8),
                        Range.get(NINE, ELEVEN)),
                SampledBinomialDistribution.create(1L, FOUR, FIVE, ELEVEN).getSortedKeys());
    }

    /**
     * Verifies that a sampled binomial distribution is not equal to null.
     */
    @Test
    public void aSampledBinomialDistributionShouldNotBeEqualToNull() {
        assertFalse(SAMPLED_BINOMIAL_DISTRIBUTION.equals(null));
    }

    /**
     * Verifies that a sampled binomial distribution is not equal to an object of another class, like a string.
     */
    @Test
    public void aSampledBinomialDistributionShouldNotBeEqualToAString() {
        assertFalse(SAMPLED_BINOMIAL_DISTRIBUTION.equals(""));
    }

    /**
     * Verifies that a sampled binomial distribution is equal to itself.
     */
    @Test
    public void aSampledBinomialDistributionShouldBeEqualToItself() {
        assertTrue(SAMPLED_BINOMIAL_DISTRIBUTION.equals(SAMPLED_BINOMIAL_DISTRIBUTION));
    }

    /**
     * Verifies that calling hashCode twice on a sampled binomial distribution returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnASampledBinomialDistributionReturnsTheSameResult() {
        assertEquals(SAMPLED_BINOMIAL_DISTRIBUTION.hashCode(), SAMPLED_BINOMIAL_DISTRIBUTION.hashCode());
    }

    /**
     * Verifies that two sampled binomial distributions constructed with the same parameter are equal.
     */
    @Test
    public void twoSampledBinomialDistributionsConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(SAMPLED_BINOMIAL_DISTRIBUTION, SampledBinomialDistribution.create(1L, FOUR, FIVE, TEN));
    }

    /**
     * Verifies that two sampled binomial distributions constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoSampledBinomialDistributionsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(SAMPLED_BINOMIAL_DISTRIBUTION.hashCode(),
                SampledBinomialDistribution.create(1L, FOUR, FIVE, TEN).hashCode());
    }

    /**
     * Verifies that two different sampled binomial distribution with different values are not equal.
     */
    @Test
    public void twoDifferentSampledBinomialDistributionsWithDifferentValuesShouldNotBeEqual() {
        assertFalse(SAMPLED_BINOMIAL_DISTRIBUTION.equals(SampledBinomialDistribution.create(2L, FOUR, FIVE, TEN)));
    }

    /**
     * Verifies that two different sampled binomial distribution with different values have different hash codes.
     */
    @Test
    public void twoDifferentSampledBinomialDistributionsWithDifferentValuesShouldHaveDifferentHashCodes() {
        assertFalse(SAMPLED_BINOMIAL_DISTRIBUTION.hashCode() == SampledBinomialDistribution.create(2L, FOUR, FIVE, TEN)
                .hashCode());
    }
}
