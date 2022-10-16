package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ExpectedDate</code> class.
 */
public class ExpectedDateTest {
    /**
     * Verifies that the end date of an expected date is equal to the date if a full date is given.
     */
    @Test
    public void endDateShouldBeEqualToDateForFullDate() {
        assertEquals(LocalDate.of(2022, Month.OCTOBER, 16), ExpectedDate.parse("2022-10-16").getEndDate());
    }

    /**
     * Verifies that the end date of an expected date is equal to the last day of the month if a month is given.
     */
    @Test
    public void endDateShouldBeLastDayOfMonth() {
        assertEquals(LocalDate.of(2022, Month.OCTOBER, 31), ExpectedDate.parse("2022-10").getEndDate());
    }

    /**
     * Verifies that the end date of an expected date is equal to the last day of the year if a year is given.
     */
    @Test
    public void endDateShouldBeLastDayOfYear() {
        assertEquals(LocalDate.of(2022, Month.DECEMBER, 31), ExpectedDate.parse("2022").getEndDate());
    }

    /**
     * Verifies that the date string for a full date is correct.
     */
    @Test
    public void dateStringForFullDateShouldBeCorrect() {
        assertEquals("2022-10-16", ExpectedDate.parse("2022-10-16").getDateString());
    }

    /**
     * Verifies that the date string for a month is correct.
     */
    @Test
    public void dateStringForMonthShouldBeCorrect() {
        assertEquals("2022-10", ExpectedDate.parse("2022-10").getDateString());
    }

    /**
     * Verifies that the date string for a year is correct.
     */
    @Test
    public void dateStringForYearShouldBeCorrect() {
        assertEquals("2022", ExpectedDate.parse("2022").getDateString());
    }

    /**
     * Verifies that an exact date is not approximate.
     */
    @Test
    public void exactDateShouldNotBeApproximate() {
        assertFalse(ExpectedDate.parse("2022-10-16").isApproximate());
    }

    /**
     * Verifies that an approximate date is approximate.
     */
    @Test
    public void approximateDateShouldBeApproximate() {
        assertTrue(ExpectedDate.parse("≈2022-10-16").isApproximate());
    }

    /**
     * Verifies that a deadline is not approximate.
     */
    @Test
    public void deadlineShouldNotBeApproximate() {
        assertFalse(ExpectedDate.parse("≤2022-10-16").isApproximate());
    }

    /**
     * Verifies that an exact date is not da eadline.
     */
    @Test
    public void exactDateShouldNotBeADeadline() {
        assertFalse(ExpectedDate.parse("2022-10-16").isDeadline());
    }

    /**
     * Verifies that an approximate date is not a deadline.
     */
    @Test
    public void approximateDateShouldNotBeADeadline() {
        assertFalse(ExpectedDate.parse("≈2022-10-16").isDeadline());
    }

    /**
     * Verifies that a deadline is deadline.
     */
    @Test
    public void deadlineShouldBeDeadline() {
        assertTrue(ExpectedDate.parse("≤2022-10-16").isDeadline());
    }

    /**
     * Verifies that an earlier date is less.
     */
    @Test
    public void earlierDateShouldBeLess() {
        assertTrue(ExpectedDate.parse("2022-10-15").compareTo(ExpectedDate.parse("2022-10-16")) < 0);
    }

    /**
     * Verifies that an equal date is equal.
     */
    @Test
    public void equalDateShouldBeEqual() {
        assertTrue(ExpectedDate.parse("2022-10-16").compareTo(ExpectedDate.parse("2022-10-16")) == 0);
    }

    /**
     * Verifies that a later date is greater.
     */
    @Test
    public void laterDateShouldBeGreater() {
        assertTrue(ExpectedDate.parse("2022-10-17").compareTo(ExpectedDate.parse("2022-10-16")) > 0);
    }

    /**
     * Verifies that an earlier date is less than a month.
     */
    @Test
    public void earlierDateShouldBeLessThanMonth() {
        assertTrue(ExpectedDate.parse("2022-10-15").compareTo(ExpectedDate.parse("2022-10")) < 0);
    }

    /**
     * Verifies that last day of month is less than a month.
     */
    @Test
    public void lastDayOfMonthShouldBeLessThanMonth() {
        assertTrue(ExpectedDate.parse("2022-10-31").compareTo(ExpectedDate.parse("2022-10")) < 0);
    }

    /**
     * Verifies that a month is greater than last day of month.
     */
    @Test
    public void monthShouldBeGreaterThanLastDayOfMonth() {
        assertTrue(ExpectedDate.parse("2022-10").compareTo(ExpectedDate.parse("2022-10-31")) > 0);
    }

    /**
     * Verifies that a later date is greater than a month.
     */
    @Test
    public void laterDateShouldBeGreaterThanMonth() {
        assertTrue(ExpectedDate.parse("2022-11-01").compareTo(ExpectedDate.parse("2022-10")) > 0);
    }
}
