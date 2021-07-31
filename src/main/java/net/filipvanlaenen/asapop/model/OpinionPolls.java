package net.filipvanlaenen.asapop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing a list of opinion polls.
 */
public final class OpinionPolls {
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
     * The string for the pattern to match separator between the key and the value.
     */
    private static final String KEY_VALUE_SEPARATOR_PATTERN = ":";
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

    /**
     * The list with the opinion polls.
     */
    private final List<OpinionPoll> opinionPolls = new ArrayList<OpinionPoll>();

    /**
     * Parses a string array into an OpinionPolls instance.
     *
     * @param lines The multiline string to parse.
     * @return The OpinionPolls instance.
     */
    public static OpinionPolls parse(final String... lines) {
        OpinionPolls result = new OpinionPolls();
        for (String line : lines) {
            OpinionPoll.Builder builder = new OpinionPoll.Builder();
            String remainder = line;
            while (!remainder.isEmpty()) {
                remainder = parseKeyValue(builder, remainder);
            }
            result.addOpinionPoll(builder.build());
        }
        return result;
    }

    /**
     * Processes a key and value from a part of a line.
     *
     * @param builder The opinion builder to build on.
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
     * Processes a data block with metadata.
     *
     * @param builder The opinion builder to build on.
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
     * Processes a data block with results.
     *
     * @param builder The opinion builder to build on.
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
     * Adds an opinion poll.
     *
     * @param poll An opinion poll to be added.
     */
    private void addOpinionPoll(final OpinionPoll poll) {
        opinionPolls.add(poll);
    }

    /**
     * Returns the opinion polls as a list.
     *
     * @return An unmodifiable list with the opinion polls.
     */
    List<OpinionPoll> getOpinionPollsList() {
        return Collections.unmodifiableList(opinionPolls);
    }

    public String toEopaodPsvString() {
        //TODO in issue #30.
        return "";
    }
}
