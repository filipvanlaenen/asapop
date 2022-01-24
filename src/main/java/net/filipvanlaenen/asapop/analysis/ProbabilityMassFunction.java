package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;

/**
 * Class representing a probability mass function.
 *
 * @param <K> The type for the keys of the probability mass function.
 */
abstract class ProbabilityMassFunction<K> {
    abstract BigDecimal getProbabilityMass(K k);
}
