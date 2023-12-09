package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ElectionType;
import net.filipvanlaenen.asapop.model.Elections;

/**
 * Unit tests on the <code>ElectionsBuilder</code> class.
 */
public class ElectionsBuilderTest {
    /**
     * The area code for the unit tests.
     */
    private static final String AREA_CODE = "bg";
    /**
     * The election date for the unit tests.
     */
    private static final String ELECTION_DATE = "2023-04-29";

    /**
     * Verifies that an empty instance of elections is extracted from an empty website configuration.
     */
    @Test
    public void shouldExtractEmptyElectionsFromEmptyWebsiteConfiguration() {
        Elections elections = ElectionsBuilder.extractElections(new WebsiteConfiguration(), Collections.EMPTY_MAP);
        assertEquals(new Elections(), elections);
    }

    /**
     * Verifies that an elections instance with one European election is extracted from a website configuration with one
     * European election.
     */
    @Test
    public void shouldExtractOneEuropeanElectionFromWebsiteConfigurationWithOneEuropeanElection() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        ElectionLists elections = createElectionLists(websiteConfiguration);
        elections.setEuropean(createElectionList());
        Elections expected = new Elections();
        expected.addElection(AREA_CODE, ElectionType.EUROPEAN, 1, ELECTION_DATE, null);
        assertEquals(expected, ElectionsBuilder.extractElections(websiteConfiguration, Collections.EMPTY_MAP));
    }

    /**
     * Verifies that an elections instance with one national election is extracted from a website configuration with one
     * national election.
     */
    @Test
    public void shouldExtractOneNationalElectionFromWebsiteConfigurationWithOneNationalElection() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        ElectionLists elections = createElectionLists(websiteConfiguration);
        elections.setNational(createElectionList());
        Elections expected = new Elections();
        expected.addElection(AREA_CODE, ElectionType.NATIONAL, 1, ELECTION_DATE, null);
        assertEquals(expected, ElectionsBuilder.extractElections(websiteConfiguration, Collections.EMPTY_MAP));
    }

    /**
     * Verifies that an elections instance with one presidential election is extracted from a website configuration with
     * one national election.
     */
    @Test
    public void shouldExtractOnePresidentialElectionFromWebsiteConfigurationWithOnePresidentialElection() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        ElectionLists elections = createElectionLists(websiteConfiguration);
        elections.setPresidential(createElectionList());
        Elections expected = new Elections();
        expected.addElection(AREA_CODE, ElectionType.PRESIDENTIAL, 1, ELECTION_DATE, null);
        assertEquals(expected, ElectionsBuilder.extractElections(websiteConfiguration, Collections.EMPTY_MAP));
    }

    /**
     * Creates election lists on a website configuration.
     *
     * @param websiteConfiguration The website configuration to create the election lists on.
     * @return The election lists.
     */
    private ElectionLists createElectionLists(final WebsiteConfiguration websiteConfiguration) {
        AreaConfiguration areaConfiguration = new AreaConfiguration();
        areaConfiguration.setAreaCode(AREA_CODE);
        ElectionLists elections = new ElectionLists();
        areaConfiguration.setElections(elections);
        websiteConfiguration.setAreaConfigurations(Set.of(areaConfiguration));
        return elections;
    }

    /**
     * Creates an election list.
     *
     * @return An election list.
     */
    private ElectionList createElectionList() {
        ElectionList electionList = new ElectionList();
        electionList.setDates(Map.of(1, ELECTION_DATE));
        return electionList;
    }
}
