package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ResponseScenarioLine</code> class.
 */
public final class ResponseScenarioLineTest {
    /**
     * Simple response scenario line.
     */
    private static final String SIMPLE_RESPONSE_SCENARIO_LINE = "& A:55 B:45 •O:2";

    /**
     * Verifies that the <code>isResponseScenarioLine</code> method can detect a response scenario line.
     */
    @Test
    public void isResponseScenarioLineShouldDetectResponseScenarioLine() {
        assertTrue(ResponseScenarioLine.isResponseScenarioLine(SIMPLE_RESPONSE_SCENARIO_LINE));
    }

    /**
     * Verifies that the <code>isResponseScenarioLine</code> method can detect a line that isn't a response scenario
     * line.
     */
    @Test
    public void isResponseScenarioLineShouldDetectNonResponseScenarioLine() {
        assertFalse(ResponseScenarioLine.isResponseScenarioLine("Foo"));
    }
}
