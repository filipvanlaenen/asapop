package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>BinomialDistribution</code> class.
 */
public class BinomialDistributionTest {
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number five.
     */
    private static final long FIVE = 5L;
    /**
     * A binomial distribution to run the tests on.
     */
    private static final BinomialDistribution BINOMIAL_DISTRIBUTION = BinomialDistribution.create(1L, FOUR, FIVE);

    /**
     * Verifies that the probability mass is zero at zero.
     */
    @Test
    public void probabilityMassShouldBeZeroAtZero() {
        assertEquals(BigDecimal.ZERO, BINOMIAL_DISTRIBUTION.getProbabilityMass(0L));
    }

    /**
     * Verifies that the probability mass is four at one.
     */
    @Test
    public void probabilityMassShouldBeFourAtOne() {
        assertEquals(new BigDecimal(FOUR), BINOMIAL_DISTRIBUTION.getProbabilityMass(1L));
    }

    /**
     * Verifies that the probability mass is zero at five.
     */
    @Test
    public void probabilityMassShouldBeZeroAtFive() {
        assertEquals(BigDecimal.ZERO, BINOMIAL_DISTRIBUTION.getProbabilityMass(FIVE));
    }

    /**
     * Verifies that a binomial distribution is not equal to null.
     */
    @Test
    public void aBinomialDistributionShouldNotBeEqualToNull() {
        assertFalse(BINOMIAL_DISTRIBUTION.equals(null));
    }

    /**
     * Verifies that a binomial distribution is not equal to an object of another class, like a string.
     */
    @Test
    public void aBinomialDistributionShouldNotBeEqualToAString() {
        assertFalse(BINOMIAL_DISTRIBUTION.equals(""));
    }

    /**
     * Verifies that a binomial distribution is equal to itself.
     */
    @Test
    public void aBinomialDistributionShouldBeEqualToItself() {
        assertTrue(BINOMIAL_DISTRIBUTION.equals(BINOMIAL_DISTRIBUTION));
    }

    /**
     * Verifies that calling hashCode twice on a binomial distribution returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnABinomialDistributionReturnsTheSameResult() {
        assertEquals(BINOMIAL_DISTRIBUTION.hashCode(), BINOMIAL_DISTRIBUTION.hashCode());
    }

    /**
     * Verifies that two binomial distributions constructed with the same parameter are equal.
     */
    @Test
    public void twoBinomialDistributionsConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(BINOMIAL_DISTRIBUTION, BinomialDistribution.create(1L, FOUR, FIVE));
    }

    /**
     * Verifies that two binomial distributions constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoBinomialDistributionsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(BINOMIAL_DISTRIBUTION.hashCode(), BinomialDistribution.create(1L, FOUR, FIVE).hashCode());
    }

    /**
     * Verifies that two different binomial distribution with different values are not equal.
     */
    @Test
    public void twoDifferentBinomialDistributionsWithDifferentValuesShouldNotBeEqual() {
        assertFalse(BINOMIAL_DISTRIBUTION.equals(BinomialDistribution.create(2L, FOUR, FIVE)));
    }

    /**
     * Verifies that two different binomial distribution with different values have different hash codes.
     */
    @Test
    public void twoDifferentBinomialDistributionsWithDifferentValuesShouldHaveDifferentHashCodes() {
        assertFalse(BINOMIAL_DISTRIBUTION.hashCode() == BinomialDistribution.create(2L, FOUR, FIVE).hashCode());
    }
}
