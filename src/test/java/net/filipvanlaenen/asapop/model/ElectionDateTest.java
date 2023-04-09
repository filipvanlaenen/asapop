package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ElectionDate</code> class.
 */
public class ElectionDateTest {
    /**
     * The magic number sixteen.
     */
    private static final int SIXTEEN = 16;
    /**
     * The magic number thirty-one.
     */
    private static final int THIRTY_ONE = 31;
    /**
     * The magic number 2022.
     */
    private static final int TWENTY_TWENTY_TWO = 2022;

    /**
     * Verifies that the end date of an election date is equal to the date if a full date is given.
     */
    @Test
    public void endDateShouldBeEqualToDateForFullDate() {
        assertEquals(LocalDate.of(TWENTY_TWENTY_TWO, Month.OCTOBER, SIXTEEN),
                ElectionDate.parse("2022-10-16").getEndDate());
    }

    /**
     * Verifies that the end date of an election date is equal to the last day of the month if a month is given.
     */
    @Test
    public void endDateShouldBeLastDayOfMonth() {
        assertEquals(LocalDate.of(TWENTY_TWENTY_TWO, Month.OCTOBER, THIRTY_ONE),
                ElectionDate.parse("2022-10").getEndDate());
    }

    /**
     * Verifies that the end date of an election date is equal to the last day of the year if a year is given.
     */
    @Test
    public void endDateShouldBeLastDayOfYear() {
        assertEquals(LocalDate.of(TWENTY_TWENTY_TWO, Month.DECEMBER, THIRTY_ONE),
                ElectionDate.parse("2022").getEndDate());
    }

    /**
     * Verifies that the date string for a full date is correct.
     */
    @Test
    public void dateStringForFullDateShouldBeCorrect() {
        assertEquals("2022-10-16", ElectionDate.parse("2022-10-16").getDateString());
    }

    /**
     * Verifies that the date string for a month is correct.
     */
    @Test
    public void dateStringForMonthShouldBeCorrect() {
        assertEquals("2022-10", ElectionDate.parse("2022-10").getDateString());
    }

    /**
     * Verifies that the date string for a year is correct.
     */
    @Test
    public void dateStringForYearShouldBeCorrect() {
        assertEquals("2022", ElectionDate.parse("2022").getDateString());
    }

    /**
     * Verifies that an exact date has no qualifier term key.
     */
    @Test
    public void exactDateShouldNotHaveQualifierTermKey() {
        assertNull(ElectionDate.parse("2022-10-16").getQualifierTermKey());
    }

    /**
     * Verifies that an approximate date has the qualifier term key <code>around</code>.
     */
    @Test
    public void approximateDateShouldHaveQualifierTermKeyAround() {
        assertEquals("around", ElectionDate.parse("≈2022-10-16").getQualifierTermKey());
    }

    /**
     * Verifies that a deadline has the qualifier term key <code>no-later-than</code>..
     */
    @Test
    public void deadlineShouldHaveQualifierTermKeyNoLaterThan() {
        assertEquals("no-later-than", ElectionDate.parse("≤2022-10-16").getQualifierTermKey());
    }

    /**
     * Verifies that an approximate deadline has the qualifier term key <code>no-later-than-around</code>..
     */
    @Test
    public void approximateDeadlineShouldHaveQualifierTermKeyNoLaterThanAround() {
        assertEquals("no-later-than-around", ElectionDate.parse("⪅2022-10-16").getQualifierTermKey());
    }

    /**
     * Verifies that an earlier date is less.
     */
    @Test
    public void earlierDateShouldBeLess() {
        assertTrue(ElectionDate.parse("2022-10-15").compareTo(ElectionDate.parse("2022-10-16")) < 0);
    }

    /**
     * Verifies that an equal date is equal.
     */
    @Test
    public void equalDateShouldBeEqual() {
        assertTrue(ElectionDate.parse("2022-10-16").compareTo(ElectionDate.parse("2022-10-16")) == 0);
    }

    /**
     * Verifies that a later date is greater.
     */
    @Test
    public void laterDateShouldBeGreater() {
        assertTrue(ElectionDate.parse("2022-10-17").compareTo(ElectionDate.parse("2022-10-16")) > 0);
    }

    /**
     * Verifies that an earlier date is less than a month.
     */
    @Test
    public void earlierDateShouldBeLessThanMonth() {
        assertTrue(ElectionDate.parse("2022-10-15").compareTo(ElectionDate.parse("2022-10")) < 0);
    }

    /**
     * Verifies that last day of month is less than a month.
     */
    @Test
    public void lastDayOfMonthShouldBeLessThanMonth() {
        assertTrue(ElectionDate.parse("2022-10-31").compareTo(ElectionDate.parse("2022-10")) < 0);
    }

