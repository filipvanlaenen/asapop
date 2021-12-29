package net.filipvanlaenen.asapop.yaml;

import java.util.List;

public class AnalysisResult {
    private List<OpinionPoll> opinionPolls;

    public AnalysisResult() {
    }

    public List<OpinionPoll> getOpinionPolls() {
        return opinionPolls;
    }

    public void setOpinionPolls(List<OpinionPoll> opinionPolls) {
        this.opinionPolls = opinionPolls;
    }
}