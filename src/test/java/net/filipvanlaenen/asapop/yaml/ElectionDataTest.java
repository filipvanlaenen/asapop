package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ElectionData</code> class.
 */
public class ElectionDataTest {
    /**
     * Verifies that the getter method <code>getElectoralSystem</code> is wired correctly to the setter method
     * <code>setElectoralSystem</code>.
     */
    @Test
    public void getElectoralSystemShouldBeWiredCorrectlyToSetElectoralSystem() {
        ElectionData electionData = new ElectionData();
        ElectoralSystem electoralSystem = new ElectoralSystem();
        electionData.setElectoralSystem(electoralSystem);
        assertEquals(electoralSystem, electionData.getElectoralSystem());
    }

    /**
     * Verifies that the getter method <code>getPopulationSize</code> is wired correctly to the setter method
     * <code>setPopulationSize</code>.
     */
    @Test
    public void getPopulationSizeShouldBeWiredCorrectlyToSetPopulationSize() {
        ElectionData electionData = new ElectionData();
        electionData.setPopulationSize(2L);
        assertEquals(2L, electionData.getPopulationSize());
    }
}
