package net.filipvanlaenen.asapop.analysis;

import java.util.Objects;

/**
 * Class representing a confidence interval with a lower and an upper bound.
 *
 * @param <SK> The class for the sortable keys.
 */
class ConfidenceInterval<SK> {
    /**
     * The lower bound of the confidence interval.
     */
    private final SK lowerBound;
    /**
     * The upper bound of the confidence interval.
     */
    private final SK upperBound;

    /**
     * Constructor taking the lower and the upper bound of the confidence interval.
     *
     * @param lowerBound The lower bound of the confidence interval.
     * @param upperBound The upper bound of the confidence interval.
     */
    ConfidenceInterval(final SK lowerBound, final SK upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ConfidenceInterval) {
            ConfidenceInterval<SK> other = (ConfidenceInterval<SK>) obj;
            return other.lowerBound.equals(lowerBound) && other.upperBound.equals(upperBound);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBound, upperBound);
    }
}
