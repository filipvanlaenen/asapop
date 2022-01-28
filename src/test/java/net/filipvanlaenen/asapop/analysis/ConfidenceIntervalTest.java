package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ConfidenceInterval</code> class.
 */
public class ConfidenceIntervalTest {
    /**
     * A confidence interval to run tests on.
     */
    private static final ConfidenceInterval<Integer> CONFIDENCE_INTERVAL = new ConfidenceInterval<Integer>(0, 1);

    /**
     * Verifies that a confidence interval is not equal to null.
     */
    @Test
    public void aConfidenceIntervalShouldNotBeEqualToNull() {
        assertFalse(CONFIDENCE_INTERVAL.equals(null));
    }

    /**
     * Verifies that a confidence interval is not equal to an object of another class, like a string.
     */
    @Test
    public void aConfidenceIntervalShouldNotBeEqualToAString() {
        assertFalse(CONFIDENCE_INTERVAL.equals(""));
    }

    /**
     * Verifies that a confidence interval is equal to itself.
     */
    @Test
    public void aConfidenceIntervalShouldBeEqualToItself() {
        assertTrue(CONFIDENCE_INTERVAL.equals(CONFIDENCE_INTERVAL));
    }

    /**
     * Verifies that calling hashCode twice on a confidence interval returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnAConfidenceIntervalReturnsTheSameResult() {
        assertEquals(CONFIDENCE_INTERVAL.hashCode(), CONFIDENCE_INTERVAL.hashCode());
    }

    /**
     * Verifies that two confidence intervals constructed with the same parameter are equal.
     */
    @Test
    public void twoConfidenceIntervalsConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(CONFIDENCE_INTERVAL, new ConfidenceInterval<Integer>(0, 1));
    }

    /**
     * Verifies that two confidence intervals constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoConfidenceIntervalsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(CONFIDENCE_INTERVAL.hashCode(), new ConfidenceInterval<Integer>(0, 1).hashCode());
    }

    /**
     * Verifies that two confidence intervals with different lower bounds are not equal.
     */
    @Test
    public void twoConfidenceIntervalsWithDifferentLowerBoundsShouldNotBeEqual() {
        assertFalse(CONFIDENCE_INTERVAL.equals(new ConfidenceInterval<Integer>(1, 1)));
    }

    /**
     * Verifies that two confidence intervals with different lower bounds have different hash codes.
     */
    @Test
    public void twoConfidenceIntervalsWithDifferentLowerBoundsShouldHaveDifferentHashCodes() {
        assertFalse(CONFIDENCE_INTERVAL.hashCode() == new ConfidenceInterval<Integer>(1, 1).hashCode());
    }

    /**
     * Verifies that two confidence intervals with different upper bounds are not equal.
     */
    @Test
    public void twoConfidenceIntervalsWithDifferentUpperBoundsShouldNotBeEqual() {
        assertFalse(CONFIDENCE_INTERVAL.equals(new ConfidenceInterval<Integer>(0, 2)));
    }

    /**
     * Verifies that two confidence intervals with different upper bounds have different hash codes.
     */
    @Test
    public void twoConfidenceIntervalsWithDifferentUpperBoundsShouldHaveDifferentHashCodes() {
        assertFalse(CONFIDENCE_INTERVAL.hashCode() == new ConfidenceInterval<Integer>(0, 2).hashCode());
    }
}
