package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
}
