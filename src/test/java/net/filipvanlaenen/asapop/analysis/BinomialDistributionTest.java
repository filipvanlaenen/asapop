package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

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
     * Verifies that <i>C</i>(1,0) = 1.
     */
    @Test
    public void binomialCoefficientAsAProductOfQuotientsOf0OutOf1ShouldBe0() {
        assertEquals(BigDecimal.ONE, BinomialDistribution.binomialCoefficientAsAProductOfQuotients(1, 0));
    }

    /**
     * Verifies that <i>C</i>(4,2) = 6.
     */
    @Test
    public void binomialCoefficientAsAProductOfQuotientsOf2OutOf4ShouldBe6() {
        assertEquals(new BigDecimal(6), BinomialDistribution.binomialCoefficientAsAProductOfQuotients(4, 2));
    }
}
