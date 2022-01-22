package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;

public class BinomialDistribution extends ProbabilityMassFunction {

    public static BinomialDistribution create(Long sampled, Long sampleSize, Long populationSize) {
        BinomialDistribution result = new BinomialDistribution();
        // TODO: Large population sizes
        for (int i = 0; i <= populationSize; i++) {
            result.add(i, binomialCoefficient(i, sampled).multiply(
                    binomialCoefficient(populationSize - i, sampleSize - sampled), MathContext.DECIMAL128));
        }

        return result;
    }
    /**
     * Calculates the binomial coefficient <i>C(n,k)</i>.
     * 
     * @param n The parameter <i>n</i> of the binomial coefficient <i>C(n,k)</i>.
     * @param k The parameter <i>k</i> of the binomial coefficient <i>C(n,k)</i>.
     * @return The binomial coefficient <i>C(n,k)</i>
     */
    private static BigDecimal binomialCoefficient(long n, long k) {
        // TODO: Product of quotients or quotient of products?
        BigDecimal p = BigDecimal.ONE;
        for (int i = 1; i <= k; i++) {
            p = p.multiply(new BigDecimal(n + 1 - i), MathContext.DECIMAL128).divide(new BigDecimal(i),
                    MathContext.DECIMAL128);
        }
        return p;
    }
}
