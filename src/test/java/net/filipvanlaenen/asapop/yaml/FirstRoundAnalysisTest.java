package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>FirstRoundAnalysis</code> class.
 */
public class FirstRoundAnalysisTest {
    /**
     * Verifies that the getter method <code>getProbabilityMassFunction</code> is wired correctly to the setter method
     * <code>setProbabilityMassFunction</code>.
     */
    @Test
    public void getProbabilityMassFunctionShouldBeWiredCorrectlyToSetProbabilityMassFunction() {
        FirstRoundAnalysis firstRoundAnalysis = new FirstRoundAnalysis();
        FirstRoundResultProbabilityMass firstRoundResultProbabilityMass = new FirstRoundResultProbabilityMass();
        Set<FirstRoundResultProbabilityMass> probabilityMassFunction = Set.of(firstRoundResultProbabilityMass);
        firstRoundAnalysis.setProbabilityMassFunction(probabilityMassFunction);
        assertEquals(probabilityMassFunction, firstRoundAnalysis.getProbabilityMassFunction());
    }
}
