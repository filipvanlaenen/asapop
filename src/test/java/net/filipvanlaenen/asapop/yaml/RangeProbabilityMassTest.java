package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>RangeProbabilityMass</code> class.
 */
public class RangeProbabilityMassTest {
    /**
     * Verifies that the getter method <code>getLowerBound</code> is wired correctly to the setter method
     * <code>setLowerBound</code>.
     */
    @Test
    public void getLowerBoundShouldBeWiredCorrectlyToSetLowerBound() {
        RangeProbabilityMass rangeProbabilityMass = new RangeProbabilityMass();
        rangeProbabilityMass.setLowerBound(1L);
        assertEquals(1L, rangeProbabilityMass.getLowerBound());
    }

    /**
     * Verifies that the getter method <code>getUpperBound</code> is wired correctly to the setter method
     * <code>setUpperBound</code>.
     */
    @Test
    public void getUpperBoundShouldBeWiredCorrectlyToSetUpperBound() {
        RangeProbabilityMass rangeProbabilityMass = new RangeProbabilityMass();
        rangeProbabilityMass.setUpperBound(1L);
        assertEquals(1L, rangeProbabilityMass.getUpperBound());
    }

    /**
     * Verifies that the getter method <code>getProbabilityMass</code> is wired correctly to the setter method
     * <code>setProbabilityMass</code>.
     */
    @Test
    public void getProbabilityMassShouldBeWiredCorrectlyToSetProbabilityMass() {
        RangeProbabilityMass rangeProbabilityMass = new RangeProbabilityMass();
        rangeProbabilityMass.setProbabilityMass(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, rangeProbabilityMass.getProbabilityMass());
    }
}
