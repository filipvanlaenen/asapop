package net.filipvanlaenen.asapop.yaml.scrapeconfiguration;

/**
 * Class representing the top element for the YAML file containing scrape configuration data.
 */
public class ScrapeConfiguration {
    /**
     * The possible next election page names.
     */
    private String[] possibleNextElectionPageNames;

    /**
     * Returns the possible next election page names.
     *
     * @return The possible next election page names.
     */
    public String[] getPossibleNextElectionPageNames() {
        return possibleNextElectionPageNames;
    }

    /**
     * Sets the possible next election page names.
     *
     * @param electedOffices The possible next election page names.
     */
    public void setPossibleNextElectionPageNames(final String[] possibleNextElectionPageNames) {
        this.possibleNextElectionPageNames = possibleNextElectionPageNames;
    }
}
