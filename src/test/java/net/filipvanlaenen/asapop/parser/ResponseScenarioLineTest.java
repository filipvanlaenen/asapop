package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.LaconicConfigurator;
import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResponseScenarioTestBuilder;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the <code>ResponseScenarioLine</code> class.
 */
public final class ResponseScenarioLineTest {
    /**
     * Simple response scenario line.
     */
    private static final String SIMPLE_RESPONSE_SCENARIO_LINE = "& A:55 B:43";
    /**
     * Simple response scenario line, including a result for no responses.
     */
    private static final String RESPONSE_SCENARIO_LINE_WITH_NO_RESPONSES = "& A:55 B:43 •N:2";
    /**
     * Simple response scenario line, including a result for other.
     */
    private static final String RESPONSE_SCENARIO_LINE_WITH_OTHER = "& A:55 B:43 •O:2";
    /**
     * Simple response scenario line with a combined list.
     */
    private static final String RESPONSE_SCENARIO_LINE_WITH_COMBINED_ELECTORAL_LISTS = "& A+C:55 B:43";
    /**
     * A map mapping keys to electoral lists.
     */
    private static final Map<String, ElectoralList> ELECTORAL_LIST_KEY_MAP =
            Map.of("A", ElectoralList.get("A"), "B", ElectoralList.get("B"), "C", ElectoralList.get("C"));
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test ResponseScenarioLineTest.");

    /**
     * Verifies that the <code>isResponseScenarioLine</code> method can detect a response scenario line.
     */
    @Test
    public void isResponseScenarioLineShouldDetectResponseScenarioLine() {
        assertTrue(ResponseScenarioLine.isResponseScenarioLine(SIMPLE_RESPONSE_SCENARIO_LINE));
    }

