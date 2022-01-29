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
 * Class representing a binomial distribution, but sampled.
 */
class SampledBinomialDistribution extends SortableProbabilityMassFunction<Range> {
    /**
     * A map holding the key value pairs for the sampled binomial distribution.
     */
    private final Map<Range, BigDecimal> pmf = new HashMap<Range, BigDecimal>();
    /**
     * The probability mass sum.
     */
    private BigDecimal probabilityMassSum = BigDecimal.ZERO;

    /**
     * Creates a sampled binomial distribution for a given value measured in a population size for a number of ranges in
     * a sample size.
     *
     * @param value           The measured value.
     * @param sampleSize      The sample size.
     * @param numberOfSamples The number of samples.
     * @param populationSize  The population size.
     * @return A binomial distribution.
     */
    static SampledBinomialDistribution create(final Long value, final Long sampleSize, final Long numberOfSamples,
            final Long populationSize) {
        SampledBinomialDistribution result = new SampledBinomialDistribution();
        long baseLength = populationSize / numberOfSamples;
        long remainder = populationSize - baseLength * numberOfSamples;
        long numberOfRangesOfBaseLength = numberOfSamples - remainder;
        long rangeStartIndex = 0L;
        long rangeEndIndex;
        for (long i = 0; i < numberOfSamples; i++) {
            rangeEndIndex = rangeStartIndex + baseLength + (i >= numberOfRangesOfBaseLength ? 0L : -1L);
            Range range = new Range(rangeStartIndex, rangeEndIndex);
            long m = range.getMidpoint();
            result.put(range, BinomialCoefficients.get(m, value).multiply(
                    BinomialCoefficients.get(populationSize - m, sampleSize - value), MathContext.DECIMAL128));
            rangeStartIndex = rangeEndIndex + 1L;
        }
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SampledBinomialDistribution) {
            SampledBinomialDistribution other = (SampledBinomialDistribution) obj;
            return other.pmf.equals(pmf);
        } else {
            return false;
        }
    }

    @Override
    BigDecimal getProbabilityMass(final Range key) {
        return pmf.get(key);
    }

    @Override
    BigDecimal getProbabilityMassSum() {
        return probabilityMassSum;
    }

    @Override
    List<Range> getSortedKeys() {
        List<Range> list = new ArrayList<Range>(pmf.keySet());
        Collections.sort(list);
        return Collections.unmodifiableList(list);
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
    private void put(final Range key, final BigDecimal value) {
        pmf.put(key, value);
        probabilityMassSum = probabilityMassSum
                .add(value.multiply(new BigDecimal(key.getLength()), MathContext.DECIMAL128), MathContext.DECIMAL128);
    }
}
