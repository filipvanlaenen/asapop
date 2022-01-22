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
    private final Map<OpinionPoll, VoteShareAnalysis> voteShareAnalyses = new HashMap<OpinionPoll, VoteShareAnalysis>();
    private int populationSize = 5;

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
     * Runs the statistical analyses.
     */
    public void run() {
        for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
            VoteShareAnalysis voteShareAnalysis = new VoteShareAnalysis();
            for (ElectoralList electoralList : opinionPoll.getElectoralLists()) {
                ProbabilityMassFunction pmf = new ProbabilityMassFunction();
                Integer sampleSize = opinionPoll.getSampleSizeValue();
                // TODO: Overspecified (issue #82)
                // TODO: Underspecified (issue #83)
                // TODO: Decimal percentages
                Integer sampled = Integer.parseInt(opinionPoll.getResult(electoralList.getKey()).getPrimitiveText())
                        * sampleSize / 100;
                // TODO: Large population sizes
                for (int i = 0; i <= populationSize; i++) {
                    pmf.add(i, combinations(sampled, i) * combinations(sampleSize - sampled, populationSize - i));
                }
                voteShareAnalysis.add(electoralList, pmf);
            }
            // TODO: Add Other too
            voteShareAnalyses.put(opinionPoll, voteShareAnalysis);
        }
    }

    private int combinations(int k, int n) {
        // TODO: Large k and n
        int p = 1;
        for (int i = n; i >= n - (k - 1); i--) {
            p *= i;
        }
        for (int i = 1; i <= k; i++) {
            p /= i;
        }
        return p;
    }

    VoteShareAnalysis getVoteShareAnalysis(final OpinionPoll opinionPoll) {
        return voteShareAnalyses.get(opinionPoll);
    }
}
