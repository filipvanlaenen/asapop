package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.Scope;

/**
 * Unit tests on the <code>Line</code> class.
 */
public final class LineTest {
    /**
     * Verifies that a match is detected by the <code>lineMatchesPattern</code> method.
     */
    @Test
    public void lineMatchesPatternShouldDetectAMatch() {
        assertTrue(Line.textMatchesPattern(Line.RESULT_KEY_VALUE_PATTERN, "A:5"));
    }

    /**
     * Verifies that a non-match is detected by the <code>lineMatchesPattern</code> method.
     */
    @Test
    public void lineMatchesPatternShouldDetectANonMatch() {
        assertFalse(Line.textMatchesPattern(Line.RESULT_KEY_VALUE_PATTERN, "a:5"));
    }

    /**
     * Verifies that the parser method for the scopes returns a value for string E.
     */
    @Test
    public void parseScopeShouldReturnAScopeForAValidValue() {
        assertEquals(Scope.EUROPEAN, Line.parseScope("E"));
    }

    /**
     * Verifies that the parser method for the scopes returns <code>null</code> for an invalid string.
     */
    @Test
    public void parseScopeShouldReturnNullForInvalidValue() {
        assertNull(Line.parseScope("X"));
    }
}
