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
    private static final Map<List<Long>, SampledBinomialDistribution> CACHE =
            new HashMap<List<Long>, SampledBinomialDistribution>();

    /**
     * Private constructor to prevent the instantiation of this utility class.
     */
    private SampledBinomialDistributions() {
    }

    /**
     * Returns a binomial distribution for a given value measured in a sample size for a population size, with at least
     * a given number of samples.
     *
     * @param value                  The measured value.
     * @param sampleSize             The sample size.
     * @param minimalNumberOfSamples The number of samples.
     * @param populationSize         The population size.
     * @return A binomial distribution.
     */
    static SampledBinomialDistribution get(final Long value, final Long sampleSize, final Long minimalNumberOfSamples,
            final Long populationSize) {
        List<Long> key = List.of(value, sampleSize, populationSize);
        if (!CACHE.containsKey(key)) {
            CACHE.put(key,
                    SampledBinomialDistribution.create(value, sampleSize, minimalNumberOfSamples, populationSize));
        } else {
            SampledBinomialDistribution currentSampledBinomialDistribution = CACHE.get(key);
            if (currentSampledBinomialDistribution.getNumberOfSamples() < minimalNumberOfSamples) {
                CACHE.put(key,
                        SampledBinomialDistribution.create(value, sampleSize, minimalNumberOfSamples, populationSize));
            }
        }
        return CACHE.get(key);
    }
}
