package net.filipvanlaenen.asapop.yaml;

import java.util.Set;

/**
 * Class representing the top element for the YAML file containing the SAPOR configuration data.
 */
public class SaporConfiguration {
    /**
     * The area as it should appear in the SAPOR files.
     */
    private String area;
    /**
     * The date of the last election.
     */
    private String lastElectionDate;
    /**
     * The mapping from the results in the ROPF file to the entries in the SAPOR files.
     */
    private Set<SaporMapping> mapping;

    /**
     * Returns the area as it should appear in the SAPOR files.
     *
     * @return The area as it should appear in the SAPOR files.
     */
    public String getArea() {
        return area;
    }

    /**
     * Returns the date of the last election.
     *
     * @return The date of the last election.
     */
    public String getLastElectionDate() {
        return lastElectionDate;
    }

    /**
     * Returns the mapping from the results in the ROPF file to the entries in the SAPOR files.
     *
     * @return The mapping from the results in the ROPF file to the entries in the SAPOR files.
     */
    public Set<SaporMapping> getMapping() {
        return mapping;
    }

    /**
     * Sets the area as it should appear in the SAPOR files.
     *
     * @param area The area as it should appear in the SAPOR files.
     */
    public void setArea(final String area) {
        this.area = area;
    }

    /**
     * Sets the date of the last election.
     *
     * @param lastElectionDate The date of the last election.
     */
    public void setLastElectionDate(final String lastElectionDate) {
        this.lastElectionDate = lastElectionDate;
    }

    /**
     * Sets the mapping from the results in the ROPF file to the entries in the SAPOR files.
     *
     * @param mapping The mapping from the results in the ROPF file to the entries in the SAPOR files.
     */
    public void setMapping(final Set<SaporMapping> mapping) {
        this.mapping = mapping;
    }
}
