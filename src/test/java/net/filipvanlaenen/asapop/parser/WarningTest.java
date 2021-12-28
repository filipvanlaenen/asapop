package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Warning</code> class.
 */
public class WarningTest {
    /**
     * Verifies that the getter method <code>getLineNumber</code> is wired correctly to the constructor.
     */
    @Test
    public void getLineNumberShouldBeWiredCorrectlyToTheConstructor() {
        MalformedResultValueWarning warning = new MalformedResultValueWarning(1, "a");
        assertEquals(1, warning.getLineNumber());
    }
}
