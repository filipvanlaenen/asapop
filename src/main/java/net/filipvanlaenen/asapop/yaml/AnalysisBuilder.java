package net.filipvanlaenen.asapop.yaml;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import net.filipvanlaenen.asapop.analysis.AnalysisEngine;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.ResponseScenario;

public class AnalysisBuilder {
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

    private ResponseScenarioAnalysis buildResponseScenarioAnalysis(OpinionPoll poll) {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        responseScenarioAnalysis.setArea(poll.getArea());
        responseScenarioAnalysis.setScope(nullOrToString(poll.getScope()));
        // TODO: results
        // TODO: other
        return responseScenarioAnalysis;
    }

    private ResponseScenarioAnalysis buildResponseScenarioAnalysis(ResponseScenario responseScenario) {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        responseScenarioAnalysis.setArea(responseScenario.getArea());
        responseScenarioAnalysis.setScope(nullOrToString(responseScenario.getScope()));
        // TODO: results
        // TODO: other
        return responseScenarioAnalysis;
    }

    private String nullOrToString(Object object) {
        return object == null ? null : object.toString();
    }
}
