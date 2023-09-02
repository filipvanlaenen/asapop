package net.filipvanlaenen.asapop.yaml;

import java.util.Map;

public class EssentialEntriesSaporMapping {
    private Integer residual;
    /**
     * The targets in the SAPOR file.
     */
    private Map<String, Integer> targets;

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

    public void setResidual(Integer residual) {
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
