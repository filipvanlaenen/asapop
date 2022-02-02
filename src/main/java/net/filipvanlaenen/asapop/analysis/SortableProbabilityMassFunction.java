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
public abstract class SortableProbabilityMassFunction<SK> extends ProbabilityMassFunction<SK> {
    /**
     * Calculates the confidence interval for a confidence level.
     *
     * @param level The confidence level.
     * @return A pair of keys representing the lower and the upper bound of the confidence level.
     */
    public ConfidenceInterval<SK> getConfidenceInterval(final double level) {
        BigDecimal fraction = getProbabilityMassSum().multiply(new BigDecimal(1 - level)).divide(new BigDecimal(2));
        List<SK> sortedKeys = getSortedKeys();
        SK lowerBound = findQuantileBoundary(fraction, sortedKeys);
        List<SK> reverseSortedKeys = new ArrayList<SK>(sortedKeys);
        Collections.reverse(reverseSortedKeys);
        SK upperBound = findQuantileBoundary(fraction, reverseSortedKeys);
        return new ConfidenceInterval<SK>(lowerBound, upperBound);
    }

    /**
     * Finds a quantile of a given probability mass fraction.
     *
     * @param probabilityMassFraction The probability mass fraction.
     * @param sortedKeys              The sorted keys of the probability mass function.
     * @return The boundary for the quantile.
     */
    private SK findQuantileBoundary(final BigDecimal probabilityMassFraction, final List<SK> sortedKeys) {
        BigDecimal accumulatedProbabilityMass = BigDecimal.ZERO;
        SK previousKey = sortedKeys.get(0);
        for (SK s : sortedKeys) {
            accumulatedProbabilityMass = accumulatedProbabilityMass.add(
                    getProbabilityMass(s).multiply(getKeyWeight(s), MathContext.DECIMAL128), MathContext.DECIMAL128);
            // Changing the conditional boundary below produces a mutant that is hard to kill due to rounding errors.
            if (accumulatedProbabilityMass.compareTo(probabilityMassFraction) > 0) {
                return previousKey;
            }
            previousKey = s;
        }
        return null;
    }

    /**
     * Calculates the median.
     *
     * @return The median.
     */
    public SK getMedian() {
        BigDecimal halfProbabilityMassSum = getProbabilityMassSum().divide(new BigDecimal(2), MathContext.DECIMAL128);
        BigDecimal accumulatedProbabilityMass = BigDecimal.ZERO;
        for (SK s : getSortedKeys()) {
            accumulatedProbabilityMass = accumulatedProbabilityMass.add(
                    getProbabilityMass(s).multiply(getKeyWeight(s), MathContext.DECIMAL128), MathContext.DECIMAL128);
            if (accumulatedProbabilityMass.compareTo(halfProbabilityMassSum) >= 0) {
                return s;
            }
        }
        return null;
    }

    abstract BigDecimal getKeyWeight(final SK key);

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
