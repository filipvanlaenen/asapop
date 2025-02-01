package net.filipvanlaenen.asapop.analysis;

import net.filipvanlaenen.kolektoj.ModifiableMap;

/**
 * Class providing methods to calculate and handle hypergeometric distributions.
 */
final class HypergeometricDistributions {
    /**
     * A record type holding the key parameters defining a hypergeometric distribution.
     *
     * @param value          The value.
     * @param sampleSize     The sample size.
     * @param populationSize The population size.
     */
    private record HypergeometricDistributionKey(Long value, Long sampleSize, Long populationSize) {
    }

    /**
     * A map caching all the hypergeometric distributions created in the <code>get</code> method.
     */
    private static final ModifiableMap<HypergeometricDistributionKey, HypergeometricDistribution> CACHE =
            ModifiableMap.empty();

    /**
     * Private constructor to prevent the instantiation of this utility class.
     */
    private HypergeometricDistributions() {
    }

    /**
     * Returns a hypergeometric distribution for a given value measured in a population size for a sample size.
     *
     * @param value          The measured value.
     * @param sampleSize     The sample size.
     * @param populationSize The population size.
     * @return A hypergeometric distribution.
     */
    static HypergeometricDistribution get(final Long value, final Long sampleSize, final Long populationSize) {
        HypergeometricDistributionKey key = new HypergeometricDistributionKey(value, sampleSize, populationSize);
        if (!CACHE.containsKey(key)) {
            CACHE.put(key, new HypergeometricDistribution(value, sampleSize, populationSize));
        }
        return CACHE.get(key);
    }
}
