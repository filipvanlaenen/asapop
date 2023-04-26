package net.filipvanlaenen.asapop.yaml;

import java.util.Map;

import net.filipvanlaenen.asapop.model.ElectionType;
import net.filipvanlaenen.asapop.model.Elections;

/**
 * Utility class to extract elections from a website configuration.
 */
public final class ElectionsBuilder {
    /**
     * Private constructor to prevent instantiation.
     */
    private ElectionsBuilder() {
    }

    /**
     * Adds the elections from an election list to the elections instance.
     *
     * @param elections    The elections instance.
     * @param electionList The election list.
     * @param areaCode     The area code for the elections in the election list.
     * @param electionType The election type for the elections in the election list.
     */
    private static void addElectionList(final Elections elections, final ElectionList electionList,
            final String areaCode, final ElectionType electionType) {
        if (electionList != null) {
            Map<Integer, String> dates = electionList.getDates();
            for (String date : dates.values()) {
                elections.addElection(areaCode, electionType, date);
            }
        }
    }

    /**
     * Extracts elections from a website configuration.
     *
     * @param websiteConfiguration A website configuration.
     * @return The extracted elections.
     */
    public static Elections extractElections(final WebsiteConfiguration websiteConfiguration) {
        Elections elections = new Elections();
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            String areaCode = areaConfiguration.getAreaCode();
            ElectionLists electionLists = areaConfiguration.getElections();
            if (electionLists != null) {
                addElectionList(elections, electionLists.getNational(), areaCode, ElectionType.NATIONAL);
                addElectionList(elections, electionLists.getPresidential(), areaCode, ElectionType.PRESIDENTIAL);
                addElectionList(elections, electionLists.getEuropean(), areaCode, ElectionType.EUROPEAN);
            }
        }
        return elections;
    }
}
