package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class implementing a range. A range has a lower and an upper bound.
 */
public final class Range implements Comparable<Range> {
    /**
     * A map caching all the ranges created in the <code>get</code> method.
     */
    private static final Map<List<Long>, Range> CACHE = new HashMap<List<Long>, Range>();
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
    private Range(final long lowerBound, final long upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public int compareTo(final Range other) {
        return lowerBound - other.lowerBound < 0 ? -1 : lowerBound == other.lowerBound ? 0 : 1;
    }

    /**
     * Returns a range with the specified lower and upper bound. If the range is already present in the cache, it is
     * returned from there.
     *
     * @param lowerBound The lower bound of the requested range.
     * @param upperBound The upper bound of the requested range.
     * @return A range with the provided lower and upper bound.
     */
    static Range get(final long lowerBound, final long upperBound) {
        List<Long> key = List.of(lowerBound, upperBound);
        if (!CACHE.containsKey(key)) {
            CACHE.put(key, new Range(lowerBound, upperBound));
        }
        return CACHE.get(key);
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

    /**
     * Returns the lower bound of the range.
     *
     * @return The lower bound of the range.
     */
    public long getLowerBound() {
        return lowerBound;
    }

    /**
     * Returns the upper bound of the range.
     *
     * @return The upper bound of the range.
     */
    public long getUpperBound() {
        return upperBound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBound, upperBound);
    }
}
