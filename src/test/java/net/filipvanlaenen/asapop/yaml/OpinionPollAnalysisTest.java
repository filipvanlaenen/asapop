package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>OpinionPollAnalysis</code> class.
 */
public class OpinionPollAnalysisTest {
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
}
