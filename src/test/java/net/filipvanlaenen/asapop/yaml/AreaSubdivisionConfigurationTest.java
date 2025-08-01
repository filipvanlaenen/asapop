package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;

/**
 * Unit tests on the <code>AreaSubdivisionConfiguration</code> class.
 */
public class AreaSubdivisionConfigurationTest {
    /**
     * Verifies that the getter method <code>getAreaCode</code> is wired correctly to the setter method
     * <code>setAreaCode</code>.
     */
    @Test
    public void getAreaCodeShouldBeWiredCorrectlyToSetAreaCode() {
        AreaSubdivisionConfiguration areaSubdivisionConfiguration = new AreaSubdivisionConfiguration();
        areaSubdivisionConfiguration.setAreaCode("ab");
        assertEquals("ab", areaSubdivisionConfiguration.getAreaCode());
    }

    /**
     * Verifies that the getter method <code>getCsvConfiguration</code> is wired correctly to the setter method
     * <code>setCsvConfiguration</code>.
     */
    @Test
    public void getCsvConfigurationShouldBeWiredCorrectlyToSetCsvConfiguration() {
        AreaSubdivisionConfiguration areaSubdivisionConfiguration = new AreaSubdivisionConfiguration();
        CsvConfiguration csvConfiguration = new CsvConfiguration();
        areaSubdivisionConfiguration.setCsvConfiguration(csvConfiguration);
        assertEquals(csvConfiguration, areaSubdivisionConfiguration.getCsvConfiguration());
    }

    /**
     * Verifies that the getter method <code>getTranslations</code> is wired correctly to the setter method
     * <code>setTranslations</code>.
     */
    @Test
    public void getTranslationsShouldBeWiredCorrectlyToSetTranslations() {
        AreaSubdivisionConfiguration areaSubdivisionConfiguration = new AreaSubdivisionConfiguration();
        Map<String, String> translations = Map.of("en", "Foo");
        areaSubdivisionConfiguration.setTranslations(translations);
        assertEquals(translations, areaSubdivisionConfiguration.getTranslations());
    }
}
