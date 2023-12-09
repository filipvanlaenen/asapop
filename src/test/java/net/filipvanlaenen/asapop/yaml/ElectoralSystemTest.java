package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ElectoralSystem</code> class.
 */
public class ElectoralSystemTest {
    /**
     * Verifies that the getter method <code>getNumberOfSeats</code> is wired correctly to the setter method
     * <code>setNumberOfSeats</code>.
     */
    @Test
    public void getNumberOfSeatsShouldBeWiredCorrectlyToSetNumberOfSeats() {
        ElectoralSystem electoralSystem = new ElectoralSystem();
        electoralSystem.setNumberOfSeats(2);
        assertEquals(2, electoralSystem.getNumberOfSeats());
    }

    /**
     * Verifies that the getter method <code>getThreshold</code> is wired correctly to the setter method
     * <code>setThreshold</code>.
     */
    @Test
    public void getThresholdShouldBeWiredCorrectlyToSetThreshold() {
        ElectoralSystem electoralSystem = new ElectoralSystem();
        electoralSystem.setThreshold(1D);
        assertEquals(1D, electoralSystem.getThreshold());
    }
}
