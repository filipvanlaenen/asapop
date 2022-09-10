package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.analysis.SampledHypergeometricDistribution;
import net.filipvanlaenen.asapop.analysis.SampledHypergeometricDistributions;

/**
 * Unit tests on the <code>SampledHypergeometricDistributionDataBuilder</code> class.
 */
public class SampledHypergeometricDistributionDataBuilderTest {
    /**
     * The magic number three.
     */
    private static final long THREE = 3L;

    /**
     * Creates a <code>SampledHypergeometricDistributionData</code> instance to run the tests on.
     *
     * @return A <code>SampledHypergeometricDistributionData</code> instance to run the tests on.
     */
    private SampledHypergeometricDistributionData createDataInstance() {
        SampledHypergeometricDistribution shd = SampledHypergeometricDistributions.get(0L, 1L, 2L, THREE);
        return new SampledHypergeometricDistributionDataBuilder().toData(shd);
    }

    /**
     * Verifies that the builder sets the lower bounds correctly.
     */
    @Test
    public void shouldSetTheLowerBoundsCorrectly() {
        SampledHypergeometricDistributionData shdd = createDataInstance();
        Set<Long> actual =
                shdd.getProbabilityMassFunction().stream().map(rpm -> rpm.getLowerBound()).collect(Collectors.toSet());
        assertEquals(Set.of(0L, 2L), actual);
    }

    /**
     * Verifies that the builder sets the upper bounds correctly.
     */
    @Test
    public void shouldSetTheUpperBoundsCorrectly() {
        SampledHypergeometricDistributionData shdd = createDataInstance();
        Set<Long> actual =
                shdd.getProbabilityMassFunction().stream().map(rpm -> rpm.getUpperBound()).collect(Collectors.toSet());
        assertEquals(Set.of(1L, THREE), actual);
    }

    /**
     * Verifies that the builder sets the probability masses correctly.
     */
    @Test
    public void shouldSetTheProbabilityMassesCorrectly() {
        SampledHypergeometricDistributionData shdd = createDataInstance();
        Set<BigDecimal> actual = shdd.getProbabilityMassFunction().stream().map(rpm -> rpm.getProbabilityMass())
                .collect(Collectors.toSet());
        assertEquals(Set.of(BigDecimal.ONE, BigDecimal.valueOf(THREE)), actual);
    }
}
