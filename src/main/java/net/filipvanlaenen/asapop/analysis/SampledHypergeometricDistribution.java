package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class representing a hypergeometric distribution, but sampled.
 */
class SampledHypergeometricDistribution extends SortableProbabilityMassFunction<Range> {
    /**
     * A map holding the key value pairs for the sampled hypergeometric distribution.
     */
    private final Map<Range, BigDecimal> pmf = new HashMap<Range, BigDecimal>();
    /**
     * The probability mass sum.
     */
    private BigDecimal probabilityMassSum = BigDecimal.ZERO;

    /**
     * Creates a sampled hypergeometric distribution for a given value measured in a population size for a number of
     * ranges in a sample size.
     *
     * @param value           The measured value.
     * @param sampleSize      The sample size.
     * @param numberOfSamples The number of samples.
     * @param populationSize  The population size.
     * @return A hypergeometric distribution.
     */
    static SampledHypergeometricDistribution create(final Long value, final Long sampleSize, final Long numberOfSamples,
            final Long populationSize) {
        SampledHypergeometricDistribution result = new SampledHypergeometricDistribution();
        long baseLength = populationSize / numberOfSamples;
        long remainder = 1 + populationSize - baseLength * numberOfSamples;
        long numberOfRangesOfBaseLength = numberOfSamples - remainder;
        long rangeStartIndex = 0L;
        long rangeEndIndex;
        for (long i = 0; i < numberOfSamples; i++) {
            rangeEndIndex = rangeStartIndex + baseLength + (i >= numberOfRangesOfBaseLength ? 0L : -1L);
            Range range = Range.get(rangeStartIndex, rangeEndIndex);
            long m = range.getMidpoint();
            result.put(range, BinomialCoefficients.get(m, value).multiply(
                    BinomialCoefficients.get(populationSize - m, sampleSize - value), MathContext.DECIMAL128));
            rangeStartIndex = rangeEndIndex + 1L;
        }
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SampledHypergeometricDistribution) {
            SampledHypergeometricDistribution other = (SampledHypergeometricDistribution) obj;
            return other.pmf.equals(pmf);
        } else {
            return false;
        }
    }

    @Override
    Set<Range> geKeys() {
        return pmf.keySet();
    }

    @Override
    BigDecimal getKeyWeight(final Range key) {
        return new BigDecimal(key.getLength());
    }

    /**
     * Returns the number of samples.
     *
     * @return The number of samples.
     */
    Long getNumberOfSamples() {
        return (long) pmf.size();
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
