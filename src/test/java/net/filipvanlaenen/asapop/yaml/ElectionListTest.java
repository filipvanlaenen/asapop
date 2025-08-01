package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;

/**
 * Unit tests on the <code>ElectionList</code> class.
 */
public class ElectionListTest {
    /**
     * Verifies that the getter method <code>getDates</code> is wired correctly to the setter method
     * <code>setDates</code>.
     */
    @Test
    public void getDatesShouldBeWiredCorrectlyToSetDates() {
        ElectionList electionList = new ElectionList();
        Map<Integer, String> dates = Map.of(1, "2023-04-26");
        electionList.setDates(dates);
        assertEquals(dates, electionList.getDates());
    }

    /**
     * Verifies that the getter method <code>getGitHubWebsiteUrl</code> is wired correctly to the setter method
     * <code>setGitHubWebsiteUrl</code>.
     */
    @Test
    public void getGitHubWebsiteUrlShouldBeWiredCorrectlyToSetGitHubWebsiteUrl() {
        ElectionList electionList = new ElectionList();
        electionList.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/austrian_polls");
        assertEquals("https://filipvanlaenen.github.io/austrian_polls", electionList.getGitHubWebsiteUrl());
    }
}
