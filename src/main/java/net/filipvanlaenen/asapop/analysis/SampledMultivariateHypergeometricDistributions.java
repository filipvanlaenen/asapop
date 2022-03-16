package net.filipvanlaenen.asapop.analysis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SampledMultivariateHypergeometricDistributions {
    private static final Map<List<SampledHypergeometricDistribution>, SampledMultivariateHypergeometricDistribution> CACHE = new HashMap<List<SampledHypergeometricDistribution>, SampledMultivariateHypergeometricDistribution>();

    static SampledMultivariateHypergeometricDistribution get(
            List<SampledHypergeometricDistribution> probabilityMassFunctions) {
        List<SampledHypergeometricDistribution> key = new ArrayList<SampledHypergeometricDistribution>(
                probabilityMassFunctions);
        key.sort(new Comparator<SampledHypergeometricDistribution>() {
            @Override
            public int compare(SampledHypergeometricDistribution spmf0, SampledHypergeometricDistribution spmf1) {
                return spmf0.getMedian().compareTo(spmf1.getMedian());
            }
        });
        if (!CACHE.containsKey(key)) {
            CACHE.put(key, new SampledMultivariateHypergeometricDistribution(probabilityMassFunctions));
        }
        return CACHE.get(key);
    }
}
