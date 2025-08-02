package net.filipvanlaenen.asapop.yaml;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.filipvanlaenen.kolektoj.Map;

/**
 * Class representing a subdivision's area configuration element for the YAML file containing the website configuration.
 */
public class AreaSubdivisionConfiguration {
    /**
     * The subdivision's area code.
     */
    private String areaCode;
    /**
     * The configuration for the CSV file.
     */
    private CsvConfiguration csvConfiguration;
    /**
     * A map with the translation of the term in a number of languages.
     */
    @JsonDeserialize(using = StringStringMapDeserializer.class)
    private Map<String, String> translations;

    /**
     * Returns the subdivision's area code.
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
     * Returns a map with the translation of the term in a number of languages.
     *
     * @return A map with the translation of the term in a number of languages.
     */
    public Map<String, String> getTranslations() {
        return translations;
    }

    /**
     * Sets the subdivision's area code.
     *
     * @param areaCode The subdivision's area code.
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
     * Sets the map with the translations of the term in a number of languages.
     *
     * @param translations The map with the translations of the term in a number of languages.
     */
    public void setTranslations(final Map<String, String> translations) {
        this.translations = translations;
    }
}
