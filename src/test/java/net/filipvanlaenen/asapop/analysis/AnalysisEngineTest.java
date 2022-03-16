package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.asapop.yaml.ElectionData;

/**
 * Unit tests on the <code>AnalysisEngine</code> class.
 */
public class AnalysisEngineTest {
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number ten thousand.
     */
    private static final long TEN_THOUSAND = 10_000L;
    /**
     * The size of the population (the number of voters for the first round of the French presidential election of
     * 2017).
     */
    private static final long POPULATION_SIZE = 36_054_394L;

    /**
     * Verifies that the getter method <code>getOpinionPolls</code> is wired correctly to the constructor.
     */
    @Test
    public void getOpinionPollsShouldBeWiredCorrectlyToTheConstructor() {
        OpinionPoll opinionPoll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        OpinionPolls opinionPolls = new OpinionPolls(Set.of(opinionPoll));
        ElectionData electionData = new ElectionData();
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, electionData);
        assertEquals(opinionPolls, engine.getOpinionPolls());
    }

    /**
     * Verifies that the analysis engine calculates the probability mass functions for the vote shares of the opinion
     * polls based on a sample size with no responses excluded.
     */
    @Test
    public void runShouldCalculateTheVoteShareProbabilityMassFunctionForASampleSizeWithoutExcludedResponses() {
        OpinionPoll opinionPoll = new OpinionPoll.Builder().setSampleSize("4").addResult("A", new ResultValue("25"))
                .build();
        verifyProbabilityMassFunctionCalculationForElectoralListA(opinionPoll,
                SampledHypergeometricDistributions.get(1L, FOUR, TEN_THOUSAND, POPULATION_SIZE));
    }

    /**
     * Verifies that the analysis engine calculates the probability mass functions for the vote shares of the opinion
     * polls based on a sample size and excluded responses.
     */
    @Test
    public void runShouldCalculateTheVoteShareProbabilityMassFunctionForASampleSizeWithExcludedResponses() {
        OpinionPoll opinionPoll = new OpinionPoll.Builder().setSampleSize("5").setExcluded(DecimalNumber.parse("20"))
                .addResult("A", new ResultValue("25")).build();
        verifyProbabilityMassFunctionCalculationForElectoralListA(opinionPoll,
                SampledHypergeometricDistributions.get(1L, FOUR, TEN_THOUSAND, POPULATION_SIZE));
    }

    /**
     * Performs the calculation of the probability mass function for electoral list A.
     *
     * @param opinionPoll             The opinion poll.
     * @param probabilityMassFunction The expected probability mass function.
     */
    private void verifyProbabilityMassFunctionCalculationForElectoralListA(final OpinionPoll opinionPoll,
            final SampledHypergeometricDistribution probabilityMassFunction) {
        OpinionPolls opinionPolls = new OpinionPolls(Set.of(opinionPoll));
        ElectionData electionData = new ElectionData();
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, electionData);
        engine.run();
        VoteSharesAnalysis expected = new VoteSharesAnalysis();
        expected.add(ElectoralList.get("A"), probabilityMassFunction);
        assertEquals(expected, engine.getVoteSharesAnalysis(opinionPoll.getMainResponseScenario()));
    }
}
