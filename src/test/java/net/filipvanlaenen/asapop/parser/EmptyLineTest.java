package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>EmptyLine</code> class.
 */
public final class EmptyLineTest {
    /**
     * Verifies that the <code>isEmptyLine</code> method can detect an empty line.
     */
    @Test
    public void isEmptyLineShouldDetectEmptyLine() {
        assertTrue(EmptyLine.isEmptyLine(""));
    }

    /**
     * Verifies that the <code>isEmptyLine</code> method can detect an empty line with whitespace.
     */
    @Test
    public void isEmptyLineShouldDetectEmptyLineWithWhitespace() {
        assertTrue(EmptyLine.isEmptyLine(" "));
    }

    /**
     * Verifies that the <code>isEmptyLine</code> method can detect a line that isn't empty.
     */
    @Test
    public void isEmptyLineShouldDetectNonEmptyLine() {
        assertFalse(EmptyLine.isEmptyLine("Foo"));
    }
}
