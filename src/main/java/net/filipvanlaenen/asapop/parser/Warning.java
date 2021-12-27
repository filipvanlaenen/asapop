package net.filipvanlaenen.asapop.parser;

/**
 * Abstract class defining a warning from the parser and implementing functionality common for all warnings.
 */
public abstract class Warning {
    /**
     * The number of the line where the warning occurred.
     */
    private final int lineNumber;

    /**
     * Constructor taking the line number as its parameter.
     *
     * @param lineNumber The number of the line where the warning occurred.
     */
    Warning(final int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    protected int getLineNumber() {
        return lineNumber;
    }
}
