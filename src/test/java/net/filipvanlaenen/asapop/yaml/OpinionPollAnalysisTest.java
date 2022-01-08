package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>OpinionPollAnalysis</code> class.
 */
public class OpinionPollAnalysisTest {
    /**
     * Verifies that the getter method <code>getCommissioners</code> is wired correctly to the setter method
     * <code>setCommissioners</code>.
     */
    @Test
    public void getCommissionersShouldBeWiredCorrectlyToSetCommissioners() {
        OpinionPollAnalysis opinionPollAnalysis = new OpinionPollAnalysis();
        Set<String> commissioners = Set.of("The Times");
        opinionPollAnalysis.setCommissioners(commissioners);
        assertEquals(commissioners, opinionPollAnalysis.getCommissioners());
    }

    /**
     * Verifies that the getter method <code>getFieldworkEnd</code> is wired correctly to the setter method
     * <code>setFieldworkEnd</code>.
     */
    @Test
    public void getFieldworkEndShouldBeWiredCorrectlyToSetFieldworkEnd() {
        OpinionPollAnalysis opinionPollAnalysis = new OpinionPollAnalysis();
        opinionPollAnalysis.setFieldworkEnd("2022-01-08");
        assertEquals("2022-01-08", opinionPollAnalysis.getFieldworkEnd());
    }

    /**
     * Verifies that the getter method <code>getFieldworkStart</code> is wired correctly to the setter method
     * <code>setFieldworkStart</code>.
     */
    @Test
    public void getFieldworkStartShouldBeWiredCorrectlyToSetFieldworkStart() {
        OpinionPollAnalysis opinionPollAnalysis = new OpinionPollAnalysis();
        opinionPollAnalysis.setFieldworkStart("2022-01-08");
        assertEquals("2022-01-08", opinionPollAnalysis.getFieldworkStart());
    }

    /**
     * Verifies that the getter method <code>getPollingFirm</code> is wired correctly to the setter method
     * <code>setPollingFirm</code>.
     */
    @Test
    public void getPollingFirmShouldBeWiredCorrectlyToSetPollingFirm() {
        OpinionPollAnalysis opinionPollAnalysis = new OpinionPollAnalysis();
        opinionPollAnalysis.setPollingFirm("ACME");
        assertEquals("ACME", opinionPollAnalysis.getPollingFirm());
    }

    /**
     * Verifies that the getter method <code>getPollingFirmPartner</code> is wired correctly to the setter method
     * <code>setPollingFirmPartner</code>.
     */
    @Test
    public void getPollingFirmPartnerShouldBeWiredCorrectlyToSetPollingFirmPartner() {
        OpinionPollAnalysis opinionPollAnalysis = new OpinionPollAnalysis();
        opinionPollAnalysis.setPollingFirmPartner("ACME");
        assertEquals("ACME", opinionPollAnalysis.getPollingFirmPartner());
    }

    /**
     * Verifies that the getter method <code>getPublicationDate</code> is wired correctly to the setter method
     * <code>setPublicationDate</code>.
     */
    @Test
    public void getPublicationDateShouldBeWiredCorrectlyToSetPublicationDate() {
        OpinionPollAnalysis opinionPollAnalysis = new OpinionPollAnalysis();
        opinionPollAnalysis.setPublicationDate("2022-01-08");
        assertEquals("2022-01-08", opinionPollAnalysis.getPublicationDate());
    }

    /**
     * Verifies that the getter method <code>getResponseScenarioAnalyses</code> is wired correctly to the setter method
     * <code>setResponseScenarioAnalyses</code>.
     */
    @Test
    public void getResponseScenarioAnalysesShouldBeWiredCorrectlyToSetResponseScenarioAnalyses() {
        OpinionPollAnalysis opinionPollAnalysis = new OpinionPollAnalysis();
        Set<ResponseScenarioAnalysis> responseScenarioAnalyses = Set.of(new ResponseScenarioAnalysis());
        opinionPollAnalysis.setResponseScenarioAnalyses(responseScenarioAnalyses);
        assertEquals(responseScenarioAnalyses, opinionPollAnalysis.getResponseScenarioAnalyses());
    }
}
