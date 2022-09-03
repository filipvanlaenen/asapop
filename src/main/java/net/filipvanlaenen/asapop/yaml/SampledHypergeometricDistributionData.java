package net.filipvanlaenen.asapop.yaml;

import java.util.Set;

/**
 * Class representing the top element for the YAML file containing the data of a sampled hypergeometric distribution.
 */
public class SampledHypergeometricDistributionData {
    /**
     * The probability mass function as a set of <code>RangeProbabilityMass</code> instances.
     */
    private Set<RangeProbabilityMass> probabilityMassFunction;

    /**
     * Returns the probability mass function.
     *
     * @return The probability mass function.
     */
    public Set<RangeProbabilityMass> getProbabilityMassFunction() {
        return probabilityMassFunction;
    }

    /**
     * Sets the probability mass function.
     *
     * @param probabilityMassFunction The probability mass function.
     */
    public void setProbabilityMassFunction(final Set<RangeProbabilityMass> probabilityMassFunction) {
        this.probabilityMassFunction = probabilityMassFunction;
    }
}
