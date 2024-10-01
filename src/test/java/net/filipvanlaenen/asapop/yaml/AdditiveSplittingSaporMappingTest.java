package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>AdditiveSplittingSaporMapping</code> class.
 */
public class AdditiveSplittingSaporMappingTest {
    /**
     * Verifies that the getter method <code>getSources</code> is wired correctly to the setter method
     * <code>setSources</code>.
     */
    @Test
    public void getSourcesShouldBeWiredCorrectlyToSetSources() {
        AdditiveSplittingSaporMapping additiveSplittingSaporMapping = new AdditiveSplittingSaporMapping();
        Set<String> sources = Set.of("AA001", "AA002");
        additiveSplittingSaporMapping.setSources(sources);
        assertEquals(sources, additiveSplittingSaporMapping.getSources());
    }

    /**
     * Verifies that the getter method <code>getTargets</code> is wired correctly to the setter method
     * <code>setTargets</code>.
     */
    @Test
    public void getTargetShouldBeWiredCorrectlyToSetTarget() {
        AdditiveSplittingSaporMapping additiveSplittingSaporMapping = new AdditiveSplittingSaporMapping();
        Map<String, Integer> targets = Map.of("Party A", 1, "Party B", 2);
        additiveSplittingSaporMapping.setTargets(targets);
        assertEquals(targets, additiveSplittingSaporMapping.getTargets());
    }
}
