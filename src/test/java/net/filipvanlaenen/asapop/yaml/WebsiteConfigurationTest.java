package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>WebsiteConfiguration</code> class.
 */
public class WebsiteConfigurationTest {
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
}
