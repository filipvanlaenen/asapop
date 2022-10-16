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
}
