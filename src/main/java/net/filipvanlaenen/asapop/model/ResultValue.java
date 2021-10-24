package net.filipvanlaenen.asapop.model;

import java.util.Objects;

public class ResultValue {
    private final String text;
    public ResultValue(final String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ResultValue) {
            ResultValue otherResultValue = (ResultValue) obj;
            return otherResultValue.text.equals(text);
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
