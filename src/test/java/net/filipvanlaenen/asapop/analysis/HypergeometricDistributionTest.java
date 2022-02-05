package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>HypergeometricDistribution</code> class.
 */
public class HypergeometricDistributionTest {
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
     * A hypergeometric distribution to run the tests on.
     */
    private static final HypergeometricDistribution HYPERGEOMETRIC_DISTRIBUTION = new HypergeometricDistribution(1L,
            FOUR, FIVE);

    /**
     * Verifies that the probability mass is zero at zero.
     */
    @Test
    public void probabilityMassShouldBeZeroAtZero() {
        assertEquals(BigDecimal.ZERO, HYPERGEOMETRIC_DISTRIBUTION.getProbabilityMass(0L));
    }

    /**
     * Verifies that the probability mass is four at one.
     */
    @Test
    public void probabilityMassShouldBeFourAtOne() {
        assertEquals(new BigDecimal(FOUR), HYPERGEOMETRIC_DISTRIBUTION.getProbabilityMass(1L));
    }

    /**
     * Verifies that the probability mass is zero at five.
     */
    @Test
    public void probabilityMassShouldBeZeroAtFive() {
        assertEquals(BigDecimal.ZERO, HYPERGEOMETRIC_DISTRIBUTION.getProbabilityMass(FIVE));
    }

    /**
     * Verifies that the key weight is one.
     */
    @Test
    public void keyWeightShouldBeOne() {
        assertEquals(BigDecimal.ONE, HYPERGEOMETRIC_DISTRIBUTION.getKeyWeight(0L));
    }

    /**
     * Verifies that a hypergeometric distribution is not equal to null.
     */
    @Test
    public void aHypergeometricDistributionShouldNotBeEqualToNull() {
        assertFalse(HYPERGEOMETRIC_DISTRIBUTION.equals(null));
    }

    /**
     * Verifies that a hypergeometric distribution is not equal to an object of another class, like a string.
     */
    @Test
    public void aHypergeometricDistributionShouldNotBeEqualToAString() {
        assertFalse(HYPERGEOMETRIC_DISTRIBUTION.equals(""));
    }

    /**
     * Verifies that a hypergeometric distribution is equal to itself.
     */
    @Test
    public void aHypergeometricDistributionShouldBeEqualToItself() {
        assertTrue(HYPERGEOMETRIC_DISTRIBUTION.equals(HYPERGEOMETRIC_DISTRIBUTION));
    }

    /**
     * Verifies that calling hashCode twice on a hypergeometric distribution returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnAHypergeometricDistributionReturnsTheSameResult() {
        assertEquals(HYPERGEOMETRIC_DISTRIBUTION.hashCode(), HYPERGEOMETRIC_DISTRIBUTION.hashCode());
    }

    /**
     * Verifies that two hypergeometric distributions constructed with the same parameter are equal.
     */
    @Test
    public void twoHypergeometricDistributionsConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(HYPERGEOMETRIC_DISTRIBUTION, new HypergeometricDistribution(1L, FOUR, FIVE));
    }

    /**
     * Verifies that two hypergeometric distributions constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoHypergeometricDistributionsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(HYPERGEOMETRIC_DISTRIBUTION.hashCode(), new HypergeometricDistribution(1L, FOUR, FIVE).hashCode());
    }

    /**
     * Verifies that two different hypergeometric distribution with different values are not equal.
     */
    @Test
    public void twoDifferentHypergeometricDistributionsWithDifferentValuesShouldNotBeEqual() {
        assertFalse(HYPERGEOMETRIC_DISTRIBUTION.equals(new HypergeometricDistribution(2L, FOUR, FIVE)));
    }

    /**
     * Verifies that two different hypergeometric distribution with different values have different hash codes.
     */
    @Test
    public void twoDifferentHypergeometricDistributionsWithDifferentValuesShouldHaveDifferentHashCodes() {
        assertFalse(
                HYPERGEOMETRIC_DISTRIBUTION.hashCode() == new HypergeometricDistribution(2L, FOUR, FIVE).hashCode());
    }
}
