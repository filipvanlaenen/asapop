package net.filipvanlaenen.asapop.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;

/**
 * Class implementing an ROPF file.
 */
public final class RichOpinionPollsFile {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;
    /**
     * The string for the pattern to match a metadata marker.
     */
    private static final String METADATA_MARKER_PATTERN = "•";
    /**
     * The string for the pattern to match a metadata key.
     */
    private static final String METADATA_KEY_PATTERN = "\\p{Upper}+";
    /**
     * The string for the pattern to match the key of an electoral list.
     */
    private static final String ELECTORAL_LIST_KEY_PATTERN = "\\p{javaUpperCase}+";
    /**
     * The string for the pattern to match the separator between the key and the value.
     */
    private static final String KEY_VALUE_SEPARATOR_PATTERN = ":";
    /**
     * The string for the pattern to match the marker for a response scenario line.
     */
    private static final String RESPONSE_SCENARIO_MARKER_PATTERN = "&";
    /**
     * The string for the pattern to match either a metadata key or a key of an electoral list.
     */
    private static final String KEY_PATTERN = "(" + METADATA_MARKER_PATTERN + METADATA_KEY_PATTERN + "|"
                                                  + ELECTORAL_LIST_KEY_PATTERN + ")";
    /**
     * The string for the pattern to match a key and a value.
     */
    private static final String KEY_VALUE_PATTERN = KEY_PATTERN + KEY_VALUE_SEPARATOR_PATTERN + ".+?";
    /**
     * The pattern to match an opinion poll line.
     */
    private static final Pattern OPINION_POLL_PATTERN = Pattern.compile("^\\s*" + METADATA_MARKER_PATTERN
                                                                                + METADATA_KEY_PATTERN
                                                                                + KEY_VALUE_SEPARATOR_PATTERN
                                                                                + ".+?(\\s+"
                                                                                + KEY_VALUE_PATTERN
                                                                                + ")+$");
    /**
     * The pattern to match a response scenario.
     */
    private static final Pattern RESPONSE_SCENARIO_PATTERN = Pattern.compile("^\\s*" + RESPONSE_SCENARIO_MARKER_PATTERN
                                                                                     + "\\s*("
                                                                                     + KEY_VALUE_PATTERN
                                                                                     + "(\\s+"
                                                                                     + KEY_VALUE_PATTERN
                                                                                     + ")*)$");
    /**
     * The pattern to match the key and value of a metadata block.
     */
    private static final Pattern METADATA_KEY_VALUE_PATTERN = Pattern.compile("^\\s*" + METADATA_MARKER_PATTERN
                                                                                      + "("
                                                                                      + METADATA_KEY_PATTERN
                                                                                      + ")"
                                                                                      + KEY_VALUE_SEPARATOR_PATTERN
                                                                                      + "\\s*(.+?)\\s*$");
    /**
     * The pattern to match the key and value of a result block.
     */
    private static final Pattern RESULT_KEY_VALUE_PATTERN = Pattern.compile("^\\s*(" + ELECTORAL_LIST_KEY_PATTERN
                                                                                     + ")"
                                                                                     + KEY_VALUE_SEPARATOR_PATTERN
                                                                                     + "\\s*(.+?)\\s*$");
    /**
     * The pattern to match key and value blocks.
     */
    private static final Pattern KEY_VALUES_PATTERN = Pattern.compile("^\\s*(" + KEY_VALUE_PATTERN
                                                                               + ")((\\s+"
                                                                               + KEY_VALUE_PATTERN
                                                                               + ")*)$");
    private final OpinionPolls opinionPolls;

    private RichOpinionPollsFile(final List<OpinionPoll> opinionPolls) {
        this.opinionPolls = new OpinionPolls(opinionPolls);
    }

    public OpinionPolls getOpinionPolls() {
        return opinionPolls;
    }

    /**
     * Parses a string array into an OpinionPolls instance.
     *
     * @param lines The multiline string to parse.
     * @return The OpinionPolls instance.
     */
    public static RichOpinionPollsFile parse(final String... lines) {
        List<OpinionPoll> opinionPolls = new ArrayList<OpinionPoll>();
        OpinionPoll lastOpinionPoll = null;
        for (String line : lines) {
            if (lineMatchesPattern(OPINION_POLL_PATTERN, line)) {
                lastOpinionPoll = parseOpinionPollLine(line);
                opinionPolls.add(lastOpinionPoll);
            } else if (lineMatchesPattern(RESPONSE_SCENARIO_PATTERN, line)) {
                lastOpinionPoll.addAlternativeResponseScenario(parseResponseScenarioLine(line));
            }
        }
        return new RichOpinionPollsFile(opinionPolls);
    }

    /**
     * Checks whether a line matches a pattern.
     *
     * @param pattern The pattern to match against.
     * @param line The line to match.
     * @return True if the line matches with the pattern, false otherwise.
     */
    private static boolean lineMatchesPattern(final Pattern pattern, final String line) {
        return pattern.matcher(line).matches();
    }

    /**
     * Processes a key and value from a part of an opinion poll line.
     *
     * @param builder The opinion poll builder to build on.
     * @param remainder The remainder of a line to parse a key and value from.
     * @return The unprocessed part of the line.
     */
    private static String parseKeyValue(final OpinionPoll.Builder builder, final String remainder) {
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
     * Parses an opinion poll line.
     *
     * @param line The line to parse an opinion poll from.
     * @return The opinion poll parsed from the line.
     */
    private static OpinionPoll parseOpinionPollLine(final String line) {
        OpinionPoll.Builder builder = new OpinionPoll.Builder();
        String remainder = line;
        while (!remainder.isEmpty()) {
            remainder = parseKeyValue(builder, remainder);
        }
        return builder.build();
    }

    /**
     * Parses a response scenario line.
     *
     * @param line The line to parse a response scenario from.
     * @return The response scenario parsed from the line.
     */
    private static ResponseScenario parseResponseScenarioLine(final String line) {
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
     * Processes a data block with metadata for an opinon poll.
     *
     * @param builder The opinion poll builder to build on.
     * @param keyValueString The data block to process.
     */
    private static void processMetadata(final OpinionPoll.Builder builder, final String keyValueString) {
        Matcher keyValueMatcher = METADATA_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String key = keyValueMatcher.group(1);
        String value = keyValueMatcher.group(2);
        switch (key) {
            case "C": builder.addCommissioner(value);
                break;
            case "FE": builder.setFieldworkEnd(value);
                break;
            case "FS": builder.setFieldworkStart(value);
                break;
            case "O": builder.setOther(value);
                break;
            case "PD": builder.setPublicationDate(value);
                break;
            case "PF": builder.setPollingFirm(value);
                break;
            case "SC": builder.setScope(value);
                break;
            case "SS": builder.setSampleSize(value);
                break;
            // The default case should be handled as part of issue #9.
        }
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
     * Processes a data block with results for an opinion poll.
     *
     * @param builder The opinion poll builder to build on.
     * @param keyValueString The data block to process.
     */
    private static void processResultData(final OpinionPoll.Builder builder, final String keyValueString) {
        Matcher keyValueMatcher = RESULT_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String key = keyValueMatcher.group(1);
        String value = keyValueMatcher.group(2);
        builder.addResult(key, value);
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
