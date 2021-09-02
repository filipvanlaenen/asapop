package net.filipvanlaenen.asapop.model;

import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

/**
 * Class wrapping around either a Date (<code>LocalDate</code>) or <code>YearMonth</code>.
 */
public abstract class DateOrYearMonth {
    private static final class Date extends DateOrYearMonth {
        private LocalDate date;
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
        public LocalDate getEnd() {
            return date;
        }
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
    private static final class Month extends DateOrYearMonth {
        private YearMonth month;
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
        public LocalDate getEnd() {
            return month.atEndOfMonth();
        }
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
    static DateOrYearMonth parse(final String text) {
        try {
            return new Date(LocalDate.parse(text));
        } catch (DateTimeParseException dtpe) {
            return new Month(YearMonth.parse(text));
        }
    }
    public abstract LocalDate getEnd();
    public abstract LocalDate getStart();
}
