package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>DirectSaporMapping</code> class.
 */
public class DirectSaporMappingTest {
    /**
     * Sample compensation factor for testing.
     */
    private static final double COMPENSATION_FACTOR = 0.56D;

    /**
     * Verifies that the getter method <code>getCompensationFactor</code> is wired correctly to the setter method
     * <code>setCompensationFactor</code>.
     */
    @Test
    public void getCompensationFactorShouldBeWiredCorrectlyToSetCompensationFactor() {
        DirectSaporMapping directSaporMapping = new DirectSaporMapping();
        directSaporMapping.setCompensationFactor(COMPENSATION_FACTOR);
        assertEquals(COMPENSATION_FACTOR, directSaporMapping.getCompensationFactor());
    }

    /**
     * Verifies that the getter method <code>getSource</code> is wired correctly to the setter method
     * <code>setSource</code>.
     */
    @Test
    public void getSourceShouldBeWiredCorrectlyToSetSource() {
        DirectSaporMapping directSaporMapping = new DirectSaporMapping();
        directSaporMapping.setSource("SOURCE");
        assertEquals("SOURCE", directSaporMapping.getSource());
    }

    /**
     * Verifies that the getter method <code>getTarget</code> is wired correctly to the setter method
     * <code>setTarget</code>.
     */
    @Test
    public void getTargetShouldBeWiredCorrectlyToSetTarget() {
        DirectSaporMapping directSaporMapping = new DirectSaporMapping();
        directSaporMapping.setTarget("Target");
        assertEquals("Target", directSaporMapping.getTarget());
    }
}
