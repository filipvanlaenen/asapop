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
}
