package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>DateOrMonth</code>.
 */
public class DateOrMonthTest {
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
         DateOrMonth date = DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT);
         assertEquals(SECOND_OF_SEPTEMBER_2021_DATE, date.getStart());
    }

    /**
     * Verifies that for a month, the start is the first day of the month.
     */
     @Test
     public void getStartShouldReturnTheFirstDayOfTheMonth() {
         DateOrMonth date = DateOrMonth.parse(SEPTEMBER_2021_TEXT);
         assertEquals(LocalDate.parse("2021-09-01"), date.getStart());
    }

    /**
     * Verifies that for a date, the end is the date itself.
     */
     @Test
     public void getEndShouldReturnTheDateItself() {
         DateOrMonth date = DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT);
         assertEquals(SECOND_OF_SEPTEMBER_2021_DATE, date.getEnd());
    }

    /**
     * Verifies that for a month, the end is the last day of the month.
     */
     @Test
     public void getEndShouldReturnTheLastDayOfTheMonth() {
         DateOrMonth date = DateOrMonth.parse(SEPTEMBER_2021_TEXT);
         assertEquals(LocalDate.parse("2021-09-30"), date.getEnd());
    }

    /**
     * Verifies that a date is converted correctly to a string.
     */
    @Test
    public void shouldConvertADateToAStringCorrectly() {
        assertEquals(SECOND_OF_SEPTEMBER_2021_TEXT, DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT).toString());
    }

    /**
     * Verifies that a month is converted correctly to a string.
     */
    @Test
    public void shouldConvertAMonthToAStringCorrectly() {
        assertEquals(SEPTEMBER_2021_TEXT, DateOrMonth.parse(SEPTEMBER_2021_TEXT).toString());
    }

    /**
     * Verifies that a date is not equal to null.
     */
    @Test
    public void aDateShouldNotBeEqualToNull() {
        assertFalse(DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT).equals(null));
    }

    /**
     * Verifies that a month is not equal to null.
     */
    @Test
    public void aMonthShouldNotBeEqualToNull() {
        assertFalse(DateOrMonth.parse(SEPTEMBER_2021_TEXT).equals(null));
    }

    /**
     * Verifies that a date is not equal to an object of another class, like a string.
     */
    @Test
    public void aDateShouldNotBeEqualToAString() {
        assertFalse(DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT).equals(""));
    }

    /**
     * Verifies that a month is not equal to an object of another class, like a string.
     */
    @Test
    public void aMonthShouldNotBeEqualToAString() {
        assertFalse(DateOrMonth.parse(SEPTEMBER_2021_TEXT).equals(""));
    }

    /**
     * Verifies that a date is equal to itself.
     */
    @Test
    public void aDateShouldBeEqualToItself() {
        DateOrMonth date = DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT);
        assertTrue(date.equals(date));
    }

    /**
     * Verifies that a month is equal to itself.
     */
    @Test
    public void aMonthShouldBeEqualToItself() {
        DateOrMonth month = DateOrMonth.parse(SEPTEMBER_2021_TEXT);
        assertTrue(month.equals(month));
    }

    /**
     * Verifies that calling hashCode twice on a date returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnDateReturnsTheSameResult() {
        DateOrMonth date = DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT);
        assertEquals(date.hashCode(), date.hashCode());
    }

    /**
     * Verifies that calling hashCode twice on a month returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnMonthReturnsTheSameResult() {
        DateOrMonth month = DateOrMonth.parse(SEPTEMBER_2021_TEXT);
        assertEquals(month.hashCode(), month.hashCode());
    }

    /**
     * Verifies that two dates parsed from the same text are equal.
     */
    @Test
    public void twoDatesParsedFromTheSameStringShouldBeEqual() {
        assertEquals(DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT),
                     DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT));
    }

    /**
     * Verifies that two months parsed from the same text are equal.
     */
    @Test
    public void twoMonthsParsedFromTheSameStringShouldBeEqual() {
        assertEquals(DateOrMonth.parse(SEPTEMBER_2021_TEXT), DateOrMonth.parse(SEPTEMBER_2021_TEXT));
    }

    /**
     * Verifies that two dates parsed from the same text return the same hashCode.
     */
    @Test
    public void twoDatesParsedFromTheSameStringShouldHaveTheSameHashCode() {
        assertEquals(DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT).hashCode(),
                     DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT).hashCode());
    }

    /**
     * Verifies that two months parsed from the same text return the same hashCode.
     */
    @Test
    public void twoMonthsParsedFromTheSameStringShouldHaveTheSameHashCode() {
        assertEquals(DateOrMonth.parse(SEPTEMBER_2021_TEXT).hashCode(),
                     DateOrMonth.parse(SEPTEMBER_2021_TEXT).hashCode());
    }

    /**
     * Verifies that two different dates are not equal.
     */
    @Test
    public void twoDifferentDatesShouldNotBeEqual() {
        assertFalse(DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT).equals(DateOrMonth.parse("2021-10-02")));
    }

    /**
     * Verifies that two different months are not equal.
     */
    @Test
    public void twoDifferentMonthsShouldNotBeEqual() {
        assertFalse(DateOrMonth.parse(SEPTEMBER_2021_TEXT).equals(DateOrMonth.parse("2021-10")));
    }

    /**
     * Verifies that two different dates have different hashCodes.
     */
    @Test
    public void twoDifferentDatesShouldHaveDifferentHashCodes() {
        assertFalse(DateOrMonth.parse(SECOND_OF_SEPTEMBER_2021_TEXT).hashCode() == DateOrMonth.parse("2021-10-02").hashCode());
    }

    /**
     * Verifies that two different months have different hashCodes.
     */
    @Test
    public void twoDifferentMonthsShouldHaveDifferentHashCodes() {
        assertFalse(DateOrMonth.parse(SEPTEMBER_2021_TEXT).hashCode() == DateOrMonth.parse("2021-10").hashCode());
    }
}
