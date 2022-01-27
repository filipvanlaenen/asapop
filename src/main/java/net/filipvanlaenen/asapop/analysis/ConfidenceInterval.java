package net.filipvanlaenen.asapop.analysis;

import java.util.Objects;

class ConfidenceInterval<SK> {
    private final SK lowerBound;
    private final SK upperBound;

    ConfidenceInterval(SK lowerBound, SK upperBound) {
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
