package net.filipvanlaenen.asapop.yaml;

import java.util.Set;

/**
 * Class representing the top element for the YAML file containing the website configuration data.
 */
public class WebsiteConfiguration {
    /**
     * The area configurations.
     */
    private Set<AreaConfiguration> areaConfigurations;

    /**
     * Default constructor.
     */
    public WebsiteConfiguration() {
    }

    /**
     * Returns a set with the area configurations.
     *
     * @return A set with the area configurations.
     */
    public Set<AreaConfiguration> getAreaConfigurations() {
        return areaConfigurations;
    }

    /**
     * Sets the set with the area configurations.
     *
     * @param areaConfigurations The set with the area configurations.
     */
    public void setAreaConfigurations(final Set<AreaConfiguration> areaConfigurations) {
        this.areaConfigurations = areaConfigurations;
    }
}
