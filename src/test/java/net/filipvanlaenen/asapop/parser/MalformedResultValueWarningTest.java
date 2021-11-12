package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>MalformedResultValueWarning</code> class.
 */
public final class MalformedResultValueWarningTest {
    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        Warning warning = new MalformedResultValueWarning(1, "a");
        assertEquals("Malformed result value (“a”) detected in line 1.", warning.toString());
    }
}
