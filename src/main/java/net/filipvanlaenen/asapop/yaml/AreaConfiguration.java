package net.filipvanlaenen.asapop.yaml;

import net.filipvanlaenen.kolektoj.Map;

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
     * The election configurations.
     */
    private ElectionLists electionLists;
    /**
     * A map with the names of the polling firms not included and the keys for the reason why.
     */
    private Map<String, String> pollingFirmsNotIncluded;
    /**
     * The area's subdivisions.
     */
    private AreaSubdivisionConfiguration[] subdivisions;
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
     * Returns the elections.
     *
     * @return The elections.
     */
    public ElectionLists getElections() {
        return electionLists;
    }

    /**
     * Returns the map with the names of the polling firms not included and the keys for the reasons why.
     *
     * @return The map with the names of the polling firms not included and the keys for the reasons why.
     */
    public Map<String, String> getPollingFirmsNotIncluded() {
        return pollingFirmsNotIncluded;
    }

    /**
     * Returns an array with the area's subdivision configurations.
     *
     * @return An array with the area's subdivision configurations.
     */
    public AreaSubdivisionConfiguration[] getSubdivsions() {
        return subdivisions;
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
     * Sets the elections.
     *
     * @param elections The elections.
     */
    public void setElections(final ElectionLists elections) {
        this.electionLists = elections;
    }

    /**
     * Sets the map with the names of the polling firms not included and the keys for the reasons why.
     *
     * @param pollingFirmsNotIncluded The map with the names of the polling firms not included and the keys for the
     *                                reasons why.
     */
    public void setPollingFirmsNotIncluded(final Map<String, String> pollingFirmsNotIncluded) {
        this.pollingFirmsNotIncluded = pollingFirmsNotIncluded;
    }

    /**
     * Sets the array with the area's subdivision configurations.
     *
     * @param subdivisions The array with the area's subdivision configurations.'
     */
    public void setSubdivisions(final AreaSubdivisionConfiguration[] subdivisions) {
        this.subdivisions = subdivisions;
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
