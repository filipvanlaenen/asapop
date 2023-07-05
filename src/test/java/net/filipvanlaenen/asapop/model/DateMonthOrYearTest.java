package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>DateMonthOrYear</code>.
 */
public class DateMonthOrYearTest {
    /**
     * 2 September 2021 as a text.
     */
    private static final String SECOND_OF_SEPTEMBER_2021_TEXT = "2021-09-02";
    /**
     * September 2021 as a text.
     */
    private static final String SEPTEMBER_2021_TEXT = "2021-09";
    /**
     * 2 September 2021 as a date.
     */
    private static final LocalDate SECOND_OF_SEPTEMBER_2021_DATE = LocalDate.of(2021, 9, 2);

    /**
     * Verifies that for a date, the start is the date itself.
     */
    @Test
    public void getStartShouldReturnTheDateItself() {
        DateMonthOrYear date = DateMonthOrYear.parse(SECOND_OF_SEPTEMBER_2021_TEXT);
        assertEquals(SECOND_OF_SEPTEMBER_2021_DATE, date.getStart());
    }

    /**
     * Verifies that for a month, the start is the first day of the month.
     */
    @Test
    public void getStartShouldReturnTheFirstDayOfTheMonth() {
        DateMonthOrYear date = DateMonthOrYear.parse(SEPTEMBER_2021_TEXT);
        assertEquals(LocalDate.parse("2021-09-01"), date.getStart());
    }

    /**
     * Verifies that for a date, the end is the date itself.
     */
    @Test
    public void getEndShouldReturnTheDateItself() {
        DateMonthOrYear date = DateMonthOrYear.parse(SECOND_OF_SEPTEMBER_2021_TEXT);
        assertEquals(SECOND_OF_SEPTEMBER_2021_DATE, date.getEnd());
    }

    /**
     * Verifies that for a month, the end is the last day of the month.
     */
    @Test
    public void getEndShouldReturnTheLastDayOfTheMonth() {
        DateMonthOrYear date = DateMonthOrYear.parse(SEPTEMBER_2021_TEXT);
        assertEquals(LocalDate.parse("2021-09-30"), date.getEnd());
    }

    /**
     * Verifies that a date is converted correctly to a string.
     */
    @Test
    public void shouldConvertADateToAStringCorrectly() {
        assertEquals(SECOND_OF_SEPTEMBER_2021_TEXT, DateMonthOrYear.parse(SECOND_OF_SEPTEMBER_2021_TEXT).toString());
    }

    /**
     * Verifies that a month is converted correctly to a string.
     */
    @Test
    public void shouldConvertAMonthToAStringCorrectly() {
        assertEquals(SEPTEMBER_2021_TEXT, DateMonthOrYear.parse(SEPTEMBER_2021_TEXT).toString());
    }
}
