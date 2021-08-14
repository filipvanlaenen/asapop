package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse(SIMPLE_RESPONSE_SCENARIO_LINE);
        ResponseScenario expected = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a result for other can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithAResultForOther() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse(RESPONSE_SCENARIO_LINE_WITH_OTHER);
        ResponseScenario expected = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                  .setOther("2").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a different scope can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithADifferentScope() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse("& •SC: E A:55 B:43");
        ResponseScenario expected = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                  .setScope("E").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }
}
