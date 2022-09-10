package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ElectoralList;

/**
 * Unit tests on the <code>FirstRoundWinnersAnalysis</code> class.
 */
public class FirstRoundWinnersAnalysisTest {
    /**
     * The number of iterations in the multivariate hypergeometric distribution.
     */
    private static final long NUMBER_OF_ITERATIONS = 20_000L;
    /**
     * The number of samples in the hypergeometric distributions.
     */
    private static final long NUMBER_OF_SAMPLES = 2_000L;
    /**
     * The sample size for the polls.
     */
    private static final long SAMPLE_SIZE = 1000L;
    /**
     * The population size of the polls.
     */
    private static final long POPULATION_SIZE = 10_001L;
    /**
     * Precision for floating point assertions where moderate precision can be expected.
     */
    private static final double MODERATE_DELTA = 1E-4;
    /**
     * The magic number one half.
     */
    private static final double ONE_HALF = 0.5D;
    /**
     * The magic number one hundred.
     */
    private static final long ONE_HUNDRED = 100L;
    /**
     * The magic number five hundred.
     */
    private static final long FIVE_HUNDRED = 500L;
    /**
     * The magic number seven hundred.
     */
    private static final long SEVEN_HUNDRED = 700L;

    /**
     * Verifies that when one of two candidates is a clear winner, the calculation results are mapped correctly.
     */
    @Test
    public void shouldMapTheCalculationResultsCorrectlyForAClearWinner() {
        FirstRoundWinnersAnalysis firstRoundWinnersAnalysis =
                createFirstRoundWinnersAnalysis(SEVEN_HUNDRED, ONE_HUNDRED);
        assertEquals(Set.of(Set.of(ElectoralList.get("L1"))), firstRoundWinnersAnalysis.getElectoralListSets());
        assertEquals(1D, firstRoundWinnersAnalysis.getProbabilityMass(Set.of(ElectoralList.get("L1"))));
        assertEquals(0D, firstRoundWinnersAnalysis.getProbabilityMass(Set.of(ElectoralList.get("L2"))));
        assertEquals(0D,
                firstRoundWinnersAnalysis.getProbabilityMass(Set.of(ElectoralList.get("L1"), ElectoralList.get("L2"))));
    }

    /**
     * Verifies that when one of two candidates has fifty percent support, the calculation results are mapped correctly.
     */
    @Test
    public void shouldMapTheCalculationResultsCorrectlyForTwoListsWithOneHavingFiftyPercentSupport() {
        FirstRoundWinnersAnalysis firstRoundWinnersAnalysis =
                createFirstRoundWinnersAnalysis(FIVE_HUNDRED, ONE_HUNDRED);
        assertEquals(Set.of(Set.of(ElectoralList.get("L1")), Set.of(ElectoralList.get("L1"), ElectoralList.get("L2"))),
                firstRoundWinnersAnalysis.getElectoralListSets());
        assertEquals(ONE_HALF, firstRoundWinnersAnalysis.getProbabilityMass(Set.of(ElectoralList.get("L1"))),
                MODERATE_DELTA);
        assertEquals(0D, firstRoundWinnersAnalysis.getProbabilityMass(Set.of(ElectoralList.get("L2"))));
        assertEquals(ONE_HALF,
                firstRoundWinnersAnalysis.getProbabilityMass(Set.of(ElectoralList.get("L1"), ElectoralList.get("L2"))),
                MODERATE_DELTA);
    }

    /**
     * Creates a first round winners analysis object.
     *
     * @param values The sample values for the candidates.
     * @return A first round winners analysis object.
     */
    private FirstRoundWinnersAnalysis createFirstRoundWinnersAnalysis(final long... values) {
        List<SampledHypergeometricDistribution> pmfs = createProbabilityMassFunctions(values);
        SampledMultivariateHypergeometricDistribution multivariateDistribution =
                SampledMultivariateHypergeometricDistributions.get(pmfs, POPULATION_SIZE, SAMPLE_SIZE,
                        NUMBER_OF_ITERATIONS);
        VoteSharesAnalysis voteSharesAnalysis = createVoteSharesAnalysisObject(pmfs);
        FirstRoundWinnersAnalysis firstRoundWinnersAnalysis =
                new FirstRoundWinnersAnalysis(voteSharesAnalysis, multivariateDistribution);
        return firstRoundWinnersAnalysis;
    }

    /**
     * Creates a vote shares analysis object.
     *
     * @param pmfs The probability mass functions for the candidates.
     * @return A vote shares analysis object.
     */
    private static VoteSharesAnalysis createVoteSharesAnalysisObject(
            final List<SampledHypergeometricDistribution> pmfs) {
        VoteSharesAnalysis voteSharesAnalysisObject = new VoteSharesAnalysis();
        for (int i = 0; i < pmfs.size(); i++) {
            voteSharesAnalysisObject.add(ElectoralList.get("L" + (i + 1)), pmfs.get(i));
        }
        return voteSharesAnalysisObject;
    }

    /**
     * Creates a list of probability mass functions based on a set of values.
     *
     * @param values The sample values for the candidates.
     * @return A list of probability mass functions based on a set of values.
     */
    private static List<SampledHypergeometricDistribution> createProbabilityMassFunctions(final long... values) {
        List<SampledHypergeometricDistribution> probabilityMassFunctions;
        probabilityMassFunctions = new ArrayList<SampledHypergeometricDistribution>();
        for (long value : values) {
            probabilityMassFunctions.add(
                    SampledHypergeometricDistributions.get(value, SAMPLE_SIZE, NUMBER_OF_SAMPLES, POPULATION_SIZE));
        }
        return probabilityMassFunctions;
    }
}
