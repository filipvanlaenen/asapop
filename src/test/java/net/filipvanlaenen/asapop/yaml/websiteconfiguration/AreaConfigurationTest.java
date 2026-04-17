package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;

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
     * Verifies that the getter method <code>getElectedBodies</code> is wired correctly to the setter method
     * <code>setElectedBodies</code>.
     */
    @Test
    public void getElectedBodiesShouldBeWiredCorrectlyToSetElectedBodies() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        ElectedBody[] electedBodies = new ElectedBody[] {new ElectedBody()};
        areaConfiguration.setElectedBodies(electedBodies);
        assertEquals(electedBodies, areaConfiguration.getElectedBodies());
    }

    /**
     * Verifies that the getter method <code>getElectedOffices</code> is wired correctly to the setter method
     * <code>setElectedOffices</code>.
     */
    @Test
    public void getElectedOfficesShouldBeWiredCorrectlyToSetElectedOffices() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        ElectedOffice[] electedOffices = new ElectedOffice[] {new ElectedOffice()};
        areaConfiguration.setElectedOffices(electedOffices);
        assertEquals(electedOffices, areaConfiguration.getElectedOffices());
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
        assertEquals(subdivisions, areaConfiguration.getSubdivisions());
    }

    /**
     * Verifies that the getter method <code>getTranslations</code> is wired correctly to the setter method
     * <code>setTranslations</code>.
     */
    @Test
    public void getTranslationsShouldBeWiredCorrectlyToSetTranslations() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        Map<String, String> translations = Map.of("en", "Foo");
        areaConfiguration.setTranslations(translations);
        assertEquals(translations, areaConfiguration.getTranslations());
    }
}
