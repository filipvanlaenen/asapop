package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

/**
 * Class representing a probability mass function with sortable keys. When the keys are sortable, medians and confidence
 * intervals can be calculated.
 *
 * @param <SK> The class for the sortable keys.
 */
abstract class SortableProbabilityMassFunction<SK> extends ProbabilityMassFunction<SK> {
    /**
     * Calculates the median.
     *
     * @return The median.
     */
    SK getMedian() {
        BigDecimal halfProbabilityMassSum = getProbabilityMassSum().divide(new BigDecimal(2), MathContext.DECIMAL128);
        BigDecimal accumulatedProbabilityMass = BigDecimal.ZERO;
        for (SK s : getSortedKeys()) {
            accumulatedProbabilityMass = accumulatedProbabilityMass.add(getProbabilityMass(s), MathContext.DECIMAL128);
            if (accumulatedProbabilityMass.compareTo(halfProbabilityMassSum) >= 0) {
                return s;
            }
        }
        return null;
    }

    /**
     * Returns the sum of the probability masses.
     *
     * @return The sum of the probability masses.
     */
    abstract BigDecimal getProbabilityMassSum();

    /**
     * Returns a sorted list with the keys.
     *
     * @return A sorted list with the keys.
     */
    abstract List<SK> getSortedKeys();
}
