package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SaporConfiguration</code> class.
 */
public class SaporConfigurationTest {
    /**
     * Verifies that the getter method <code>getArea</code> is wired correctly to the setter method
     * <code>setArea</code>.
     */
    @Test
    public void getAreaShouldBeWiredCorrectlyToSetArea() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setArea("AR");
        assertEquals("AR", saporConfiguration.getArea());
    }

    /**
     * Verifies that the getter method <code>getLastElectionDate</code> is wired correctly to the setter method
     * <code>setLastElectionDate</code>.
     */
    @Test
    public void getLastElectionDateShouldBeWiredCorrectlyToSetLastElectionDate() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setLastElectionDate("2022-12-25");
        assertEquals("2022-12-25", saporConfiguration.getLastElectionDate());
    }

    /**
     * Verifies that the getter method <code>getMapping</code> is wired correctly to the setter method
     * <code>setMapping</code>.
     */
    @Test
    public void getMappingShouldBeWiredCorrectlyToSetMapping() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        Set<SaporMapping> mapping = Set.of(new SaporMapping());
        saporConfiguration.setMapping(mapping);
        assertEquals(mapping, saporConfiguration.getMapping());
    }

    /**
     * Verifies that the getter method <code>getPreferredScope</code> is wired correctly to the setter method
     * <code>setPreferredScope</code>.
     */
    @Test
    public void getPreferredScopeShouldBeWiredCorrectlyToSetPreferredScope() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setPreferredScope("E");
        assertEquals("E", saporConfiguration.getPreferredScope());
    }

    /**
     * Verifies that the getter method <code>getRegion</code> is wired correctly to the setter method
     * <code>setRegion</code>.
     */
    @Test
    public void getRegionShouldBeWiredCorrectlyToSetRegion() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setRegion("RE");
        assertEquals("RE", saporConfiguration.getRegion());
    }

    /**
     * Verifies that the getter method <code>getScope</code> is wired correctly to the setter method
     * <code>setScope</code>.
     */
    @Test
    public void getScopeShouldBeWiredCorrectlyToSetScope() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setScope("E");
        assertEquals("E", saporConfiguration.getScope());
    }

    /**
     * Verifies that the getter method <code>getResponseScenarioSelection</code> is wired correctly to the setter method
     * <code>setResponseScenarioSelection</code>.
     */
    @Test
    public void getResponseScenarioSelectionShouldBeWiredCorrectlyToSetResponseScenarioSelection() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setResponseScenarioSelection("averaged");
        assertEquals("averaged", saporConfiguration.getResponseScenarioSelection());
    }
}
