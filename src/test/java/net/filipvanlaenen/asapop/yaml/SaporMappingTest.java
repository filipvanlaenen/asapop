package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SaporMapping</code> class.
 */
public class SaporMappingTest {
    /**
     * Verifies that the getter method <code>getAdditiveMapping</code> is wired correctly to the setter method
     * <code>setAdditiveMapping</code>.
     */
    @Test
    public void getAdditiveMappingShouldBeWiredCorrectlyToSetAdditiveMapping() {
        SaporMapping saporMapping = new SaporMapping();
        AdditiveSaporMapping additiveMapping = new AdditiveSaporMapping();
        saporMapping.setAdditiveMapping(additiveMapping);
        assertEquals(additiveMapping, saporMapping.getAdditiveMapping());
    }

    /**
     * Verifies that the getter method <code>getDirectMapping</code> is wired correctly to the setter method
     * <code>setDirectMapping</code>.
     */
    @Test
    public void getDirectMappingShouldBeWiredCorrectlyToSetDirectMapping() {
        SaporMapping saporMapping = new SaporMapping();
        DirectSaporMapping directMapping = new DirectSaporMapping();
        saporMapping.setDirectMapping(directMapping);
        assertEquals(directMapping, saporMapping.getDirectMapping());
    }
}
