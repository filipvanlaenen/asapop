package net.filipvanlaenen.asapop.yaml;

/**
 * Class representing the configuration for the widgets.
 */
public class WidgetsConfiguration {
    /**
     * The stylesheets for the table widgets.
     */
    private String[] tableStylesheets;

    /**
     * Default constructor.
     */
    public WidgetsConfiguration() {
    }

    /**
     * Returns the stylesheets for the table widgets.
     *
     * @return The stylesheets for the table widgets.
     */
    public String[] getTableStylesheets() {
        return tableStylesheets;
    }

    /**
     * Sets the stylesheets for the table widgets.
     *
     * @param tableStylesheets The stylesheets for the table widgets.
     */
    public void setTableStylesheets(final String[] tableStylesheets) {
        this.tableStylesheets = tableStylesheets;
    }
}
