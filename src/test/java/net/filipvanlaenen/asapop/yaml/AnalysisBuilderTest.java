package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.analysis.AnalysisEngine;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.asapop.model.Scope;

/**
 * Unit tests on the <code>AnalysisBuilder</code> class.
 */
public class AnalysisBuilderTest {
    /**
     * The area.
     */
    private static final String AREA = "AR";
    /**
     * The name of the commissioner.
     */
    private static final String COMMISSIONER_NAME = "The Times";
    /**
     * The fieldwork end date.
     */
    private static final String FIELDWORK_END = "2022-01-19";
    /**
     * The fieldwork start date.
     */
    private static final String FIELDWORK_START = "2022-01-18";
    /**
     * The name of the polling firm.
     */
    private static final String POLLING_FIRM_NAME = "ACME";
    /**
     * The name of the polling firm partner.
     */
    private static final String POLLING_FIRM_PARTNER_NAME = "BCME";
    /**
     * The publication date.
     */
    private static final String PUBLICATION_DATE = "2022-01-20";
    /**
     * An OpinionPollAnalysis object to run the tests on.
     */
    private static OpinionPollAnalysis opinionPollAnalysis;
    /**
     * A ResponseScenarioAnalysis object to run the tests on.
     */
    private static ResponseScenarioAnalysis responseScenarioAnalysis;
    /**
     * A ResultAnalysis object to run the tests on.
     */
    private static ResultAnalysis resultAnalysis;

    /**
     * Initializes the test objects.
     */
    @BeforeAll
    public static void createAnalysisObject() {
        OpinionPolls opinionPolls = new OpinionPolls(Set.of(new OpinionPoll.Builder().setPollingFirm(POLLING_FIRM_NAME)
                .setPollingFirmPartner(POLLING_FIRM_PARTNER_NAME).addCommissioner(COMMISSIONER_NAME)
                .setFieldworkStart(FIELDWORK_START).setFieldworkEnd(FIELDWORK_END).setPublicationDate(PUBLICATION_DATE)
                .setArea(AREA).setScope(Scope.National).addResult("A", new ResultValue("1")).build()));
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, new ElectionData());
        AnalysisBuilder builder = new AnalysisBuilder(engine);
        Analysis analysis = builder.build();
        opinionPollAnalysis = analysis.getOpinionPollAnalyses().iterator().next();
        responseScenarioAnalysis = opinionPollAnalysis.getResponseScenarioAnalyses().iterator().next();
        resultAnalysis = responseScenarioAnalysis.getResultAnalyses().get("A");
    }

    /**
     * Verifies that the analysis builder sets the area of an opinion poll.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheAreaOfAnOpinionPoll() {
        assertEquals(AREA, responseScenarioAnalysis.getArea());
    }

    /**
     * Verifies that the analysis builder sets the commissioner.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheCommissionerOfAnOpinionPoll() {
        assertEquals(Set.of(COMMISSIONER_NAME), opinionPollAnalysis.getCommissioners());
    }

    /**
     * Verifies that the analysis builder sets the fieldwork end.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheFieldworkEndOfAnOpinionPoll() {
        assertEquals(FIELDWORK_END, opinionPollAnalysis.getFieldworkEnd());
    }

    /**
     * Verifies that the analysis builder sets the fieldwork start.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheFieldworkStartOfAnOpinionPoll() {
        assertEquals(FIELDWORK_START, opinionPollAnalysis.getFieldworkStart());
    }

    /**
     * Verifies that the analysis builder sets the polling firm of an opinion poll.
     */
    @Test
    public void buildingAnAnalysisShouldSetThePollingFirmOfAnOpinionPoll() {
        assertEquals(POLLING_FIRM_NAME, opinionPollAnalysis.getPollingFirm());
    }

    /**
     * Verifies that the analysis builder sets the polling firm of an opinion poll.
     */
    @Test
    public void buildingAnAnalysisShouldSetThePollingFirmPartnerOfAnOpinionPoll() {
        assertEquals(POLLING_FIRM_PARTNER_NAME, opinionPollAnalysis.getPollingFirmPartner());
    }

    /**
     * Verifies that the analysis builder sets the publication date.
     */
    @Test
    public void buildingAnAnalysisShouldSetThePublicationDateOfAnOpinionPoll() {
        assertEquals(PUBLICATION_DATE, opinionPollAnalysis.getPublicationDate());
    }

    /**
     * Verifies that the analysis builder sets the scope of an opinion poll.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheScopeOfAnOpinionPoll() {
        assertEquals(Scope.National.toString(), responseScenarioAnalysis.getScope());
    }

    /**
     * Verifies that the analysis builder sets the median for an electoral list to <code>null</code>.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheMedianOfAnElectoralListInAnOpinionPollToNull() {
        assertNull(resultAnalysis.getMedian());
    }

    /**
     * Verifies that the analysis builder sets the confidence interval for an electoral list to <code>null</code>.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheConfidenceIntervalsOfAnElectoralListInAnOpinionPollToNull() {
        Set<Integer> levels = Set.of(80, 90, 95, 99);
        Map<Integer, Float[]> expected = new HashMap<Integer, Float[]>();
        for (Integer level : levels) {
            expected.put(level, null);
        }
        assertEquals(expected, resultAnalysis.getConfidenceIntervals());
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
