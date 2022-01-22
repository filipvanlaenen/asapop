package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;

class BinomialCoefficients {
    /**
     * Returns the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     *
     * It seems that the calculation as a quotient of products is slightly faster than a calculation as a products of
     * quotients.
     *
     * @param n The parameter <i>n</i> of the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     * @param k The parameter <i>k</i> of the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     * @return The binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     */
    static BigDecimal get(final long n, final long k) {
        // Changing the conditional boundary below produces an equivalent mutant.
        long lowerK = 2 * k > n ? n - k : k;
        return binomialCoefficientAsAQuotientOfProducts(n, lowerK);
    }

    /**
     * Calculates the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>) as a quotient of products.
     *
     * @param n The parameter <i>n</i> of the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     * @param k The parameter <i>k</i> of the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     * @return The binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     */
    private static BigDecimal binomialCoefficientAsAQuotientOfProducts(final long n, final long k) {
        BigDecimal p1 = BigDecimal.ONE;
        BigDecimal p2 = BigDecimal.ONE;
        for (int i = 1; i <= k; i++) {
            p1 = p1.multiply(new BigDecimal(n + 1 - i), MathContext.DECIMAL128);
            p2 = p2.multiply(new BigDecimal(i), MathContext.DECIMAL128);
        }
        return p1.divide(p2, MathContext.DECIMAL128);
    }
}
