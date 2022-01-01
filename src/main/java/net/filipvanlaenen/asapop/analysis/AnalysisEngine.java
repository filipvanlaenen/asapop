package net.filipvanlaenen.asapop.analysis;

import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.ElectionData;

/**
 * Class implementing the engine running the statistical analyses.
 */
public class AnalysisEngine {
    /**
     * The opinion polls to run the statistical analyses on.
     */
    private OpinionPolls opinionPolls;

    /**
     * Constructor taking the opinion polls and election data as its parameters.
     *
     * @param opinionPolls The opinion polls to run the statistical analyses on.
     * @param electionData The election data needed to run the statistical analyses.
     */
    public AnalysisEngine(final OpinionPolls opinionPolls, final ElectionData electionData) {
        this.opinionPolls = opinionPolls;
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
     * Runs the statistical analyses.
     */
    public void run() {
    }
}
