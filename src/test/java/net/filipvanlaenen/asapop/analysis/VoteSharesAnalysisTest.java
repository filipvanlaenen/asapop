package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     * The vote shares analysis object to run the tests on.
     */
    private static VoteSharesAnalysis voteSharesAnalysis;

    /**
     * Initializes the test objects.
     */
    @BeforeAll
    public static void createVoteSharesAnalysis() {
        voteSharesAnalysis = createVoteSharesAnalysisObject();
    }

    /**
     * Creates a vote shares analysis object.
     *
     * @return A vote shares analysis object.
     */
    public static VoteSharesAnalysis createVoteSharesAnalysisObject() {
        VoteSharesAnalysis voteSharesAnalysisObject = new VoteSharesAnalysis();
        voteSharesAnalysisObject.add(ElectoralList.get("A"), BinomialDistribution.create(1L, FOUR, FIVE));
        voteSharesAnalysisObject.add(ElectoralList.get("B"), BinomialDistribution.create(2L, FOUR, FIVE));
        return voteSharesAnalysisObject;
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