    /**
     * Verifies that a month is greater than last day of month.
     */
    @Test
    public void monthShouldBeGreaterThanLastDayOfMonth() {
        assertTrue(ElectionDate.parse("2022-10").compareTo(ElectionDate.parse("2022-10-31")) > 0);
    }

    /**
     * Verifies that a later date is greater than a month.
     */
    @Test
    public void laterDateShouldBeGreaterThanMonth() {
        assertTrue(ElectionDate.parse("2022-11-01").compareTo(ElectionDate.parse("2022-10")) > 0);
    }

    /**
     * Verifies that an earlier month is less than a year.
     */
    @Test
    public void earlierMonthShouldBeLessThanYear() {
        assertTrue(ElectionDate.parse("2022-10").compareTo(ElectionDate.parse("2022")) < 0);
    }

    /**
     * Verifies that last month of year is less than a year.
     */
    @Test
    public void lastMonthOfYearShouldBeLessThanYear() {
        assertTrue(ElectionDate.parse("2022-12").compareTo(ElectionDate.parse("2022")) < 0);
    }

    /**
     * Verifies that a year is greater than last month of year.
     */
    @Test
    public void yearShouldBeGreaterThanLastMonthOfYear() {
        assertTrue(ElectionDate.parse("2022").compareTo(ElectionDate.parse("2022-12")) > 0);
    }

    /**
     * Verifies that a later month is greater than a year.
     */
    @Test
    public void laterMonthShouldBeGreaterThanYear() {
        assertTrue(ElectionDate.parse("2023-01").compareTo(ElectionDate.parse("2022")) > 0);
    }

    /**
     * Verifies that an exact date is less than a deadline.
     */
    @Test
    public void exactDateShouldBeLessThanADeadline() {
        assertTrue(ElectionDate.parse("2022-10-16").compareTo(ElectionDate.parse("≤2022-10-16")) < 0);
    }

    /**
     * Verifies that a deadline is greater than an exact date.
     */
    @Test
    public void deadlineShouldBeGreaterThanExactDate() {
        assertTrue(ElectionDate.parse("≤2022-10-16").compareTo(ElectionDate.parse("2022-10-16")) > 0);
    }

    /**
     * Verifies that an approximate date is greater than a deadline.
     */
    @Test
    public void approximateDateShouldBeGreaterThanADeadline() {
        assertTrue(ElectionDate.parse("≈2022-10-16").compareTo(ElectionDate.parse("≤2022-10-16")) > 0);
    }

    /**
     * Verifies that a deadline is less than an approximate date.
     */
    @Test
    public void deadlineShouldBeLessThanApproximateDate() {
        assertTrue(ElectionDate.parse("≤2022-10-16").compareTo(ElectionDate.parse("≈2022-10-16")) < 0);
    }

    /**
     * Verifies that an exact date is less than an approximate deadline.
     */
    @Test
    public void exactDateShouldBeLessThanApproximateDeadline() {
        assertTrue(ElectionDate.parse("2022-10-16").compareTo(ElectionDate.parse("⪅2022-10-16")) < 0);
    }

    /**
     * Verifies that an approximate deadline is greater than an exact date.
     */
    @Test
    public void approximateDeadlineShouldBeGreaterThanExactDate() {
        assertTrue(ElectionDate.parse("⪅2022-10-16").compareTo(ElectionDate.parse("2022-10-16")) > 0);
    }

    /**
     * Verifies that an approximate deadline is greater than a deadline.
     */
    @Test
    public void approximateDeadlineShouldBeGreaterThanADeadline() {
        assertTrue(ElectionDate.parse("⪅2022-10-16").compareTo(ElectionDate.parse("≤2022-10-16")) > 0);
    }

    /**
     * Verifies that a deadline is less than an approximate deadline.
     */
    @Test
    public void deadlineShouldBeLessThanApproximateDeadline() {
        assertTrue(ElectionDate.parse("≤2022-10-16").compareTo(ElectionDate.parse("⪅2022-10-16")) < 0);
    }

    /**
     * Verifies that an approximate date is greater than an approximate deadline.
     */
    @Test
    public void approximateDateShouldBeGreaterThanApproximateDeadline() {
        assertTrue(ElectionDate.parse("≈2022-10-16").compareTo(ElectionDate.parse("⪅2022-10-16")) > 0);
    }

    /**
     * Verifies that an approximate deadline is less than an approximate date.
     */
    @Test
    public void approximateDeadlineShouldBeLessThanApproximateDate() {
        assertTrue(ElectionDate.parse("⪅2022-10-16").compareTo(ElectionDate.parse("≈2022-10-16")) < 0);
    }
}
