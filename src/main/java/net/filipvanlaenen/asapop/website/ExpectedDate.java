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
    private static class ExpectedDay extends ExpectedDate {
        private final LocalDate date;

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

    private static class ExpectedMonth extends ExpectedDate {
        /**
         * The month.
         */
        private final YearMonth month;

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

    private static class ExpectedYear extends ExpectedDate {
        /**
         * The year.
         */
        private final Year year;

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

    private final boolean approximate;
    private final boolean deadline;

    private ExpectedDate(final boolean approximate, final boolean deadline) {
        this.approximate = approximate;
        this.deadline = deadline;
    }

    static ExpectedDate parse(final String aString) {
        if (aString.startsWith("≈")) {
            return parseDate(aString.substring(1), true, false);
        } else if (aString.startsWith("≤")) {
            return parseDate(aString.substring(1), false, true);
        } else {
            return parseDate(aString, false, false);
        }
    }

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

    abstract String getDateString();

    int compareTo(ExpectedDate other) {
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
        return 0;
    }

    protected abstract LocalDate getEndDate();

    boolean isApproximate() {
        return approximate;
    }

    boolean isDeadline() {
        return deadline;
    }
}
