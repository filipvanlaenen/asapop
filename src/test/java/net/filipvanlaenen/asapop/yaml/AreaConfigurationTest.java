package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>AreaConfiguration</code> class.
 */
public class AreaConfigurationTest {
    /**
     * Verifies that the getter method <code>getGitHubWebsiteUrl</code> is wired correctly to the setter method
     * <code>setGitHubWebsiteUrl</code>.
     */
    @Test
    public void getGitHubWebsiteUrlShouldBeWiredCorrectlyToSetGitHubWebsiteUrl() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        areaConfiguration.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/swedish_polls");
        assertEquals("https://filipvanlaenen.github.io/swedish_polls", areaConfiguration.getGitHubWebsiteUrl());
    }

    /**
     * Verifies that the getter method <code>getNextElectionDate</code> is wired correctly to the setter method
     * <code>setNextElectionDate</code>.
     */
    @Test
    public void getNextElectionDateShouldBeWiredCorrectlyToSetNextElectionDate() {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        areaConfiguration.setNextElectionDate("2022-09-11");
        assertEquals("2022-09-11", areaConfiguration.getNextElectionDate());
    }
}
