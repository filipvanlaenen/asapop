package net.filipvanlaenen.asapop.yaml;

import java.util.List;

/**
 * Class representing the CSV file configuration element for the YAML file containing the website configuration.
 */
public class CsvConfiguration {
    /**
     * The list with the IDs for the electoral lists.
     */
    private List<String> electoralListIds;
    /**
     * The area to be included in the export of the national polls.
     */
    private String includeAreaAsNational;

    /**
     * Returns the list with the IDs for the electoral lists.
     *
     * @return The list with the IDs for the electoral lists.
     */
    public List<String> getElectoralListIds() {
        return electoralListIds;
    }

    /**
     * Returns the area to be included in the export of the national polls.
     *
     * @return The area to be included in the export of the national polls.
     */
    public String getIncludeAreaAsNational() {
        return includeAreaAsNational;
    }

    /**
     * Sets the list with the IDs for the electoral lists.
     *
     * @param electoralListIds The list with the IDs for the electoral lists.
     */
    public void setElectoralListIds(final List<String> electoralListIds) {
        this.electoralListIds = electoralListIds;
    }

    /**
     * Sets the area to be included in the export of the national polls.
     *
     * @param includeAreaAsNational The area to be included in the export of the national polls.
     */
    public void setIncludeAreaAsNational(final String includeAreaAsNational) {
        this.includeAreaAsNational = includeAreaAsNational;
    }
}
