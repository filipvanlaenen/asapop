package net.filipvanlaenen.asapop.model;

import java.util.Collections;
import java.util.Set;

/**
 * Class representing a list of opinion polls.
 */
public final class OpinionPolls {
    /**
     * The set with the opinion polls.
     */
    private final Set<OpinionPoll> opinionPolls;

    /**
     * Constructor taking a set of opinion polls as its parameter.
     *
     * @param opinionPolls A set of opinion polls.
     */
    public OpinionPolls(final Set<OpinionPoll> opinionPolls) {
        this.opinionPolls = Collections.unmodifiableSet(opinionPolls);
    }

    /**
     * Returns the opinion polls.
     *
     * @return An unmodifiable set with the opinion polls.
     */
    public Set<OpinionPoll> getOpinionPolls() {
        return opinionPolls;
    }

    public int getNumberOfOpinionPolls() {
        return opinionPolls.size();
    }

    public int getNumberOfResponseScenarios() {
        int result = 0;
        for (OpinionPoll opinionPoll : opinionPolls) {
            result += opinionPoll.getNumberOfResponseScenarios();
        }
        return result;
    }

    public int getNumberOfResultValues() {
        int result = 0;
        for (OpinionPoll opinionPoll : opinionPolls) {
            result += opinionPoll.getNumberOfResultValues();
        }
        return result;
    }
}
