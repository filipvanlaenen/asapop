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
     * An additive splitting mapping from two or more results in an ROPF file to many entries in a SAPOR file.
     */
    private AdditiveSplittingSaporMapping additiveSplittingMapping;
    /**
     * A direct mapping from a result in an ROPF file to an entry in a SAPOR file.
     */
    private DirectSaporMapping directMapping;
    /**
     * An essential entries mapping with the essential entries in a SAPOR file.
     */
    private EssentialEntriesSaporMapping essentialEntriesSaporMapping;
    /**
     * A splitting mapping from a result in an ROPF file to many entries in a SAPOR file.
     */
    private SplittingSaporMapping splittingMapping;
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
     * Returns the additive splitting mapping from two or more results in an ROPF file to many entries in a SAPOR file.
     *
     * @return The additive splitting mapping from two or more results in an ROPF file to many entries in a SAPOR file.
     */
    public AdditiveSplittingSaporMapping getAdditiveSplittingMapping() {
        return additiveSplittingMapping;
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
     * Returns the essential entries mapping with the essential entries in a SAPOR file.
     *
     * @return The essential entries mapping with the essential entries in a SAPOR file.
     */
    public EssentialEntriesSaporMapping getEssentialEntriesMapping() {
        return essentialEntriesSaporMapping;
    }

    /**
     * Returns the splitting mapping from a result in an ROPF file to many entries in a SAPOR file.
     *
     * @return The splitting mapping from a result in an ROPF file to many entries in a SAPOR file.
     */
    public SplittingSaporMapping getSplittingMapping() {
        return splittingMapping;
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
     * Sets the additive splitting mapping from two or more results in an ROPF file to many entries in a SAPOR file.
     *
     * @param additiveSplittingMapping The additive splitting mapping from two or more results in an ROPF file to many
     *                                 entries in a SAPOR file.
     */
    public void setAdditiveSplittingMapping(final AdditiveSplittingSaporMapping additiveSplittingMapping) {
        this.additiveSplittingMapping = additiveSplittingMapping;
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
     * Sets the essential entries mapping with the essential entries in a SAPOR file.
     *
     * @param essentialEntriesMapping The essential entries mapping with the essential entries in a SAPOR file.
     */
    public void setEssentialEntriesMapping(final EssentialEntriesSaporMapping essentialEntriesMapping) {
        this.essentialEntriesSaporMapping = essentialEntriesMapping;
    }

    /**
     * Sets the splitting mapping from a result in an ROPF file to many entries in a SAPOR file.
     *
     * @param splittingMapping The splitting mapping from a result in an ROPF file to many entries in a SAPOR file.
     */
    public void setSplittingMapping(final SplittingSaporMapping splittingMapping) {
        this.splittingMapping = splittingMapping;
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
