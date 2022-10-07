package net.filipvanlaenen.asapop.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectoralList;

/**
 * Class representing an analysis of the votes shares per electoral list.
 */
public final class VoteSharesAnalysis {
    /**
     * A map containing a probability mass function per set of electoral lists.
     */
    private final Map<Set<ElectoralList>, SampledHypergeometricDistribution> probabilityMassFunctions;

    /**
     * Default constructor.
     */
    public VoteSharesAnalysis() {
        probabilityMassFunctions = new HashMap<Set<ElectoralList>, SampledHypergeometricDistribution>();
    }

    /**
     * Adds a set of electoral lists with its probability mass function.
     *
     * @param electoralListSet        The set of electoral lists for which to add a probability mass function.
     * @param probabilityMassFunction The probability mass function for the set of electoral lists.
     */
    void add(final Set<ElectoralList> electoralListSet,
            final SampledHypergeometricDistribution probabilityMassFunction) {
        probabilityMassFunctions.put(electoralListSet, probabilityMassFunction);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof VoteSharesAnalysis) {
            VoteSharesAnalysis other = (VoteSharesAnalysis) obj;
            return other.probabilityMassFunctions.equals(probabilityMassFunctions);
        } else {
            return false;
        }
    }

    /**
     * Returns the sets of electoral lists.
     *
     * @return The sets of electoral lists.
     */
    Set<Set<ElectoralList>> getElectoralListSets() {
        return probabilityMassFunctions.keySet();
    }

    /**
     * Returns the probability mass function for a set of electoral lists.
     *
     * @param electoralListSet The set of electoral lists for which to return the probability mass function.
     * @return The probability mass function for the set of electoral lists.
     */
    public SampledHypergeometricDistribution getProbabilityMassFunction(final Set<ElectoralList> electoralListSet) {
        return probabilityMassFunctions.get(electoralListSet);
    }

    /**
     * Returns a list with the probability mass functions.
     *
     * A list and not a set is returned because there may be duplicates.
     *
     * @return A list with the probability mass functions.
     */
    List<SampledHypergeometricDistribution> getProbabilityMassFunctions() {
        return new ArrayList<SampledHypergeometricDistribution>(probabilityMassFunctions.values());
    }

    @Override
    public int hashCode() {
        return Objects.hash(probabilityMassFunctions);
    }
}
