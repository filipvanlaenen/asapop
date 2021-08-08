package net.filipvanlaenen.asapop.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.ResponseScenario;

final class ResponseScenarioLine extends Line {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;
    /**
     * The string for the pattern to match the marker for a response scenario line.
     */
    private static final String RESPONSE_SCENARIO_MARKER_PATTERN = "&";
    /**
     * The pattern to match a response scenario.
     */
    private static final Pattern RESPONSE_SCENARIO_PATTERN = Pattern.compile("^\\s*" + RESPONSE_SCENARIO_MARKER_PATTERN
                                                                                     + "\\s*("
                                                                                     + KEY_VALUE_PATTERN
                                                                                     + "(\\s+"
                                                                                     + KEY_VALUE_PATTERN
                                                                                     + ")*)$");

    static boolean isResponseScenarioLine(final String line) {
        return lineMatchesPattern(RESPONSE_SCENARIO_PATTERN, line);
    }

    /**
     * Parses a response scenario line.
     *
     * @param line The line to parse a response scenario from.
     * @return The response scenario parsed from the line.
     */
    static ResponseScenario parse(final String line) {
        ResponseScenario.Builder builder = new ResponseScenario.Builder();
        Matcher responseScenarioMatcher = RESPONSE_SCENARIO_PATTERN.matcher(line);
        responseScenarioMatcher.find();
        String remainder = responseScenarioMatcher.group(1);
        while (!remainder.isEmpty()) {
            remainder = parseKeyValue(builder, remainder);
        }
        return builder.build();
    }

    /**
     * Processes a key and value from a part of a response scenario line.
     *
     * @param builder The response scenario builder to build on.
     * @param remainder The remainder of a line to parse a key and value from.
     * @return The unprocessed part of the line.
     */
    private static String parseKeyValue(final ResponseScenario.Builder builder, final String remainder) {
        Matcher keyValuesMatcher = KEY_VALUES_PATTERN.matcher(remainder);
        keyValuesMatcher.find();
        String keyValueBlock = keyValuesMatcher.group(1);
        if (keyValueBlock.startsWith(METADATA_MARKER_PATTERN)) {
            processMetadata(builder, keyValueBlock);
        } else {
            processResultData(builder, keyValueBlock);
        }
        return keyValuesMatcher.group(THREE);
    }

    /**
     * Processes a data block with metadata for a response scenario.
     *
     * @param builder The response scenario builder to build on.
     * @param keyValueString The data block to process.
     */
    private static void processMetadata(final ResponseScenario.Builder builder, final String keyValueString) {
        Matcher keyValueMatcher = METADATA_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String key = keyValueMatcher.group(1);
        String value = keyValueMatcher.group(2);
        switch (key) {
            case "O": builder.setOther(value);
                break;
            case "SC": builder.setScope(value);
                break;
            // The default case should be handled as part of issue #9.
        }
    }

    /**
     * Processes a data block with results for a response scenario.
     *
     * @param builder The response scenario builder to build on.
     * @param keyValueString The data block to process.
     */
    private static void processResultData(final ResponseScenario.Builder builder, final String keyValueString) {
        Matcher keyValueMatcher = RESULT_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String key = keyValueMatcher.group(1);
        String value = keyValueMatcher.group(2);
        builder.addResult(key, value);
    }
}
