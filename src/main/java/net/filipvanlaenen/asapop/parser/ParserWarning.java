package net.filipvanlaenen.asapop.parser;

import net.filipvanlaenen.asapop.Warning;

/**
 * Abstract class defining a warning from the parser and implementing functionality common for all warnings.
 */
abstract class ParserWarning implements Warning {
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
