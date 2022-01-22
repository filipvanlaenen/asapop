package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class ProbabilityMassFunction {
    private final Map<Integer, Integer> pmf = new HashMap<Integer, Integer>();

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ProbabilityMassFunction) {
            ProbabilityMassFunction other = (ProbabilityMassFunction) obj;
            return other.pmf.equals(pmf);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(pmf);
    }

    void add(int i, int j) {
        pmf.put(i, j);
    }
}
