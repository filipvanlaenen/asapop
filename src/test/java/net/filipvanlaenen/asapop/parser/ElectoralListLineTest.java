package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.LaconicConfigurator;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the <code>ElectoralListLine</code> class.
 */
public final class ElectoralListLineTest {
    /**
     * A sample electoral list line.
     */
    private static final String SAMPLE_LINE = "A: AA202501 •A: AP •EN: Apple Party";
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test ElectoralListLineTest.");

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
        ElectoralListLine electoralListLine = ElectoralListLine.parse(TOKEN, SAMPLE_LINE);
        assertEquals(ElectoralList.get("AA202501"), electoralListLine.getElectoralList());
    }

    /**
     * Verifies that the key is returned.
     */
    @Test
    public void shouldReturnTheKey() {
        ElectoralListLine electoralListLine = ElectoralListLine.parse(TOKEN, SAMPLE_LINE);
        assertEquals("A", electoralListLine.getKey());
    }

    /**
     * Verifies that the abbreviation of an electoral list is updated.
     */
    @Test
    public void shouldUpdateTheAbbreviationOfAnElectoralList() {
        ElectoralListLine electoralListLine = ElectoralListLine.parse(TOKEN, SAMPLE_LINE);
        electoralListLine.updateElectoralList();
        assertEquals("AP", ElectoralList.get("AA202501").getAbbreviation());
    }

    /**
     * Verifies that the romanized abbreviation of an electoral list is updated.
     */
    @Test
    public void shouldUpdateTheRomanizedAbbreviationOfAnElectoralList() {
        ElectoralListLine electoralListLine =
                ElectoralListLine.parse(TOKEN, "A: AA202501 •A: ΑΠ •R:AP •EN: Apple Party");
        electoralListLine.updateElectoralList();
        assertEquals("AP", ElectoralList.get("AA202501").getRomanizedAbbreviation());
    }

    /**
     * Verifies that the names of an electoral list are updated.
     */
    @Test
    public void shouldUpdateTheNamesOfAnElectoralList() {
        ElectoralListLine electoralListLine =
                ElectoralListLine.parse(TOKEN, "A: AA202501 •A: AP •EN: Apple Party •NL: Appelpartij");
        electoralListLine.updateElectoralList();
        assertEquals("Appelpartij", ElectoralList.get("AA202501").getName("NL"));
    }

    /**
     * Verifies that no warning is logged for a valid electoral list line.
     */
    @Test
    public void shouldNotLogAWarningForValidLine() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test ElectoralListLineTest.shouldNotLogAWarningForValidLine.");
        ElectoralListLine.parse(token, "A: AA202501 •A: AP •EN: Apple Party •NL: Appelpartij");
        assertTrue(outputStream.toString().isEmpty());
    }

    /**
     * Verifies that a warning is logged when a non-permanent ID is used.
     */
    @Test
    public void shouldLogAWarningForANonpermanentId() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test ElectoralListLineTest.shouldNotLogAWarningForValidLine.");
        ElectoralListLine.parse(token, "A: AA001 •A: AP •EN: Apple Party •NL: Appelpartij");
        String expected = "‡ ⬐ Unit test ElectoralListLineTest.shouldNotLogAWarningForValidLine.\n"
                + "‡ Electoral list ID AA001 is a non-permanent electoral list ID.\n";
        assertEquals(expected, outputStream.toString());
    }
}
