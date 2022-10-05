package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>AreaConfiguration</code> class.
 */
public class AreaConfigurationTest {
    /**
     * Verifies that the getter method <code>getAreaCode</code> is wired correctly to the setter method
     * <code>setAreaCode</code>.
     */
    @Test
    public void getAreaCodeShouldBeWiredCorrectlyToSetAreaCode() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        areaConfiguration.setAreaCode("ab");
        assertEquals("ab", areaConfiguration.getAreaCode());
    }

    /**
     * Verifies that the getter method <code>getCsvConfiguration</code> is wired correctly to the setter method
     * <code>setCsvConfiguration</code>.
     */
    @Test
    public void getCsvConfigurationShouldBeWiredCorrectlyToSetCsvConfiguration() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        CsvConfiguration csvConfiguration = new CsvConfiguration();
        areaConfiguration.setCsvConfiguration(csvConfiguration);
        assertEquals(csvConfiguration, areaConfiguration.getCsvConfiguration());
    }

    /**
     * Verifies that the getter method <code>getGitHubWebsiteUrl</code> is wired correctly to the setter method
     * <code>setGitHubWebsiteUrl</code>.
     */
    @Test
    public void getGitHubWebsiteUrlShouldBeWiredCorrectlyToSetGitHubWebsiteUrl() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        areaConfiguration.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/swedish_polls");
        assertEquals("https://filipvanlaenen.github.io/swedish_polls", areaConfiguration.getGitHubWebsiteUrl());
    }

    /**
     * Verifies that the getter method <code>getNextElectionDate</code> is wired correctly to the setter method
     * <code>setNextElectionDate</code>.
     */
    @Test
    public void getNextElectionDateShouldBeWiredCorrectlyToSetNextElectionDate() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        areaConfiguration.setNextElectionDate("2022-09-11");
        assertEquals("2022-09-11", areaConfiguration.getNextElectionDate());
    }

    /**
     * Verifies that the getter method <code>getTranslations</code> is wired correctly to the setter method
     * <code>setTranslations</code>.
     */
    @Test
    public void getTranslationsShouldBeWiredCorrectlyToSetTranslations() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        Map<String, String> translations = new HashMap<String, String>();
        areaConfiguration.setTranslations(translations);
        assertEquals(translations, areaConfiguration.getTranslations());
    }
}
