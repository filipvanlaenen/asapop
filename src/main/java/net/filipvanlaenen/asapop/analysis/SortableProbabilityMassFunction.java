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
    private final Map<SK, BigDecimal> pmf;
    /**
     * The probability mass sum.
     */
    private BigDecimal probabilityMassSum;
    /**
     * A map with calculated confidence intervals.
     */
    private final Map<Double, ConfidenceInterval<SK>> confidenceIntervals;
    /**
     * A sorted list with the keys.
     */
    private final List<SK> sortedKeys;
    /**
     * A sorted list with the keys, reversed.
     */
    private final List<SK> reverseSortedKeys;
    /**
     * The median.
     */
    private SK median;

    /**
     * Creates a sortable probability mass function based on a map with the key-value pairs.
     *
     * @param pmf A map with the key-value pairs for the probability mass function, using a sortable key.
     */
    protected SortableProbabilityMassFunction(final Map<SK, BigDecimal> pmf) {
        this.pmf = Collections.unmodifiableMap(pmf);
        sortedKeys = new ArrayList<SK>(pmf.keySet());
        Collections.sort(sortedKeys);
        reverseSortedKeys = new ArrayList<SK>(sortedKeys);
        Collections.reverse(reverseSortedKeys);
        confidenceIntervals = new HashMap<Double, ConfidenceInterval<SK>>();
    }

    /**
     * Calculates the confidence interval for a confidence level.
     *
     * @param level The confidence level.
     * @return The confidence interval.
     */
    private ConfidenceInterval<SK> calculateConfidenceInterval(final double level) {
        BigDecimal fraction = getProbabilityMassSum().multiply(new BigDecimal(1 - level)).divide(new BigDecimal(2));
        SK lowerBound = findQuantileBoundary(fraction, sortedKeys);
        SK upperBound = findQuantileBoundary(fraction, reverseSortedKeys);
        return new ConfidenceInterval<SK>(lowerBound, upperBound);
    }

    /**
     * Calculates the median.
     *
     * @return The median.
     */
    private SK calculateMedian() {
        BigDecimal halfProbabilityMassSum = getProbabilityMassSum().divide(new BigDecimal(2), MathContext.DECIMAL128);
        BigDecimal accumulatedProbabilityMass = BigDecimal.ZERO;
        for (SK s : sortedKeys) {
            accumulatedProbabilityMass = accumulatedProbabilityMass.add(
                    getProbabilityMass(s).multiply(getKeyWeight(s), MathContext.DECIMAL128), MathContext.DECIMAL128);
            if (accumulatedProbabilityMass.compareTo(halfProbabilityMassSum) >= 0) {
                return s;
            }
        }
        return null;
    }

    /**
     * Calculates the sum of all probability masses.
     *
     * @return The sum of all probability masses.
     */
    private BigDecimal calculateProbabilityMassSum() {
        BigDecimal result = BigDecimal.ZERO;
        for (SK key : pmf.keySet()) {
            result = result.add(pmf.get(key).multiply(getKeyWeight(key), MathContext.DECIMAL128),
                    MathContext.DECIMAL128);
        }
        return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj The object to compare this object to.
     * @return True if the other object is equal to this object.
     */
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
     * Finds a quantile of a given probability mass fraction.
     *
     * @param probabilityMassFraction The probability mass fraction.
     * @param sortedKeysList          The sorted keys of the probability mass function.
     * @return The boundary for the quantile.
     */
    private SK findQuantileBoundary(final BigDecimal probabilityMassFraction, final List<SK> sortedKeysList) {
        BigDecimal accumulatedProbabilityMass = BigDecimal.ZERO;
        SK previousKey = sortedKeysList.get(0);
        for (SK s : sortedKeysList) {
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
     * Calculates the confidence interval for a confidence level.
     *
     * @param level The confidence level.
     * @return The confidence interval.
     */
    public ConfidenceInterval<SK> getConfidenceInterval(final double level) {
        if (!confidenceIntervals.containsKey(level)) {
            confidenceIntervals.put(level, calculateConfidenceInterval(level));
        }
        return confidenceIntervals.get(level);
    }

    /**
     * Returns the keys belonging to the confidence interval for a confidence level as a sorted list.
     *
     * @param level The confidence level.
     * @return A sorted list with the keys belonging to the confidence interval for a confidence level.
     */
    public List<SK> getConfidenceIntervalKeyList(final double level) {
        ConfidenceInterval<SK> confidenceInterval = getConfidenceInterval(level);
        int fromIndex = sortedKeys.indexOf(confidenceInterval.getLowerBound());
        int toIndex = sortedKeys.indexOf(confidenceInterval.getUpperBound());
        return sortedKeys.subList(fromIndex, toIndex + 1);
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
     * Returns the median.
     *
     * @return The median.
     */
    public SK getMedian() {
        if (median == null) {
            median = calculateMedian();
        }
        return median;
    }

    /**
     * Returns the number of samples.
     *
     * @return The number of samples.
     */
    Long getNumberOfSamples() {
        return (long) pmf.size();
    }

    /**
     * Returns the probability mass for a key.
     *
     * @param key The key for which to return the probability mass.
     * @return The probability mass for the key.
     */
    BigDecimal getProbabilityMass(final SK key) {
        return pmf.get(key);
    }

    /**
     * Returns the sum of the probability masses.
     *
     * @return The sum of the probability masses.
     */
    private BigDecimal getProbabilityMassSum() {
        if (probabilityMassSum == null) {
            probabilityMassSum = calculateProbabilityMassSum();
        }
        return probabilityMassSum;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return A hash code value for the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getClass(), pmf);
    }
}
