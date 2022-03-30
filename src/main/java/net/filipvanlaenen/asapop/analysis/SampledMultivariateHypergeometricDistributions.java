package net.filipvanlaenen.asapop.analysis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class providing methods to calculate and handle sampled multivariate hypergeometric distributions.
 */
final class SampledMultivariateHypergeometricDistributions {
    /**
     * A map caching all the sampled multivariate hypergeometric distributions created in the <code>get</code> method.
     */
    private static final Map<List<SampledHypergeometricDistribution>,
                             SampledMultivariateHypergeometricDistribution> CACHE =
                             new HashMap<List<SampledHypergeometricDistribution>,
                                         SampledMultivariateHypergeometricDistribution>();

    /**
     * Private constructor to prevent the instantiation of this utility class.
     */
    private SampledMultivariateHypergeometricDistributions() {
    }

    /**
     * Returns a sampled multivariate hypergeometric distribution based on a set of sampled hypergeometric distributions
     * in a sample size for a population size, with at least a given number of iterations.
     *
     * @param probabilityMassFunctions  The sampled hypergeometric distributions.
     * @param populationSize            The population size.
     * @param sampleSize                The sample size.
     * @param minimalNumberOfIterations The minimal number of iterations.
     * @return A sampled multivariate hypergeometric distribution.
     */
    static SampledMultivariateHypergeometricDistribution get(
            final List<SampledHypergeometricDistribution> probabilityMassFunctions, final long populationSize,
            final long sampleSize, final long minimalNumberOfIterations) {
        List<SampledHypergeometricDistribution> key = new ArrayList<SampledHypergeometricDistribution>(
                probabilityMassFunctions);
        key.sort(new Comparator<SampledHypergeometricDistribution>() {
            @Override
            public int compare(final SampledHypergeometricDistribution spmf0,
                    final SampledHypergeometricDistribution spmf1) {
                return spmf0.getMedian().compareTo(spmf1.getMedian());
            }
        });
        if (!CACHE.containsKey(key)) {
            CACHE.put(key, new SampledMultivariateHypergeometricDistribution(probabilityMassFunctions, populationSize,
                    sampleSize, minimalNumberOfIterations));
        } else {
            SampledMultivariateHypergeometricDistribution current = CACHE.get(key);
            if (current.getNumberOfIterations() < minimalNumberOfIterations) {
                CACHE.put(key, new SampledMultivariateHypergeometricDistribution(probabilityMassFunctions,
                        populationSize, sampleSize, minimalNumberOfIterations));
            }
        }
        return CACHE.get(key);
    }
}
