package net.filipvanlaenen.asapop.analysis;

/**
 * Class providing methods to calculate and handle binomial distributions.
 */
final class BinomialDistributions {
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
        return BinomialDistribution.create(value, sampleSize, populationSize);
    }
}
