package net.filipvanlaenen.asapop.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.filipvanlaenen.asapop.analysis.AnalysisEngine;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.ResponseScenario;

public class AnalysisBuilder {
    private static final Integer[] CONFIDENCE_INTERVAL_LEVELS = new Integer[] {80, 90, 95, 99};
    private AnalysisEngine engine;

    public AnalysisBuilder(AnalysisEngine engine) {
        this.engine = engine;
    }

    public Analysis build() {
        Analysis result = new Analysis();
        List<OpinionPollAnalysis> opinionPollAnalyses = new ArrayList<OpinionPollAnalysis>();
        for (OpinionPoll poll : engine.getOpinionPolls().getOpinionPolls()) {
            opinionPollAnalyses.add(buildOpinionPollAnalysis(poll));
        }
        result.setOpinionPollAnalyses(opinionPollAnalyses);
        return result;
    }

    private OpinionPollAnalysis buildOpinionPollAnalysis(OpinionPoll poll) {
        OpinionPollAnalysis opinionPollAnalysis = new OpinionPollAnalysis();
        opinionPollAnalysis.setPollingFirm(poll.getPollingFirm());
        opinionPollAnalysis.setPollingFirmPartner(poll.getPollingFirmPartner());
        opinionPollAnalysis.setCommissioners(poll.getCommissioners());
        opinionPollAnalysis.setFieldworkStart(nullOrToString(poll.getFieldworkStart()));
        opinionPollAnalysis.setFieldworkEnd(nullOrToString(poll.getFieldworkEnd()));
        opinionPollAnalysis.setPublicationDate(nullOrToString(poll.getPublicationDate()));
        List<ResponseScenarioAnalysis> responseScenarioAnalyses = new ArrayList<ResponseScenarioAnalysis>();
        responseScenarioAnalyses.add(buildResponseScenarioAnalysis(poll));
        for (ResponseScenario responseScenario : poll.getAlternativeResponseScenarios()) {
            responseScenarioAnalyses.add(buildResponseScenarioAnalysis(responseScenario));
        }
        opinionPollAnalysis.setResponseScenarioAnalyses(responseScenarioAnalyses);
        return opinionPollAnalysis;
    }

    private ResultAnalysis buildOtherAnalysis(ResponseScenario responseScenario) {
        ResultAnalysis resultAnalysis = new ResultAnalysis();
        resultAnalysis.setMedian(responseScenario.getMedianOther());
        Map<Integer, Float[]> confidenceIntervals = new HashMap<Integer, Float[]>();
        for (Integer level : CONFIDENCE_INTERVAL_LEVELS) {
            confidenceIntervals.put(level, responseScenario.getConfidenceIntervalOther());
        }
        resultAnalysis.setConfidenceIntervals(confidenceIntervals);
        return resultAnalysis;
    }

    private ResponseScenarioAnalysis buildResponseScenarioAnalysis(OpinionPoll poll) {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        responseScenarioAnalysis.setArea(poll.getArea());
        responseScenarioAnalysis.setScope(nullOrToString(poll.getScope()));
        Map<String, ResultAnalysis> resultAnalyses = new HashMap<String, ResultAnalysis>();
        for (ElectoralList electoralList : poll.getElectoralLists()) {
            resultAnalyses.put(electoralList.getKey(),
                    buildResultAnalysis(poll.getMainResponseScenario(), electoralList));
        }
        responseScenarioAnalysis.setResultAnalyses(resultAnalyses);
        responseScenarioAnalysis.setOtherAnalysis(buildOtherAnalysis(poll.getMainResponseScenario()));
        return responseScenarioAnalysis;
    }

    private ResponseScenarioAnalysis buildResponseScenarioAnalysis(ResponseScenario responseScenario) {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        responseScenarioAnalysis.setArea(responseScenario.getArea());
        responseScenarioAnalysis.setScope(nullOrToString(responseScenario.getScope()));
        Map<String, ResultAnalysis> resultAnalyses = new HashMap<String, ResultAnalysis>();
        for (ElectoralList electoralList : responseScenario.getElectoralLists()) {
            resultAnalyses.put(electoralList.getKey(), buildResultAnalysis(responseScenario, electoralList));
        }
        responseScenarioAnalysis.setResultAnalyses(resultAnalyses);
        responseScenarioAnalysis.setOtherAnalysis(buildOtherAnalysis(responseScenario));
        return responseScenarioAnalysis;
    }

    private ResultAnalysis buildResultAnalysis(ResponseScenario responseScenario, ElectoralList electoralList) {
        ResultAnalysis resultAnalysis = new ResultAnalysis();
        resultAnalysis.setMedian(responseScenario.getMedian(electoralList));
        Map<Integer, Float[]> confidenceIntervals = new HashMap<Integer, Float[]>();
        for (Integer level : CONFIDENCE_INTERVAL_LEVELS) {
            confidenceIntervals.put(level, responseScenario.getConfidenceInterval(electoralList));
        }
        resultAnalysis.setConfidenceIntervals(confidenceIntervals);
        return resultAnalysis;
    }

    private String nullOrToString(Object object) {
        return object == null ? null : object.toString();
    }
}
