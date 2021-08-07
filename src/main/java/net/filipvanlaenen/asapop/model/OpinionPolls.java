package net.filipvanlaenen.asapop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a list of opinion polls.
 */
public final class OpinionPolls {
    /**
     * The list with the opinion polls.
     */
    private final List<OpinionPoll> opinionPolls;

    public OpinionPolls(final List<OpinionPoll> opinionPolls) {
        this.opinionPolls = Collections.unmodifiableList(opinionPolls);
    }

    /**
     * Returns the opinion polls as a list.
     *
     * @return An unmodifiable list with the opinion polls.
     */
    public List<OpinionPoll> getOpinionPollsList() {
        return opinionPolls;
    }
}
