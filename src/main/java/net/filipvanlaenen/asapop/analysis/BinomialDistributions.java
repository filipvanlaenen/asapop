package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class providing methods to calculate and handle binomial distributions.
 */
final class BinomialDistributions {
    /**
     * A map caching all the binomial distributions created in the <code>get</code> method.
     */
    private static final Map<List<Long>, BinomialDistribution> CACHE = new HashMap<List<Long>, BinomialDistribution>();

    /**
     * Private constructor to prevent the instantiation of this utility class.
     */
    private BinomialDistributions() {
    }

    /**
     * Returns a binomial distribution for a given value measured in a population size for a sample size.
     *
     * @param value          The measured value.
     * @param sampleSize     The sample size.
     * @param populationSize The population size.
     * @return A binomial distribution.
     */
    static BinomialDistribution get(final Long value, final Long sampleSize, final Long populationSize) {
        List<Long> key = List.of(value, sampleSize, populationSize);
        if (!CACHE.containsKey(key)) {
            CACHE.put(key, BinomialDistribution.create(value, sampleSize, populationSize));
        }
        return CACHE.get(key);
    }
}
