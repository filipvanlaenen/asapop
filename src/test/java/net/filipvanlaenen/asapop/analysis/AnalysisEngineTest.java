package net.filipvanlaenen.asapop.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.ElectionData;

/**
 * Unit tests on the <code>AnalysisEngine</code> class.
 */
public class AnalysisEngineTest {
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
}
