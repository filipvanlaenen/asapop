package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a probability mass function with sortable keys. When the keys are sortable, medians and confidence
 * intervals can be calculated.
 *
 * @param <SK> The class for the sortable keys.
 */
public abstract class SortableProbabilityMassFunction<SK extends Comparable<SK>> extends ProbabilityMassFunction<SK> {
    /**
     * A map holding the key value pairs for the probability mass function.
     */
    private final Map<SK, BigDecimal> pmf = new HashMap<SK, BigDecimal>();
    /**
     * The probability mass sum.
     */
    private BigDecimal probabilityMassSum = BigDecimal.ZERO;

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SortableProbabilityMassFunction) {
            SortableProbabilityMassFunction<SK> other = (SortableProbabilityMassFunction<SK>) obj;
            return getClass().equals(other.getClass()) && pmf.equals(other.pmf);
        } else {
            return false;
        }
    }

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
            // EQMU: Changing the conditional boundary below produces a mutant that is practically equivalent because
            // it is hard to kill due to rounding errors.
            if (accumulatedProbabilityMass.compareTo(probabilityMassFraction) > 0) {
                return previousKey;
            }
            previousKey = s;
        }
        return null;
    }

    /**
     * Returns the weight of the key in the calculations of the median, the confidence intervals, the probability mass
     * sum, etc.
     *
     * @param key The key.
     * @return The weight of the key.
     */
    abstract BigDecimal getKeyWeight(SK key);

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

    /**
     * Returns the number of samples.
     *
     * @return The number of samples.
     */
    Long getNumberOfSamples() {
        return (long) pmf.size();
    }

    BigDecimal getProbabilityMass(final SK key) {
        return pmf.get(key);
    }

    /**
     * Returns the sum of the probability masses.
     *
     * @return The sum of the probability masses.
     */
    BigDecimal getProbabilityMassSum() {
        return probabilityMassSum;
    }

    /**
     * Returns a sorted list with the keys.
     *
     * @return A sorted list with the keys.
     */
    List<SK> getSortedKeys() {
        List<SK> list = new ArrayList<SK>(pmf.keySet());
        Collections.sort(list);
        return Collections.unmodifiableList(list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pmf);
    }

    /**
     * Sets a value for a key.
     *
     * @param key   The key.
     * @param value The value.
     */
    protected void put(final SK key, final BigDecimal value) {
        pmf.put(key, value);
        probabilityMassSum = probabilityMassSum.add(value.multiply(getKeyWeight(key), MathContext.DECIMAL128),
                MathContext.DECIMAL128);
    }
}
