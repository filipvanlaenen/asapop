package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ElectionConfiguration</code> class.
 */
public class ElectionConfigurationTest {
    /**
     * Verifies that the getter method <code>getGitHubWebsiteUrl</code> is wired correctly to the setter method
     * <code>setGitHubWebsiteUrl</code>.
     */
    @Test
    public void getGitHubWebsiteUrlShouldBeWiredCorrectlyToSetGitHubWebsiteUrl() {
        ElectionConfiguration electionConfiguration = new ElectionConfiguration();
        electionConfiguration.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/swedish_polls");
        assertEquals("https://filipvanlaenen.github.io/swedish_polls", electionConfiguration.getGitHubWebsiteUrl());
    }

    /**
     * Verifies that the getter method <code>getNextElectionDate</code> is wired correctly to the setter method
     * <code>setNextElectionDate</code>.
     */
    @Test
    public void getNextElectionDateShouldBeWiredCorrectlyToSetNextElectionDate() {
        ElectionConfiguration electionConfiguration = new ElectionConfiguration();
        electionConfiguration.setNextElectionDate("2022-09-11");
        assertEquals("2022-09-11", electionConfiguration.getNextElectionDate());
    }

    /**
     * Verifies that the getter method <code>getType</code> is wired correctly to the setter method
     * <code>setType</code>.
     */
    @Test
    public void getTypeShouldBeWiredCorrectlyToSetType() {
        ElectionConfiguration electionConfiguration = new ElectionConfiguration();
        electionConfiguration.setType("Parliament");
        assertEquals("Parliament", electionConfiguration.getType());
    }
}
