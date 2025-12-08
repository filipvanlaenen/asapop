package net.filipvanlaenen.asapop.parser;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.model.Candidate;
import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.SampleSize;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.asapop.model.Unit;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Class implementing a line representing a response alternative.
 */
final class ResponseScenarioLine extends Line {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;
    /**
     * The integer number four.
     */
    private static final int FOUR = 4;
    /**
     * The string for the pattern to match the marker for a response scenario line.
     */
    private static final String RESPONSE_SCENARIO_MARKER_PATTERN = "&";
    /**
     * The pattern to match a response scenario.
     */
    private static final Pattern RESPONSE_SCENARIO_PATTERN = Pattern.compile("^\\s*" + RESPONSE_SCENARIO_MARKER_PATTERN
            + "\\s*(" + KEY_VALUE_PATTERN + "(\\s+" + KEY_VALUE_PATTERN + ")*)$");

    /**
     * The response scenario represented by the line.
     */
    private final ResponseScenario responseScenario;

    /**
     * Private constructor taking the response scenario as its parameter.
     *
     * @param responseScenario The response scenario represented by the line.
     */
    private ResponseScenarioLine(final ResponseScenario responseScenario) {
        this.responseScenario = responseScenario;
    }

    /**
     * Returns the response scenario represented by this line.
     *
     * @return The response scenario represented by this line.
     */
    ResponseScenario getResponseScenario() {
        return responseScenario;
    }

    /**
     * Verifies whether a line is a response scenario line.
     *
     * @param line The line to check against the pattern of a response scenario line.
     * @return True if the line matches the pattern of a response scenario line, false otherwise.
     */
    static boolean isResponseScenarioLine(final String line) {
        return textMatchesPattern(RESPONSE_SCENARIO_PATTERN, line);
    }

    /**
     * Parses a response scenario line.
     *
     * @param line                The line to parse a response scenario from.
     * @param electoralListKeyMap The map mapping keys to electoral lists.
     * @param token               The Laconic logging token.
     * @return A ResponseScenarioLine instance representing the line.
     */
    static ResponseScenarioLine parse(final String line, final Map<String, ElectoralList> electoralListKeyMap,
            final Map<String, Candidate> candidateKeyMap, final Token token) {
        ResponseScenario.Builder builder = new ResponseScenario.Builder();
        Matcher responseScenarioMatcher = RESPONSE_SCENARIO_PATTERN.matcher(line);
        responseScenarioMatcher.find();
        String remainder = responseScenarioMatcher.group(1);
        while (!remainder.isEmpty()) {
            remainder = parseKeyValue(builder, remainder, electoralListKeyMap, candidateKeyMap, token);
        }
        if (!builder.hasResults()) {
            Laconic.LOGGER.logError("No results found.", token);
        }
        Token sumToken = Laconic.LOGGER.logMessage(token, "Total sum is %f.", builder.getSum());
        if (!builder.resultsAddUp()) {
            Laconic.LOGGER.logError("Results don’t add up within rounding error interval.", sumToken);
        }
        if (builder.hasOtherAndNoResponses() && (builder.hasOther() || builder.hasNoResponses())) {
            Laconic.LOGGER.logError(
                    "Other and no responses (ON) shouldn’t be combined with other (O) and/or no responses (N).", token);
        }
        return new ResponseScenarioLine(builder.build());
    }

    /**
     * Processes a key and value from a part of a response scenario line.
     *
     * @param builder             The response scenario builder to build on.
     * @param remainder           The remainder of a line to parse a key and value from.
     * @param electoralListKeyMap The map mapping keys to electoral lists.
     * @param token               The Laconic logging token.
     * @return The unprocessed part of the line.
     */
    private static String parseKeyValue(final ResponseScenario.Builder builder, final String remainder,
            final Map<String, ElectoralList> electoralListKeyMap, final Map<String, Candidate> candidateKeyMap,
            final Token token) {
        Matcher keyValuesMatcher = KEY_VALUES_PATTERN.matcher(remainder);
        keyValuesMatcher.find();
        String keyValueBlock = keyValuesMatcher.group(1);
        if (keyValueBlock.startsWith(METADATA_MARKER_PATTERN)) {
            processMetadata(builder, keyValueBlock, token);
        } else {
            processResultData(builder, keyValueBlock, electoralListKeyMap, candidateKeyMap, token);
        }
        return keyValuesMatcher.group(FOUR);
    }

