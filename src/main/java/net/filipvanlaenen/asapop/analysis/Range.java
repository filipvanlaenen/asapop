package net.filipvanlaenen.asapop.analysis;

import java.util.Objects;

/**
 * Class implementing a range. A range has a lower and an upper bound.
 */
class Range implements Comparable<Range> {
    /**
     * The lower bound of the range.
     */
    private final long lowerBound;
    /**
     * The upper bound of the range.
     */
    private final long upperBound;

    /**
     * Constructor taking the lower and the upper bound of the range as its parameters.
     *
     * @param lowerBound The lower bound of the range.
     * @param upperBound The upper bound of the range.
     */
    Range(final long lowerBound, final long upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public int compareTo(final Range other) {
        return lowerBound - other.lowerBound < 0 ? -1 : lowerBound == other.lowerBound ? 0 : 1;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Range) {
            Range other = (Range) obj;
            return other.lowerBound == lowerBound && other.upperBound == upperBound;
        } else {
            return false;
        }
    }

    /**
     * Returns the midpoint of the range.
     *
     * @return The midpoint of the range.
     */
    long getMidpoint() {
        return lowerBound + (upperBound - lowerBound) / 2;
    }

    /**
     * Gets the length of the range.
     *
     * @return The length of the range.
     */
    long getLength() {
        return 1L + upperBound - lowerBound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBound, upperBound);
    }
}
