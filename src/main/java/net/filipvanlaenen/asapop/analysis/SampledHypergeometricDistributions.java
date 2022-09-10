package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.filipvanlaenen.asapop.filecache.SampledHypergeometricDistributionsFileCache;

/**
 * Class providing methods to calculate and handle sampled hypergeometric distributions.
 */
public final class SampledHypergeometricDistributions {
    /**
     * A map caching all the sampled hypergeometric distributions created in the <code>get</code> method.
     */
    private static final Map<List<Long>, SampledHypergeometricDistribution> CACHE =
            new HashMap<List<Long>, SampledHypergeometricDistribution>();

    /**
     * Private constructor to prevent the instantiation of this utility class.
     */
    private SampledHypergeometricDistributions() {
    }

    /**
     * Returns a sampled hypergeometric distribution for a given value measured in a sample size for a population size,
     * with at least a given number of samples.
     *
     * @param value                  The measured value.
     * @param sampleSize             The sample size.
     * @param minimalNumberOfSamples The number of samples.
     * @param populationSize         The population size.
     * @return A sampled hypergeometric distribution.
     */
    public static SampledHypergeometricDistribution get(final Long value, final Long sampleSize,
            final Long minimalNumberOfSamples, final Long populationSize) {
        List<Long> key = List.of(value, sampleSize, populationSize);
        SampledHypergeometricDistribution pmf = CACHE.get(key);
        if (pmf != null && pmf.getNumberOfSamples() >= minimalNumberOfSamples) {
            return pmf;
        }
        pmf = new SampledHypergeometricDistribution(value, sampleSize, minimalNumberOfSamples, populationSize);
        CACHE.put(key, pmf);
        SampledHypergeometricDistributionsFileCache.write(value, sampleSize, populationSize, pmf);
        return pmf;
    }
}
