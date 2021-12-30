package net.filipvanlaenen.asapop.yaml;

import java.util.List;

public class Analysis {
    private List<OpinionPollAnalysis> opinionPollAnalyses;

    public Analysis() {
    }

    public List<OpinionPollAnalysis> getOpinionPollAnalyses() {
        return opinionPollAnalyses;
    }

    public void setOpinionPollAnalyses(List<OpinionPollAnalysis> opinionPollAnalyses) {
        this.opinionPollAnalyses = opinionPollAnalyses;
    }
}