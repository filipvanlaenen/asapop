package net.filipvanlaenen.asapop.yaml;

/**
 * Class representing the top element for the YAML file containing the election data needed for the analysis of the
 * opinion polls in an ROPF file.
 */
public class ElectionData {
    /**
     * The electoral system.
     */
    private ElectoralSystem electoralSystem;
    /**
     * The population size for the election.
     */
    private long populationSize;

    /**
     * Returns the electoral system.
     *
     * @return The electoral system.
     */
    public ElectoralSystem getElectoralSystem() {
        return electoralSystem;
    }

    /**
     * Returns the population size for the election.
     *
     * @return The population size for the election.
     */
    public long getPopulationSize() {
        return populationSize;
    }

    /**
     * Sets the electoral system.
     *
     * @param electoralSystem The electoral system.
     */
    public void setElectoralSystem(final ElectoralSystem electoralSystem) {
        this.electoralSystem = electoralSystem;
    }

    /**
     * Sets the population size for the election.
     *
     * @param populationSize The population size for the election.
     */
    public void setPopulationSize(final long populationSize) {
        this.populationSize = populationSize;
    }
}
