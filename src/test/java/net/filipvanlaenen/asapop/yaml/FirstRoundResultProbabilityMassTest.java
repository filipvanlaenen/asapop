package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>FirstRoundResultProbabilityMass</code> class.
 */
public class FirstRoundResultProbabilityMassTest {
    /**
     * Verifies that the getter method <code>getElectoralLists</code> is wired correctly to the setter method
     * <code>setElectoralLists</code>.
     */
    @Test
    public void getElectoralListsShouldBeWiredCorrectlyToSetElectoralLists() {
        FirstRoundResultProbabilityMass firstRoundResultProbabilityMass = new FirstRoundResultProbabilityMass();
        Set<String> electoralLists = Set.of("ABC", "DEF");
        firstRoundResultProbabilityMass.setElectoralLists(electoralLists);
        assertEquals(electoralLists, firstRoundResultProbabilityMass.getElectoralLists());
    }

    /**
     * Verifies that the getter method <code>getProbabilityMass</code> is wired correctly to the setter method
     * <code>setProbabilityMass</code>.
     */
    @Test
    public void getProbabilityMassShouldBeWiredCorrectlyToSetProbabilityMass() {
        FirstRoundResultProbabilityMass firstRoundResultProbabilityMass = new FirstRoundResultProbabilityMass();
        firstRoundResultProbabilityMass.setProbabilityMass(1D);
        assertEquals(1D, firstRoundResultProbabilityMass.getProbabilityMass());
    }
}
