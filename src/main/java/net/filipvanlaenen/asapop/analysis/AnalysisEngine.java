package net.filipvanlaenen.asapop.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.Scope;
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

    /**
     * Calculates the first round winners analysis.
     *
     * @param voteSharesAnalysis  The vote shares analysis.
     * @param effectiveSampleSize The effective sample size of the response scenario.
     * @return The first round winners analysis.
     */
    private FirstRoundWinnersAnalysis calculateFirstRoundWinnersAnalysis(final VoteSharesAnalysis voteSharesAnalysis,
            final Integer effectiveSampleSize) {
        List<SampledHypergeometricDistribution> probabilityMassFunctions = voteSharesAnalysis
                .getProbabilityMassFunctions();
        return new FirstRoundWinnersAnalysis(voteSharesAnalysis, SampledMultivariateHypergeometricDistributions
                .get(probabilityMassFunctions, POPULATION_SIZE, effectiveSampleSize, TEN_THOUSAND));
    }

    /**
     * Calculates the most recent polls, i.e. for each polling firm the most recent poll.
     *
     * @return A collection with the most recent polls.
     */
    private Collection<OpinionPoll> calculateMostRecentPolls() {
        Map<String, OpinionPoll> mostRecentPollMap = new HashMap<String, OpinionPoll>();
        for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
            String pollingFirm = opinionPoll.getPollingFirm();
            if (mostRecentPollMap.containsKey(pollingFirm)) {
                if (mostRecentPollMap.get(pollingFirm).getFieldworkEnd().getEnd()
                        .compareTo(opinionPoll.getFieldworkEnd().getEnd()) < 0) {
                    mostRecentPollMap.put(pollingFirm, opinionPoll);
                }
            } else {
                mostRecentPollMap.put(pollingFirm, opinionPoll);
            }
        }
        return mostRecentPollMap.values();
    }

    /**
     * Calculates the vote shares analysis for an opinion poll.
     *
     * @param opinionPoll The opinion poll.
     * @return The vote shares analysis.
     */
    private VoteSharesAnalysis calculateVoteSharesAnalysis(final OpinionPoll opinionPoll) {
        Integer effectiveSampleSize = opinionPoll.getEffectiveSampleSize();
        VoteSharesAnalysis voteShareAnalysis = new VoteSharesAnalysis();
        for (ElectoralList electoralList : opinionPoll.getElectoralLists()) {
            double result = Double.parseDouble(opinionPoll.getResult(electoralList.getKey()).getPrimitiveText());
            Long sampled = Math.round(result * effectiveSampleSize / HUNDRED);
            voteShareAnalysis.add(electoralList, SampledHypergeometricDistributions.get(sampled,
                    (long) effectiveSampleSize, TEN_THOUSAND, POPULATION_SIZE));
        }
        return voteShareAnalysis;
    }

    /**
     * Returns the first round winners analysis for a response scenario.
     *
     * @param responseScenario The response scenario for which to return the vote shares analysis.
     * @return The first round winners analysis for the response scenario.
     */
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
     * @return The vote shares analysis for the response scenario.
     */
    public VoteSharesAnalysis getVoteSharesAnalysis(final ResponseScenario responseScenario) {
        return voteSharesAnalyses.get(responseScenario);
    }

    /**
     * Runs the statistical analyses.
     */
    public void run() {
        for (OpinionPoll opinionPoll : calculateMostRecentPolls()) {
            Integer effectiveSampleSize = opinionPoll.getEffectiveSampleSize();
            if (effectiveSampleSize != null) {
                VoteSharesAnalysis voteShareAnalysis = calculateVoteSharesAnalysis(opinionPoll);
                voteSharesAnalyses.put(opinionPoll.getMainResponseScenario(), voteShareAnalysis);
                if (opinionPoll.getScope() == Scope.PresidentialFirstRound) {
                    firstRoundWinnersAnalyses.put(opinionPoll.getMainResponseScenario(),
                            calculateFirstRoundWinnersAnalysis(voteShareAnalysis, effectiveSampleSize));
                }
            }
        }
    }
}
