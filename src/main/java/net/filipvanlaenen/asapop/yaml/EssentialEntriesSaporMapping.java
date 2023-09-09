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
     * The targets in the SAPOR file.
     */
    private Map<String, Integer> targets;

    /**
     * Returns the residual weight in the SAPOR file.
     *
     * @return The residual weight in the SAPOR file.
     */
    public Integer getResidual() {
        return residual;
    }

    /**
     * Returns the targets in the SAPOR file.
     *
     * @return The targets in the SAPOR file.
     */
    public Map<String, Integer> getTargets() {
        return targets;
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
     * Sets the targets in the SAPOR file.
     *
     * @param targets The targets in the SAPOR file.
     */
    public void setTargets(final Map<String, Integer> targets) {
        this.targets = targets;
    }
}
