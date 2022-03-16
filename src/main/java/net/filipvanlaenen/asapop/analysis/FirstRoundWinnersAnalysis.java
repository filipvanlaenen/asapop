package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectoralList;

public class FirstRoundWinnersAnalysis {
    private final Map<Set<ElectoralList>, Double> probabilityMassFunction = new HashMap<Set<ElectoralList>, Double>();

    FirstRoundWinnersAnalysis(VoteSharesAnalysis voteShareAnalysis,
            SampledMultivariateHypergeometricDistribution sampledMultivariateHypergeometricDistribution) {
        ElectoralList[] electoralLists = voteShareAnalysis.getElectoralLists().toArray(new ElectoralList[0]);
        for (ElectoralList electoralList : electoralLists) {
            SampledHypergeometricDistribution pmf = voteShareAnalysis.getProbabilityMassFunction(electoralList);
            double probabilityMass = sampledMultivariateHypergeometricDistribution.getProbabilityMass(pmf);
            if (probabilityMass > 0) {
                probabilityMassFunction.put(Set.of(electoralList), probabilityMass);
            }
        }
        for (int i = 0; i < electoralLists.length - 1; i++) {
            SampledHypergeometricDistribution pmf1 = voteShareAnalysis.getProbabilityMassFunction(electoralLists[i]);
            for (int j = i + 1; j < electoralLists.length; j++) {
                SampledHypergeometricDistribution pmf2 = voteShareAnalysis
                        .getProbabilityMassFunction(electoralLists[j]);
                double probabilityMass = sampledMultivariateHypergeometricDistribution.getProbabilityMass(pmf1, pmf2);
                if (probabilityMass > 0) {
                    probabilityMassFunction.put(Set.of(electoralLists[i], electoralLists[j]), probabilityMass);
                }
            }
        }
    }

    public Double getProbabilityMass(Set<ElectoralList> electoralListSet) {
        if (probabilityMassFunction.containsKey(electoralListSet)) {
            return probabilityMassFunction.get(electoralListSet);
        } else {
            return 0D;
        }
    }

    public Set<Set<ElectoralList>> getElectoralListSets() {
        return probabilityMassFunction.keySet();
    }
}
