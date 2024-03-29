package net.filipvanlaenen.asapop.yaml;

import java.util.Map;
import java.util.Set;

/**
 * Class representing the response scenario analysis element for the YAML file containing the analysis of the opinion
 * polls in an ROPF file.
 */

public class ResponseScenarioAnalysis {
    /**
     * The area.
     */
    private String area;
    /**
     * The analysis of the first round of a presidential election.
     */
    private FirstRoundAnalysis firstRoundAnalysis;
    /**
     * The analysis for the result for other.
     */
    private ResultAnalysis otherAnalysis;
    /**
     * The map with the analyses for the results, with the keys of the electoral lists as their keys.
     */
    private Map<Set<String>, ResultAnalysis> resultAnalyses;
    /**
     * The scope.
     */
    private String scope;

    /**
     * Returns the area.
     *
     * @return The area.
     */
    public String getArea() {
        return area;
    }

    /**
     * Returns the analysis of the first round of a presidential election.
     *
     * @return The analysis of the first round of a presidential election.
     */
    public FirstRoundAnalysis getFirstRoundAnalysis() {
        return firstRoundAnalysis;
    }

    /**
     * Returns the analysis for the result for other.
     *
     * @return The analysis for the result for other.
     */
    public ResultAnalysis getOtherAnalysis() {
        return otherAnalysis;
    }

    /**
     * Returns the map with the analyses of the results.
     *
     * @return The map with the analyses of the result.
     */
    public Map<Set<String>, ResultAnalysis> getResultAnalyses() {
        return resultAnalyses;
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
     * Sets the area.
     *
     * @param area The area.
     */
    public void setArea(final String area) {
        this.area = area;
    }

    /**
     * Sets the analysis of the first round of a presidential election.
     *
     * @param firstRoundAnalysis The analysis of the first round of a presidential election.
     */
    public void setFirstRoundAnalysis(final FirstRoundAnalysis firstRoundAnalysis) {
        this.firstRoundAnalysis = firstRoundAnalysis;
    }

    /**
     * Sets the analysis for the result for other.
     *
     * @param otherAnalysis The analysis for the result for other.
     */
    public void setOtherAnalysis(final ResultAnalysis otherAnalysis) {
        this.otherAnalysis = otherAnalysis;
    }

    /**
     * Sets the map with the analyses of the results.
     *
     * @param resultAnalyses The map with the analyses of the results.
     */
    public void setResultAnalyses(final Map<Set<String>, ResultAnalysis> resultAnalyses) {
        this.resultAnalyses = resultAnalyses;
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
