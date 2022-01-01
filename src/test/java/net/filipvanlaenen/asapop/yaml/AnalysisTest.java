package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Analysis</code> class.
 */
public class AnalysisTest {
    /**
     * Verifies that the getter method <code>getOpinionPollAnalyses</code> is wired correctly to the setter method
     * <code>setOpinionPollAnalyses</code>.
     */
    @Test
    public void getOpinionPollAnalysesShouldBeWiredCorrectlyToSetOpinionPollAnalyses() {
        Set<OpinionPollAnalysis> opinionPollAnalyses = Set.of(new OpinionPollAnalysis());
        Analysis analysis = new Analysis();
        analysis.setOpinionPollAnalyses(opinionPollAnalyses);
        assertEquals(opinionPollAnalyses, analysis.getOpinionPollAnalyses());
    }
}
