package net.filipvanlaenen.asapop.parser;

/**
 * Abstract class defining a warning from the parser and implementing functionality common for all warnings.
 */
public abstract class ParserWarning {
    /**
     * The number of the line where the warning occurred.
     */
    private final int lineNumber;

    /**
     * Constructor taking the line number as its parameter.
     *
     * @param lineNumber The number of the line where the warning occurred.
     */
    ParserWarning(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Returns the line number.
     *
     * @return The line number.
     */
    protected int getLineNumber() {
        return lineNumber;
    }
}
