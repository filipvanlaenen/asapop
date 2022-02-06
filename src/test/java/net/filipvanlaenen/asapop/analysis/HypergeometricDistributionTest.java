package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;

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
     * The magic number ten.
     */
    private static final long TEN = 10L;
    /**
     * The magic number twenty.
     */
    private static final long TWENTY = 20L;
    /**
     * The magic number thirty.
     */
    private static final long THIRTY = 30L;
    /**
     * The magic number seventy.
     */
    private static final long SEVENTY = 70L;
    /**
     * The magic number hundred.
     */
    private static final long HUNDRED = 100L;
    /**
     * A hypergeometric distribution to run the tests on.
     */
    private static final HypergeometricDistribution HYPERGEOMETRIC_DISTRIBUTION_1_4_10 = new HypergeometricDistribution(
            1L, FOUR, TEN);

    /**
     * Verifies that the probability mass is calculated correctly.
     */
    @Test
    public void probabilityMassShouldBeCalculatedCorrectly() {
        assertEquals(BigDecimal.ZERO, HYPERGEOMETRIC_DISTRIBUTION_1_4_10.getProbabilityMass(0L));
        BigDecimal expected = BinomialCoefficients.get(THIRTY, TEN).multiply(BinomialCoefficients.get(SEVENTY, TEN),
                MathContext.DECIMAL128);
        assertEquals(expected, new HypergeometricDistribution(TEN, TWENTY, HUNDRED).getProbabilityMass(THIRTY));
    }

    /**
     * Verifies that the key weight is one.
     */
    @Test
    public void keyWeightShouldBeOne() {
        assertEquals(BigDecimal.ONE, HYPERGEOMETRIC_DISTRIBUTION_1_4_10.getKeyWeight(0L));
    }
}
