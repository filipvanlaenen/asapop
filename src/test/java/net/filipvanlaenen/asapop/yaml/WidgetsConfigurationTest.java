package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>WidgetsConfiguration</code> class.
 */
public class WidgetsConfigurationTest {
    /**
     * Verifies that the getter method <code>getTableFontFamily</code> is wired correctly to the setter method
     * <code>setTableFontFamily</code>.
     */
    @Test
    public void getTableFontFamilyShouldBeWiredCorrectlyToSetTableFontFamily() {
        WidgetsConfiguration widgetsConfiguration = new WidgetsConfiguration();
        widgetsConfiguration.setTableFontFamily("Foo");
        assertEquals("Foo", widgetsConfiguration.getTableFontFamily());
    }

    /**
     * Verifies that the getter method <code>getTableStylesheets</code> is wired correctly to the setter method
     * <code>setTableStylesheets</code>.
     */
    @Test
    public void getTableStylesheetsShouldBeWiredCorrectlyToGetTableStylesheets() {
        WidgetsConfiguration widgetsConfiguration = new WidgetsConfiguration();
        String[] tableStylesheets = new String[] {"Foo"};
        widgetsConfiguration.setTableStylesheets(tableStylesheets);
        assertEquals(tableStylesheets, widgetsConfiguration.getTableStylesheets());
    }
}
