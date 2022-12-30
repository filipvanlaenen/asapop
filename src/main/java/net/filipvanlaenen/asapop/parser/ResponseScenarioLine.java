package net.filipvanlaenen.asapop.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.Scope;

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
     * The warnings.
     */
    private final Set<ParserWarning> warnings;

    /**
     * Private constructor taking the response scenario as its parameter.
     *
     * @param responseScenario The response scenario represented by the line.
     * @param warnings         The warnings related to this line.
     */
    private ResponseScenarioLine(final ResponseScenario responseScenario, final Set<ParserWarning> warnings) {
        this.responseScenario = responseScenario;
        this.warnings = warnings;
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
     * Returns the warnings.
     *
     * @return The warnings.
     */
    Set<ParserWarning> getWarnings() {
        return Collections.unmodifiableSet(warnings);
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
     * @param line       The line to parse a response scenario from.
     * @param lineNumber The line number the data block.
     * @return A ResponseScenarioLine instance representing the line.
     */
    static ResponseScenarioLine parse(final String line, final int lineNumber) {
        ResponseScenario.Builder builder = new ResponseScenario.Builder();
        Set<ParserWarning> warnings = new HashSet<ParserWarning>();
        Matcher responseScenarioMatcher = RESPONSE_SCENARIO_PATTERN.matcher(line);
        responseScenarioMatcher.find();
        String remainder = responseScenarioMatcher.group(1);
        while (!remainder.isEmpty()) {
            remainder = parseKeyValue(builder, warnings, remainder, lineNumber);
        }
        if (!builder.hasResults()) {
            warnings.add(new ResultsMissingWarning(lineNumber));
        }
        return new ResponseScenarioLine(builder.build(), warnings);
    }

    /**
     * Processes a key and value from a part of a response scenario line.
     *
     * @param builder    The response scenario builder to build on.
     * @param warnings   The set to add any warnings too.
     * @param remainder  The remainder of a line to parse a key and value from.
     * @param lineNumber The line number the data block.
     * @return The unprocessed part of the line.
     */
    private static String parseKeyValue(final ResponseScenario.Builder builder, final Set<ParserWarning> warnings,
            final String remainder, final int lineNumber) {
        Matcher keyValuesMatcher = KEY_VALUES_PATTERN.matcher(remainder);
        keyValuesMatcher.find();
        String keyValueBlock = keyValuesMatcher.group(1);
        if (keyValueBlock.startsWith(METADATA_MARKER_PATTERN)) {
            processMetadata(builder, warnings, keyValueBlock, lineNumber);
        } else {
            processResultData(builder, warnings, keyValueBlock, lineNumber);
        }
        return keyValuesMatcher.group(FOUR);
    }

    /**
     * Processes a data block with metadata for a response scenario.
     *
     * @param builder        The response scenario builder to build on.
     * @param warnings       The set to add any warnings too.
     * @param keyValueString The data block to process.
     * @param lineNumber     The line number the data block.
     */
    private static void processMetadata(final ResponseScenario.Builder builder, final Set<ParserWarning> warnings,
            final String keyValueString, final int lineNumber) {
        Matcher keyValueMatcher = METADATA_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String key = keyValueMatcher.group(1);
        String value = keyValueMatcher.group(2);
        switch (key) {
        case "A":
            if (builder.hasArea()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                builder.setArea(value);
            }
            break;
        case "N":
            if (builder.hasNoResponses()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                ResultValueText noResponse = ResultValueText.parse(value, lineNumber);
                warnings.addAll(noResponse.getWarnings());
                builder.setNoResponses(noResponse.getValue());
            }
            break;
        case "O":
            if (builder.hasOther()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                ResultValueText other = ResultValueText.parse(value, lineNumber);
                warnings.addAll(other.getWarnings());
                builder.setOther(other.getValue());
            }
            break;
        case "SC":
            if (builder.hasScope()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                Scope scope = parseScope(value);
                if (scope == null) {
                    warnings.add(new UnknownScopeValueWarning(lineNumber, value));
                } else {
                    builder.setScope(scope);
                }
            }
            break;
        case "SS":
            if (builder.hasSampleSize()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                builder.setSampleSize(value);
            }
            break;
        default:
            warnings.add(new UnknownMetadataKeyWarning(lineNumber, key));
        }
    }

    /**
     * Processes a data block with results for a response scenario.
     *
     * @param builder        The response scenario builder to build on.
     * @param warnings       The set to add any warnings too.
     * @param keyValueString The data block to process.
     * @param lineNumber     The line number the data block.
     */
    private static void processResultData(final ResponseScenario.Builder builder, final Set<ParserWarning> warnings,
            final String keyValueString, final int lineNumber) {
        Matcher keyValueMatcher = RESULT_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String keysValue = keyValueMatcher.group(1);
        Set<String> keys = new HashSet<String>(Arrays.asList(keysValue.split(ELECTORAL_LIST_KEY_SEPARATOR)));
        ResultValueText value = ResultValueText.parse(keyValueMatcher.group(THREE), lineNumber);
        warnings.addAll(value.getWarnings());
        builder.addResult(keys, value.getValue());
    }
}
