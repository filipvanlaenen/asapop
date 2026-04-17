package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import java.time.LocalDate;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectionType;
import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.asapop.yaml.ElectionData;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Utility class to extract elections from a website configuration.
 */
@Deprecated
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
            for (Map.Entry<Integer, String> entry : electionList.getDates()) {
                int electionNumber = entry.key();
                ElectionData electionData = electionDataFiles.get(areaCode + "-" + electionNumber, null);
                elections.addElection(areaCode, electionType, electionNumber, entry.value(), electionData);
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
    public static Elections extractAndValidateElections(final WebsiteConfiguration websiteConfiguration,
            final Map<String, ElectionData> electionDataFiles, final LocalDate now) {
        Token token = Laconic.LOGGER.logMessage("Extracting and validating the election dates.");
        Elections elections = new Elections();
        Set<AreaConfiguration> areaConfigurations = websiteConfiguration.getAreaConfigurations();
        if (areaConfigurations == null) {
            return elections;
        }
        for (AreaConfiguration areaConfiguration : areaConfigurations) {
            String areaCode = areaConfiguration.getAreaCode();
            Token areaToken = Laconic.LOGGER.logMessage(token,
                    "Extracting and validating the election dates for area %s.", areaCode);
            ElectionLists electionLists = areaConfiguration.getElections();
            if (electionLists != null) {
                Laconic.LOGGER.logError("The area contains the deprecated elections field.", areaToken);
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
