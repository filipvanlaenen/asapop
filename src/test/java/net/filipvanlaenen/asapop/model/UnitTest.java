package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Unit</code> class.
 */
public class UnitTest {
    /**
     * Verifies that the string representation of a unit is returned by the <code>toString()</code> method.
     */
    @Test
    public void toStringShouldReturnTheStringRepresentationOfAUnit() {
        assertEquals("%", Unit.PERCENTAGES.toString());
    }

    /**
     * Verifies that a string representation of a unit is parsed correctly.
     */
    @Test
    public void shouldParseStringRepresentationIntoAUnit() {
        assertEquals(Unit.PERCENTAGES, Unit.parse("%"));
    }
}
