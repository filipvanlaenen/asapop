package net.filipvanlaenen.asapop.analysis;

public class ProbabilityMassFunction {
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ProbabilityMassFunction) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
