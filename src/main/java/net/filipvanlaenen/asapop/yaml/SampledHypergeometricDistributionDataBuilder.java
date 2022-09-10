package net.filipvanlaenen.asapop.yaml;

import java.util.HashSet;
import java.util.Set;

import net.filipvanlaenen.asapop.analysis.Range;
import net.filipvanlaenen.asapop.analysis.SampledHypergeometricDistribution;

/**
 * Builder class to build an <code>SampledHypergeometricDistributionData</code> instance.
 */
public class SampledHypergeometricDistributionDataBuilder {
    /**
     * Builds a <code>SampledHypergeometricDistributionData</code> instance from a
     * <code>SampledHypergeometricDistribution</code> instance.
     *
     * @param pmf A <code>SampledHypergeometricDistribution</code> instance.
     * @return The resulting <code>SampledHypergeometricDistributionData</code> instance.
     */
    public SampledHypergeometricDistributionData toData(final SampledHypergeometricDistribution pmf) {
        SampledHypergeometricDistributionData data = new SampledHypergeometricDistributionData();
        Set<RangeProbabilityMass> rangeProbabilityMasses = new HashSet<RangeProbabilityMass>();
        for (Range range : pmf.getKeys()) {
            RangeProbabilityMass rangeProbabilityMass = new RangeProbabilityMass();
            rangeProbabilityMass.setLowerBound(range.getLowerBound());
            rangeProbabilityMass.setUpperBound(range.getUpperBound());
            rangeProbabilityMass.setProbabilityMass(pmf.getProbabilityMass(range));
            rangeProbabilityMasses.add(rangeProbabilityMass);
        }
        data.setProbabilityMassFunction(rangeProbabilityMasses);
        return data;
    }
}
