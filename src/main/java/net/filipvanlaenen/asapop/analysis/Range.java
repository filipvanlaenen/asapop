package net.filipvanlaenen.asapop.analysis;

/**
 * Class implementing a range. A range has a lower and an upper bound.
 *
 * @param lowerBound The lower bound of the range.
 * @param upperBound The upper bound of the range.
 */
public record Range(long lowerBound, long upperBound) implements Comparable<Range> {
    @Override
    public int compareTo(final Range other) {
        return lowerBound - other.lowerBound < 0 ? -1 : lowerBound == other.lowerBound ? 0 : 1;
    }

    /**
     * Returns the midpoint of the range.
     *
     * @return The midpoint of the range.
     */
    public long getMidpoint() {
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
}
