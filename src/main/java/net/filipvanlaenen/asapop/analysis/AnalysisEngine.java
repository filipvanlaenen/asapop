package net.filipvanlaenen.asapop.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * The size of the population (the number of voters for the first round of the French presidential election of
     * 2017).
     */
    private static final long POPULATION_SIZE = 36_054_394L;
    /**
     * The opinion polls to run the statistical analyses on.
     */
    private OpinionPolls opinionPolls;
    /**
     * A map containing the vote shares analysis per response scenario.
     */
    private final Map<ResponseScenario, VoteSharesAnalysis> voteSharesAnalyses;
    /**
     * A map containing the first round winners analysis per response scenario.
     */
    private final Map<ResponseScenario, FirstRoundWinnersAnalysis> firstRoundWinnersAnalyses;

    /**
     * Constructor taking the opinion polls and election data as its parameters.
     *
     * @param opinionPolls The opinion polls to run the statistical analyses on.
     * @param electionData The election data needed to run the statistical analyses.
     */
    public AnalysisEngine(final OpinionPolls opinionPolls, final ElectionData electionData) {
        this.opinionPolls = opinionPolls;
        voteSharesAnalyses = new HashMap<ResponseScenario, VoteSharesAnalysis>();
        firstRoundWinnersAnalyses = new HashMap<ResponseScenario, FirstRoundWinnersAnalysis>();
    }

    public FirstRoundWinnersAnalysis getFirstRoundWinnersAnalysis(final ResponseScenario responseScenario) {
        return firstRoundWinnersAnalyses.get(responseScenario);
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
        System.out.println("Number of opinion polls: " + opinionPolls.getOpinionPolls().size());
        for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
            System.out.println(opinionPoll.getFieldworkEnd() + " " + opinionPoll.getPollingFirm());
            Integer effectiveSampleSize = opinionPoll.getEffectiveSampleSize();
            if (effectiveSampleSize != null) {
                VoteSharesAnalysis voteShareAnalysis = new VoteSharesAnalysis();
                for (ElectoralList electoralList : opinionPoll.getElectoralLists()) {
                    Long sampled = Math
                            .round(Double.parseDouble(opinionPoll.getResult(electoralList.getKey()).getPrimitiveText())
                                    * effectiveSampleSize / HUNDRED);
                    voteShareAnalysis.add(electoralList, SampledHypergeometricDistributions.get(sampled,
                            (long) effectiveSampleSize, TEN_THOUSAND, POPULATION_SIZE));
                }
                voteSharesAnalyses.put(opinionPoll.getMainResponseScenario(), voteShareAnalysis);
                List<SampledHypergeometricDistribution> probabilityMassFunctions = voteShareAnalysis
                        .getProbabilityMassFunctions();
                SampledMultivariateHypergeometricDistribution sampledMultivariateHypergeometricDistribution = SampledMultivariateHypergeometricDistributions
                        .get(probabilityMassFunctions, POPULATION_SIZE, effectiveSampleSize, TEN_THOUSAND);
                FirstRoundWinnersAnalysis frwa = new FirstRoundWinnersAnalysis(voteShareAnalysis,
                        sampledMultivariateHypergeometricDistribution);
                firstRoundWinnersAnalyses.put(opinionPoll.getMainResponseScenario(), frwa);
                for (Set<ElectoralList> electoralListSet : frwa.getElectoralListSets()) {
                    String s = "";
                    for (ElectoralList electoralList : electoralListSet) {
                        s += electoralList.getKey() + ", ";
                    }
                    double p = frwa.getProbabilityMass(electoralListSet);
                    if (p > 0.001D) {
                        System.out.println(s + String.format("%,.2f", p) + "%");
                    }
                }
                System.out.println();
            }
        }
    }
}
