package net.filipvanlaenen.asapop.yaml;

import java.util.Map;
import java.util.Set;

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
     * @param elections         The elections instance.
     * @param electionList      The election list.
     * @param areaCode          The area code for the elections in the election list.
     * @param electionType      The election type for the elections in the election list.
     * @param electionDataFiles
     */
    private static void addElectionList(final Elections elections, final ElectionList electionList,
            final String areaCode, final ElectionType electionType, final Map<String, ElectionData> electionDataFiles) {
        if (electionList != null) {
            for (Map.Entry<Integer, String> entry : electionList.getDates().entrySet()) {
                int electionNumber = entry.getKey();
                ElectionData electionData = electionDataFiles.getOrDefault(areaCode + "-" + electionNumber, null);
                elections.addElection(areaCode, electionType, electionNumber, entry.getValue(), electionData);
            }
        }
    }

    /**
     * Extracts elections from a website configuration.
     *
     * @param websiteConfiguration A website configuration.
     * @param electionDataFiles
     * @return The extracted elections.
     */
    public static Elections extractElections(final WebsiteConfiguration websiteConfiguration,
            Map<String, ElectionData> electionDataFiles) {
        Elections elections = new Elections();
        Set<AreaConfiguration> areaConfigurations = websiteConfiguration.getAreaConfigurations();
        if (areaConfigurations == null) {
            return elections;
        }
        for (AreaConfiguration areaConfiguration : areaConfigurations) {
            String areaCode = areaConfiguration.getAreaCode();
            ElectionLists electionLists = areaConfiguration.getElections();
            if (electionLists != null) {
                addElectionList(elections, electionLists.getNational(), areaCode, ElectionType.NATIONAL,
                        electionDataFiles);
                addElectionList(elections, electionLists.getPresidential(), areaCode, ElectionType.PRESIDENTIAL,
                        electionDataFiles);
                addElectionList(elections, electionLists.getEuropean(), areaCode, ElectionType.EUROPEAN,
                        electionDataFiles);
            }
        }
        return elections;
    }
}
