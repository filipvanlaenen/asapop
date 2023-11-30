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
     * Verifies that the getter method <code>getElections</code> is wired correctly to the setter method
     * <code>setElections</code>.
     */
    @Test
    public void getElectionsShouldBeWiredCorrectlyToSetElections() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        ElectionLists elections = new ElectionLists();
        areaConfiguration.setElections(elections);
        assertEquals(elections, areaConfiguration.getElections());
    }

    /**
     * Verifies that the getter method <code>getPollingFirmsNotIncluded</code> is wired correctly to the setter method
     * <code>setPollingFirmsNotIncluded</code>.
     */
    @Test
    public void getPollingFirmsNotIncludedShouldBeWiredCorrectlyToSetPollingFirmsNotIncluded() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        Map<String, String> pollingFirmsNotIncluded = Map.of("ACME", "notAPollingFirm");
        areaConfiguration.setPollingFirmsNotIncluded(pollingFirmsNotIncluded);
        assertEquals(pollingFirmsNotIncluded, areaConfiguration.getPollingFirmsNotIncluded());
    }

    /**
     * Verifies that the getter method <code>getSubdivisions</code> is wired correctly to the setter method
     * <code>setSubdivisions</code>.
     */
    @Test
    public void getSubdivisionsShouldBeWiredCorrectlyToSetSubdivisions() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        AreaSubdivisionConfiguration[] subdivisions =
                new AreaSubdivisionConfiguration[] {new AreaSubdivisionConfiguration()};
        areaConfiguration.setSubdivisions(subdivisions);
        assertEquals(subdivisions, areaConfiguration.getSubdivsions());
    }

    /**
     * Verifies that the getter method <code>getTranslations</code> is wired correctly to the setter method
     * <code>setTranslations</code>.
     */
    @Test
    public void getTranslationsShouldBeWiredCorrectlyToSetTranslations() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        Map<String, String> translations = new HashMap<String, String>();
        translations.put("en", "Foo");
        areaConfiguration.setTranslations(translations);
        assertEquals(translations, areaConfiguration.getTranslations());
    }
}
