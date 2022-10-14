package net.filipvanlaenen.asapop.yaml;

import java.util.Map;
import java.util.Set;

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
    private Set<ElectionConfiguration> electionConfigurations;
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

    public Set<ElectionConfiguration> getElectionConfigurations() {
        return electionConfigurations;
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

    public void setElectionConfigurations(final Set<ElectionConfiguration> electionConfigurations) {
        this.electionConfigurations = electionConfigurations;
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
