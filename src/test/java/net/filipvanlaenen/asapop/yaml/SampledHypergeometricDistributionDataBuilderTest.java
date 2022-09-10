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
     * Verifies that the builder sets the lower bounds of the data instance correctly.
     */
    @Test
    public void shouldSetTheLowerBoundsOfTheDataInstanceCorrectly() {
        SampledHypergeometricDistributionData shdd = createDataInstance();
        Set<Long> actual =
                shdd.getProbabilityMassFunction().stream().map(rpm -> rpm.getLowerBound()).collect(Collectors.toSet());
        assertEquals(Set.of(0L, 2L), actual);
    }

    /**
     * Verifies that the builder sets the upper bounds of the data instance correctly.
     */
    @Test
    public void shouldSetTheUpperBoundsOfTheDataInstanceCorrectly() {
        SampledHypergeometricDistributionData shdd = createDataInstance();
        Set<Long> actual =
                shdd.getProbabilityMassFunction().stream().map(rpm -> rpm.getUpperBound()).collect(Collectors.toSet());
        assertEquals(Set.of(1L, THREE), actual);
    }

    /**
     * Verifies that the builder sets the probability masses of the data instance correctly.
     */
    @Test
    public void shouldSetTheProbabilityMassesOfTheDataInstanceCorrectly() {
        SampledHypergeometricDistributionData shdd = createDataInstance();
        Set<BigDecimal> actual = shdd.getProbabilityMassFunction().stream().map(rpm -> rpm.getProbabilityMass())
                .collect(Collectors.toSet());
        assertEquals(Set.of(BigDecimal.ONE, BigDecimal.valueOf(THREE)), actual);
    }

    /**
     * Verifies that the builder creates a correct sampled hypergeometric distribution from a data instance.
     */
    @Test
    public void shouldConvertADataInstanceCorrectly() {
        SampledHypergeometricDistributionData shdd = new SampledHypergeometricDistributionData();
        RangeProbabilityMass rpm1 = new RangeProbabilityMass();
        rpm1.setLowerBound(0L);
        rpm1.setUpperBound(1L);
        rpm1.setProbabilityMass(BigDecimal.valueOf(THREE));
        RangeProbabilityMass rpm2 = new RangeProbabilityMass();
        rpm2.setLowerBound(2L);
        rpm2.setUpperBound(THREE);
        rpm2.setProbabilityMass(BigDecimal.ONE);
        shdd.setProbabilityMassFunction(Set.of(rpm1, rpm2));
        SampledHypergeometricDistribution shd = new SampledHypergeometricDistributionDataBuilder().fromData(shdd);
        assertEquals(SampledHypergeometricDistributions.get(0L, 1L, 2L, THREE), shd);
    }
}
