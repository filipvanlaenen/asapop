package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

abstract class SortableProbabilityMassFunction<SK> extends ProbabilityMassFunction<SK> {
    SK getMedian() {
        BigDecimal halfProbabilityMassSum = getProbabilityMassSum().divide(new BigDecimal(2), MathContext.DECIMAL128);
        System.out.println("HPMS: " + halfProbabilityMassSum);
        BigDecimal accumulatedProbabilityMass = BigDecimal.ZERO;
        System.out.println("APM: " + accumulatedProbabilityMass);
        for (SK s : getSortedKeys()) {
            System.out.println("s: " + s);
            accumulatedProbabilityMass = accumulatedProbabilityMass.add(getProbabilityMass(s), MathContext.DECIMAL128);
            System.out.println("APM: " + accumulatedProbabilityMass);
            if (accumulatedProbabilityMass.compareTo(halfProbabilityMassSum) >= 0) {
                return s;
            }
        }
        return null;
    }

    abstract BigDecimal getProbabilityMassSum();

    abstract List<SK> getSortedKeys();
}
