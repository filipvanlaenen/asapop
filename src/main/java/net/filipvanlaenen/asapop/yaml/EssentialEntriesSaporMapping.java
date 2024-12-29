package net.filipvanlaenen.asapop.yaml;

import java.util.Map;

/**
 * Class representing the SAPOR mapping element containing the essential entries for the YAML file containing the SAPOR
 * configuration.
 */
public class EssentialEntriesSaporMapping {
    /**
     * The residual weight.
     */
    private Integer residual;
    /**
     * The absolute targets in the SAPOR file.
     */
    private Map<String, Double> absoluteTargets;
    /**
     * The relative targets in the SAPOR file.
     */
    private Map<String, Integer> relativeTargets;

    /**
     * Returns the residual weight in the SAPOR file.
     *
     * @return The residual weight in the SAPOR file.
     */
    public Integer getResidual() {
        return residual;
    }

    /**
     * Returns the absolute targets in the SAPOR file.
     *
     * @return The absolute targets in the SAPOR file.
     */
    public Map<String, Double> getAbsoluteTargets() {
        return absoluteTargets;
    }

    /**
     * Returns the relative targets in the SAPOR file.
     *
     * @return The relative targets in the SAPOR file.
     */
    public Map<String, Integer> getRelativeTargets() {
        return relativeTargets;
    }

    /**
     * Sets the residual weight in the SAPOR file.
     *
     * @param residual The residual weight in the SAPOR file.
     */
    public void setResidual(final Integer residual) {
        this.residual = residual;
    }

    /**
     * Sets the absolute targets in the SAPOR file.
     *
     * @param relativeTargets The absolute targets in the SAPOR file.
     */
    public void setAbsoluteTargets(final Map<String, Double> absoluteTargets) {
        this.absoluteTargets = absoluteTargets;
    }

    /**
     * Sets the relative targets in the SAPOR file.
     *
     * @param relativeTargets The relative targets in the SAPOR file.
     */
    public void setRelativeTargets(final Map<String, Integer> relativeTargets) {
        this.relativeTargets = relativeTargets;
    }
}
