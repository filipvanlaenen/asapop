package net.filipvanlaenen.asapop.analysis;

import static net.filipvanlaenen.kolektoj.Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DISTINCT_VALUES;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.asapop.yaml.ElectionData;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.kolektoj.hash.ModifiableHashMap;

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
     * The magic number two million, the default number of simulations.
     */
    private static final long TWO_MILLION = 2_000_000L;
    /**
     * The opinion polls to run the statistical analyses on.
     */
    private OpinionPolls opinionPolls;
    /**
     * The election specific data to be used during the statistical analyses.
     */
    private ElectionData electionData;
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
        this.electionData = electionData;
        voteSharesAnalyses = new HashMap<ResponseScenario, VoteSharesAnalysis>();
        firstRoundWinnersAnalyses = new HashMap<ResponseScenario, FirstRoundWinnersAnalysis>();
    }

    /**
     * Calculates the first round winners analysis.
     *
     * @param voteSharesAnalysis             The vote shares analysis.
     * @param effectiveSampleSize            The effective sample size of the response scenario.
     * @param numberOfMultivariateIterations Number of iterations for the multivariate analysis.
     * @return The first round winners analysis.
     */
    private FirstRoundWinnersAnalysis calculateFirstRoundWinnersAnalysis(final VoteSharesAnalysis voteSharesAnalysis,
            final Integer effectiveSampleSize, final long numberOfMultivariateIterations) {
        List<SampledHypergeometricDistribution> probabilityMassFunctions =
                voteSharesAnalysis.getProbabilityMassFunctions();
        return new FirstRoundWinnersAnalysis(voteSharesAnalysis,
                SampledMultivariateHypergeometricDistributions.get(probabilityMassFunctions,
                        electionData.getPopulationSize(), effectiveSampleSize, numberOfMultivariateIterations));
    }

    /**
     * Calculates the most recent polls, i.e. for each polling firm the most recent poll.
     *
     * @return A collection with the most recent polls.
     */
    Collection<OpinionPoll> calculateMostRecentPolls() {
        ModifiableMap<String, OpinionPoll> mostRecentPollMap =
                new ModifiableHashMap<String, OpinionPoll>(DUPLICATE_KEYS_WITH_DISTINCT_VALUES);
        for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
            String pollingFirm = opinionPoll.getPollingFirm();
            if (mostRecentPollMap.containsKey(pollingFirm)) {
                LocalDate mostRecentDate = mostRecentPollMap.get(pollingFirm).getFieldworkEnd().getEnd();
                int moreRecent = mostRecentDate.compareTo(opinionPoll.getFieldworkEnd().getEnd());
                if (moreRecent < 0) {
                    mostRecentPollMap.removeIf(e -> e.key().equals(pollingFirm));
                    mostRecentPollMap.put(pollingFirm, opinionPoll);
                } else if (moreRecent == 0) {
                    mostRecentPollMap.add(pollingFirm, opinionPoll);
                }
            } else {
                mostRecentPollMap.add(pollingFirm, opinionPoll);
            }
        }
        return mostRecentPollMap.getValues();
    }

    /**
     * Calculates the vote shares analysis for an opinion poll.
     *
     * @param opinionPoll     The opinion poll.
     * @param numberOfSamples The number of samples.
     * @return The vote shares analysis.
     */
    private VoteSharesAnalysis calculateVoteSharesAnalysis(final OpinionPoll opinionPoll, final long numberOfSamples) {
        Integer effectiveSampleSize = opinionPoll.getEffectiveSampleSize();
        VoteSharesAnalysis voteShareAnalysis = new VoteSharesAnalysis();
        for (Set<ElectoralList> electoralListSet : opinionPoll.getElectoralListSets()) {
            double result = Double
                    .parseDouble(opinionPoll.getResult(ElectoralList.getIds(electoralListSet)).getPrimitiveText());
            Long sampled = Math.round(result * effectiveSampleSize / HUNDRED);
            voteShareAnalysis.add(electoralListSet, SampledHypergeometricDistributions.get(sampled,
                    (long) effectiveSampleSize, numberOfSamples, electionData.getPopulationSize()));
        }
        return voteShareAnalysis;
    }

    /**
     * Returns the election data used for the statistical analyses.
     *
     * @return The election data used for the statistical analyses.
     */
    public ElectionData getElectionData() {
        return electionData;
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
        run(TWO_MILLION);
    }

    /**
     * Runs the statistical analyses with a specified number of iterations for the multivariate analysis.
     *
     * @param numberOfMultivariateIterations Number of iterations for the multivariate analysis.
     */
    public void run(final long numberOfMultivariateIterations) {
        run(TEN_THOUSAND, numberOfMultivariateIterations);
    }

    /**
     * Runs the statistical analyses with a specified number of iterations for the multivariate analysis.
     *
     * @param numberOfSamples                Number of samples for the analysis.
     * @param numberOfMultivariateIterations Number of iterations for the multivariate analysis.
     */
    public void run(final long numberOfSamples, final long numberOfMultivariateIterations) {
        for (OpinionPoll opinionPoll : calculateMostRecentPolls()) {
            Integer effectiveSampleSize = opinionPoll.getEffectiveSampleSize();
            if (effectiveSampleSize != null) {
                VoteSharesAnalysis voteShareAnalysis = calculateVoteSharesAnalysis(opinionPoll, numberOfSamples);
                voteSharesAnalyses.put(opinionPoll.getMainResponseScenario(), voteShareAnalysis);
                if (opinionPoll.getScope() == Scope.PRESIDENTIAL_FIRST_ROUND) {
                    firstRoundWinnersAnalyses.put(opinionPoll.getMainResponseScenario(),
                            calculateFirstRoundWinnersAnalysis(voteShareAnalysis, effectiveSampleSize,
                                    numberOfMultivariateIterations));
                }
            }
        }
    }
}
