package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;

/**
 * Class representing a probability mass function.
 *
 * @param <K> The type for the keys of the probability mass function.
 */
abstract class ProbabilityMassFunction<K> {
    /**
     * Returns the probability mass for a key.
     *
     * @param key The key for which to return the probability mass.
     * @return The probability mass for a key.
     */
    abstract BigDecimal getProbabilityMass(K key);
}
