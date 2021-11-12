package net.filipvanlaenen.asapop.parser;

class MalformedResultValueWarning implements Warning {
    private final int lineNumber;
    private final String value;
    MalformedResultValueWarning(final int lineNumber, final String value) {
        this.lineNumber = lineNumber;
        this.value = value;
    }
    @Override
    public String toString() {
        return "Malformed result value (“" + value + "”) detected in line " + lineNumber+".";
    }
}
