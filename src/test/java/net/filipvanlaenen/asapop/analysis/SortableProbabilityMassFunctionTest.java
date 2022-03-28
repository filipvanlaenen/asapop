package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SortableProbabilityMassFunction</code> class.
 */
public class SortableProbabilityMassFunctionTest {
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number five.
     */
    private static final long FIVE = 5L;
    /**
     * The magic number eight.
     */
    private static final long EIGHT = 8L;
    /**
     * The magic number nine.
     */
    private static final long NINE = 9L;
    /**
     * The magic number ten.
     */
    private static final long TEN = 10L;
    /**
     * The magic number twenty.
     */
    private static final long TWENTY = 20L;
    /**
     * The magic number hundred.
     */
    private static final long HUNDRED = 100L;
    /**
     * The magic number thousand.
     */
    private static final long THOUSAND = 1000L;
    /**
     * The magic number 0.79.
     */
    private static final double SEVENTY_NINE_PERCENT = 0.79;
    /**
     * The magic number 0.8.
     */
    private static final double EIGHTY_PERCENT = 0.8;
    /**
     * The magic number 0.81.
     */
    private static final double EIGHTY_ONE_PERCENT = 0.81;
    /**
     * The range from 350 to 359.
     */
    private static final Range RANGE_350_359 = Range.get(350L, 359L);
    /**
     * The range from 640 to 649.
     */
    private static final Range RANGE_640_649 = Range.get(640L, 649L);
    /**
     * Test object to run the tests on.
     */
    private static final SortableProbabilityMassFunction<Integer> TEST_OBJECT = new TestSubclass(
            Map.of(1, BigDecimal.ONE));

    /**
     * Local subclass for <code>SortableProbabilityMassFunction</code> for testing purposes.
     */
    private static final class TestSubclass extends SortableProbabilityMassFunction<Integer> {
        /**
         * Constructor taking a map with the probability mass function using integers as key.
         *
         * @param pmf A map with the probability mass function.
         */
        private TestSubclass(final Map<Integer, BigDecimal> pmf) {
            super(pmf);
        }

        @Override
        BigDecimal getKeyWeight(final Integer key) {
            return null;
        }

        @Override
        BigDecimal getProbabilityMass(final Integer key) {
            return null;
        }
    }

    /**
     * Verifies that the median is calculated correctly for a hypergeometric distribution of 1 out of 4 in a population
     * of 5.
     */
    @Test
    public void medianShouldBe1ForHypergeometricDistribution1OutOf4In5() {
        assertEquals(1L, new HypergeometricDistribution(1L, FOUR, FIVE).getMedian());
    }

    /**
     * Verifies that the median is calculated correctly for a hypergeometric distribution of 2 out of 4 in a population
     * of 5.
     */
    @Test
    public void medianShouldBe2ForHypergeometricDistribution2OutOf4In5() {
        assertEquals(2L, new HypergeometricDistribution(2L, FOUR, FIVE).getMedian());
    }

    /**
     * Verifies that the 81% confidence interval [0, 2] is calculated correctly for a hypergeometric distribution of 0
     * out of 8 in a population of 9.
     */
    @Test
    public void confidenceInterval81ShouldBe0To2ForHypergeometricDistribution0OutOf8In9() {
        HypergeometricDistribution binomialDistribution = new HypergeometricDistribution(0L, EIGHT, NINE);
        assertEquals(new ConfidenceInterval<Long>(0L, 2L),
                binomialDistribution.getConfidenceInterval(EIGHTY_ONE_PERCENT));
    }

    /**
     * Verifies that the 79% confidence interval [0, 1] is calculated correctly for a hypergeometric distribution of 0
     * out of 8 in a population of 9.
     */
    @Test
    public void confidenceInterval79ShouldBe0To1ForHypergeometricDistribution0OutOf8In9() {
        HypergeometricDistribution binomialDistribution = new HypergeometricDistribution(0L, EIGHT, NINE);
        assertEquals(new ConfidenceInterval<Long>(0L, 1L),
                binomialDistribution.getConfidenceInterval(SEVENTY_NINE_PERCENT));
    }

