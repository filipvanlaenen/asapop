package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class representing a hypergeometric distribution.
 */
final class HypergeometricDistribution extends SortableProbabilityMassFunction<Long> {
    /**
     * A map holding the key value pairs for the hypergeometric distribution.
     */
    private final Map<Long, BigDecimal> pmf = new HashMap<Long, BigDecimal>();
    /**
     * The probability mass sum.
     */
    private BigDecimal probabilityMassSum = BigDecimal.ZERO;

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
    public boolean equals(final Object obj) {
        if (obj instanceof HypergeometricDistribution) {
            HypergeometricDistribution other = (HypergeometricDistribution) obj;
            return other.pmf.equals(pmf);
        } else {
            return false;
        }
    }

    @Override
    Set<Long> geKeys() {
        return pmf.keySet();
    }

    @Override
    BigDecimal getKeyWeight(final Long key) {
        return BigDecimal.ONE;
    }

    @Override
    BigDecimal getProbabilityMass(final Long s) {
        return pmf.get(s);
    }

    @Override
    BigDecimal getProbabilityMassSum() {
        return probabilityMassSum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pmf);
    }

    /**
     * Sets a value for a key.
     *
     * @param key   The key.
     * @param value The value.
     */
    private void put(final long key, final BigDecimal value) {
        pmf.put(key, value);
        probabilityMassSum = probabilityMassSum.add(value, MathContext.DECIMAL128);
    }
}
