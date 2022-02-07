package net.filipvanlaenen.asapop.yaml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.analysis.AnalysisEngine;
import net.filipvanlaenen.asapop.analysis.Range;
import net.filipvanlaenen.asapop.analysis.SortableProbabilityMassFunction;
import net.filipvanlaenen.asapop.analysis.VoteSharesAnalysis;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.ResponseScenario;

/**
 * Builder class to build an <code>Analysis</code>.
 */
public class AnalysisBuilder {
    /**
     * The magic number hundred.
     */
    private static final float HUNDRED = 100F;
    /**
     * The size of the population (the number of voters for the first round of the French presidential election of
     * 2017).
     */
    private static final long POPULATION_SIZE = 36_054_394L;
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
        Map<String, ResultAnalysis> resultAnalyses = new HashMap<String, ResultAnalysis>();
        VoteSharesAnalysis voteSharesAnalysis = engine.getVoteSharesAnalysis(poll.getMainResponseScenario());
        for (ElectoralList electoralList : poll.getElectoralLists()) {
            resultAnalyses.put(electoralList.getKey(), buildResultAnalysis(voteSharesAnalysis, electoralList));
        }
        responseScenarioAnalysis.setResultAnalyses(resultAnalyses);
        return responseScenarioAnalysis;
    }

    /**
     * Builds the response scenario analysis for a response scenario.
     *
     * @param responseScenario The response scenario for which to build the response scenario analysis.
     * @return The response scenarion analysis for the response scenario.
     */
    private ResponseScenarioAnalysis buildResponseScenarioAnalysis(final ResponseScenario responseScenario) {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        responseScenarioAnalysis.setArea(responseScenario.getArea());
        responseScenarioAnalysis.setScope(nullOrToString(responseScenario.getScope()));
        Map<String, ResultAnalysis> resultAnalyses = new HashMap<String, ResultAnalysis>();
        VoteSharesAnalysis voteSharesAnalysis = engine.getVoteSharesAnalysis(responseScenario);
        for (ElectoralList electoralList : responseScenario.getElectoralLists()) {
            resultAnalyses.put(electoralList.getKey(), buildResultAnalysis(voteSharesAnalysis, electoralList));
        }
        responseScenarioAnalysis.setResultAnalyses(resultAnalyses);
        return responseScenarioAnalysis;
    }

    /**
     * Builds a result analysis for an electoral list in a response scenario.
     *
     * @param voteSharesAnalysis The vote shares analysis for a response scenario.
     * @param electoralList      The electoral list.
     * @return A result analysis for an electoral list in a response scenario.
     */
    private ResultAnalysis buildResultAnalysis(final VoteSharesAnalysis voteSharesAnalysis,
            final ElectoralList electoralList) {
        ResultAnalysis resultAnalysis = new ResultAnalysis();
        SortableProbabilityMassFunction<Range> probabilityMassFunction = voteSharesAnalysis
                .getProbabilityMassFunction(electoralList);
        resultAnalysis.setMedian(probabilityMassFunction.getMedian().getMidpoint() * HUNDRED / POPULATION_SIZE);
        Map<Integer, Float[]> confidenceIntervals = new HashMap<Integer, Float[]>();
        for (Integer level : CONFIDENCE_INTERVAL_LEVELS) {
            Float[] confidenceInterval = new Float[2];
            confidenceInterval[0] = probabilityMassFunction.getConfidenceInterval(level / HUNDRED).getLowerBound()
                    .getLowerBound() * HUNDRED / POPULATION_SIZE;
            confidenceInterval[1] = probabilityMassFunction.getConfidenceInterval(level / HUNDRED).getUpperBound()
                    .getUpperBound() * HUNDRED / POPULATION_SIZE;
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
