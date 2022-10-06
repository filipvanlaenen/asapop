package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ElectoralList;

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

    /**
     * Verifies that the abbreviation of an electoral list is updated.
     */
    @Test
    public void shouldUpdateTheAbbreviationOfAnElectoralList() {
        ElectoralListLine electoralListLine = ElectoralListLine.parse("A: •A: AP •EN: Apple Party");
        electoralListLine.updateElectoralList();
        assertEquals("AP", ElectoralList.get("A").getAbbreviation());
    }

    /**
     * Verifies that the romanized abbreviation of an electoral list is updated.
     */
    @Test
    public void shouldUpdateTheRomanizedAbbreviationOfAnElectoralList() {
        ElectoralListLine electoralListLine = ElectoralListLine.parse("A: •A: ΑΠ •R:AP •EN: Apple Party");
        electoralListLine.updateElectoralList();
        assertEquals("AP", ElectoralList.get("A").getRomanizedAbbreviation());
    }

    /**
     * Verifies that the names of an electoral list are updated.
     */
    @Test
    public void shouldUpdateTheNamesOfAnElectoralList() {
        ElectoralListLine electoralListLine = ElectoralListLine.parse("A: •A: AP •EN: Apple Party");
        electoralListLine.updateElectoralList();
        assertEquals("Apple Party", ElectoralList.get("A").getName("EN"));
    }
}
