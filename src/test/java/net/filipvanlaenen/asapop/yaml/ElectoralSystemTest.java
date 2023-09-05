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
        ElectoralSystem slectoralSystem = new ElectoralSystem();
        slectoralSystem.setNumberOfSeats(2);
        assertEquals(2, slectoralSystem.getNumberOfSeats());
    }
}
