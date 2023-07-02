package net.filipvanlaenen.asapop.yaml;

import java.util.Set;

/**
 * Class representing the additive SAPOR mapping element for the YAML file containing the SAPOR configuration.
 */
public class AdditiveSaporMapping {
    /**
     * The sources in the ROPF file.
     */
    private Set<String> sources;
    /**
     * The target in the SAPOR file.
     */
    private String target;

    /**
     * Returns the sources in the ROPF file.
     *
     * @return The sources in the ROPF file.
     */
    public Set<String> getSources() {
        return sources;
    }

    /**
     * Returns the target in the SAPOR file.
     *
     * @return The target in the SAPOR file.
     */
    public String getTarget() {
        return target;
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
     * Sets the target in the SAPOR file.
     *
     * @param target The target in the SAPOR file.
     */
    public void setTarget(final String target) {
        this.target = target;
    }
}
