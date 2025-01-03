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
     * The ID of the website.
     */
    private String id;
    /**
     * The name of the website.
     */
    private String name;
    /**
     * The widgets configuration.
     */
    private WidgetsConfiguration widgetsConfiguration;

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
     * Returns the ID of the website.
     *
     * @return The ID of the website.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the website.
     *
     * @return The name of the website.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the widgets configuration.
     *
     * @return The widgets configuration.
     */
    public WidgetsConfiguration getWidgetsConfiguration() {
        return widgetsConfiguration;
    }

    /**
     * Sets the set with the area configurations.
     *
     * @param areaConfigurations The set with the area configurations.
     */
    public void setAreaConfigurations(final Set<AreaConfiguration> areaConfigurations) {
        this.areaConfigurations = areaConfigurations;
    }

    /**
     * Sets the ID of the website.
     *
     * @param id The ID of the website.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Sets the name of the website.
     *
     * @param name The name of the website.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the widgets configuration.
     *
     * @param widgetsConfiguration The widgets configuration.
     */
    public void setWidgetsConfiguration(final WidgetsConfiguration widgetsConfiguration) {
        this.widgetsConfiguration = widgetsConfiguration;
    }
}
