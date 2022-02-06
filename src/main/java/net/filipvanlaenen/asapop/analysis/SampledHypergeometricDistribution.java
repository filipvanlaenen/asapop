package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a hypergeometric distribution, but sampled.
 */
class SampledHypergeometricDistribution extends SortableProbabilityMassFunction<Range> {
    /**
     * Creates a sampled hypergeometric distribution for a given value measured in a population size for a number of
     * ranges in a sample size.
     *
     * @param value           The measured value.
     * @param sampleSize      The sample size.
     * @param numberOfSamples The number of samples.
     * @param populationSize  The population size.
     */
    SampledHypergeometricDistribution(final Long value, final Long sampleSize, final Long numberOfSamples,
            final Long populationSize) {
        super(createPmf(value, sampleSize, numberOfSamples, populationSize));
    }

    /**
     * Creates a key-value map for a sampled hypergeometric distribution for a given value measured in a population size
     * for a number of ranges in a sample size.
     *
     * @param value           The measured value.
     * @param sampleSize      The sample size.
     * @param numberOfSamples The number of samples.
     * @param populationSize  The population size.
     * @return A hypergeometric distribution.
     */
    private static Map<Range, BigDecimal> createPmf(final Long value, final Long sampleSize, final Long numberOfSamples,
            final Long populationSize) {
        Map<Range, BigDecimal> pmf = new HashMap<Range, BigDecimal>();
        long baseLength = populationSize / numberOfSamples;
        long remainder = 1 + populationSize - baseLength * numberOfSamples;
        long numberOfRangesOfBaseLength = numberOfSamples - remainder;
        long rangeStartIndex = 0L;
        long rangeEndIndex;
        for (long i = 0; i < numberOfSamples; i++) {
            rangeEndIndex = rangeStartIndex + baseLength + (i >= numberOfRangesOfBaseLength ? 0L : -1L);
            Range range = Range.get(rangeStartIndex, rangeEndIndex);
            long m = range.getMidpoint();
            pmf.put(range, BinomialCoefficients.get(m, value).multiply(
                    BinomialCoefficients.get(populationSize - m, sampleSize - value), MathContext.DECIMAL128));
            rangeStartIndex = rangeEndIndex + 1L;
        }
        return pmf;
    }

    @Override
    BigDecimal getKeyWeight(final Range key) {
        return new BigDecimal(key.getLength());
    }
}
