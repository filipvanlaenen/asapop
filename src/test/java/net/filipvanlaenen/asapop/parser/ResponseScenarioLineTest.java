package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResponseScenarioTestBuilder;
import net.filipvanlaenen.asapop.model.Scope;

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
                ResponseScenarioLine.parse(SIMPLE_RESPONSE_SCENARIO_LINE, ELECTORAL_LIST_KEY_MAP, 1);
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
                .parse(RESPONSE_SCENARIO_LINE_WITH_COMBINED_ELECTORAL_LISTS, ELECTORAL_LIST_KEY_MAP, 1);
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
                ResponseScenarioLine.parse(RESPONSE_SCENARIO_LINE_WITH_NO_RESPONSES, ELECTORAL_LIST_KEY_MAP, 1);
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
                ResponseScenarioLine.parse(RESPONSE_SCENARIO_LINE_WITH_OTHER, ELECTORAL_LIST_KEY_MAP, 1);
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
                ResponseScenarioLine.parse("& •A: AB A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1);
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
                ResponseScenarioLine.parse("& •SC: E A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1);
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
                ResponseScenarioLine.parse("& •SS: 999 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1);
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
                ResponseScenarioLine.parse("& •SS: ≥999 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1);
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
                ResponseScenarioLine.parse("& •SS: 600–700 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1);
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
                ResponseScenarioLine.parse("& •EX: 10 A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1);
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
                ResponseScenarioLine.parse(SIMPLE_RESPONSE_SCENARIO_LINE, ELECTORAL_LIST_KEY_MAP, 1);
        assertTrue(responseScenarioLine.getWarnings().isEmpty());
    }

    /**
     * Verifies that a line with a malformed result value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedResultValue() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 A:Error B:43", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new MalformedResultValueWarning(1, "Error"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed no responses value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedNoResponsesValue() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 A:55 B:43 •N:Error", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new MalformedResultValueWarning(1, "Error"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed other value produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedOtherValue() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 A:55 B:43 •O:Error", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new MalformedResultValueWarning(1, "Error"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed verified sum produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedVerifiedSum() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 A:55 B:43 •VS:Error", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new MalformedVerifiedSumWarning(1, "Error"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line with an unknown metadata key produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownMetadataKey() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 •XX: X A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new UnknownMetadataKeyWarning(1, "XX"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line with an unknown scope produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownScopeValue() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 •SC: X A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new UnknownScopeValueWarning(1, "X"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line with a malformed decimal number for excluded produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAMalformedDecimalNumberForExcludedResponses() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 •EX: X A:55 B:43", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new MalformedDecimalNumberWarning(1, "EX", "X"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line missing results produces a warning.
     */
    @Test
    public void shouldProduceAWarningForMissingResults() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 •SC: N", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new ResultsMissingWarning(1));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line with an unknown electoral list key produces a warning.
     */
    @Test
    public void shouldProduceAWarningForAnUnknownElectoralListKey() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 999 •SC: N A:55 B:43 XX:2", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new UnknownElectoralListKeyWarning(1, "XX"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line adding the area twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenAreaIsAddedTwice() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •A: A •A: A A:55 B:45", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "A"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line adding no responses twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenNoResponsesIsAddedTwice() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("&  •N: 12 •N: 12 A:53 B:35", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "N"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line adding other twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenOtherIsAddedTwice() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •O: 12 •O: 12 A:53 B:35", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "O"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line adding verified sum twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenVerifiedSumIsAddedTwice() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •VS: 88 •VS: 88 A:53 B:35", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "VS"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line adding scope twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenScopeIsAddedTwice() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SC: N •SC: N A:55 B:45", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "SC"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }

    /**
     * Verifies that a line adding sample size twice produces a warning.
     */
    @Test
    public void shouldProduceAWarningWhenSampleSizeIsAddedTwice() {
        ResponseScenarioLine responseScenarioLine =
                ResponseScenarioLine.parse("& •SS: 1000 •SS: 1000 A:55 B:45", ELECTORAL_LIST_KEY_MAP, 1);
        Set<ParserWarning> expected = Set.of(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(1, "SS"));
        assertEquals(expected, responseScenarioLine.getWarnings());
    }
}
