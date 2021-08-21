package net.filipvanlaenen.asapop.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.OpinionPoll;

/**
 * Class implementing a line representing an opinion poll.
 */
final class OpinionPollLine extends Line {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;
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
     * The opinion poll represented by the line.
     */
    private final OpinionPoll opinionPoll;

    /**
     * Private constructor taking the opinion poll as its parameter.
     *
     * @param opinionPoll The opinion poll represented by the line.
     */
    private OpinionPollLine(final OpinionPoll opinionPoll) {
        this.opinionPoll = opinionPoll;
    }

    /**
     * Returns the opinion poll represented by this line.
     *
     * @return The opinion poll represented by this line.
     */
    OpinionPoll getOpinionPoll() {
        return opinionPoll;
    }

    /**
     * Verifies whether a line is an opinion poll line.
     *
     * @param line The line to check against the pattern of an opinion poll line.
     * @return True if the line matches the pattern of an opinion poll line, false otherwise.
     */
    static boolean isOpinionPollLine(final String line) {
        return lineMatchesPattern(OPINION_POLL_PATTERN, line);
    }

    /**
     * Parses an opinion poll line.
     *
     * @param line The line to parse an opinion poll from.
     * @return An OpinionPollLine representing the line.
     */
    static OpinionPollLine parse(final String line) {
        OpinionPoll.Builder builder = new OpinionPoll.Builder();
        String remainder = line;
        while (!remainder.isEmpty()) {
            remainder = parseKeyValue(builder, remainder);
        }
        return new OpinionPollLine(builder.build());
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
            case "A": builder.setArea(value);
                break;
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
}
