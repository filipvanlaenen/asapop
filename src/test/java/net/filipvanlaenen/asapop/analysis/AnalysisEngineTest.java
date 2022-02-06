package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

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
     * polls.
     */
    @Test
    public void runCalculatesTheProbabilityMassFunctionsForTheVoteSharesOfTheOpinionPolls() {
        OpinionPoll opinionPoll = new OpinionPoll.Builder().setSampleSize("4").addResult("A", new ResultValue("25"))
                .build();
        OpinionPolls opinionPolls = new OpinionPolls(Set.of(opinionPoll));
        ElectionData electionData = new ElectionData();
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, electionData);
        engine.run();
        VoteSharesAnalysis expected = new VoteSharesAnalysis();
        expected.add(ElectoralList.get("A"),
                SampledHypergeometricDistributions.get(1L, FOUR, TEN_THOUSAND, 36_054_394L));
        assertEquals(expected, engine.getVoteSharesAnalysis(opinionPoll.getMainResponseScenario()));
    }
}
