package net.filipvanlaenen.asapop.yaml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.analysis.AnalysisEngine;
import net.filipvanlaenen.asapop.analysis.FirstRoundWinnersAnalysis;
import net.filipvanlaenen.asapop.analysis.Range;
import net.filipvanlaenen.asapop.analysis.SortableProbabilityMassFunction;
import net.filipvanlaenen.asapop.analysis.VoteSharesAnalysis;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.Scope;

/**
 * Builder class to build an <code>Analysis</code>.
 */
public class AnalysisBuilder {
    /**
     * The magic number hundred.
     */
    private static final float HUNDRED = 100F;
    /**
     * The levels for the confidence intervals.
     */
    private static final Integer[] CONFIDENCE_INTERVAL_LEVELS = new Integer[] {80, 90, 95, 99};
    /**
     * The analysis engine to extract the analysis from.
     */
    private AnalysisEngine engine;

    /**
     * Constructor taking an analysis engine as its parameter.
     *
     * @param engine The analysis engine to extract the analysis from.
     */
    public AnalysisBuilder(final AnalysisEngine engine) {
        this.engine = engine;
    }

    /**
     * Builds the analysis.
     *
     * @return The analysis extracted from the analysis engine.
     */
    public Analysis build() {
        Analysis result = new Analysis();
        Set<OpinionPollAnalysis> opinionPollAnalyses = new HashSet<OpinionPollAnalysis>();
        for (OpinionPoll poll : engine.getOpinionPolls().getOpinionPolls()) {
            opinionPollAnalyses.add(buildOpinionPollAnalysis(poll));
        }
        result.setOpinionPollAnalyses(opinionPollAnalyses);
        return result;
    }

    /**
     * Builds a first round analysis object based on a first round winners analysis.
     *
     * @param firstRoundWinnersAnalysis The first round analysis to build a first round analysis.
     * @return A first round analysis for the first round winners analysis.
     */
    private FirstRoundAnalysis buildFirstRoundWinnersAnalysis(
            final FirstRoundWinnersAnalysis firstRoundWinnersAnalysis) {
        FirstRoundAnalysis firstRoundAnalysis = new FirstRoundAnalysis();
        Set<FirstRoundResultProbabilityMass> firstRoundProbabilityMassFunction =
                new HashSet<FirstRoundResultProbabilityMass>();
        for (Set<Set<ElectoralList>> electoralListSetSet : firstRoundWinnersAnalysis.getElectoralListSetSets()) {
            FirstRoundResultProbabilityMass firstRoundResultAnalysis = new FirstRoundResultProbabilityMass();
            Set<Set<String>> electoralListIdSet = new HashSet<Set<String>>();
            for (Set<ElectoralList> electoralListSet : electoralListSetSet) {
                electoralListIdSet.add(ElectoralList.getIds(electoralListSet));
            }
            firstRoundResultAnalysis.setElectoralListSets(electoralListIdSet);
            firstRoundResultAnalysis
                    .setProbabilityMass(firstRoundWinnersAnalysis.getProbabilityMass(electoralListSetSet) * HUNDRED);
            firstRoundProbabilityMassFunction.add(firstRoundResultAnalysis);
        }
        firstRoundAnalysis.setProbabilityMassFunction(firstRoundProbabilityMassFunction);
        return firstRoundAnalysis;
    }

    /**
     * Builds an opinion poll analysis for an opinion poll.
     *
     * @param poll The opinion poll for which to build an opinion poll analysis.
     * @return Returns the opinion poll analysis for the opinion poll.
     */
    private OpinionPollAnalysis buildOpinionPollAnalysis(final OpinionPoll poll) {
        OpinionPollAnalysis opinionPollAnalysis = new OpinionPollAnalysis();
        opinionPollAnalysis.setPollingFirm(poll.getPollingFirm());
        opinionPollAnalysis.setPollingFirmPartner(poll.getPollingFirmPartner());
        opinionPollAnalysis.setCommissioners(poll.getCommissioners());
        opinionPollAnalysis.setFieldworkStart(nullOrToString(poll.getFieldworkStart()));
        opinionPollAnalysis.setFieldworkEnd(nullOrToString(poll.getFieldworkEnd()));
        opinionPollAnalysis.setPublicationDate(nullOrToString(poll.getPublicationDate()));
        Set<ResponseScenarioAnalysis> responseScenarioAnalyses = new HashSet<ResponseScenarioAnalysis>();
        responseScenarioAnalyses.add(buildResponseScenarioAnalysis(poll));
        for (ResponseScenario responseScenario : poll.getAlternativeResponseScenarios()) {
            responseScenarioAnalyses.add(buildResponseScenarioAnalysis(responseScenario));
        }
        opinionPollAnalysis.setResponseScenarioAnalyses(responseScenarioAnalyses);
        return opinionPollAnalysis;
    }

