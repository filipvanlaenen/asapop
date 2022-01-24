package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a binomial distribution.
 */
final class BinomialDistribution extends SortableProbabilityMassFunction<Long> {
    /**
     * A map holding the key value pairs for the binomial distribution.
     */
    private final Map<Long, BigDecimal> pmf = new HashMap<Long, BigDecimal>();
    private BigDecimal valueSum = BigDecimal.ZERO;

    /**
     * Creates a binomial distribution for a given value measured in a population size for a sample size.
     *
     * @param value          The measured value.
     * @param sampleSize     The sample size.
     * @param populationSize The population size.
     * @return A binomial distribution.
     */
    static BinomialDistribution create(final Long value, final Long sampleSize, final Long populationSize) {
        BinomialDistribution result = new BinomialDistribution();
        for (long i = 0; i <= populationSize; i++) {
            result.put(i, BinomialCoefficients.get(i, value).multiply(
                    BinomialCoefficients.get(populationSize - i, sampleSize - value), MathContext.DECIMAL128));
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
    List<Long> getSortedKeys() {
        List<Long> list = new ArrayList<Long>(pmf.keySet());
        Collections.sort(list);
        return Collections.unmodifiableList(list);
    }

    @Override
    BigDecimal getProbabilityMass(Long s) {
        return pmf.get(s);
    }

    @Override
    BigDecimal getProbabilityMassSum() {
        return valueSum;
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
        valueSum = valueSum.add(value, MathContext.DECIMAL128);
    }
}
