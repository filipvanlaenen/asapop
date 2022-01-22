package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Unit tests on the <code>BinomialDistribution</code> class.
 */
public class BinomialCoefficientsTest {
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
    private static final BigDecimal SIX = new BigDecimal(6);
    /**
     * The magic number ten.
     */
    private static final BigDecimal TEN = new BigDecimal(10);
    /**
     * The magic number hundred.
     */
    private static final int HUNDRED = 100;
    /**
     * The magic number one billion.
     */
    private static final long ONE_BILLION = 100_000_000L;

    /**
     * Verifies that <i>C</i>(1,0) = 1.
     */
    @Test
    public void binomialCoefficientOf0OutOf1ShouldBe0() {
        assertEquals(BigDecimal.ONE, BinomialCoefficients.get(1, 0));
    }

    /**
     * Verifies that <i>C</i>(4,2) = 6.
     */
    @Test
    public void binomialCoefficientOf2OutOf4ShouldBe6() {
        assertEquals(SIX, BinomialCoefficients.get(FOUR, 2));
    }

    /**
     * Verifies that <i>C</i>(5,3) = 6.
     */
    @Test
    public void binomialCoefficientOf3OutOf5ShouldBe10() {
        assertEquals(TEN, BinomialCoefficients.get(FIVE, THREE));
    }

    /**
     * Verifies that for large <i>k</i>, <i>C</i>(<i>n</i>,<i>k</i>) is calculated as
     * <i>C</i>(<i>n</i>,<i>n</i>-<i>k</i>).
     */
    @Test
    @Timeout(value = HUNDRED, unit = TimeUnit.MILLISECONDS)
    public void binomialCoefficientShouldCalculateLowerHalfOfN() {
        BinomialCoefficients.get(ONE_BILLION, ONE_BILLION - 1L);
    }
}
