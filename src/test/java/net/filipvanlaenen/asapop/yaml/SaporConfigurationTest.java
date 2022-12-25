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
}
