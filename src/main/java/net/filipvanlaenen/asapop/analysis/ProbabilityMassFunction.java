package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class ProbabilityMassFunction {
    private final Map<Integer, BigDecimal> pmf = new HashMap<Integer, BigDecimal>();

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

    void add(int key, BigDecimal value) {
        if (pmf.containsKey(key)) {
            pmf.put(key, pmf.get(key).add(value, MathContext.DECIMAL128));
        } else {
            pmf.put(key, value);
        }
    }
}
