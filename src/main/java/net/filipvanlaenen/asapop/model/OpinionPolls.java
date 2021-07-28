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
     * The pattern to match metadata.
     */
    private final static Pattern METADATA_PATTERN = Pattern.compile("^\\s*•([A-Z]+):\\s*([^•]+?)\\s*(•?[A-Z]+:.*)$");
    /**
     * The pattern to match results data.
     */
    private final static Pattern RESULTS_PATTERN = Pattern.compile("^\\s*([A-Z]+):\\s*([^A-Z]+?)\\s*([A-Z]+:.*)?$");
    /**
     * The list with the opinion polls.
     */
    private final List<OpinionPoll> opinionPolls = new ArrayList<OpinionPoll>();

    /**
     * Parses a multiline string into an OpinionPolls instance.
     */
    static OpinionPolls parse(final String content) {
        OpinionPolls result = new OpinionPolls();
        String[] lines = content.split("\\r?\\n");
        for(String line : lines) {
            OpinionPoll.Builder builder = new OpinionPoll.Builder();
            String remainder = parseMetadata(builder, line);
            remainder = parseResults(builder, remainder);
            result.addOpinionPoll(builder.build());
        }
        return result;
    }

    /**
     * Parses the metadata for an opinion poll from a line.
     */
    private static String parseMetadata(final OpinionPoll.Builder builder, final String line) {
        String remainder = line;
        Matcher metadataMatcher = METADATA_PATTERN.matcher(remainder);
        while (metadataMatcher.find()) {
            String key = metadataMatcher.group(1);
            String value = metadataMatcher.group(2);
            switch (key) {
                case "PF": builder.setPollingFirm(value);
                    break;
                case "PD": builder.setPublicationDate(value);
                    break;
            }
            remainder = metadataMatcher.group(3);
            metadataMatcher = METADATA_PATTERN.matcher(remainder);
        }
        return remainder;
    }

    /**
     * Parses the results for an opinion poll from a line.
     */
    private static String parseResults(final OpinionPoll.Builder builder, final String line) {
        String remainder = line;
        Matcher resultsMatcher = RESULTS_PATTERN.matcher(remainder);
        while (resultsMatcher.find()) {
            String key = resultsMatcher.group(1);
            String value = resultsMatcher.group(2);
            builder.addResult(key, value);
            remainder = resultsMatcher.group(3);
            if (remainder == null) {
                remainder = "";
            }
            resultsMatcher = RESULTS_PATTERN.matcher(remainder);
        }
        return remainder;
    }

    /**
     * Adds an opinion poll.
     */
    private void addOpinionPoll(final OpinionPoll poll) {
        opinionPolls.add(poll);
    }

    /**
     * Returns the opinion polls as a list.
     */
    List<OpinionPoll> getOpinionPollsList() {
        return Collections.unmodifiableList(opinionPolls);
    }
}
