package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>DateOrMonth</code>.
 */
public class DateOrMonthTest {
    /**
     * Verifies that for a date, the start is the date itself.
     */
     @Test
     public void getStartShouldReturnTheDateItself() {
         DateOrMonth date = DateOrMonth.parse("2021-09-02");
         assertEquals(LocalDate.parse("2021-09-02"), date.getStart());
    }

    /**
     * Verifies that for a month, the start is the first day of the month.
     */
     @Test
     public void getStartShouldReturnTheFirstDayOfTheMonth() {
         DateOrMonth date = DateOrMonth.parse("2021-09");
         assertEquals(LocalDate.parse("2021-09-01"), date.getStart());
    }

    /**
     * Verifies that for a date, the end is the date itself.
     */
     @Test
     public void getEndShouldReturnTheDateItself() {
         DateOrMonth date = DateOrMonth.parse("2021-09-02");
         assertEquals(LocalDate.parse("2021-09-02"), date.getEnd());
    }

    /**
     * Verifies that for a month, the end is the last day of the month.
     */
     @Test
     public void getEndShouldReturnTheLastDayOfTheMonth() {
         DateOrMonth date = DateOrMonth.parse("2021-09");
         assertEquals(LocalDate.parse("2021-09-30"), date.getEnd());
    }
}
