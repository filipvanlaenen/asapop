package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Class representing a hypergeometric distribution.
 */
final class HypergeometricDistribution extends SortableProbabilityMassFunction<Long> {
    /**
     * Private constructor to prevent direct calls.
     */
    private HypergeometricDistribution() {
    }

    /**
     * Creates a hypergeometric distribution for a given value measured in a population size for a sample size.
     *
     * @param value          The measured value.
     * @param sampleSize     The sample size.
     * @param populationSize The population size.
     * @return A hypergeometric distribution.
     */
    static HypergeometricDistribution create(final Long value, final Long sampleSize, final Long populationSize) {
        HypergeometricDistribution result = new HypergeometricDistribution();
        for (long i = 0; i <= populationSize; i++) {
            result.put(i, BinomialCoefficients.get(i, value).multiply(
                    BinomialCoefficients.get(populationSize - i, sampleSize - value), MathContext.DECIMAL128));
        }
        return result;
    }

    @Override
    BigDecimal getKeyWeight(final Long key) {
        return BigDecimal.ONE;
    }
}
