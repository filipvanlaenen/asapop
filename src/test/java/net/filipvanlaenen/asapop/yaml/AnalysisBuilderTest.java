package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.analysis.AnalysisEngine;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;

/**
 * Unit tests on the <code>AnalysisBuilder</code> class.
 */
public class AnalysisBuilderTest {
    /**
     * Sets the polling firm of an opinion poll.
     */
    @Test
    public void buildingAnAnalysisShouldSetThePollingFirmOfAnOpinionPoll() {
        OpinionPolls opinionPolls = new OpinionPolls(Set.of(new OpinionPoll.Builder().setPollingFirm("ACME").build()));
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, new ElectionData());
        AnalysisBuilder builder = new AnalysisBuilder(engine);
        Analysis analysis = builder.build();
        assertEquals("ACME", analysis.getOpinionPollAnalyses().iterator().next().getPollingFirm());
    }

    /**
     * <code>nullOrToString</code> should return when <code>null</code> is passed as an argument.
     */
    @Test
    public void nullOrToStringShouldReturnNullWhenPassingNull() {
        assertNull(AnalysisBuilder.nullOrToString(null));
    }

    /**
     * <code>nullOrToString</code> should return the result of calling <code>toString</code> on the object passed as an
     * argument when it isn't <code>null</code>.
     */
    @Test
    public void nullOrToStringShouldReturnResultOfCallingToStringWhenPassingNonNullObject() {
        assertEquals("1", AnalysisBuilder.nullOrToString(1));
    }
}
