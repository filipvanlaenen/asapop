package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>HypergeometricDistribution</code> class.
 */
public class HypergeometricDistributionTest {
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number five.
     */
    private static final long FIVE = 5L;
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
}
