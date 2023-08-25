package net.filipvanlaenen.asapop.yaml;

import java.util.Map;

public class SplittingSaporMapping {
    /**
     * The source in the ROPF file.
     */
    private String source;
    /**
     * The targets in the SAPOR file.
     */
    private Map<String, Integer> targets;

    /**
     * Returns the source in the ROPF file.
     *
     * @return The source in the ROPF file.
     */
    public String getSource() {
        return source;
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
     * Sets the source in the ROPF file.
     *
     * @param source The source in the ROPF file.
     */
    public void setSource(final String source) {
        this.source = source;
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
