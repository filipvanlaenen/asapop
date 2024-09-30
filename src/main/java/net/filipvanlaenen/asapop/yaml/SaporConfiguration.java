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
     * The preferred scope.
     */
    private String preferredScope;
    /**
     * The region for the response scenarios.
     */
    private String region;
    /**
     * How to select between response scenarios when there is more than one relevant.
     */
    private String responseScenarioSelection;
    /**
     * The scope.
     */
    private String scope;

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
     * Returns the preferred scope.
     *
     * @return The preferred scope.
     */
    public String getPreferredScope() {
        return preferredScope;
    }

    /**
     * Returns the region for the response scenarios.
     *
     * @return The region for the response scenarios.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Returns how to select between response scenarios when there is more than one relevant.
     *
     * @return How to select between response scenarios when there is more than one relevant.
     */
    public String getResponseScenarioSelection() {
        return responseScenarioSelection;
    }

    /**
     * Returns the scope.
     *
     * @return The scope.
     */
    public String getScope() {
        return scope;
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

    /**
     * Sets the preferred scope.
     *
     * @param preferredScope The preferred scope.
     */
    public void setPreferredScope(final String preferredScope) {
        this.preferredScope = preferredScope;
    }

    /**
     * Sets the region for the response scenarios.
     *
     * @param region The region for the response scenarios.
     */
    public void setRegion(final String region) {
        this.region = region;
    }

    /**
     * Sets how to select between response scenarios when there is more than one relevant.
     *
     * @param responseScenarioSelection How to select between response scenarios when there is more than one relevant.
     */
    public void setResponseScenarioSelection(final String responseScenarioSelection) {
        this.responseScenarioSelection = responseScenarioSelection;
    }

    /**
     * Sets the scope.
     *
     * @param scope The scope.
     */
    public void setScope(final String scope) {
        this.scope = scope;
    }
}
