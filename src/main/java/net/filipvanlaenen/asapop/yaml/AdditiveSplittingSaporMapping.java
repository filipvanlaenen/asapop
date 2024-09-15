package net.filipvanlaenen.asapop.yaml;

import java.util.Map;
import java.util.Set;

/**
 * Class representing the additive splitting SAPOR mapping element for the YAML file containing the SAPOR configuration.
 */
public class AdditiveSplittingSaporMapping {
    /**
     * The sources in the ROPF file.
     */
    private Set<String> sources;
    /**
     * The targets in the SAPOR file.
     */
    private Map<String, Integer> targets;

    /**
     * Returns the sources in the ROPF file.
     *
     * @return The sources in the ROPF file.
     */
    public Set<String> getSources() {
        return sources;
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
     * Sets the sources in the ROPF file.
     *
     * @param sources The sources in the ROPF file.
     */
    public void setSources(final Set<String> sources) {
        this.sources = sources;
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
