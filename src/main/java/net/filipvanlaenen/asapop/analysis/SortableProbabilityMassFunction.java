package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a probability mass function with sortable keys. When the keys are sortable, medians and confidence
 * intervals can be calculated.
 *
 * @param <SK> The class for the sortable keys.
 */
abstract class SortableProbabilityMassFunction<SK> extends ProbabilityMassFunction<SK> {
    /**
     * Calculates the confidence interval for a confidence level.
     *
     * @param level The confidence level.
     * @return A pair of keys representing the lower and the upper bound of the confidence level.
     */
    ConfidenceInterval<SK> getConfidenceInterval(final double level) {
        BigDecimal fraction = getProbabilityMassSum().multiply(new BigDecimal(1D).subtract(new BigDecimal(level))).divide(new BigDecimal(2));
        List<SK> sortedKeys = getSortedKeys();
        SK lowerBound = findQuantileBoundary(fraction, sortedKeys);
        List<SK> reverseSortedKeys = new ArrayList<SK>(sortedKeys);
        Collections.reverse(reverseSortedKeys);
        SK upperBound = findQuantileBoundary(fraction, reverseSortedKeys);
        return new ConfidenceInterval<SK>(lowerBound, upperBound);
    }

    private SK findQuantileBoundary(BigDecimal fraction, List<SK> sortedKeys) {
        BigDecimal accumulatedProbabilityMass = BigDecimal.ZERO;
        SK previousKey = sortedKeys.get(0);
        for (SK s : sortedKeys) {
            accumulatedProbabilityMass = accumulatedProbabilityMass.add(getProbabilityMass(s), MathContext.DECIMAL128);
            if (accumulatedProbabilityMass.compareTo(fraction) > 0) {
                return previousKey;
            }
            previousKey = s;
        }
        return previousKey;
    }

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
