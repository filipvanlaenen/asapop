package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.asapop.yaml.ElectionData;

/**
 * Unit tests on the <code>AnalysisEngine</code> class.
 */
public class AnalysisEngineTest {
    /**
     * Precision for floating point assertions.
     */
    private static final double DELTA = 1E-6;
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number one third (as a percentage).
     */
    private static final double ONE_THIRD = 0.3333333D;
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

    /**
     * Verifies that when only one poll is registered, it is returned as the most recent poll.
     */
    @Test
    public void shouldReturnSinglePollAsMostRecentPoll() {
        OpinionPoll opinionPoll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2022-03-29")
                .setSampleSize("5").setExcluded(DecimalNumber.parse("20")).addResult("A", new ResultValue("25"))
                .build();
        OpinionPolls opinionPolls = new OpinionPolls(Set.of(opinionPoll));
        ElectionData electionData = new ElectionData();
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, electionData);
        assertEquals(Set.of(opinionPoll), new HashSet<OpinionPoll>(engine.calculateMostRecentPolls()));
    }

    /**
     * Verifies that when two polls by the same polling firm are registered, the most recent one is returned as the most
     * recent poll.
     */
    @Test
    public void shouldReturnMostRecentOfTwoOpinionPolls() {
        OpinionPoll opinionPoll1 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2022-03-29")
                .setSampleSize("5").setExcluded(DecimalNumber.parse("20")).addResult("A", new ResultValue("25"))
                .build();
        OpinionPoll opinionPoll2 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2022-03-28")
                .setSampleSize("5").setExcluded(DecimalNumber.parse("20")).addResult("A", new ResultValue("25"))
                .build();
        OpinionPolls opinionPolls = new OpinionPolls(Set.of(opinionPoll1, opinionPoll2));
        ElectionData electionData = new ElectionData();
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, electionData);
        assertEquals(Set.of(opinionPoll1), new HashSet<OpinionPoll>(engine.calculateMostRecentPolls()));
    }

    /**
     * Verifies that when two polls by different polling firms are registered, both are returned as the most recent
     * polls.
     */
    @Test
    public void shouldReturnMostRecentOpinionPollsOfBothPollingFirms() {
        OpinionPoll opinionPoll1 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2022-03-29")
                .setSampleSize("5").setExcluded(DecimalNumber.parse("20")).addResult("A", new ResultValue("25"))
                .build();
        OpinionPoll opinionPoll2 = new OpinionPoll.Builder().setPollingFirm("BCME").setFieldworkEnd("2022-03-28")
                .setSampleSize("5").setExcluded(DecimalNumber.parse("20")).addResult("A", new ResultValue("25"))
                .build();
        OpinionPolls opinionPolls = new OpinionPolls(Set.of(opinionPoll1, opinionPoll2));
        ElectionData electionData = new ElectionData();
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, electionData);
        assertEquals(Set.of(opinionPoll1, opinionPoll2), new HashSet<OpinionPoll>(engine.calculateMostRecentPolls()));
    }

    /**
     * Verifies that when two polls by the same polling firm are registered with the same end date, both are returned as
     * the most recent polls.
     */
    @Test
    public void shouldReturnBothRecentOpinionPollsOfPollingFirm() {
        OpinionPoll opinionPoll1 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2022-03-29")
                .setSampleSize("5").setExcluded(DecimalNumber.parse("20")).addResult("A", new ResultValue("25"))
                .build();
        OpinionPoll opinionPoll2 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2022-03-29")
                .setSampleSize("5").setExcluded(DecimalNumber.parse("20")).addResult("A", new ResultValue("26"))
                .build();
        OpinionPolls opinionPolls = new OpinionPolls(Set.of(opinionPoll1, opinionPoll2));
        ElectionData electionData = new ElectionData();
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, electionData);
        assertEquals(Set.of(opinionPoll1, opinionPoll2), new HashSet<OpinionPoll>(engine.calculateMostRecentPolls()));
    }

    /**
     * Verifies that the first round winners analysis is calculated for an opinion poll scoped for the first round of a
     * presidential election.
     */
    @Test
    public void shouldCalculateFirstRoundWinnersAnalysisForFirstRoundOpinionPoll() {
        OpinionPoll opinionPoll = new OpinionPoll.Builder().setPollingFirm("ACME")
                .setScope(Scope.PresidentialFirstRound).setFieldworkEnd("2022-03-29").setSampleSize("500")
                .addResult("A", new ResultValue("30")).addResult("B", new ResultValue("30"))
                .addResult("C", new ResultValue("30")).build();
        OpinionPolls opinionPolls = new OpinionPolls(Set.of(opinionPoll));
        ElectionData electionData = new ElectionData();
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, electionData);
        engine.run(1);
        FirstRoundWinnersAnalysis firstRoundWinnersAnalysis = engine
                .getFirstRoundWinnersAnalysis(opinionPoll.getMainResponseScenario());
        assertEquals(ONE_THIRD,
                firstRoundWinnersAnalysis.getProbabilityMass(Set.of(ElectoralList.get("A"), ElectoralList.get("B"))),
                DELTA);
    }
}
