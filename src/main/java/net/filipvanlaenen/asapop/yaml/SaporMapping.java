package net.filipvanlaenen.asapop.yaml;

/**
 * Class representing the SAPOR mapping element for the YAML file containing the SAPOR configuration.
 */
public class SaporMapping {
    /**
     * An additive mapping from two or more results in an ROPF file to an entry in a SAPOR file.
     */
    private AdditiveSaporMapping additiveMapping;
    /**
     * A direct mapping from a result in an ROPF file to an entry in a SAPOR file.
     */
    private DirectSaporMapping directMapping;
    /**
     * The end date.
     */
    private String endDate;
    /**
     * The start date.
     */
    private String startDate;

    /**
     * Returns the additive mapping from two or more results in an ROPF file to an entry in a SAPOR file.
     *
     * @return The additive mapping from two or more results in an ROPF file to an entry in a SAPOR file.
     */
    public AdditiveSaporMapping getAdditiveMapping() {
        return additiveMapping;
    }

    /**
     * Returns the direct mapping from a result in an ROPF file to an entry in a SAPOR file.
     *
     * @return The direct mapping from a result in an ROPF file to an entry in a SAPOR file.
     */
    public DirectSaporMapping getDirectMapping() {
        return directMapping;
    }

    /**
     * Returns the end date.
     *
     * @return The end date.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Returns the start date.
     *
     * @return The start date.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the additive mapping from two or more results in an ROPF file to an entry in a SAPOR file.
     *
     * @param additiveMapping The additive mapping from two or more results in an ROPF file to an entry in a SAPOR file.
     */
    public void setAdditiveMapping(final AdditiveSaporMapping additiveMapping) {
        this.additiveMapping = additiveMapping;
    }

    /**
     * Sets the direct mapping from a result in an ROPF file to an entry in a SAPOR file.
     *
     * @param directMapping The direct mapping from a result in an ROPF file to an entry in a SAPOR file.
     */
    public void setDirectMapping(final DirectSaporMapping directMapping) {
        this.directMapping = directMapping;
    }

    /**
     * Sets the end date.
     *
     * @param endDate The end date.
     */
    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    /**
     * Sets the start date.
     *
     * @param startDate The start date.
     */
    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }
}
