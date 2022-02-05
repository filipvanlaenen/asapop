package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class providing methods to calculate and handle sampled hypergeometric distributions.
 */
final class SampledHypergeometricDistributions {
    /**
     * A map caching all the sampled hypergeometric distributions created in the <code>get</code> method.
     */
    private static final Map<List<Long>, SampledHypergeometricDistribution> CACHE = new HashMap<List<Long>, SampledHypergeometricDistribution>();

    /**
     * Private constructor to prevent the instantiation of this utility class.
     */
    private SampledHypergeometricDistributions() {
    }

    /**
     * Returns a hypergeometric distribution for a given value measured in a sample size for a population size, with at
     * least a given number of samples.
     *
     * @param value                  The measured value.
     * @param sampleSize             The sample size.
     * @param minimalNumberOfSamples The number of samples.
     * @param populationSize         The population size.
     * @return A hypergeometric distribution.
     */
    static SampledHypergeometricDistribution get(final Long value, final Long sampleSize,
            final Long minimalNumberOfSamples, final Long populationSize) {
        List<Long> key = List.of(value, sampleSize, populationSize);
        if (!CACHE.containsKey(key)) {
            CACHE.put(key,
                    new SampledHypergeometricDistribution(value, sampleSize, minimalNumberOfSamples, populationSize));
        } else {
            SampledHypergeometricDistribution currentSampledBinomialDistribution = CACHE.get(key);
            if (currentSampledBinomialDistribution.getNumberOfSamples() < minimalNumberOfSamples) {
                CACHE.put(key, new SampledHypergeometricDistribution(value, sampleSize, minimalNumberOfSamples,
                        populationSize));
            }
        }
        return CACHE.get(key);
    }
}
