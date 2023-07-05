package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

/**
 * Class wrapping around either a date (<code>LocalDate</code>),a month (<code>YearMonth</code>) or a year
 * (<code>Year</code>.
 */
public interface DateMonthOrYear {
    /**
     * Record class wrapping around a date.
     *
     * @param date The date to wrap around.
     */
    record Date(LocalDate date) implements DateMonthOrYear {
        /**
         * Returns the end date, which is identical to the date.
         *
         * @return The end date.
         */
        @Override
        public LocalDate getEnd() {
            return date;
        }

        /**
         * Returns the start date, which is identical to the date.
         *
         * @return The start date.
         */
        @Override
        public LocalDate getStart() {
            return date;
        }

        @Override
        public String toString() {
            return date.toString();
        }
    }

    /**
     * Record class wrapping around a month.
     *
     * @param month The month to wrap around.
     */
    record Month(YearMonth month) implements DateMonthOrYear {
        /**
         * Returns the end date, which is the last day of the month.
         *
         * @return The end date.
         */
        @Override
        public LocalDate getEnd() {
            return month.atEndOfMonth();
        }

        /**
         * Returns the start date, which is the first day of the month.
         *
         * @return The start date.
         */
        @Override
        public LocalDate getStart() {
            return month.atDay(1);
        }

        @Override
        public String toString() {
            return month.toString();
        }
    }

    /**
     * Record class wrapping around a year.
     *
     * @param year The year to wrap around.
     */
    record Year(java.time.Year year) implements DateMonthOrYear {
        /**
         * Returns the end date, which is the last day of the year.
         *
         * @return The end date.
         */
        @Override
        public LocalDate getEnd() {
            return year.atMonth(12).atEndOfMonth();
        }

        /**
         * Returns the start date, which is the first day of the year.
         *
         * @return The start date.
         */
        @Override
        public LocalDate getStart() {
            return year.atMonth(1).atDay(1);
        }

        @Override
        public String toString() {
            return year.toString();
        }
    }

    /**
     * Parses a text into a date, month or year. It tries to parse the text into a date first, then a month, then a
     * year.
     *
     * @param text The text to parse.
     * @return A date, month or year wrapped in a DateMonthOrYear instance.
     */
    static DateMonthOrYear parse(final String text) {
        try {
            return new Date(LocalDate.parse(text));
        } catch (DateTimeParseException dtpe1) {
            try {
                return new Month(YearMonth.parse(text));
            } catch (DateTimeParseException dtpe2) {
                return new Year(java.time.Year.parse(text));
            }
        }
    }

    /**
     * Returns the end of the date, month or year.
     *
     * @return The end of the date, month or year.
     */
    LocalDate getEnd();

    /**
     * Returns the start of the date, month or year.
     *
     * @return The start of the date, month or year.
     */
    LocalDate getStart();
}
