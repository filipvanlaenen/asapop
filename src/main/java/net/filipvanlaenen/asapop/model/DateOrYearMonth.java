package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Class wrapping around either a Date (<code>LocalDate</code>) or <code>YearMonth</code>.
 */
public final class DateOrYearMonth {
    private LocalDate date;
    private DateOrYearMonth(final LocalDate date) {
        this.date = date;
    }
    static DateOrYearMonth parse(final String text) {
        return new DateOrYearMonth(LocalDate.parse(text));
    }
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DateOrYearMonth) {
            DateOrYearMonth otherDateOrYearMonth = (DateOrYearMonth) obj;
            return otherDateOrYearMonth.date.equals(date);
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
