package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>WebsiteConfiguration</code> class.
 */
public class WebsiteConfigurationTest {
    /**
     * Verifies that the getter method <code>getId</code> is wired correctly to the setter method <code>setId</code>.
     */
    @Test
    public void getIdShouldBeWiredCorrectlyToSetId() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setId("ab");
        assertEquals("ab", websiteConfiguration.getId());
    }

    /**
     * Verifies that the getter method <code>getAreaConfigurations</code> is wired correctly to the setter method
     * <code>setAreaConfigurations</code>.
     */
    @Test
    public void getAreaConfigurationsShouldBeWiredCorrectlyToSetAreaConfigurations() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        Set<AreaConfiguration> areaConfigurations = Set.of(new AreaConfiguration(), new AreaConfiguration());
        websiteConfiguration.setAreaConfigurations(areaConfigurations);
        assertEquals(areaConfigurations, websiteConfiguration.getAreaConfigurations());
    }

    /**
     * Verifies that the getter method <code>getName</code> is wired correctly to the setter method
     * <code>setName</code>.
     */
    @Test
    public void getNameShouldBeWiredCorrectlyToSetName() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Foo");
        assertEquals("Foo", websiteConfiguration.getName());
    }

    /**
     * Verifies that the getter method <code>getWidgetsConfigurations</code> is wired correctly to the setter method
     * <code>setWidgetsConfigurations</code>.
     */
    @Test
    public void getWidgetsConfigurationsShouldBeWiredCorrectlyToSetWidgetsConfigurations() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        WidgetsConfiguration widgetsConfiguration = new WidgetsConfiguration();
        websiteConfiguration.setWidgetsConfiguration(widgetsConfiguration);
        assertEquals(widgetsConfiguration, websiteConfiguration.getWidgetsConfiguration());
    }
}
