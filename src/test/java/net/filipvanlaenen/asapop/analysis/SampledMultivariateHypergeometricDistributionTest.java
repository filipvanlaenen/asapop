package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.asapop.yaml.ElectionData;

/**
 * Unit tests on the <code>SampledMultivariateHypergeometricDistribution</code> class.
 */
public class SampledMultivariateHypergeometricDistributionTest {
    /**
     * Precision for floating point assertions where high precision can be expected.
     */
    private static final double SMALL_DELTA = 1E-6;
    /**
     * The magic number one third.
     */
    private static final double ONE_THIRD = 0.3333333D;

    /**
     * Verifies that when there are two candidates with the same support, these candidates are certain to win the first
     * round.
     */
    @Test
    public void twoCandidatesWithTheSameSupportShouldBeCertainToWin() {
        FirstRoundWinnersAnalysis firstRoundWinnersAnalysis = runSimulations("30", "30");
        assertEquals(1,
                firstRoundWinnersAnalysis.getProbabilityMass(Set.of(ElectoralList.get("L1"), ElectoralList.get("L2"))));
    }

    /**
     * Verifies that when there are three candidates with the same support, a pair of two of these candidates has a
     * probability of one third to win the first round.
     */
    @Test
    public void threeCandidatesWithTheSameSupportShouldEachHaveAProbabilityOfOneThirdToWin() {
        FirstRoundWinnersAnalysis firstRoundWinnersAnalysis = runSimulations("30", "30", "30");
        assertEquals(ONE_THIRD,
                firstRoundWinnersAnalysis.getProbabilityMass(Set.of(ElectoralList.get("L1"), ElectoralList.get("L2"))),
                SMALL_DELTA);
    }

    /**
     * Verifies that when there are three candidates with almost the same support, a pair of two largest candidates has
     * a probability of more than one third to win the first round.
     */
    @Test
    public void twoLargestOfThreeCandidatesWithTheAlmostSameSupportShouldHaveAProbabilityAboveOneThirdToWin() {
        FirstRoundWinnersAnalysis firstRoundWinnersAnalysis = runSimulations("31", "30", "29");
        assertTrue(ONE_THIRD < firstRoundWinnersAnalysis
                .getProbabilityMass(Set.of(ElectoralList.get("L1"), ElectoralList.get("L2"))));
    }

    /**
     * Runs simulations based on a set of results.
     *
     * @param values The results to run the simulations on.
     * @return The resulting first round winners analysis.
     */
    private FirstRoundWinnersAnalysis runSimulations(String... values) {
        OpinionPoll.Builder opinionPollBuilder = new OpinionPoll.Builder().setPollingFirm("ACME")
                .setScope(Scope.PresidentialFirstRound).setFieldworkEnd("2022-03-29").setSampleSize("500");
        int i = 0;
        for (String value : values) {
            opinionPollBuilder.addResult("L" + (++i), new ResultValue(value));
        }
        OpinionPoll opinionPoll = opinionPollBuilder.build();
        OpinionPolls opinionPolls = new OpinionPolls(Set.of(opinionPoll));
        ElectionData electionData = new ElectionData();
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, electionData);
        engine.run(10000);
        FirstRoundWinnersAnalysis firstRoundWinnersAnalysis = engine
                .getFirstRoundWinnersAnalysis(opinionPoll.getMainResponseScenario());
        return firstRoundWinnersAnalysis;
    }
}
