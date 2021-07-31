package net.filipvanlaenen.asapop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>CommandLineInterface</code>.
 */
public class CommandLineInterfaceTest {
    /**
     * Verifies the utility method to capitalize a word.
     */
    @Test
    public void shouldCapitalizeAWord() {
        assertEquals("Foo", CommandLineInterface.capitalizeWord("foo"));
    }
}
