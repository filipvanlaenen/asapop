package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.Map;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.yaml.ElectionData;

/**
 * Class implementing the engine running the statistical analyses.
 */
public class AnalysisEngine {
    /**
     * The magic number hundred.
     */
    private static final double HUNDRED = 100D;
    /**
     * The magic number ten thousand, the default number of samples.
     */
    private static final long TEN_THOUSAND = 10_000L;
    /**
     * The opinion polls to run the statistical analyses on.
     */
    private OpinionPolls opinionPolls;
    /**
     * A map containing the vote shares analysis per response scenario.
     */
    private final Map<ResponseScenario, VoteSharesAnalysis> voteSharesAnalyses;

    /**
     * Constructor taking the opinion polls and election data as its parameters.
     *
     * @param opinionPolls The opinion polls to run the statistical analyses on.
     * @param electionData The election data needed to run the statistical analyses.
     */
    public AnalysisEngine(final OpinionPolls opinionPolls, final ElectionData electionData) {
        this.opinionPolls = opinionPolls;
        voteSharesAnalyses = new HashMap<ResponseScenario, VoteSharesAnalysis>();
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
     * Returns the vote shares analysis for a response scenario.
     *
     * @param responseScenario The response scenario for which to return the vote shares analysis.
     * @return The vote shares analysis for the opinion poll.
     */
    public VoteSharesAnalysis getVoteSharesAnalysis(final ResponseScenario responseScenario) {
        return voteSharesAnalyses.get(responseScenario);
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
                                * sampleSize / HUNDRED);
                voteShareAnalysis.add(electoralList,
                        SampledHypergeometricDistributions.get(sampled, sampleSize, TEN_THOUSAND, 36_054_394L));
            }
            voteSharesAnalyses.put(opinionPoll.getMainResponseScenario(), voteShareAnalysis);
        }
    }
}
