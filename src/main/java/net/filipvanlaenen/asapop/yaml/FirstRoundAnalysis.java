package net.filipvanlaenen.asapop.yaml;

import java.util.Set;

/**
 * Class representing the opinion poll analysis element for the first round of a presidential election for the YAML file
 * containing the analysis of the opinion polls in an ROPF file.
 */
public class FirstRoundAnalysis {
    /**
     * The probability mass function as a set of <code>FirstRoundResultProbabilityMass</code> instances.
     */
    private Set<FirstRoundResultProbabilityMass> probabilityMassFunction;

    /**
     * Returns the probability mass function.
     *
     * @return The probability mass function.
     */
    public Set<FirstRoundResultProbabilityMass> getProbabilityMassFunction() {
        return probabilityMassFunction;
    }

    /**
     * Sets the probability mass function.
     *
     * @param probabilityMassFunction The probability mass function.
     */
    public void setProbabilityMassFunction(final Set<FirstRoundResultProbabilityMass> probabilityMassFunction) {
        this.probabilityMassFunction = probabilityMassFunction;
    }
}
