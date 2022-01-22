package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.filipvanlaenen.asapop.model.ElectoralList;

class VoteShareAnalysis {
    private final Map<ElectoralList, ProbabilityMassFunction> probabilityMassFunctions = new HashMap<ElectoralList, ProbabilityMassFunction>();

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof VoteShareAnalysis) {
            VoteShareAnalysis other = (VoteShareAnalysis) obj;
            return other.probabilityMassFunctions.equals(probabilityMassFunctions);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(probabilityMassFunctions);
    }

    void add(ElectoralList electoralList, ProbabilityMassFunction probabilityMassFunction) {
        probabilityMassFunctions.put(electoralList, probabilityMassFunction);
    }
}
