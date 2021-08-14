package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ElectoralListLine</code> class.
 */
public final class ElectoralListLineTest {
    /**
     * Verifies that the <code>isElecoralListLine</code> method can detect a line with an electoral list.
     */
    @Test
    public void isElectoralListLineShouldDetectElectoralListLine() {
        assertTrue(ElectoralListLine.isElectoralListLine("A: •A: A •EN: Apple Party"));
    }

    /**
     * Verifies that the <code>isElecoralListLine</code> method can detect a line with an electoral list with the name
     * in more than one language.
     */
    @Test
    public void isElectoralListLineShouldDetectElectoralListLineWithNamesInTwoLanguages() {
        assertTrue(ElectoralListLine.isElectoralListLine("A: •A: A •EN: Apple Party •EO: Pomo Partio"));
    }

    /**
     * Verifies that the <code>isElecoralListLine</code> method can detect a line that doesn't match a definition for an
     * electoral list.
     */
    @Test
    public void isElectoralListLineShouldDetectNonElectoralListLine() {
        assertFalse(ElectoralListLine.isElectoralListLine("Foo"));
    }
}
