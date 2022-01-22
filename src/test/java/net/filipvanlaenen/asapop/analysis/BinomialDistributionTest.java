package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class BinomialDistributionTest {
    /**
     * Verifies that <i>C</i>(1,0) = 1.
     */
    @Test
    public void binomialCoefficientOf0OutOf1ShouldBe0() {
        assertEquals(BigDecimal.ONE, BinomialDistribution.binomialCoefficient(1, 0));
    }

    /**
     * Verifies that <i>C</i>(4,2) = 6.
     */
    @Test
    public void binomialCoefficientOf2OutOf4ShouldBe6() {
        assertEquals(new BigDecimal(6), BinomialDistribution.binomialCoefficient(4, 2));
    }

    /**
     * Verifies that <i>C</i>(5,3) = 6.
     */
    @Test
    public void binomialCoefficientOf3OutOf5ShouldBe10() {
        assertEquals(new BigDecimal(10), BinomialDistribution.binomialCoefficient(5, 3));
    }

    /**
     * Verifies that <i>C</i>(1,0) = 1 for the calculation as a product of quotients.
     */
    @Test
    public void binomialCoefficientAsAProductOfQuotientsOf0OutOf1ShouldBe0() {
        assertEquals(BigDecimal.ONE, BinomialDistribution.binomialCoefficientAsAProductOfQuotients(1, 0));
    }

    /**
     * Verifies that <i>C</i>(4,2) = 6 for the calculation as a product of quotients.
     */
    @Test
    public void binomialCoefficientAsAProductOfQuotientsOf2OutOf4ShouldBe6() {
        assertEquals(new BigDecimal(6), BinomialDistribution.binomialCoefficientAsAProductOfQuotients(4, 2));
    }

    /**
     * Verifies that for large <i>k</i>, <i>C</i>(<i>n</i>,<i>k</i>) is calculated as
     * <i>C</i>(<i>n</i>,<i>n</i>-<i>k</i>).
     */
    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    public void binomialCoefficientShouldCalculateLowerHalfOfN() {
        BinomialDistribution.binomialCoefficient(100_000_000L, 99_999_990L);
    }
}
