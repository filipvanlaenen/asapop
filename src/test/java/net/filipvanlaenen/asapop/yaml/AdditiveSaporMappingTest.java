package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>AdditiveSaporMapping</code> class.
 */
public class AdditiveSaporMappingTest {
    /**
     * Verifies that the getter method <code>getSources</code> is wired correctly to the setter method
     * <code>setSources</code>.
     */
    @Test
    public void getSourceShouldBeWiredCorrectlyToSetSource() {
        AdditiveSaporMapping additiveSaporMapping = new AdditiveSaporMapping();
        Set<String> sources = Set.of("AA001", "AA002");
        additiveSaporMapping.setSources(sources);
        assertEquals(sources, additiveSaporMapping.getSources());
    }

    /**
     * Verifies that the getter method <code>getTarget</code> is wired correctly to the setter method
     * <code>setTarget</code>.
     */
    @Test
    public void getTargetShouldBeWiredCorrectlyToSetTarget() {
        AdditiveSaporMapping additiveSaporMapping = new AdditiveSaporMapping();
        additiveSaporMapping.setTarget("Target");
        assertEquals("Target", additiveSaporMapping.getTarget());
    }
}
