package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.yaml.ElectionData;

/**
 * Class implementing the engine running the statistical analyses.
 */
public class AnalysisEngine {
    /**
     * The magic number hundred.
     */
    private static final double HUNDRED = 100D;
    /**
     * The magic number ten thousand, the default number of samples.
     */
    private static final long TEN_THOUSAND = 10_000L;
    /**
     * The size of the population (the number of voters for the first round of the French presidential election of
     * 2017).
     */
    private static final long POPULATION_SIZE = 36_054_394L;
    /**
     * The opinion polls to run the statistical analyses on.
     */
    private OpinionPolls opinionPolls;
    /**
     * A map containing the vote shares analysis per response scenario.
     */
    private final Map<ResponseScenario, VoteSharesAnalysis> voteSharesAnalyses;
    /**
     * A map containing the first round winners analysis per response scenario.
     */
    private final Map<ResponseScenario, FirstRoundWinnersAnalysis> firstRoundWinnersAnalyses;

    /**
     * Constructor taking the opinion polls and election data as its parameters.
     *
     * @param opinionPolls The opinion polls to run the statistical analyses on.
     * @param electionData The election data needed to run the statistical analyses.
     */
    public AnalysisEngine(final OpinionPolls opinionPolls, final ElectionData electionData) {
        this.opinionPolls = opinionPolls;
        voteSharesAnalyses = new HashMap<ResponseScenario, VoteSharesAnalysis>();
        firstRoundWinnersAnalyses = new HashMap<ResponseScenario, FirstRoundWinnersAnalysis>();
    }

