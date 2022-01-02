package net.filipvanlaenen.asapop.yaml;

import java.util.Map;

/**
 * Class representing the result analysis element for the YAML file containing the analysis of the opinion polls in an
 * ROPF file.
 */
public class ResultAnalysis {
    /**
     * A map with confidence intervals for the result, with their confidence levels as their keys.
     */
    private Map<Integer, Float[]> confidenceIntervals;
    /**
     * The median for the result.
     */
    private Float median;

    /**
     * Returns the map with the confidence intervals.
     *
     * @return The map with the confidence intervals.
     */
    public Map<Integer, Float[]> getConfidenceIntervals() {
        return confidenceIntervals;
    }

    /**
     * Returns the median.
     *
     * @return The median.
     */
    public Float getMedian() {
        return median;
    }

    /**
     * Sets the map with the confidence intervals.
     *
     * @param confidenceIntervals The map with the confidence intervals.
     */
    public void setConfidenceIntervals(final Map<Integer, Float[]> confidenceIntervals) {
        this.confidenceIntervals = confidenceIntervals;
    }

    /**
     * Sets the median.
     *
     * @param median The median.
     */
    public void setMedian(final Float median) {
        this.median = median;
    }
}
