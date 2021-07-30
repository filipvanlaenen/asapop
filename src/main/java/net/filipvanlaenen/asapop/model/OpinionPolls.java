package net.filipvanlaenen.asapop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing a list of opinion polls.
 */
final class OpinionPolls {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;
    /**
     * The string for the pattern to match a metadata key.
     */
    private static final String METADATA_KEY_PATTERN = "\\p{Upper}+";
    /**
     * The string for the pattern to match the key of an electoral list.
     */
    private static final String ELECTORAL_LIST_KEY_PATTERN = "\\p{javaUpperCase}+";
    /**
     * The string for the pattern to match either a metadata key or a key of an electoral list.
     */
    private static final String METADATA_OR_ELECTORAL_LIST_KEY_PATTERN = "(•" + METADATA_KEY_PATTERN + "|"
                                                                         + ELECTORAL_LIST_KEY_PATTERN + ")";
    /**
     * The pattern to match metadata.
     */
    private static final Pattern METADATA_PATTERN = Pattern.compile("^\\s*•(" + METADATA_KEY_PATTERN
                                                                    + "):\\s*([^•]+?)\\s*("
                                                                    + METADATA_OR_ELECTORAL_LIST_KEY_PATTERN + ":.*)$");
    /**
     * The pattern to match results data.
     */
    private static final Pattern RESULTS_PATTERN = Pattern.compile("^\\s*(" + ELECTORAL_LIST_KEY_PATTERN
                                                                   + "):\\s*([^\\p{javaUpperCase}]+?)\\s*("
                                                                   + ELECTORAL_LIST_KEY_PATTERN + ":.*)?$");
    /**
     * The list with the opinion polls.
     */
    private final List<OpinionPoll> opinionPolls = new ArrayList<OpinionPoll>();

    /**
     * Parses a multiline string into an OpinionPolls instance.
     *
     * @param content The multiline string to parse.
     * @return The OpinionPolls instance.
     */
    static OpinionPolls parse(final String content) {
        OpinionPolls result = new OpinionPolls();
        String[] lines = content.split("\\r?\\n");
        for (String line : lines) {
            OpinionPoll.Builder builder = new OpinionPoll.Builder();
            String remainder = parseMetadata(builder, line);
            parseResults(builder, remainder);
            result.addOpinionPoll(builder.build());
        }
        return result;
    }

    /**
     * Parses the metadata for an opinion poll from a line into an opinion builder instance and returns the remainder
     * of the line.
     *
     * @param builder The opinion poll builder instance to add the metadata to.
     * @param line The line to parse the metadata from.
     * @return The remainder of the line.
     */
    private static String parseMetadata(final OpinionPoll.Builder builder, final String line) {
        String remainder = line;
        Matcher metadataMatcher = METADATA_PATTERN.matcher(remainder);
        while (metadataMatcher.find()) {
            String key = metadataMatcher.group(1);
            String value = metadataMatcher.group(2);
            switch (key) {
                case "C": builder.addCommissioner(value);
                    break;
                case "PF": builder.setPollingFirm(value);
                    break;
                case "PD": builder.setPublicationDate(value);
                    break;
                case "SS": builder.setSampleSize(value);
                    break;
                // The default case should be handled as part of issue #9.
            }
            remainder = metadataMatcher.group(THREE);
            metadataMatcher = METADATA_PATTERN.matcher(remainder);
        }
        return remainder;
    }

    /**
     * Parses the results for an opinion poll from a line into an opinion builder instance.
     *
     * @param builder The opinion poll builder instance to add the results to.
     * @param line The line to parse the results from.
     */
    private static void parseResults(final OpinionPoll.Builder builder, final String line) {
        String remainder = line;
        Matcher resultsMatcher = RESULTS_PATTERN.matcher(remainder);
        while (resultsMatcher.find()) {
            String key = resultsMatcher.group(1);
            String value = resultsMatcher.group(2);
            builder.addResult(key, value);
            remainder = resultsMatcher.group(THREE);
            if (remainder == null) {
                remainder = "";
            }
            resultsMatcher = RESULTS_PATTERN.matcher(remainder);
        }
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
}
