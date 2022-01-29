package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class providing methods to calculate and handle sampled binomial distributions.
 */
final class SampledBinomialDistributions {
    /**
     * A map caching all the sampled binomial distributions created in the <code>get</code> method.
     */
    private static final Map<List<Long>, SampledBinomialDistribution> CACHE = new HashMap<List<Long>, SampledBinomialDistribution>();

    /**
     * Private constructor to prevent the instantiation of this utility class.
     */
    private SampledBinomialDistributions() {
    }

    /**
     * Returns a sampled binomial distribution for a given value measured in a population size for a number of samples
     * in a sample size.
     *
     * @param value          The measured value.
     * @param sampleSize     The sample size.
     * @param noOfSampled    The number of samples.
     * @param populationSize The population size.
     * @return A binomial distribution.
     */
    static SampledBinomialDistribution get(final Long value, final Long sampleSize, final Long noOfSamples,
            final Long populationSize) {
        List<Long> key = List.of(value, sampleSize, noOfSamples, populationSize);
        if (!CACHE.containsKey(key)) {
            CACHE.put(key, SampledBinomialDistribution.create(value, sampleSize, noOfSamples, populationSize));
        }
        return CACHE.get(key);
    }
}
