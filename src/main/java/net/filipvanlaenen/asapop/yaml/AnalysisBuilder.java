package net.filipvanlaenen.asapop.yaml;

import java.util.ArrayList;
import java.util.List;

import net.filipvanlaenen.asapop.analysis.AnalysisEngine;
import net.filipvanlaenen.asapop.model.OpinionPoll;

public class AnalysisBuilder {
    private AnalysisEngine engine;

    public AnalysisBuilder(AnalysisEngine engine) {
        this.engine = engine;
    }

    public Analysis build() {
        Analysis result = new Analysis();
        List<OpinionPollAnalysis> opinionPollAnalyses = new ArrayList<OpinionPollAnalysis>();
        for (OpinionPoll poll : engine.getOpinionPolls().getOpinionPolls()) {
            opinionPollAnalyses.add(new OpinionPollAnalysis());
        }
        result.setOpinionPollAnalyses(opinionPollAnalyses);
        return result;
    }
}
