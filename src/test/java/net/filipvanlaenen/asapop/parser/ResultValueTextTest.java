package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.LaconicConfigurator;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the <code>ResultValueText</code> class.
 */
public class ResultValueTextTest {
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test ResultValueTextTest.");

    /**
     * Verifies that an integer is parsed into a result value.
     */
    @Test
    public void shouldParseAnIntegerIntoAResultValue() {
        ResultValueText result = ResultValueText.parse("1", TOKEN);
        assertEquals(new ResultValue("1"), result.getValue());
    }

    /**
     * Verifies that a decimal is parsed into a result value.
     */
    @Test
    public void shouldParseADecimalIntoAResultValue() {
        ResultValueText result = ResultValueText.parse("0.1", TOKEN);
        assertEquals(new ResultValue("0.1"), result.getValue());
    }

    /**
     * Verifies that less than a decimal is parsed into a result value.
     */
    @Test
    public void shouldParseLessThanADecimalIntoAResultValue() {
        ResultValueText result = ResultValueText.parse("<0.1", TOKEN);
        assertEquals(new ResultValue("<0.1"), result.getValue());
    }

    /**
     * Verifies that when an integer is provided, no warnings are produced.
     */
    @Test
    public void parsingAnIntegerShouldNotLogAnError() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test ResultValueTextTest.parsingAnIntegerShouldNotLogAnError.");
        ResultValueText.parse("1", token);
        assertTrue(outputStream.toString().isEmpty());
    }

    /**
     * Verifies that when a decimal is provided, no warnings are produced.
     */
    @Test
    public void parsingADecimalShouldNotLogAnError() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test ResultValueTextTest.parsingADecimalShouldNotLogAnError.");
        ResultValueText.parse("0.1", token);
        assertTrue(outputStream.toString().isEmpty());
    }

    /**
     * Verifies that when less than a decimal is provided, no warnings are produced.
     */
    @Test
    public void parsingLessThanADecimalShouldNotLogAnError() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test ResultValueTextTest.parsingLessThanADecimalShouldNotLogAnError.");
        ResultValueText.parse("<0.1", token);
        assertTrue(outputStream.toString().isEmpty());
    }

    /**
     * Verifies that when a letter is provided, a malformed result value warning is produced.
     */
    @Test
    public void parsingALetterShouldLogAnError() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage("Unit test ResultValueTextTest.parsingALetterShouldLogAnError.");
        ResultValueText.parse("a", token);
        String expected =
                "‡ ⬐ Unit test ResultValueTextTest.parsingALetterShouldLogAnError.\n" + "‡ Malformed result value a.\n";
        assertEquals(expected, outputStream.toString());
    }
}
