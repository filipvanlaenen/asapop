package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.Map;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.ElectionData;

/**
 * Class implementing the engine running the statistical analyses.
 */
public class AnalysisEngine {
    /**
     * The opinion polls to run the statistical analyses on.
     */
    private OpinionPolls opinionPolls;
    private final Map<OpinionPoll, VoteSharesAnalysis> voteShareAnalyses = new HashMap<OpinionPoll, VoteSharesAnalysis>();

    /**
     * Constructor taking the opinion polls and election data as its parameters.
     *
     * @param opinionPolls The opinion polls to run the statistical analyses on.
     * @param electionData The election data needed to run the statistical analyses.
     */
    public AnalysisEngine(final OpinionPolls opinionPolls, final ElectionData electionData) {
        this.opinionPolls = opinionPolls;
    }

    /**
     * Returns the opinion polls to run the statistical analyses on.
     *
     * @return The opinion polls to run the statistical analyses on.
     */
    public OpinionPolls getOpinionPolls() {
        return opinionPolls;
    }

    /**
     * Returns the vote shares analysis for an opinion poll.
     *
     * @param opinionPoll The opinion poll for which to return the vote shares analysis.
     * @return The vote shares analysis for the opinion poll.
     */
    VoteSharesAnalysis getVoteShareAnalysis(final OpinionPoll opinionPoll) {
        return voteShareAnalyses.get(opinionPoll);
    }

    /**
     * Runs the statistical analyses.
     */
    public void run() {
        for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
            VoteSharesAnalysis voteShareAnalysis = new VoteSharesAnalysis();
            for (ElectoralList electoralList : opinionPoll.getElectoralLists()) {
                long sampleSize = (long) opinionPoll.getSampleSizeValue();
                Long sampled = Math
                        .round(Double.parseDouble(opinionPoll.getResult(electoralList.getKey()).getPrimitiveText())
                                * sampleSize / 100);
                voteShareAnalysis.add(electoralList, BinomialDistributions.get(sampled, sampleSize, 5L));
            }
            // TODO: Add Other too
            voteShareAnalyses.put(opinionPoll, voteShareAnalysis);
        }
    }
}
