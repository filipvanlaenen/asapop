package net.filipvanlaenen.asapop.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    private final Map<Set<Set<ElectoralList>>, Double> probabilityMassFunction =
            new HashMap<Set<Set<ElectoralList>>, Double>();

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
        List<Set<ElectoralList>> electoralListSets =
                new ArrayList<Set<ElectoralList>>(voteShareAnalysis.getElectoralListSets());
        for (Set<ElectoralList> electoralListSet : electoralListSets) {
            SampledHypergeometricDistribution pmf = voteShareAnalysis.getProbabilityMassFunction(electoralListSet);
            double probabilityMass = sampledMultivariateHypergeometricDistribution.getProbabilityMass(pmf);
            if (probabilityMass > 0) {
                probabilityMassFunction.put(Set.of(electoralListSet), probabilityMass);
            }
        }
        for (int i = 0; i <= electoralListSets.size() - 2; i++) {
            SampledHypergeometricDistribution pmf1 =
                    voteShareAnalysis.getProbabilityMassFunction(electoralListSets.get(i));
            for (int j = i + 1; j < electoralListSets.size(); j++) {
                SampledHypergeometricDistribution pmf2 =
                        voteShareAnalysis.getProbabilityMassFunction(electoralListSets.get(j));
                double probabilityMass = sampledMultivariateHypergeometricDistribution.getProbabilityMass(pmf1, pmf2);
                if (probabilityMass > 0) {
                    probabilityMassFunction.put(Set.of(electoralListSets.get(i), electoralListSets.get(j)),
                            probabilityMass);
                }
            }
            double probabilityMass = sampledMultivariateHypergeometricDistribution.getProbabilityMass(pmf1, null);
            if (probabilityMass > 0) {
                Set<Set<ElectoralList>> key = new HashSet<Set<ElectoralList>>();
                key.add(electoralListSets.get(i));
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
    public Set<Set<Set<ElectoralList>>> getElectoralListSetSets() {
        return probabilityMassFunction.keySet();
    }

    /**
     * Returns the probability mass for a set of sets of electoral lists to win the first round.
     *
     * @param electoralListSetSet A set of sets of electoral lists.
     * @return The probability mass to win the first round.
     */
    public Double getProbabilityMass(final Set<Set<ElectoralList>> electoralListSetSet) {
        if (probabilityMassFunction.containsKey(electoralListSetSet)) {
            return probabilityMassFunction.get(electoralListSetSet);
        } else {
            return 0D;
        }
    }
}
