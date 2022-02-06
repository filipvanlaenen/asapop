package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a hypergeometric distribution.
 */
final class HypergeometricDistribution extends SortableProbabilityMassFunction<Long> {

    /**
     * Creates a hypergeometric distribution for a given value measured in a population size for a sample size.
     *
     * @param value          The measured value.
     * @param sampleSize     The sample size.
     * @param populationSize The population size.
     */
    HypergeometricDistribution(final Long value, final Long sampleSize, final Long populationSize) {
        super(createPmf(value, sampleSize, populationSize));
    }

    /**
     * Creates a key-value map for a hypergeometric distribution for a given value measured in a population size for a
     * sample size.
     *
     * @param value          The measured value.
     * @param sampleSize     The sample size.
     * @param populationSize The population size.
     * @return A hypergeometric distribution.
     */
    private static Map<Long, BigDecimal> createPmf(final Long value, final Long sampleSize, final Long populationSize) {
        Map<Long, BigDecimal> pmf = new HashMap<Long, BigDecimal>();
        for (long i = 0; i <= populationSize; i++) {
            pmf.put(i, BinomialCoefficients.get(i, value).multiply(
                    BinomialCoefficients.get(populationSize - i, sampleSize - value), MathContext.DECIMAL128));
        }
        return pmf;
    }

    @Override
    BigDecimal getKeyWeight(final Long key) {
        return BigDecimal.ONE;
    }
}
