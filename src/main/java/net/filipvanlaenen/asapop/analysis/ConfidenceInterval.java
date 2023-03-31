package net.filipvanlaenen.asapop.analysis;

/**
 * Record representing a confidence interval with a lower and an upper bound.
 *
 * @param <SK>       The class for the sortable keys.
 * @param lowerBound The lower bound of the confidence interval.
 * @param upperBound The upper bound of the confidence interval.
 */
public record ConfidenceInterval<SK>(SK lowerBound, SK upperBound) {
}
