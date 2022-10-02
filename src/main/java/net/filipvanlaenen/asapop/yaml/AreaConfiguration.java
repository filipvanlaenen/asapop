package net.filipvanlaenen.asapop.yaml;

import java.util.Map;

/**
 * Class representing the area configuration element for the YAML file containing the website configuration.
 */
public class AreaConfiguration {
    /**
     * The area code.
     */
    private String areaCode;
    /**
     * The configuration for the CSV file.
     */
    private CsvConfiguration csvConfiguration;
    /**
     * The URL for the GitHub website.
     */
    private String gitHubWebsiteUrl;
    /**
     * The date for the next election.
     */
    private String nextElectionDate;
    /**
     * A map with the translation of the term in a number of languages.
     */
    private Map<String, String> translations;

    /**
     * Returns the area code.
     *
     * @return The area code.
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * Returns the configuration for the CSV file.
     *
     * @return The configuration for the CSV file.
     */
    public CsvConfiguration getCsvConfiguration() {
        return csvConfiguration;
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
     * Returns the date for the next election.
     *
     * @return The date for the next election.
     */
    public String getNextElectionDate() {
        return nextElectionDate;
    }

    /**
     * Returns a map with the translation of the term in a number of languages.
     *
     * @return A map with the translation of the term in a number of languages.
     */
    public Map<String, String> getTranslations() {
        return translations;
    }

    /**
     * Sets the area code.
     *
     * @param areaCode The area code.
     */
    public void setAreaCode(final String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * Sets the configuration for the CSV file.
     *
     * @param csvConfiguration The configuration for the CSV file.
     */
    public void setCsvConfiguration(final CsvConfiguration csvConfiguration) {
        this.csvConfiguration = csvConfiguration;
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

    /**
     * Sets the map with the translations of the term in a number of languages.
     *
     * @param translations The map with the translations of the term in a number of languages.
     */
    public void setTranslations(final Map<String, String> translations) {
        this.translations = translations;
    }
}
