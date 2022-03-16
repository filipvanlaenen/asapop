package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class SampledMultivariateHypergeometricDistribution {
    private static Map<SampledHypergeometricDistribution, Integer> probabilityMassFunctionCardinalities = new HashMap<SampledHypergeometricDistribution, Integer>();
    private static Map<SampledHypergeometricDistribution, BigDecimal> accumulatedSingleWinnerProbabilityMasses = new HashMap<SampledHypergeometricDistribution, BigDecimal>();
    private static Map<SampledHypergeometricDistribution, Double> singleWinnerProbabilityMasses = new HashMap<SampledHypergeometricDistribution, Double>();
    private static Map<Set<SampledHypergeometricDistribution>, BigDecimal> accumulatedPairProbabilityMasses = new HashMap<Set<SampledHypergeometricDistribution>, BigDecimal>();
    private static Map<Set<SampledHypergeometricDistribution>, Double> pairProbabilityMasses = new HashMap<Set<SampledHypergeometricDistribution>, Double>();

    SampledMultivariateHypergeometricDistribution(List<SampledHypergeometricDistribution> probabilityMassFunctions) {
        for (SampledHypergeometricDistribution probabilityMassFunction : probabilityMassFunctions) {
            if (probabilityMassFunctionCardinalities.containsKey(probabilityMassFunction)) {
                probabilityMassFunctionCardinalities.put(probabilityMassFunction,
                        probabilityMassFunctionCardinalities.get(probabilityMassFunction) + 1);
            } else {
                probabilityMassFunctionCardinalities.put(probabilityMassFunction, 1);
            }
        }
        // TODO: Filter the PMFs whose 99.9999% CI touches the largest one
        // TODO: Initialize the map with BigDecimal.ZERO for single winners and pairs
        // TODO: Simulate and accumulate
        // TODO: Translate the BigDecimal registers into probabilities, adjusted using the cardinality
    }

    double getProbabilityMass(SampledHypergeometricDistribution of) {
        if (singleWinnerProbabilityMasses.containsKey(of)) {
            return singleWinnerProbabilityMasses.get(of);
        } else {
            return 0D;
        }
    }

    double getProbabilityMass(SampledHypergeometricDistribution of1, SampledHypergeometricDistribution of2) {
        Set<SampledHypergeometricDistribution> key = of1.equals(of2) ? Set.of(of1) : Set.of(of1, of2);
        if (pairProbabilityMasses.containsKey(key)) {
            return pairProbabilityMasses.get(key);
        } else {
            return 0D;
        }
    }
}
