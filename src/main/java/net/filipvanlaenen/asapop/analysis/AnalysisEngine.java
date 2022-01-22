package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
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
    private final Map<OpinionPoll, VoteShareAnalysis> voteShareAnalyses = new HashMap<OpinionPoll, VoteShareAnalysis>();
    private int populationSize = 5;

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
        for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
            VoteShareAnalysis voteShareAnalysis = new VoteShareAnalysis();
            for (ElectoralList electoralList : opinionPoll.getElectoralLists()) {
                ProbabilityMassFunction pmf = new ProbabilityMassFunction();
                Integer sampleSize = opinionPoll.getSampleSizeValue();
                Long sampled = Math
                        .round(Double.parseDouble(opinionPoll.getResult(electoralList.getKey()).getPrimitiveText())
                                * sampleSize / 100);
                // TODO: Large population sizes
                for (int i = 0; i <= populationSize; i++) {
                    pmf.add(i, binomialCoefficient(i, sampled).multiply(
                            binomialCoefficient(populationSize - i, sampleSize - sampled), MathContext.DECIMAL128));
                }
                voteShareAnalysis.add(electoralList, pmf);
            }
            // TODO: Add Other too
            voteShareAnalyses.put(opinionPoll, voteShareAnalysis);
        }
    }

    /**
     * Calculates the binomial coefficient <i>C(n,k)</i>.
     * 
     * @param n The parameter <i>n</i> of the binomial coefficient <i>C(n,k)</i>.
     * @param k The parameter <i>k</i> of the binomial coefficient <i>C(n,k)</i>.
     * @return The binomial coefficient <i>C(n,k)</i>
     */
    private BigDecimal binomialCoefficient(long n, long k) {
        // TODO: Product of quotients or quotient of products?
        BigDecimal p = BigDecimal.ONE;
        for (int i = 1; i <= k; i++) {
            p = p.multiply(new BigDecimal(n + 1 - i), MathContext.DECIMAL128).divide(new BigDecimal(i),
                    MathContext.DECIMAL128);
        }
        return p;
    }

    VoteShareAnalysis getVoteShareAnalysis(final OpinionPoll opinionPoll) {
        return voteShareAnalyses.get(opinionPoll);
    }
}