    public FirstRoundWinnersAnalysis getFirstRoundWinnersAnalysis(final ResponseScenario responseScenario) {
        return firstRoundWinnersAnalyses.get(responseScenario);
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
     * Returns the vote shares analysis for a response scenario.
     *
     * @param responseScenario The response scenario for which to return the vote shares analysis.
     * @return The vote shares analysis for the opinion poll.
     */
    public VoteSharesAnalysis getVoteSharesAnalysis(final ResponseScenario responseScenario) {
        return voteSharesAnalyses.get(responseScenario);
    }

    /**
     * Runs the statistical analyses.
     */
    public void run() {
        System.out.println("Number of opinion polls: " + opinionPolls.getOpinionPolls().size());
        for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
            System.out.println(opinionPoll.getFieldworkEnd() + " " + opinionPoll.getPollingFirm());
            Integer effectiveSampleSize = opinionPoll.getEffectiveSampleSize();
            if (effectiveSampleSize != null) {
                VoteSharesAnalysis voteShareAnalysis = new VoteSharesAnalysis();
                for (ElectoralList electoralList : opinionPoll.getElectoralLists()) {
                    Long sampled = Math
                            .round(Double.parseDouble(opinionPoll.getResult(electoralList.getKey()).getPrimitiveText())
                                    * effectiveSampleSize / HUNDRED);
                    voteShareAnalysis.add(electoralList, SampledHypergeometricDistributions.get(sampled,
                            (long) effectiveSampleSize, TEN_THOUSAND, POPULATION_SIZE));
                }
                voteSharesAnalyses.put(opinionPoll.getMainResponseScenario(), voteShareAnalysis);
                List<SampledHypergeometricDistribution> probabilityMassFunctions = voteShareAnalysis
                        .getProbabilityMassFunctions();
                SampledMultivariateHypergeometricDistribution sampledMultivariateHypergeometricDistribution = SampledMultivariateHypergeometricDistributions
                        .get(probabilityMassFunctions);
                FirstRoundWinnersAnalysis frwa = new FirstRoundWinnersAnalysis(voteShareAnalysis,
                        sampledMultivariateHypergeometricDistribution);
                firstRoundWinnersAnalyses.put(opinionPoll.getMainResponseScenario(), frwa);
                for (Set<ElectoralList> electoralListSet : frwa.getElectoralListSets()) {
                    String s = "";
                    for (ElectoralList electoralList : electoralListSet) {
                        s += electoralList.getKey() + ", ";
                    }
                    double p = frwa.getProbabilityMass(electoralListSet);
                    if (p > 0.001D) {
                        System.out.println(s + String.format("%,.2f", p) + "%");
                    }
                }
                System.out.println();
                /**
                 * Below to be deleted.
                 */
                Map<ElectoralList, List<Range>> ranges = new HashMap<ElectoralList, List<Range>>();
                List<Long> lowerBounds = new ArrayList<Long>();
                for (ElectoralList electoralList : opinionPoll.getElectoralLists()) {
                    lowerBounds.add(voteShareAnalysis.getProbabilityMassFunction(electoralList)
                            .getConfidenceInterval(0.999999).getLowerBound().getLowerBound());
                }
                if (lowerBounds.size() > 2) {
                    Collections.sort(lowerBounds);
                    Collections.reverse(lowerBounds);
                    long lowerBound = lowerBounds.get(1);
                    double others = 100D;
                    for (ElectoralList electoralList : opinionPoll.getElectoralLists()) {
                        if (voteShareAnalysis.getProbabilityMassFunction(electoralList).getConfidenceInterval(0.999999)
                                .getUpperBound().getUpperBound() >= lowerBound) {
                            ranges.put(electoralList, voteShareAnalysis.getProbabilityMassFunction(electoralList)
                                    .getConfidenceIntervalKeyList(0.999999));
                            others -= Double
                                    .parseDouble(opinionPoll.getResult(electoralList.getKey()).getPrimitiveText());
                        }
                    }
                    Long sampled = Math.round(others * effectiveSampleSize / HUNDRED);
                    SampledHypergeometricDistribution pmfOther = SampledHypergeometricDistributions.get(sampled,
                            (long) effectiveSampleSize, TEN_THOUSAND, POPULATION_SIZE);
                    List<Range> rangesOther = pmfOther.getConfidenceIntervalKeyList(0.999999);
                    // TODO: Remove ranges that are so high that they're guaranteed to be in 1st or 2nd place
                    // TODO: Produce a generator to iterate, with widened ranges list if needed
                    Map<Set<ElectoralList>, BigDecimal> pmf = new HashMap<Set<ElectoralList>, BigDecimal>();
                    // TODO: Eliminate use of random
                    Random random = new Random();
                    long i = 0;
                    while (i < 2000000) {
                        ElectoralList first = null;
                        ElectoralList second = null;
                        Range a = null;
                        Range b = null;
                        BigDecimal p = BigDecimal.ONE;
                        long m = 0;
                        for (ElectoralList electoralList : ranges.keySet()) {
                            List<Range> r = ranges.get(electoralList);
                            Range value = r.get(random.nextInt(r.size()));
                            m += value.getMidpoint();
                            p = p.multiply(voteShareAnalysis.getProbabilityMassFunction(electoralList)
                                    .getProbabilityMass(value), MathContext.DECIMAL128);
                            if (a == null) {
                                first = electoralList;
                                a = value;
                            } else if (b == null) {
                                if (a.compareTo(value) > 0) {
                                    second = electoralList;
                                    b = value;
                                } else {
                                    second = first;
                                    b = a;
                                    first = electoralList;
                                    a = value;
                                }
                            } else if (a.compareTo(value) < 0) {
                                second = first;
                                b = a;
                                first = electoralList;
                                a = value;
                            } else if (b.compareTo(value) < 0) {
                                second = electoralList;
                                b = value;
                            }
                        }
                        if (m < POPULATION_SIZE) {
                            Long o = POPULATION_SIZE - m;
                            if (rangesOther.get(rangesOther.size() - 1).getUpperBound() > o) {
                                Range otherRange = rangesOther.get(0);
                                for (Range r : rangesOther) {
                                    if (r.getUpperBound() > o) {
                                        otherRange = r;
                                    }
                                }
                                p = p.multiply(pmfOther.getProbabilityMass(otherRange), MathContext.DECIMAL128);
                                Set<ElectoralList> runoff = Set.of(first, second);
                                if (pmf.containsKey(runoff)) {
                                    pmf.put(runoff, p.add(pmf.get(runoff), MathContext.DECIMAL128));
                                } else {
                                    pmf.put(runoff, p);
                                }
                                i += 1;
                            }
                        }
                    }
                    // TODO: Store the resulting PMF
                    BigDecimal sum = BigDecimal.ZERO;
                    for (BigDecimal bd : pmf.values()) {
                        sum = sum.add(bd, MathContext.DECIMAL128);
                    }
                    sum = sum.divide(new BigDecimal(100), MathContext.DECIMAL128);
                    for (Set<ElectoralList> runoff : pmf.keySet()) {
                        String s = "";
                        for (ElectoralList el : runoff) {
                            s += el.getKey() + ", ";
                        }
                        double p = pmf.get(runoff).divide(sum, MathContext.DECIMAL128).doubleValue();
                        if (p > 0.001D) {
                            System.out.println(s + String.format("%,.2f", p) + "%");
                        }
                    }
                    System.out.println();
                }
                /**
                 * End of code to be deleted.
                 */
            }
        }
    }
}
