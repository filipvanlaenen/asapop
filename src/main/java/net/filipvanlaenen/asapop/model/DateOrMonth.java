package net.filipvanlaenen.asapop.model;

import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

/**
 * Class wrapping around either a date (<code>LocalDate</code>) or a month (<code>YearMonth</code>).
 */
public abstract class DateOrMonth {
    /**
     * Subclass wrapping around a date.
     */
    private static final class Date extends DateOrMonth {
        /**
         * The date.
         */
        private LocalDate date;

        /**
         * Constructor taking the date as its parameter.
         *
         * @param date The date to wrap around.
         */
        private Date(final LocalDate date) {
            this.date = date;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Date) {
                Date otherDate = (Date) obj;
                return otherDate.date.equals(date);
            } else {
                return false;
            }
        }

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
        public int hashCode() {
            return Objects.hash(date);
        }

        @Override
        public String toString() {
            return date.toString();
        }
    }

    /**
     * Subclass wrapping around a month.
     */
    private static final class Month extends DateOrMonth {
        /**
         * The month.
         */
        private YearMonth month;

        /**
         * Constructor taking the month as its parameter.
         *
         * @param month The month to wrap around.
         */
        private Month(final YearMonth month) {
            this.month = month;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Month) {
                Month otherMonth = (Month) obj;
                return otherMonth.month.equals(month);
            } else {
                return false;
            }
        }

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
            return month.atDay​(1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(month);
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
    public abstract LocalDate getEnd();

    /**
     * Returns the start of the date or month.
     *
     * @return The start of the date or month.
     */
    public abstract LocalDate getStart();
}
