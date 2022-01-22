package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BinomialDistribution extends ProbabilityMassFunction {
    private final Map<Integer, BigDecimal> pmf = new HashMap<Integer, BigDecimal>();

    /**
     * Calculates the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     * 
     * It seems that the calculation as a quotient of products is slightly faster than a calculation as a products of
     * quotients.
     * 
     * @param n The parameter <i>n</i> of the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     * @param k The parameter <i>k</i> of the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     * @return The binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     */
    static BigDecimal binomialCoefficient(final long n, final long k) {
        return binomialCoefficientAsAQuotientOfProducts(n, 2 * k > n ? n - k : k);
    }

    /**
     * Calculates the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>), calculated as a product of quotients.
     * 
     * @param n The parameter <i>n</i> of the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     * @param k The parameter <i>k</i> of the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     * @return The binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     */
    static BigDecimal binomialCoefficientAsAProductOfQuotients(final long n, final long k) {
        BigDecimal p = BigDecimal.ONE;
        for (int i = 1; i <= k; i++) {
            p = p.multiply(new BigDecimal(n + 1 - i), MathContext.DECIMAL128).divide(new BigDecimal(i),
                    MathContext.DECIMAL128);
        }
        return p;
    }

    /**
     * Calculates the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>), calculated as a quotient of products.
     * 
     * @param n The parameter <i>n</i> of the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     * @param k The parameter <i>k</i> of the binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     * @return The binomial coefficient <i>C</i>(<i>n</i>,<i>k</i>).
     */
    static BigDecimal binomialCoefficientAsAQuotientOfProducts(long n, long k) {
        BigDecimal p1 = BigDecimal.ONE;
        BigDecimal p2 = BigDecimal.ONE;
        for (int i = 1; i <= k; i++) {
            p1 = p1.multiply(new BigDecimal(n + 1 - i), MathContext.DECIMAL128);
            p2 = p2.multiply(new BigDecimal(i), MathContext.DECIMAL128);
        }
        return p1.divide(p2, MathContext.DECIMAL128);
    }

    static BinomialDistribution create(Long sampled, Long sampleSize, Long populationSize) {
        BinomialDistribution result = new BinomialDistribution();
        // TODO: Large population sizes
        for (int i = 0; i <= populationSize; i++) {
            result.put(i, binomialCoefficient(i, sampled)
                    .multiply(binomialCoefficient(populationSize - i, sampleSize - sampled), MathContext.DECIMAL128));
        }

        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof BinomialDistribution) {
            BinomialDistribution other = (BinomialDistribution) obj;
            return other.pmf.equals(pmf);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(pmf);
    }

    private void put(int key, BigDecimal value) {
        pmf.put(key, value);
    }
}
