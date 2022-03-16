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
     * A map containing a probability mass function per electoral list.
     */
    private final Map<ElectoralList, SampledHypergeometricDistribution> probabilityMassFunctions = new HashMap<ElectoralList, SampledHypergeometricDistribution>();

    /**
     * Adds an electoral list with its probability mass function.
     *
     * @param electoralList           The electoral list for which to add a probability mass function.
     * @param probabilityMassFunction The probability mass function for the electoral list.
     */
    void add(final ElectoralList electoralList, final SampledHypergeometricDistribution probabilityMassFunction) {
        probabilityMassFunctions.put(electoralList, probabilityMassFunction);
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

    Set<ElectoralList> getElectoralLists() {
        return probabilityMassFunctions.keySet();
    }

    /**
     * Returns the probability mass function for an electoral list.
     *
     * @param electoralList The electoral list for which to return the probability mass function.
     * @return The probability mass function for the electoral list.
     */
    public SampledHypergeometricDistribution getProbabilityMassFunction(final ElectoralList electoralList) {
        return probabilityMassFunctions.get(electoralList);
    }

    List<SampledHypergeometricDistribution> getProbabilityMassFunctions() {
        return new ArrayList<SampledHypergeometricDistribution>(probabilityMassFunctions.values());
    }

    @Override
    public int hashCode() {
        return Objects.hash(probabilityMassFunctions);
    }
}
