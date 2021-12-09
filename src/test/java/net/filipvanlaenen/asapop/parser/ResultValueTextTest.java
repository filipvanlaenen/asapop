package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ResultValue;

/**
 * Unit tests on the <code>ResultValueText</code> class.
 */
public class ResultValueTextTest {
    /**
     * Verifies that an integer is parsed into a result value.
     */
    @Test
    public void shouldParseAnIntegerIntoAResultValue() {
        ResultValueText result = ResultValueText.parse("1", 1);
        assertEquals(new ResultValue("1"), result.getValue());
    }

    /**
     * Verifies that a decimal is parsed into a result value.
     */
    @Test
    public void shouldParseADecimalIntoAResultValue() {
        ResultValueText result = ResultValueText.parse("0.1", 1);
        assertEquals(new ResultValue("0.1"), result.getValue());
    }

    /**
     * Verifies that less than a decimal is parsed into a result value.
     */
    @Test
    public void shouldParseLessThanADecimalIntoAResultValue() {
        ResultValueText result = ResultValueText.parse("<0.1", 1);
        assertEquals(new ResultValue("<0.1"), result.getValue());
    }

    /**
     * Verifies that when an integer is provided, no warnings are produced.
     */
    @Test
    public void parsingAnIntegerShouldNotProduceWarnings() {
        ResultValueText result = ResultValueText.parse("1", 1);
        assertTrue(result.getWarnings().isEmpty());
    }

    /**
     * Verifies that when a decimal is provided, no warnings are produced.
     */
    @Test
    public void parsingADecimalShouldNotProduceWarnings() {
        ResultValueText result = ResultValueText.parse("0.1", 1);
        assertTrue(result.getWarnings().isEmpty());
    }

    /**
     * Verifies that when less than a decimal is provided, no warnings are produced.
     */
    @Test
    public void parsingLessThanADecimalShouldNotProduceWarnings() {
        ResultValueText result = ResultValueText.parse("<0.1", 1);
        assertTrue(result.getWarnings().isEmpty());
    }

    /**
     * Verifies that when a letter is provided, a malformed result value warning is produced.
     */
    @Test
    public void parsingALetterShouldProduceAMalformedResultValueWarning() {
        ResultValueText result = ResultValueText.parse("a", 1);
        assertEquals(Set.of(new MalformedResultValueWarning(1, "a")), result.getWarnings());
    }
}
