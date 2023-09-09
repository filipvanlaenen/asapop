package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SplittingSaporMapping</code> class.
 */
public class SplittingSaporMappingTest {
    /**
     * Verifies that the getter method <code>getSource</code> is wired correctly to the setter method
     * <code>setSource</code>.
     */
    @Test
    public void getSourceShouldBeWiredCorrectlyToSetSource() {
        SplittingSaporMapping splittingSaporMapping = new SplittingSaporMapping();
        splittingSaporMapping.setSource("AA001");
        assertEquals("AA001", splittingSaporMapping.getSource());
    }

    /**
     * Verifies that the getter method <code>getTargets</code> is wired correctly to the setter method
     * <code>setTargets</code>.
     */
    @Test
    public void getTargetsShouldBeWiredCorrectlyToSetTargets() {
        SplittingSaporMapping splittingSaporMapping = new SplittingSaporMapping();
        Map<String, Integer> targets = Map.of("Party A", 1, "Party B", 2);
        splittingSaporMapping.setTargets(targets);
        assertEquals(targets, splittingSaporMapping.getTargets());
    }
}
