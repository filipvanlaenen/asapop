package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ElectoralList;

/**
 * Unit tests on the <code>VoteSharesAnalysis</code> class.
 */
public class VoteSharesAnalysisTest {
    /**
     * The magic number four.
     */
    private static final long FOUR = 4L;
    /**
     * The magic number five.
     */
    private static final long FIVE = 5L;
    /**
     * The magic number ten.
     */
    private static final long TEN = 10L;
    /**
     * A probability mass function for testing purposes.
     */
    private static SampledHypergeometricDistribution probabilityMassFunction1;
    /**
     * Another probability mass function for testing purposes.
     */
    private static SampledHypergeometricDistribution probabilityMassFunction2;
    /**
     * The vote shares analysis object to run the tests on.
     */
    private static VoteSharesAnalysis voteSharesAnalysis;

    /**
     * Initializes the VoteSharesAnalysis test object.
     */
    @BeforeAll
    public static void createVoteSharesAnalysis() {
        probabilityMassFunction1 = SampledHypergeometricDistributions.get(1L, FOUR, FIVE, TEN);
        probabilityMassFunction2 = SampledHypergeometricDistributions.get(2L, FOUR, FIVE, TEN);
        voteSharesAnalysis = createVoteSharesAnalysisObject();
    }

    /**
     * Creates a vote shares analysis object.
     *
     * @return A vote shares analysis object.
     */
    public static VoteSharesAnalysis createVoteSharesAnalysisObject() {
        VoteSharesAnalysis voteSharesAnalysisObject = new VoteSharesAnalysis();
        voteSharesAnalysisObject.add(ElectoralList.get("A"), probabilityMassFunction1);
        voteSharesAnalysisObject.add(ElectoralList.get("B"), probabilityMassFunction2);
        return voteSharesAnalysisObject;
    }

    /**
     * Verifies that the correct probability mass function is returned for an electoral list.
     */
    @Test
    public void shouldReturnTheCorrectProbabilityMassFunctionForAnElectoralList() {
        assertEquals(probabilityMassFunction1, voteSharesAnalysis.getProbabilityMassFunction(ElectoralList.get("A")));
    }

    /**
     * Verifies that the electoral lists are returned.
     */
    @Test
    public void shouldReturnTheElectoralLists() {
        assertEquals(Set.of(ElectoralList.get("A"), ElectoralList.get("B")), voteSharesAnalysis.getElectoralLists());
    }

    /**
     * Verifies that the probability mass functions are returned.
     */
    @Test
    public void shouldReturnTheProbabilityMassFunctions() {
        assertTrue(Set
                .of(List.of(probabilityMassFunction1, probabilityMassFunction2),
                        List.of(probabilityMassFunction2, probabilityMassFunction1))
                .contains(voteSharesAnalysis.getProbabilityMassFunctions()));
    }

    /**
     * Verifies that a vote shares analysis is not equal to null.
     */
    @Test
    public void aVoteSharesAnalysisShouldNotBeEqualToNull() {
        assertFalse(voteSharesAnalysis.equals(null));
    }

    /**
     * Verifies that a vote shares analysis is not equal to an object of another class, like a string.
     */
    @Test
    public void aVoteSharesAnalysisShouldNotBeEqualToAString() {
        assertFalse(voteSharesAnalysis.equals(""));
    }

    /**
     * Verifies that a vote shares analysis is equal to itself.
     */
    @Test
    public void aVoteSharesAnalysisShouldBeEqualToItself() {
        assertTrue(voteSharesAnalysis.equals(voteSharesAnalysis));
    }

    /**
     * Verifies that calling hashCode twice on a vote shares analysis returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnAVoteSharesAnalysisReturnsTheSameResult() {
        assertEquals(voteSharesAnalysis.hashCode(), voteSharesAnalysis.hashCode());
    }

    /**
     * Verifies that two vote shares analyses constructed with the same parameter are equal.
     */
    @Test
    public void twoVoteSharesAnalysesConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(voteSharesAnalysis, createVoteSharesAnalysisObject());
    }

    /**
     * Verifies that two vote shares analyses constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoVoteSharesAnalysesConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(voteSharesAnalysis.hashCode(), createVoteSharesAnalysisObject().hashCode());
    }

    /**
     * Verifies that a vote shares analysis object is not equal to an empty one.
     */
    @Test
    public void emptyVoteSharesAnalysisShouldNotBeEqualToTestObject() {
        assertFalse(voteSharesAnalysis.equals(new VoteSharesAnalysis()));
    }

    /**
     * Verifies that an empty vote shares analysis object doesn't have the same hashCode as the test object.
     */
    @Test
    public void emptyVoteSharesAnalysisShouldNotHaveSameHashCodeBeEqualToTestObject() {
        assertFalse(voteSharesAnalysis.hashCode() == new VoteSharesAnalysis().hashCode());
    }
}
