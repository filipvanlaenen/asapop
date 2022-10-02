package net.filipvanlaenen.asapop.yaml;

import java.util.List;

/**
 * Class representing the CSV file configuration element for the YAML file containing the website configuration.
 */
public class CsvConfiguration {
    /**
     * The list with the keys for the electoral lists.
     */
    private List<String> electoralListKeys;

    /**
     * Returns the list with the keys for the electoral lists.
     *
     * @return The list with the keys for the electoral lists.
     */
    public List<String> getElectoralListKeys() {
        return electoralListKeys;
    }

    /**
     * Sets the list with the keys for the electoral lists.
     *
     * @param electoralListKeys The list with the keys for the electoral lists.
     */
    public void setElectoralListKeys(final List<String> electoralListKeys) {
        this.electoralListKeys = electoralListKeys;
    }
}
