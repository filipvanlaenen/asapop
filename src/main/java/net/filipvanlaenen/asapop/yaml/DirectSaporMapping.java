package net.filipvanlaenen.asapop.yaml;

/**
 * Class representing the direct SAPOR mapping element for the YAML file containing the SAPOR configuration.
 */
public class DirectSaporMapping {
    /**
     * A compensation factor.
     */
    private Double compensationFactor;
    /**
     * The source in the ROPF file.
     */
    private String source;
    /**
     * The target in the SAPOR file.
     */
    private String target;

    /**
     * Returns the compensation factor.
     *
     * @return The compensation factor.
     */
    public Double getCompensationFactor() {
        return compensationFactor;
    }

    /**
     * Returns the source in the ROPF file.
     *
     * @return The source in the ROPF file.
     */
    public String getSource() {
        return source;
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
     * Sets the compensation factor.
     *
     * @param compensationFactor The compensation factor.
     */
    public void setCompensationFactor(final Double compensationFactor) {
        this.compensationFactor = compensationFactor;
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
     * Sets the target in the SAPOR file.
     *
     * @param target The target in the SAPOR file.
     */
    public void setTarget(final String target) {
        this.target = target;
    }
}
