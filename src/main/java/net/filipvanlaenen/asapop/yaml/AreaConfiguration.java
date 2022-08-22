package net.filipvanlaenen.asapop.yaml;

/**
 * Class representing the area configuration element for the YAML file containing the website configuration.
 */
public class AreaConfiguration {
    /**
     * The URL for the GitHub website.
     */
    private String gitHubWebsiteUrl;
    /**
     * The date for the next election.
     */
    private String nextElectionDate;

    /**
     * Returns the URL for the GitHub website.
     *
     * @return The URL for the GitHub website.
     */
    public String getGitHubWebsiteUrl() {
        return gitHubWebsiteUrl;
    }

    /**
     * Returns the date for the next election.
     *
     * @return The date for the next election.
     */
    public String getNextElectionDate() {
        return nextElectionDate;
    }

    /**
     * Sets the URL for the GitHub website.
     *
     * @param gitHubWebsiteUrl The URL for the GitHub website.
     */
    public void setGitHubWebsiteUrl(final String gitHubWebsiteUrl) {
        this.gitHubWebsiteUrl = gitHubWebsiteUrl;
    }

    /**
     * Sets the date for the next election.
     *
     * @param nextElectionDate The date for the next election.
     */
    public void setNextElectionDate(final String nextElectionDate) {
        this.nextElectionDate = nextElectionDate;
    }
}