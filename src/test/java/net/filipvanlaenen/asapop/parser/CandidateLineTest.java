package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.LaconicConfigurator;
import net.filipvanlaenen.asapop.model.Candidate;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the <code>CandidateLine</code> class.
 */
public final class CandidateLineTest {
    /**
     * A sample candidate line.
     */
    private static final String SAMPLE_LINE = "DOE: AA2025A •A: DOE •N: John Doe";
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test CandidateLineTest.");

    /**
     * Verifies that the <code>isCandidateLine</code> method can detect a line with a candidate.
     */
    @Test
    public void isCandidateLineShouldDetectCandidateLine() {
        assertTrue(CandidateLine.isCandidateLine(SAMPLE_LINE));
    }

    /**
     * Verifies that the <code>isCandidateLine</code> method rejects a line with a candidate with a digit in the key.
     */
    @Test
    public void isCandidateLineShouldRejectCandidateLineWithDigitInTheKey() {
        assertFalse(CandidateLine.isCandidateLine("DOE1: AA2025A •A: DOE1 •N: John Doe"));
    }

    /**
     * Verifies that the <code>isCandidateLine</code> method can detect a line that doesn't match a definition for a
     * candidate.
     */
    @Test
    public void isCandidateLineShouldDetectNonCandidateLine() {
        assertFalse(CandidateLine.isCandidateLine("Foo"));
    }

    /**
     * Verifies that the candidate is returned.
     */
    @Test
    public void shouldReturnTheCandidate() {
        CandidateLine candidateLine = CandidateLine.parse(TOKEN, SAMPLE_LINE);
        assertEquals(Candidate.get("AA2025A"), candidateLine.getCandidate());
    }

    /**
     * Verifies that the key is returned.
     */
    @Test
    public void shouldReturnTheKey() {
        CandidateLine candidateLine = CandidateLine.parse(TOKEN, SAMPLE_LINE);
        assertEquals("DOE", candidateLine.getKey());
    }

    /**
     * Verifies that the abbreviation of a candidate is updated.
     */
    @Test
    public void shouldUpdateTheAbbreviationOfACandidate() {
        CandidateLine candidateLine = CandidateLine.parse(TOKEN, "DOE: AA2020A •A: DOE •N: John Doe");
        candidateLine.updateCandidate();
        assertEquals("DOE", Candidate.get("AA2020A").getAbbreviation());
    }

    /**
     * Verifies that the name of a candidate is updated.
     */
    @Test
    public void shouldUpdateTheNameOfACandidate() {
        String name = "John Doe " + Instant.now().toEpochMilli();
        CandidateLine candidateLine = CandidateLine.parse(TOKEN, "DOE: AA2020N •A: DOE •N: " + name);
        candidateLine.updateCandidate();
        assertEquals(name, Candidate.get("AA2020N").getName());
    }

    /**
     * Verifies that the romanized name of a candidate is updated.
     */
    @Test
    public void shouldUpdateTheRomanizedNameOfACandidate() {
        CandidateLine candidateLine =
                CandidateLine.parse(TOKEN, "PUPKIN: AA2020R •A: PUPKIN •N: Вася Пупкин •R: Vasya Pupkin");
        candidateLine.updateCandidate();
        assertEquals("Vasya Pupkin", Candidate.get("AA2020R").getRomanizedName());
    }

    /**
     * Verifies that no warning is logged for a valid candidate line.
     */
    @Test
    public void shouldNotLogAWarningForValidLine() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test CandidateLineTest.shouldNotLogAWarningForValidLine.");
        CandidateLine.parse(token, SAMPLE_LINE);
        assertTrue(outputStream.toString().isEmpty());
    }

    /**
     * Verifies that a warning is logged when a non-permanent ID is used.
     */
    @Test
    public void shouldLogAWarningForAnUnknownMetadataKey() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test CandidateLineTest.shouldLogAWarningForAnUnknownMetadataKey.");
        CandidateLine.parse(token, "DOE: AA2025A •A: DOE •N: John Doe •X: Foo");
        String expected = "‡ ⬐ Unit test CandidateLineTest.shouldLogAWarningForAnUnknownMetadataKey.\n"
                + "‡ Unknown metadata key X.\n";
        assertEquals(expected, outputStream.toString());
    }
}
