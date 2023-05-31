package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

/**
 * Class wrapping around either a date (<code>LocalDate</code>) or a month (<code>YearMonth</code>).
 */
public interface DateOrMonth {
    /**
     * Record class wrapping around a date.
     *
     * @param date The date to wrap around.
     */
    record Date(LocalDate date) implements DateOrMonth {
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
    record Month(YearMonth month) implements DateOrMonth {
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
     * Parses a text into a date or month. It tries to parse the text into a date first, and then a month.
     *
     * @param text The text to parse.
     * @return A date or month wrapped in a DateOrMonth instance.
     */
    static DateOrMonth parse(final String text) {
        try {
            return new Date(LocalDate.parse(text));
        } catch (DateTimeParseException dtpe) {
            return new Month(YearMonth.parse(text));
        }
    }

    /**
     * Returns the end of the date or month.
     *
     * @return The end of the date or month.
     */
    LocalDate getEnd();

    /**
     * Returns the start of the date or month.
     *
     * @return The start of the date or month.
     */
    LocalDate getStart();
}
