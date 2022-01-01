package net.filipvanlaenen.asapop.yaml;

import java.util.Set;

/**
 * Class representing the top element for the YAML file containing the analysis of the opinion polls in an ROPF file.
 */
public class Analysis {
    /**
     * The opinon poll analyses.
     */
    private Set<OpinionPollAnalysis> opinionPollAnalyses;

    /**
     * Default constructor.
     */
    public Analysis() {
    }

    /**
     * Returns a set with the opinion poll analyses.
     *
     * @return A set with the opinion poll analyses.
     */
    public Set<OpinionPollAnalysis> getOpinionPollAnalyses() {
        return opinionPollAnalyses;
    }

    /**
     * Sets the set with the opinion poll analyses.
     *
     * @param opinionPollAnalyses The set with the opinion poll analyses.
     */
    public void setOpinionPollAnalyses(final Set<OpinionPollAnalysis> opinionPollAnalyses) {
        this.opinionPollAnalyses = opinionPollAnalyses;
    }
}
