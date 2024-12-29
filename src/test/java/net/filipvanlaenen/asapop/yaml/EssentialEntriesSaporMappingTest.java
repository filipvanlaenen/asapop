package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>EssentialEntriesSaporMapping</code> class.
 */
public class EssentialEntriesSaporMappingTest {
    /**
     * Verifies that the getter method <code>getResidual</code> is wired correctly to the setter method
     * <code>setResidual</code>.
     */
    @Test
    public void getResidualShouldBeWiredCorrectlyToSetResidual() {
        EssentialEntriesSaporMapping essentialEntriesSaporMapping = new EssentialEntriesSaporMapping();
        essentialEntriesSaporMapping.setResidual(1);
        assertEquals(1, essentialEntriesSaporMapping.getResidual());
    }

    /**
     * Verifies that the getter method <code>getTargets</code> is wired correctly to the setter method
     * <code>setTargets</code>.
     */
    @Test
    public void getTargetsShouldBeWiredCorrectlyToSetTargets() {
        EssentialEntriesSaporMapping essentialEntriesSaporMapping = new EssentialEntriesSaporMapping();
        Map<String, Integer> targets = Map.of("Party A", 1, "Party B", 2);
        essentialEntriesSaporMapping.setRelativeTargets(targets);
        assertEquals(targets, essentialEntriesSaporMapping.getRelativeTargets());
    }
}
