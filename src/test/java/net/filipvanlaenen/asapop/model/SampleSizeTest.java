package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import net.filipvanlaenen.asapop.model.SampleSize.ExactSampleSize;
import net.filipvanlaenen.asapop.model.SampleSize.MinimalSampleSize;
import net.filipvanlaenen.asapop.model.SampleSize.SampleSizeRange;

/**
 * Unit tests on the class <code>SampleSize</code>.
 */
public class SampleSizeTest {
    /**
     * The magic number 1000.
     */
    private static final int ONE_THOUSAND = 1000;
    /**
     * An exact sample size to run the unit tests on.
     */
    private static final ExactSampleSize EXACT_SAMPLE_SIZE = new ExactSampleSize(ONE_THOUSAND);
    /**
     * A minimal sample size to run the unit tests on.
     */
    private static final MinimalSampleSize MINIMAL_SAMPLE_SIZE = new MinimalSampleSize(ONE_THOUSAND);
    /**
     * A sample size range to run the unit tests on.
     */
    private static final SampleSizeRange SAMPLE_SIZE_RANGE = new SampleSizeRange(ONE_THOUSAND, 1200);

    /**
     * Verifies that an exact sample size can be parsed.
     */
    @Test
    public void shouldParseAnIntegerIntoAnExactSampleSize() {
        assertEquals(EXACT_SAMPLE_SIZE, SampleSize.parse("1000"));
    }

    /**
     * Verifies that the minimal value of an exact sample size is equal to its value.
     */
    @Test
    public void minimalValueOfExactSampleSizeShouldBeItsValues() {
        assertEquals(ONE_THOUSAND, EXACT_SAMPLE_SIZE.getMinimalValue());
    }

    /**
     * Verifies that a minimal sample size can be parsed.
     */
    @Test
    public void shouldParseAnGreaterThanOrEqualToIntegerIntoAMinimalSampleSize() {
        assertEquals(MINIMAL_SAMPLE_SIZE, SampleSize.parse("≥1000"));
    }

    /**
     * Verifies that the minimal value of a minimal sample size is equal to its value.
     */
    @Test
    public void minimalValueOfMinimalSampleSizeShouldBeItsValues() {
        assertEquals(ONE_THOUSAND, MINIMAL_SAMPLE_SIZE.getMinimalValue());
    }

    /**
     * Verifies that a sample size range can be parsed.
     */
    @Test
    public void shouldParseARangeIntoASampleSizeRange() {
        assertEquals(SAMPLE_SIZE_RANGE, SampleSize.parse("1000–1200"));
    }

    /**
     * Verifies that the minimal value of a sample size range is equal to its lower bound.
     */
    @Test
    public void minimalValueOfSampleSizeRangeShouldBeItsLowerBound() {
        assertEquals(ONE_THOUSAND, SAMPLE_SIZE_RANGE.getMinimalValue());
    }
}
