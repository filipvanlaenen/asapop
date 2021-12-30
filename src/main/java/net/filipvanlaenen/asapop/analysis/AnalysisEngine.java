package net.filipvanlaenen.asapop.analysis;

import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.ElectionData;

public class AnalysisEngine {
    private OpinionPolls opinionPolls;

    public AnalysisEngine(OpinionPolls opinionPolls, ElectionData electionData) {
        this.opinionPolls = opinionPolls;
    }

    public OpinionPolls getOpinionPolls() {
        return opinionPolls;
    }

    public void run() {
    }
}