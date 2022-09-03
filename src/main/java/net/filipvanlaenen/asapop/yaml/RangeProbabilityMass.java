package net.filipvanlaenen.asapop.yaml;

import java.math.BigDecimal;

/**
 * Class representing the probability mass for a range for the YAML file containing a sampled hypergeometric
 * distribution.
 */
public class RangeProbabilityMass {
    /**
     * The lower bound of the range.
     */
    private Long lowerBound;
    /**
     * The upper bound of the range.
     */
    private Long upperBound;
    /**
     * The probability mass.
     */
    private BigDecimal probabilityMass;

    /**
     * Returns the lower bound of the range.
     *
     * @return The lower bound of the range.
     */
    public Long getLowerBound() {
        return lowerBound;
    }

    /**
     * Returns the probability mass.
     *
     * @return The probability mass.
     */
    public BigDecimal getProbabilityMass() {
        return probabilityMass;
    }

    /**
     * Returns the upper bound of the range.
     *
     * @return The upper bound of the range.
     */
    public Long getUpperBound() {
        return upperBound;
    }

    /**
     * Sets the lower bound of the range.
     *
     * @param lowerBound The lower bound of the range.
     */
    public void setLowerBound(final Long lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * Sets the probability mass.
     *
     * @param probabilityMass The probability mass.
     */
    public void setProbabilityMass(final BigDecimal probabilityMass) {
        this.probabilityMass = probabilityMass;
    }

    /**
     * Sets the upper bound of the range.
     *
     * @param upperBound The upper bound of the range.
     */
    public void setUpperBound(final Long upperBound) {
        this.upperBound = upperBound;
    }
}
