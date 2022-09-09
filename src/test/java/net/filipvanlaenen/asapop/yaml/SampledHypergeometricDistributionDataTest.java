package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SampledHypergeometricDistributionData</code> class.
 */
public class SampledHypergeometricDistributionDataTest {
    /**
     * Verifies that the getter method <code>getProbabilityMassFunction</code> is wired correctly to the setter method
     * <code>setProbabilityMassFunction</code>.
     */
    @Test
    public void getProbabilityMassFunctionShouldBeWiredCorrectlyToSetProbabilityMassFunction() {
        SampledHypergeometricDistributionData sampledHypergeometricDistributionData =
                new SampledHypergeometricDistributionData();
        Set<RangeProbabilityMass> probabilityMassFunction = Set.of(new RangeProbabilityMass());
        sampledHypergeometricDistributionData.setProbabilityMassFunction(probabilityMassFunction);
        assertEquals(probabilityMassFunction, sampledHypergeometricDistributionData.getProbabilityMassFunction());
    }
}
