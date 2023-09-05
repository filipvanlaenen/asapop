package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Scope</code> class.
 */
public class ScopeTest {
    /**
     * Verifies that the string representation of a scope is returned by the <code>toString()</code> method.
     */
    @Test
    public void toStringShouldReturnTheStringRepresentationOfAScope() {
        assertEquals("N", Scope.NATIONAL.toString());
    }

    /**
     * Verifies that a string representation of a scope is parsed correctly.
     */
    @Test
    public void shouldParseStringRepresentationIntoAScope() {
        assertEquals(Scope.NATIONAL, Scope.parse("N"));
    }
}
