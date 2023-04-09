package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

/**
 * Class modeling an election date, which can be a date (<code>LocalDate</code>), a month (<code>YearMonth</code>) or a
 * year (<code>Year</code>), with a qualifier.
 */
public abstract class ElectionDate {
    /**
     * Enumeration modeling the qualifier for the date.
     */
    private enum Qualifier {
        /**
         * An exact date.
         */
        EXACT_DATE(null),
        /**
         * A deadline.
         */
        DEADLINE("no-later-than"),
        /**
         * An approximate deadline.
         */
        APPROXIMATE_DEADLINE("no-later-than-around"),
        /**
         * An approximate date.
         */
        APPROXIMATE_DATE("around");

        /**
         * The term key corresponding to the qualifier.
         */
        private final String termKey;

        /**
         * Constructor taking the term key as its parameter.
         *
         * @param termKey The term key for the qualifier.
         */
        Qualifier(final String termKey) {
            this.termKey = termKey;
        }

        /**
         * Returns the term key for the qualifier.
         *
         * @return The term key for the qualifier.
         */
        private String getTermKey() {
            return termKey;
        }
    }

    /**
     * Class modeling a day as an election date.
     */
    private static final class ExpectedDay extends ElectionDate {
        /**
         * The date.
         */
        private final LocalDate date;

        /**
         * Constructor taking the date as its parameter in addition to a qualifier.
         *
         * @param date      The date.
         * @param qualifier The qualifier for the date.
         */
        private ExpectedDay(final LocalDate date, final Qualifier qualifier) {
            super(qualifier);
            this.date = date;
        }

        @Override
        public LocalDate getEndDate() {
            return date;
        }

        @Override
        public String getDateString() {
            return date.toString();
        }
    }

    /**
     * Class modeling a month as an election date.
     */
    private static final class ExpectedMonth extends ElectionDate {
        /**
         * The month.
         */
        private final YearMonth month;

        /**
         * Constructor taking the month as its parameter in addition to a qualifier.
         *
         * @param month     The month.
         * @param qualifier The qualifier for the date.
         */
        private ExpectedMonth(final YearMonth month, final Qualifier qualifier) {
            super(qualifier);
            this.month = month;
        }

        @Override
        public LocalDate getEndDate() {
            return month.atEndOfMonth();
        }

        @Override
        public String getDateString() {
            return month.toString();
        }
    }

    /**
     * Class modeling a year as an election date.
     */
    private static final class ExpectedYear extends ElectionDate {
        /**
         * The year.
         */
        private final Year year;

        /**
         * Constructor taking the year as its parameter in addition to a qualifier.
         *
         * @param year      The year.
         * @param qualifier The qualifier for the date.
         */
        private ExpectedYear(final Year year, final Qualifier qualifier) {
            super(qualifier);
            this.year = year;
        }

        @Override
        public LocalDate getEndDate() {
            return year.atMonth(Month.DECEMBER).atEndOfMonth();
        }

        @Override
        public String getDateString() {
            return year.toString();
        }
    }

    /**
     * The qualifier.
     */
    private final Qualifier qualifier;

    /**
     * Constructor taking a qualifier as its parameter.
     *
     * @param qualifier The qualifier for the date.
     */
    private ElectionDate(final Qualifier qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * Parses a text into an election date.
     *
     * @param text The text to be parse.
     * @return An election date parsed from the text.
     */
    public static ElectionDate parse(final String text) {
        if (text.startsWith("≈")) {
            return parseDate(text.substring(1), Qualifier.APPROXIMATE_DATE);
        } else if (text.startsWith("≤")) {
            return parseDate(text.substring(1), Qualifier.DEADLINE);
        } else if (text.startsWith("⪅")) {
            return parseDate(text.substring(1), Qualifier.APPROXIMATE_DEADLINE);
        } else {
            return parseDate(text, Qualifier.EXACT_DATE);
        }
    }

    /**
     * Creates an instance of the appropriate subclass based on the provided text, also passing on the qualifier.
     *
     * @param text      The text to be parsed for a date.
     * @param qualifier The qualifier for the date.
     * @return An instance of the appropriate subclass.
     */
    private static ElectionDate parseDate(final String text, final Qualifier qualifier) {
        try {
            return new ExpectedDay(LocalDate.parse(text), qualifier);
        } catch (DateTimeParseException dtpe) {
            try {
                return new ExpectedMonth(YearMonth.parse(text), qualifier);
            } catch (DateTimeParseException dtpe2) {
                return new ExpectedYear(Year.parse(text), qualifier);
            }
        }
    }

    /**
     * Compares this election date to another election date. In principle, the end date decides which date is greater
     * than the other. If the end dates are equal, days are less than months, and months less than years. Within the
     * same subclass, exact dates are less than deadlines, deadlines less than approximate deadlines, and approximate
     * deadlines are less than approximate dates.
     *
     * @param other The election date to compare this election date to.
     * @return A negative number if this instance is less than the other instance, a positive number if it is greater,
     *         and zero if both instances are equal.
     */
    public int compareTo(final ElectionDate other) {
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
        return qualifier.ordinal() - other.qualifier.ordinal();
    }

    /**
     * Returns the election date as a string.
     *
     * @return A string representing the date.
     */
    public abstract String getDateString();

    /**
     * Returns the end date. For days, this is the same as the day; for months, it is the last day of the month; for
     * years, it's the last day of the year.
     *
     * @return The end date.
     */
    public abstract LocalDate getEndDate();

    /**
     * Returns the term key according to the qualifier, or <code>null</code> for exact dates.
     *
     * @return The term key according to the qualifier, or <code>null</code> for exact dates.
     */
    public String getQualifierTermKey() {
        return qualifier.getTermKey();
    }
}