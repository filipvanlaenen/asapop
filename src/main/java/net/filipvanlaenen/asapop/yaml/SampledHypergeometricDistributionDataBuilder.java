package net.filipvanlaenen.asapop.yaml;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.analysis.Range;
import net.filipvanlaenen.asapop.analysis.SampledHypergeometricDistribution;

/**
 * Builder class to build an <code>SampledHypergeometricDistributionData</code> instance.
 */
public class SampledHypergeometricDistributionDataBuilder {
    /**
     * Builds a <code>SampledHypergeometricDistribution</code> instance from a
     * <code>SampledHypergeometricDistributionData</code> instance.
     *
     * @param data A <code>SampledHypergeometricDistributionData</code> instance.
     * @return The resulting <code>SampledHypergeometricDistribution</code> instance.
     */
    public SampledHypergeometricDistribution fromData(final SampledHypergeometricDistributionData data) {
        Map<Range, BigDecimal> pmf = new HashMap<Range, BigDecimal>();
        for (RangeProbabilityMass rpm : data.getProbabilityMassFunction()) {
            Range range = Range.get(rpm.getLowerBound(), rpm.getUpperBound());
            pmf.put(range, rpm.getProbabilityMass());
        }
        return new SampledHypergeometricDistribution(pmf);
    }

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
            rangeProbabilityMass.setLowerBound(range.lowerBound());
            rangeProbabilityMass.setUpperBound(range.upperBound());
            rangeProbabilityMass.setProbabilityMass(pmf.getProbabilityMass(range));
            rangeProbabilityMasses.add(rangeProbabilityMass);
        }
        data.setProbabilityMassFunction(rangeProbabilityMasses);
        return data;
    }
}