    /**
     * Builds the response scenario analysis for an opinion poll's main response scenario.
     *
     * @param poll The opinion poll for which to build the response scenario analysis.
     * @return A response scenario analysis for an opinion poll's main response scenario.
     */
    private ResponseScenarioAnalysis buildResponseScenarioAnalysis(final OpinionPoll poll) {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        responseScenarioAnalysis.setArea(poll.getArea());
        responseScenarioAnalysis.setScope(nullOrToString(poll.getScope()));
        VoteSharesAnalysis voteSharesAnalysis = engine.getVoteSharesAnalysis(poll.getMainResponseScenario());
        if (voteSharesAnalysis != null) {
            Map<Set<String>, ResultAnalysis> resultAnalyses = new HashMap<Set<String>, ResultAnalysis>();
            for (Set<ElectoralList> electoralListSet : poll.getElectoralListSets()) {
                resultAnalyses.put(ElectoralList.getIds(electoralListSet),
                        buildResultAnalysis(voteSharesAnalysis, electoralListSet));
            }
            responseScenarioAnalysis.setResultAnalyses(resultAnalyses);
        }
        if (poll.getScope() == Scope.PresidentialFirstRound) {
            FirstRoundWinnersAnalysis firstRoundWinnersAnalysis =
                    engine.getFirstRoundWinnersAnalysis(poll.getMainResponseScenario());
            if (firstRoundWinnersAnalysis != null) {
                FirstRoundAnalysis firstRoundAnalysis = buildFirstRoundWinnersAnalysis(firstRoundWinnersAnalysis);
                responseScenarioAnalysis.setFirstRoundAnalysis(firstRoundAnalysis);
            }
        }
        return responseScenarioAnalysis;
    }

    /**
     * Builds the response scenario analysis for a response scenario.
     *
     * @param responseScenario The response scenario for which to build the response scenario analysis.
     * @return The response scenario analysis for the response scenario.
     */
    private ResponseScenarioAnalysis buildResponseScenarioAnalysis(final ResponseScenario responseScenario) {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        responseScenarioAnalysis.setArea(responseScenario.getArea());
        responseScenarioAnalysis.setScope(nullOrToString(responseScenario.getScope()));
        Map<Set<String>, ResultAnalysis> resultAnalyses = new HashMap<Set<String>, ResultAnalysis>();
        VoteSharesAnalysis voteSharesAnalysis = engine.getVoteSharesAnalysis(responseScenario);
        for (Set<ElectoralList> electoralListSet : responseScenario.getElectoralListSets()) {
            resultAnalyses.put(ElectoralList.getIds(electoralListSet),
                    buildResultAnalysis(voteSharesAnalysis, electoralListSet));
        }
        responseScenarioAnalysis.setResultAnalyses(resultAnalyses);
        return responseScenarioAnalysis;
    }

    /**
     * Builds a result analysis for a set of electoral lists in a response scenario.
     *
     * @param voteSharesAnalysis The vote shares analysis for a response scenario.
     * @param electoralListSet   The set of electoral lists.
     * @return A result analysis for an electoral list in a response scenario.
     */
    private ResultAnalysis buildResultAnalysis(final VoteSharesAnalysis voteSharesAnalysis,
            final Set<ElectoralList> electoralListSet) {
        long populationSize = engine.getElectionData().getPopulationSize();
        ResultAnalysis resultAnalysis = new ResultAnalysis();
        SortableProbabilityMassFunction<Range> probabilityMassFunction =
                voteSharesAnalysis.getProbabilityMassFunction(electoralListSet);
        resultAnalysis.setMedian(probabilityMassFunction.getMedian().getMidpoint() * HUNDRED / populationSize);
        Map<Integer, Float[]> confidenceIntervals = new HashMap<Integer, Float[]>();
        for (Integer level : CONFIDENCE_INTERVAL_LEVELS) {
            Float[] confidenceInterval = new Float[2];
            confidenceInterval[0] =
                    probabilityMassFunction.getConfidenceInterval(level / HUNDRED).lowerBound().lowerBound()
                            * HUNDRED / populationSize;
            confidenceInterval[1] =
                    probabilityMassFunction.getConfidenceInterval(level / HUNDRED).upperBound().upperBound()
                            * HUNDRED / populationSize;
            confidenceIntervals.put(level, confidenceInterval);
        }
        resultAnalysis.setConfidenceIntervals(confidenceIntervals);
        return resultAnalysis;
    }

    /**
     * Returns <code>null</code> if the provided object is <code>null</code>, or calls the <code>toString</code> on the
     * object and returns the result of the call.
     *
     * @param object An object to call <code>toString</code> on if it isn't <code>null</code>.
     * @return Either <code>null</code> or the result of calling <code>toString</code> on the object.
     */
    static String nullOrToString(final Object object) {
        return object == null ? null : object.toString();
    }
}
