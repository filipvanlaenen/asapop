package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Set;
import net.filipvanlaenen.asapop.model.ResponseScenario;

/**
 * Unit tests on the <code>ResponseScenarioLine</code> class.
 */
public final class ResponseScenarioLineTest {
    /**
     * Simple response scenario line.
     */
    private static final String SIMPLE_RESPONSE_SCENARIO_LINE = "& A:55 B:43";
    /**
     * Simple response scenario line, including a result for other.
     */
    private static final String RESPONSE_SCENARIO_LINE_WITH_OTHER = "& A:55 B:43 •O:2";

    /**
     * Verifies that the <code>isResponseScenarioLine</code> method can detect a response scenario line.
     */
    @Test
    public void isResponseScenarioLineShouldDetectResponseScenarioLine() {
        assertTrue(ResponseScenarioLine.isResponseScenarioLine(SIMPLE_RESPONSE_SCENARIO_LINE));
    }

    /**
     * Verifies that the <code>isResponseScenarioLine</code> method can detect a response scenario line including
     * metadata.
     */
    @Test
    public void isResponseScenarioLineShouldDetectResponseScenarioLineWithMetadata() {
        assertTrue(ResponseScenarioLine.isResponseScenarioLine(RESPONSE_SCENARIO_LINE_WITH_OTHER));
    }

    /**
     * Verifies that the <code>isResponseScenarioLine</code> method can detect a line that isn't a response scenario
     * line.
     */
    @Test
    public void isResponseScenarioLineShouldDetectNonResponseScenarioLine() {
        assertFalse(ResponseScenarioLine.isResponseScenarioLine("Foo"));
    }

    /**
     * Verifies that String with a single line containing a simple response scenario can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithASimpleResponseScenario() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse(SIMPLE_RESPONSE_SCENARIO_LINE, 1);
        ResponseScenario expected = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "43").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a result for other can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithAResultForOther() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse(RESPONSE_SCENARIO_LINE_WITH_OTHER, 1);
        ResponseScenario expected = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "43").setWellformedOther("2").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a different area can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithADifferentArea() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse("& •A: AB A:55 B:43", 1);
        ResponseScenario expected = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "43").setArea("AB").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a different scope can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithADifferentScope() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse("& •SC: E A:55 B:43", 1);
        ResponseScenario expected = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "43").setScope("E").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a different sample size can be
     * parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithADifferentSampleSize() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse("& •SS: 999 A:55 B:43", 1);
        ResponseScenario expected = new ResponseScenario.Builder().addWellformedResult("A", "55")
                .addWellformedResult("B", "43").setSampleSize("999").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that a wellformed line doesn't produce a warning while parsing.
     */
    @Test
    public void shouldNotProduceAWarningWhenParsingAWellformedLine() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse(SIMPLE_RESPONSE_SCENARIO_LINE, 1);
        assertTrue(responseScenarioLine.getWarnings().isEmpty());
    }

    /**
     * Verifies that a line with a malformed result value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedResultValue() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse("& •SS: 999 A:Error B:43", 1);
        Set<Warning> expected = Set.of(new MalformedResultValueWarning(1, "Error"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed other value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedOtherValue() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse("& •SS: 999 A:55 B:43 •O:Error", 1);
        Set<Warning> expected = Set.of(new MalformedResultValueWarning(1, "Error"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line with an unknown metadata key produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownMetadataKey() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse("& •SS: 999 •XX: X A:55 B:43", 1);
        Set<Warning> expected = Set.of(new UnknownMetadataKeyWarning(1, "XX"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

}
