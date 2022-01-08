package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ResultAnalysis</code> class.
 */
public class ResultAnalysisTest {
    /**
     * Verifies that the getter method <code>getConfidenceIntervals</code> is wired correctly to the setter method
     * <code>setConfidenceIntervals</code>.
     */
    @Test
    public void getConfidenceIntervalsShouldBeWiredCorrectlyToSetConfidenceIntervals() {
        ResultAnalysis resultAnalysis = new ResultAnalysis();
        Map<Integer, Float[]> confidenceIntervals = Map.of(1, new Float[] {1F, 1F});
        resultAnalysis.setConfidenceIntervals(confidenceIntervals);
        assertEquals(confidenceIntervals, resultAnalysis.getConfidenceIntervals());
    }

    /**
     * Verifies that the getter method <code>getMedian</code> is wired correctly to the setter method
     * <code>setMedian</code>.
     */
    @Test
    public void getMedianShouldBeWiredCorrectlyToSetMedian() {
        ResultAnalysis resultAnalysis = new ResultAnalysis();
        resultAnalysis.setMedian(1F);
        assertEquals(1F, resultAnalysis.getMedian());
    }
}
