package net.filipvanlaenen.asapop.yaml;

import java.util.Set;

/**
 * Class representing the probability mass for a result in the first round of a presidential election for the YAML file
 * containing the analysis of the opinion polls in an ROPF file.
 */
public class FirstRoundResultProbabilityMass {
    /**
     * A set of IDs for the sets of electoral lists that make it to the second round.
     */
    private Set<Set<String>> electoralListSets;
    /**
     * The probability mass.
     */
    private Double probabilityMass;

    /**
     * Returns the set of IDs for the sets of electoral lists that make it to the second round.
     *
     * @return The set of IDs for the sets of electoral lists that make it to the second round.
     */
    public Set<Set<String>> getElectoralLists() {
        return electoralListSets;
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
     * Sets the set of IDs for the set of electoral lists that make it to the second round.
     *
     * @param electoralListSets The set of IDs for the sets of electoral lists that make it to the second round.
     */
    public void setElectoralListSets(final Set<Set<String>> electoralListSets) {
        this.electoralListSets = electoralListSets;
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
