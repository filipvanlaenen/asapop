package net.filipvanlaenen.asapop.analysis;

import java.util.Objects;

class Range implements Comparable<Range> {
    private final long lowerBound;
    private final long upperBound;

    Range(final long lowerBound, final long upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public int compareTo(Range other) {
        return lowerBound - other.lowerBound < 0 ? -1 : (lowerBound == other.lowerBound ? 0 : 1);
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

    long getMidpoint() {
        return lowerBound + (upperBound - lowerBound) / 2;
    }

    long getWidth() {
        return 1L + upperBound - lowerBound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBound, upperBound);
    }
}