    /**
     * Verifies that the 81% confidence interval key list [0, 1, 2] is calculated correctly for a hypergeometric
     * distribution of 0 out of 8 in a population of 9.
     */
    @Test
    public void confidenceInterval81KeyListShouldBe0To2ForHypergeometricDistribution0OutOf8In9() {
        HypergeometricDistribution binomialDistribution = new HypergeometricDistribution(0L, EIGHT, NINE);
        assertEquals(List.of(0L, 1L, 2L), binomialDistribution.getConfidenceIntervalKeyList(EIGHTY_ONE_PERCENT));
    }

    /**
     * Verifies that the number of samples is returned correctly.
     */
    @Test
    public void numberOfSamplesShouldBeEqualToTheSizeOfTheMap() {
        assertEquals(1L, TEST_OBJECT.getNumberOfSamples());
    }

    /**
     * Verifies that the keys are sorted properly before the confidence intervals are calculated.
     */
    @Test
    public void keysShouldBeSortedToCalculateTheConfidenceIntervals() {
        assertEquals(new ConfidenceInterval<Range>(RANGE_350_359, RANGE_640_649),
                new SampledHypergeometricDistribution(TEN, TWENTY, HUNDRED, THOUSAND)
                        .getConfidenceInterval(EIGHTY_PERCENT));
    }

    /**
     * Verifies that a sortable probability mass function is not equal to null.
     */
    @Test
    public void aSortableProbabilityMassFunctionShouldNotBeEqualToNull() {
        assertFalse(TEST_OBJECT.equals(null));
    }

    /**
     * Verifies that a sortable probability mass function is not equal to an object of another class, like a string.
     */
    @Test
    public void aSortableProbabilityMassFunctionShouldNotBeEqualToAString() {
        assertFalse(TEST_OBJECT.equals(""));
    }

    /**
     * Verifies that a sortable probability mass function is equal to itself.
     */
    @Test
    public void aSortableProbabilityMassFunctionShouldBeEqualToItself() {
        assertTrue(TEST_OBJECT.equals(TEST_OBJECT));
    }

    /**
     * Verifies that calling hashCode twice on a sortable probability mass function returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnASortableProbabilityMassFunctionReturnsTheSameResult() {
        assertEquals(TEST_OBJECT.hashCode(), TEST_OBJECT.hashCode());
    }

    /**
     * Verifies that two sortable probability mass functions constructed with the same parameter are equal.
     */
    @Test
    public void twoSortableProbabilityMassFunctionsConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(TEST_OBJECT, new TestSubclass(Map.of(1, BigDecimal.ONE)));
    }

    /**
     * Verifies that two sortable probability mass functions constructed with the same parameters return the same
     * hashCode.
     */
    @Test
    public void twoSortableProbabilityMassFunctionsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(TEST_OBJECT.hashCode(), new TestSubclass(Map.of(1, BigDecimal.ONE)).hashCode());
    }

    /**
     * Verifies that two different sortable probability mass functions with different values are not equal.
     */
    @Test
    public void twoDifferentSortableProbabilityMassFunctionsWithDifferentValuesShouldNotBeEqual() {
        assertFalse(TEST_OBJECT.equals(new TestSubclass(Map.of(0, BigDecimal.ONE, 1, BigDecimal.ONE))));
    }

    /**
     * Verifies that two different sortable probability mass functions with different values have different hash codes.
     */
    @Test
    public void twoDifferentSortableProbabilityMassFunctionsWithDifferentValuesShouldHaveDifferentHashCodes() {
        assertFalse(
                TEST_OBJECT.hashCode() == new TestSubclass(Map.of(0, BigDecimal.ONE, 1, BigDecimal.ONE)).hashCode());
    }

}
