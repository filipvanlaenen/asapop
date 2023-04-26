package net.filipvanlaenen.asapop.yaml;

import java.util.Map;

/**
 * Class representing a list with elections for the YAML file containing the website configuration.
 */
public class ElectionList {
    /**
     * A map holding the election dates.
     */
    private Map<Integer, String> dates;
    /**
     * The URL for the GitHub website.
     */
    private String gitHubWebsiteUrl;

    /**
     * Returns the map with the election dates.
     *
     * @return The map with the election dates.
     */
    public Map<Integer, String> getDates() {
        return dates;
    }

    /**
     * Returns the URL for the GitHub website.
     *
     * @return The URL for the GitHub website.
     */
    public String getGitHubWebsiteUrl() {
        return gitHubWebsiteUrl;
    }

    /**
     * Sets the map with the election dates.
     *
     * @param dates The map with the election dates.
     */
    public void setDates(final Map<Integer, String> dates) {
        this.dates = dates;
    }

    /**
     * Sets the URL for the GitHub website.
     *
     * @param gitHubWebsiteUrl The URL for the GitHub website.
     */
    public void setGitHubWebsiteUrl(final String gitHubWebsiteUrl) {
        this.gitHubWebsiteUrl = gitHubWebsiteUrl;
    }
}
