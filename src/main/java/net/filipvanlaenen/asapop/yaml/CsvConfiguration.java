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
     * Returns the list with the IDs for the electoral lists.
     *
     * @return The list with the IDs for the electoral lists.
     */
    public List<String> getElectoralListIds() {
        return electoralListIds;
    }

    /**
     * Sets the list with the IDs for the electoral lists.
     *
     * @param electoralListIds The list with the IDs for the electoral lists.
     */
    public void setElectoralListIds(final List<String> electoralListIds) {
        this.electoralListIds = electoralListIds;
    }
}
