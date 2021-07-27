package net.filipvanlaenen.asapop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a list of opinion polls.
 */
final class OpinionPolls {
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
            System.out.println("Found a line to parse: “" + line + "”.");
        }
        return result;
    }

    /**
     * Returns the opinion polls as a list.
     */
    List<OpinionPoll> getOpinionPollsList() {
        return Collections.unmodifiableList(opinionPolls);
    }
}
