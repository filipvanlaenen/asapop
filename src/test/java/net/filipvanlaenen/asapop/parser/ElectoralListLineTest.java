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
     * A sample electoral list line.
     */
    private static final String SAMPLE_LINE = "A: AA001 •A: AP •EN: Apple Party";

    /**
     * Verifies that the <code>isElecoralListLine</code> method can detect a line with an electoral list.
     */
    @Test
    public void isElectoralListLineShouldDetectElectoralListLine() {
        assertTrue(ElectoralListLine.isElectoralListLine(SAMPLE_LINE));
    }

    /**
     * Verifies that the <code>isElecoralListLine</code> method can detect a line with an electoral list with a digit in
     * its key.
     */
    @Test
    public void isElectoralListLineShouldDetectElectoralListLineWithDigitInKey() {
        assertTrue(ElectoralListLine.isElectoralListLine("A1: AA001 •A: A1 •EN: Apple One Party"));
    }

    /**
     * Verifies that the <code>isElecoralListLine</code> method rejects a line with an electoral list with a digit at
     * the start of the key.
     */
    @Test
    public void isElectoralListLineShouldRejectElectoralListLineWithDigitAtTheStartOfAKey() {
        assertFalse(ElectoralListLine.isElectoralListLine("1A: AA001 •A: A1 •EN: Apple One Party"));
    }

    /**
     * Verifies that the <code>isElecoralListLine</code> method can detect a line with an electoral list with the name
     * in more than one language.
     */
    @Test
    public void isElectoralListLineShouldDetectElectoralListLineWithNamesInTwoLanguages() {
        assertTrue(ElectoralListLine.isElectoralListLine("A: AA001 •A: A •EN: Apple Party •EO: Pomo Partio"));
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
     * Verifies that the electoral list is returned.
     */
    @Test
    public void shouldReturnTheElectoralList() {
        ElectoralListLine electoralListLine = ElectoralListLine.parse(SAMPLE_LINE);
        assertEquals(ElectoralList.get("AA001"), electoralListLine.getElectoralList());
    }

    /**
     * Verifies that the key is returned.
     */
    @Test
    public void shouldReturnTheKey() {
        ElectoralListLine electoralListLine = ElectoralListLine.parse(SAMPLE_LINE);
        assertEquals("A", electoralListLine.getKey());
    }

    /**
     * Verifies that the abbreviation of an electoral list is updated.
     */
    @Test
    public void shouldUpdateTheAbbreviationOfAnElectoralList() {
        ElectoralListLine electoralListLine = ElectoralListLine.parse(SAMPLE_LINE);
        electoralListLine.updateElectoralList();
        assertEquals("AP", ElectoralList.get("AA001").getAbbreviation());
    }

    /**
     * Verifies that the romanized abbreviation of an electoral list is updated.
     */
    @Test
    public void shouldUpdateTheRomanizedAbbreviationOfAnElectoralList() {
        ElectoralListLine electoralListLine = ElectoralListLine.parse("A: AA001 •A: ΑΠ •R:AP •EN: Apple Party");
        electoralListLine.updateElectoralList();
        assertEquals("AP", ElectoralList.get("AA001").getRomanizedAbbreviation());
    }

    /**
     * Verifies that the names of an electoral list are updated.
     */
    @Test
    public void shouldUpdateTheNamesOfAnElectoralList() {
        ElectoralListLine electoralListLine =
                ElectoralListLine.parse("A: AA001 •A: AP •EN: Apple Party •NL: Appelpartij");
        electoralListLine.updateElectoralList();
        assertEquals("Appelpartij", ElectoralList.get("AA001").getName("NL"));
    }
}
