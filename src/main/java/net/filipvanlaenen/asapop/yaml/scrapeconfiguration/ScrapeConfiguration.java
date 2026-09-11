package net.filipvanlaenen.asapop.yaml.scrapeconfiguration;

/**
 * Class representing the top element for the YAML file containing scrape configuration data.
 */
public class ScrapeConfiguration {
    /**
     * The next election page name.
     */
    private String nextElectionPageName;
    /**
     * The possible next election page names.
     */
    private String[] possibleNextElectionPageNames;
    /**
     * Scrape configurations for one or more tables.
     */
    private TableConfiguration[] tableConfigurations;

    /**
     * Returns the next election page name.
     *
     * @return The next election page name.
     */
    public String getNextElectionPageName() {
        return nextElectionPageName;
    }

    /**
     * Returns the possible next election page names.
     *
     * @return The possible next election page names.
     */
    public String[] getPossibleNextElectionPageNames() {
        return possibleNextElectionPageNames;
    }

    /**
     * Returns the scrape configurations for one or more tables.
     *
     * @return The scrape configurations for one or more tables.
     */
    public TableConfiguration[] getTableConfigurations() {
        return tableConfigurations;
    }

    /**
     * Sets the next election page name.
     *
     * @param nextElectionPageName The next election page name.
     */
    public void setNextElectionPageName(final String nextElectionPageName) {
        this.nextElectionPageName = nextElectionPageName;
    }

    /**
     * Sets the possible next election page names.
     *
     * @param possibleNextElectionPageNames The possible next election page names.
     */
    public void setPossibleNextElectionPageNames(final String[] possibleNextElectionPageNames) {
        this.possibleNextElectionPageNames = possibleNextElectionPageNames;
    }

    /**
     * Sets the scrape configurations for one or more tables.
     *
     * @param tableConfigurations The scrape configurations for one or more tables.
     */
    public void setTableConfigurations(final TableConfiguration[] tableConfigurations) {
        this.tableConfigurations = tableConfigurations;
    }
}