    /**
     * Verifies that the <code>isResponseScenarioLine</code> method can detect a response scenario line with combined
     * electoral lists.
     */
    @Test
    public void isResponseScenarioLineShouldDetectResponseScenarioLineWithCombinedElectoralLists() {
        assertTrue(ResponseScenarioLine.isResponseScenarioLine(RESPONSE_SCENARIO_LINE_WITH_COMBINED_ELECTORAL_LISTS));
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
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse(SIMPLE_RESPONSE_SCENARIO_LINE, ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        ResponseScenario expected = new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with combined electoral lists can be
     * parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithCombinedElectoralLists() {
        ResponseScenarioLine responseScenarioLine = ResponseScenarioLine
                .parse(RESPONSE_SCENARIO_LINE_WITH_COMBINED_ELECTORAL_LISTS, ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        ResponseScenario expected =
                new ResponseScenarioTestBuilder().addResult("A", "C", "55").addResult("B", "43").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a result for no responses can be
     * parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithAResultForNoResponses() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse(RESPONSE_SCENARIO_LINE_WITH_NO_RESPONSES, ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        ResponseScenario expected =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").setNoResponses("2").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a result for other can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithAResultForOther() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse(RESPONSE_SCENARIO_LINE_WITH_OTHER, ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        ResponseScenario expected =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").setOther("2").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a different area can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithADifferentArea() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •A: AB A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        ResponseScenario expected =
                new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43").setArea("AB").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a different scope can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithADifferentScope() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SC: E A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        ResponseScenario expected = new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43")
                .setScope(Scope.EUROPEAN).build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a different sample size can be
     * parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithADifferentSampleSize() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        ResponseScenario expected = new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43")
                .setSampleSize("999").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a different greater-than-or-equal-to
     * sample size can be parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithADifferentGreaterThanOrEqualToSampleSize() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: ≥999 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        ResponseScenario expected = new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43")
                .setSampleSize("≥999").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a different sample size range can be
     * parsed.
     */
    @Test
    public void shouldParseSingleLineWithAResponseScenarioWithADifferentSampleSizeRange() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 600–700 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        ResponseScenario expected = new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43")
                .setSampleSize("600–700").build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that String with a single line containing a response scenario with a different excluded can be parsed.
     */
    @Test
    public void shouldParseAnOpinionPollWithExcluded() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •EX: 10 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        ResponseScenario expected = new ResponseScenarioTestBuilder().addResult("A", "55").addResult("B", "43")
                .setExcluded(DecimalNumber.parse("10")).build();
        assertEquals(expected, responseScenarioLine.getResponseScenario());
    }

    /**
     * Verifies that a wellformed line doesn't produce a warning while parsing.
     */
    @Test
    public void shouldNotProduceAWarningWhenParsingAWellformedLine() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse(SIMPLE_RESPONSE_SCENARIO_LINE, ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        assertTrue(responseScenarioLine.getWarnings().isEmpty());
    }

    /**
     * Verifies that a line with a malformed result value produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedResultValue() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorForAMalformedResultValue.");
        ResponseScenarioLine.parse("& •SS: 999 A:Error B:43", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorForAMalformedResultValue.\n"
                + "‡ ⬐ Processing result key A.\n" + "‡ Malformed result value Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed no responses value produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedNoResponsesValue() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorForAMalformedNoResponsesValue.");
        ResponseScenarioLine.parse("& •SS: 999 A:55 B:43 •N:Error", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorForAMalformedNoResponsesValue.\n"
                + "‡ ⬐ Processing metadata field N.\n" + "‡ Malformed result value Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed other value produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedOtherValue() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorForAMalformedOtherValue.");
        ResponseScenarioLine.parse("& •SS: 999 A:55 B:43 •O:Error", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorForAMalformedOtherValue.\n"
                + "‡ ⬐ Processing metadata field O.\n" + "‡ Malformed result value Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with a malformed verified sum produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAMalformedVerifiedSum() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorForAMalformedVerifiedSum.");
        ResponseScenarioLine.parse("& •SS: 999 A:55 B:43 •VS:Error", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorForAMalformedVerifiedSum.\n"
                + "‡ ⬐ Processing metadata field VS.\n" + "‡ Malformed decimal number Error.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with an unknown metadata key produces a warning.
     */
    @Test
    public void shouldLogAnErrorForAnUnknownMetadataKey() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorForAnUnknownMetadataKey.");
        ResponseScenarioLine.parse("& •SS: 999 •XX: X A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorForAnUnknownMetadataKey.\n"
                + "‡ ⬐ Processing metadata field XX.\n" + "‡ Unknown metadata key XX.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with an unknown scope produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownScopeValue() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 •SC: X A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new UnknownMetadataValueWarning(1, "scope", "X"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed decimal number for excluded logs an error.
     */
    @Test
    public void shouldLogAnErrorForAMalformedDecimalNumberForExcludedResponses() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER.logMessage(
                "Unit test ResponseScenarioLineTest.shouldLogAnErrorForAMalformedDecimalNumberForExcludedResponses.");
        ResponseScenarioLine.parse("& •SS: 999 •EX: X A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected =
                "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorForAMalformedDecimalNumberForExcludedResponses.\n"
                        + "‡ ⬐ Processing metadata field EX.\n" + "‡ Malformed decimal number X.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line missing results produces a warning.
     */
    @Test
    public void shouldLogAnErrorForMissingResults() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorForMissingResults.");
        ResponseScenarioLine.parse("& •SS: 999 •SC: N", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected =
                "‡ ⬐ Unit test ResponseScenarioLineTest.shouldLogAnErrorForMissingResults.\n" + "‡ No results found.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line with an unknown electoral list key produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownElectoralListKey() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 •SC: N A:55 B:43 XX:2", ELECTORAL_LIST_KEY_MAP, 1, TOKEN);
        Set<ParserWarning> expected = Set.of(new UnknownElectoralListKeyWarning(1, "XX"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line adding the area twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenAreaIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenAreaIsAddedTwice.");
        ResponseScenarioLine.parse("& •A: A •A: A A:55 B:45", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenAreaIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field A.\n" + "‡ Single value metadata key A occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding no responses twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenNoResponsesIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenNoResponsesIsAddedTwice.");
        ResponseScenarioLine.parse("&  •N: 12 •N: 12 A:53 B:35", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenNoResponsesIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field N.\n" + "‡ Single value metadata key N occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding other twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenOtherIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenOtherIsAddedTwice.");
        ResponseScenarioLine.parse("& •O: 12 •O: 12 A:53 B:35", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenOtherIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field O.\n" + "‡ Single value metadata key O occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding verified sum twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenVerifiedSumIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenVerifiedSumIsAddedTwice.");
        ResponseScenarioLine.parse("& •VS: 88 •VS: 88 A:53 B:35", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenVerifiedSumIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field VS.\n" + "‡ Single value metadata key VS occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding scope twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenScopeIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token =
                Laconic.LOGGER.logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenScopeIsAddedTwice.");
        ResponseScenarioLine.parse("& •SC: N •SC: N A:55 B:45", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenScopeIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field SC.\n" + "‡ Single value metadata key SC occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that a line adding sample size twice produces a warning.
     */
    @Test
    public void shouldLogAnErrorWhenSampleSizeIsAddedTwice() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token = Laconic.LOGGER
                .logMessage("Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenSampleSizeIsAddedTwice.");
        ResponseScenarioLine.parse("& •SS: 1000 •SS: 1000 A:55 B:45", ELECTORAL_LIST_KEY_MAP, 1, token);
        String expected = "‡   Unit test ResponseScenarioLineTest.shouldLogAnErrorWhenSampleSizeIsAddedTwice.\n"
                + "‡ ⬐ Processing metadata field SS.\n" + "‡ Single value metadata key SS occurred more than once.\n";
        assertEquals(expected, outputStream.toString());
    }
}