    /**
     * Processes a data block with metadata for a response scenario.
     *
     * @param builder        The response scenario builder to build on.
     * @param keyValueString The data block to process.
     * @param token          The Laconic logging token.
     */
    private static void processMetadata(final ResponseScenario.Builder builder, final String keyValueString,
            final Token token) {
        Matcher keyValueMatcher = METADATA_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String key = keyValueMatcher.group(1);
        String value = keyValueMatcher.group(2);
        Token keyToken = Laconic.LOGGER.logMessage(token, "Processing metadata field %s.", key);
        switch (key) {
        case "A":
            if (builder.hasArea()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                builder.setArea(value);
            }
            break;
        case "EX":
            if (builder.hasExcluded()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else if (textMatchesPattern(DECIMAL_NUMBER_PATTERN, value)) {
                DecimalNumber excluded = DecimalNumber.parse(value);
                builder.setExcluded(excluded);
            } else {
                Laconic.LOGGER.logError("Malformed decimal number %s.", value, keyToken);
            }
            break;
        case "N":
            if (builder.hasNoResponses()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                ResultValueText noResponse = ResultValueText.parse(value, keyToken);
                builder.setNoResponses(noResponse.getValue());
            }
            break;
        case "O":
            if (builder.hasOther()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                ResultValueText other = ResultValueText.parse(value, keyToken);
                builder.setOther(other.getValue());
            }
            break;
        case "ON":
            if (builder.hasOtherAndNoResponses()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                ResultValueText otherAndNoResponses = ResultValueText.parse(value, keyToken);
                builder.setOtherAndNoResponses(otherAndNoResponses.getValue());
            }
            break;
        case "SC":
            if (builder.hasScope()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                Scope scope = Scope.parse(value);
                if (scope == null) {
                    Laconic.LOGGER.logError("Unknown metadata value %s.", value, keyToken);
                } else {
                    builder.setScope(scope);
                }
            }
            break;
        case "SS":
            if (builder.hasSampleSize()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else if (textMatchesPattern(SAMPLE_SIZE_PATTERN, value)) {
                SampleSize sampleSize = SampleSize.parse(value);
                builder.setSampleSize(sampleSize);
            } else {
                Laconic.LOGGER.logError("Malformed sample size %s.", value, keyToken);
            }
            break;
        case "U":
            if (builder.hasUnit()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                Unit unit = Unit.parse(value);
                if (unit == null) {
                    Laconic.LOGGER.logError("Unknown metadata value %s.", value, keyToken);
                } else {
                    builder.setUnit(unit);
                }
            }
            break;
        case "VS":
            if (builder.hasVerifiedSum()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                try {
                    builder.setVerifiedSum(DecimalNumber.parse(value));
                } catch (NumberFormatException nfe) {
                    Laconic.LOGGER.logError("Malformed decimal number %s.", value, keyToken);
                }
            }
            break;
        default:
            Laconic.LOGGER.logError("Unknown metadata key %s.", key, keyToken);
        }
    }

    /**
     * Processes a data block with results for a response scenario.
     *
     * @param builder             The response scenario builder to build on.
     * @param keyValueString      The data block to process.
     * @param electoralListKeyMap The map mapping keys to electoral lists.
     * @param token               The Laconic logging token.
     */
    private static void processResultData(final ResponseScenario.Builder builder, final String keyValueString,
            final Map<String, ElectoralList> electoralListKeyMap, final Map<String, Candidate> candidateKeyMap,
            final Token token) {
        Matcher keyValueMatcher = RESULT_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String keysValue = keyValueMatcher.group(1);
        Token keysToken = Laconic.LOGGER.logMessage(token, "Processing result key %s.", keysValue);
        ResultValueText value = ResultValueText.parse(keyValueMatcher.group(THREE), keysToken);
        if (candidateKeyMap.containsKey(keysValue)) {
            builder.addResult(candidateKeyMap.get(keysValue), value.getValue());
        } else {
            Collection<String> keys = Collection.of(keysValue.split(ELECTORAL_LIST_KEY_SEPARATOR));
            Set<ElectoralList> electoralLists =
                    keys.stream().map(key -> electoralListKeyMap.get(key)).collect(Collectors.toSet());
            for (String key : keys) {
                if (!electoralListKeyMap.containsKey(key)) {
                    Laconic.LOGGER.logError("Unknown electoral list key %s.", key, keysToken);
                }
            }
            builder.addResult(electoralLists, value.getValue());
        }
    }
}
