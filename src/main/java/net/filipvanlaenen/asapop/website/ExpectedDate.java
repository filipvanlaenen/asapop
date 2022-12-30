package net.filipvanlaenen.asapop.website;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

/**
 * Class modeling an expected date, which can be a date (<code>LocalDate</code>), a month (<code>YearMonth</code>) or a
 * year (<code>Year</code>), possibly with the modifier that it's approximate or a deadline.
 */
abstract class ExpectedDate {
    /**
     * Class modeling a day as an expected date.
     */
    private static final class ExpectedDay extends ExpectedDate {
        /**
         * The date.
         */
        private final LocalDate date;

        /**
         * Constructor taking the date as its parameter in addition to whether it's an approximate date or a deadline.
         *
         * @param date        The date.
         * @param approximate True if the date is approximate.
         * @param deadline    True if the date is a deadline.
         */
        private ExpectedDay(final LocalDate date, final boolean approximate, final boolean deadline) {
            super(approximate, deadline);
            this.date = date;
        }

        @Override
        protected LocalDate getEndDate() {
            return date;
        }

        @Override
        public String getDateString() {
            return date.toString();
        }
    }

    /**
     * Class modeling a month as an expected date.
     */
    private static final class ExpectedMonth extends ExpectedDate {
        /**
         * The month.
         */
        private final YearMonth month;

        /**
         * Constructor taking the month as its parameter in addition to whether it's an approximate date or a deadline.
         *
         * @param month       The month.
         * @param approximate True if the month is approximate.
         * @param deadline    True if the month is a deadline.
         */
        private ExpectedMonth(final YearMonth month, final boolean approximate, final boolean deadline) {
            super(approximate, deadline);
            this.month = month;
        }

        @Override
        protected LocalDate getEndDate() {
            return month.atEndOfMonth();
        }

        @Override
        public String getDateString() {
            return month.toString();
        }
    }

    /**
     * Class modeling a year as an expected date.
     */
    private static final class ExpectedYear extends ExpectedDate {
        /**
         * The year.
         */
        private final Year year;

        /**
         * Constructor taking the year as its parameter in addition to whether it's an approximate date or a deadline.
         *
         * @param year        The year.
         * @param approximate True if the year is approximate.
         * @param deadline    True if the year is a deadline.
         */
        private ExpectedYear(final Year year, final boolean approximate, final boolean deadline) {
            super(approximate, deadline);
            this.year = year;
        }

        @Override
        protected LocalDate getEndDate() {
            return year.atMonth(Month.DECEMBER).atEndOfMonth();
        }

        @Override
        public String getDateString() {
            return year.toString();
        }
    }

    /**
     * True if the date is approximate.
     */
    private final boolean approximate;
    /**
     * True if the date is a deadline.
     */
    private final boolean deadline;

    /**
     * Constructor taking whether the expected date is an approximate date or a deadline as its parameters.
     *
     * @param approximate True if the year is approximate.
     * @param deadline    True if the year is a deadline.
     */
    private ExpectedDate(final boolean approximate, final boolean deadline) {
        this.approximate = approximate;
        this.deadline = deadline;
    }

    /**
     * Parses a text into an expected date.
     *
     * @param text The text to be parse.
     * @return An expected date parsed from the text.
     */
    static ExpectedDate parse(final String text) {
        if (text.startsWith("≈")) {
            return parseDate(text.substring(1), true, false);
        } else if (text.startsWith("≤")) {
            return parseDate(text.substring(1), false, true);
        } else if (text.startsWith("⪅")) {
            return parseDate(text.substring(1), true, true);
        } else {
            return parseDate(text, false, false);
        }
    }

    /**
     * Creates an instance of the appropriate subclass based on the provided text, also passing on whether the date is
     * approximate or a deadline.
     *
     * @param text        The text to be parsed for a date.
     * @param approximate True if the date is approximate.
     * @param deadline    True if the date is a deadline.
     * @return An instance of the appropriate subclass.
     */
    private static ExpectedDate parseDate(final String text, final boolean approximate, final boolean deadline) {
        try {
            return new ExpectedDay(LocalDate.parse(text), approximate, deadline);
        } catch (DateTimeParseException dtpe) {
            try {
                return new ExpectedMonth(YearMonth.parse(text), approximate, deadline);
            } catch (DateTimeParseException dtpe2) {
                return new ExpectedYear(Year.parse(text), approximate, deadline);
            }
        }
    }

    /**
     * Compares this expected date to another expected date. In principle, the end date decides which date is greater
     * than the other. If the end dates are equal, days are less than months, and months less than years. Within the
     * same subclass, exact dates are less than deadlines, and deadlines are less than approximate dates.
     *
     * @param other The expected date to compare this expected date to.
     * @return A negative number if this instance is less than the other instance, a positive number if it is greater,
     *         and zero if both instances are equal.
     */
    int compareTo(final ExpectedDate other) {
        int endDateResult = getEndDate().compareTo(other.getEndDate());
        if (endDateResult != 0) {
            return endDateResult;
        }
        if (this instanceof ExpectedDay && !(other instanceof ExpectedDay)) {
            return -1;
        }
        if (!(this instanceof ExpectedDay) && other instanceof ExpectedDay) {
            return 1;
        }
        if (this instanceof ExpectedYear && !(other instanceof ExpectedYear)) {
            return 1;
        }
        if (!(this instanceof ExpectedYear) && other instanceof ExpectedYear) {
            return -1;
        }
        if (isApproximate() && !other.isApproximate()) {
            return 1;
        }
        if (!isApproximate() && other.isApproximate()) {
            return -1;
        }
        if (isDeadline() && !other.isDeadline()) {
            return 1;
        }
        if (!isDeadline() && other.isDeadline()) {
            return -1;
        }
        return 0;
    }

    /**
     * Returns the date as a string.
     *
     * @return A string representing the date.
     */
    protected abstract String getDateString();

    /**
     * Returns the end date. For days, this is the same as the day; for months, it is the last day of the month; for
     * years, it's the last day of the year.
     *
     * @return The end date.
     */
    protected abstract LocalDate getEndDate();

    /**
     * Returns true if the expected date is approximate.
     *
     * @return True if the expected date is approximate.
     */
    boolean isApproximate() {
        return approximate;
    }

    /**
     * Returns true if the expected date is a deadline.
     *
     * @return True if the expected date is a deadline.
     */
    boolean isDeadline() {
        return deadline;
    }
}
