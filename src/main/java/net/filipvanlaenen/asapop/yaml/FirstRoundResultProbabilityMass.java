package net.filipvanlaenen.asapop.yaml;

import java.util.Set;

/**
 * Class representing the probability mass for a result in the first round of a presidential election for the YAML file
 * containing the analysis of the opinion polls in an ROPF file.
 */
public class FirstRoundResultProbabilityMass {
    /**
     * A set of keys for the electoral lists that make it to the second round.
     */
    private Set<String> electoralLists;
    /**
     * The probability mass.
     */
    private Double probabilityMass;

    /**
     * Returns the set of keys for the electoral lists that make it to the second round.
     *
     * @return The set of keys for the electoral lists that make it to the second round.
     */
    public Set<String> getElectoralLists() {
        return electoralLists;
    }

    /**
     * Sets the set of keys for the electoral lists that make it to the second round.
     *
     * @param electoralLists The set of keys for the electoral lists that make it to the second round.
     */
    public void setElectoralLists(final Set<String> electoralLists) {
        this.electoralLists = electoralLists;
    }

    /**
     * Returns the probability mass.
     *
     * @return The probability mass.
     */
    public Double getProbabilityMass() {
        return probabilityMass;
    }

    /**
     * Sets the probability mass.
     *
     * @param probabilityMass The probability mass.
     */
    public void setProbabilityMass(final Double probabilityMass) {
        this.probabilityMass = probabilityMass;
    }
}
