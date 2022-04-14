package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectoralList;

/**
 * Class representing the results of the statistical analysis of an opinion poll about the first round of a presidential
 * election.
 */
public class FirstRoundWinnersAnalysis {
    /**
     * A map containing the probability mass of winning the first round per set of electoral lists.
     */
    private final Map<Set<ElectoralList>, Double> probabilityMassFunction = new HashMap<Set<ElectoralList>, Double>();

    /**
     * Constructor using a vote share analysis in combination with a sample multivariate hypergeometric distribution as
     * its parameters.
     *
     * @param voteShareAnalysis                             The vote share analysis for the opinion poll.
     * @param sampledMultivariateHypergeometricDistribution The sampled multivariate hypergeometric distribution for the
     *                                                      opinion poll.
     */
    FirstRoundWinnersAnalysis(final VoteSharesAnalysis voteShareAnalysis,
            final SampledMultivariateHypergeometricDistribution sampledMultivariateHypergeometricDistribution) {
        ElectoralList[] electoralLists = voteShareAnalysis.getElectoralLists().toArray(new ElectoralList[0]);
        for (ElectoralList electoralList : electoralLists) {
            SampledHypergeometricDistribution pmf = voteShareAnalysis.getProbabilityMassFunction(electoralList);
            double probabilityMass = sampledMultivariateHypergeometricDistribution.getProbabilityMass(pmf);
            if (probabilityMass > 0) {
                probabilityMassFunction.put(Set.of(electoralList), probabilityMass);
            }
        }
        for (int i = 0; i <= electoralLists.length - 2; i++) {
            SampledHypergeometricDistribution pmf1 = voteShareAnalysis.getProbabilityMassFunction(electoralLists[i]);
            for (int j = i + 1; j < electoralLists.length; j++) {
                SampledHypergeometricDistribution pmf2 =
                        voteShareAnalysis.getProbabilityMassFunction(electoralLists[j]);
                double probabilityMass = sampledMultivariateHypergeometricDistribution.getProbabilityMass(pmf1, pmf2);
                if (probabilityMass > 0) {
                    probabilityMassFunction.put(Set.of(electoralLists[i], electoralLists[j]), probabilityMass);
                }
            }
            double probabilityMass = sampledMultivariateHypergeometricDistribution.getProbabilityMass(pmf1, null);
            if (probabilityMass > 0) {
                Set<ElectoralList> key = new HashSet<ElectoralList>();
                key.add(electoralLists[i]);
                key.add(null);
                probabilityMassFunction.put(key, probabilityMass);
            }
        }
    }

    /**
     * Returns a set with the sets of electoral lists that have a non-zero probability of winning the first round.
     *
     * @return A set with the sets of electoral lists that have a non-zero probability of winning the first round.
     */
    public Set<Set<ElectoralList>> getElectoralListSets() {
        return probabilityMassFunction.keySet();
    }

    /**
     * Returns the probability mass for a set of electoral lists to win the first round.
     *
     * @param electoralListSet A set of electoral lists.
     * @return The probability mass to win the first round.
     */
    public Double getProbabilityMass(final Set<ElectoralList> electoralListSet) {
        if (probabilityMassFunction.containsKey(electoralListSet)) {
            return probabilityMassFunction.get(electoralListSet);
        } else {
            return 0D;
        }
    }
}
