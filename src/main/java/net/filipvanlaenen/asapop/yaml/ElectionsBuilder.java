package net.filipvanlaenen.asapop.yaml;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectionType;
import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

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
            final String areaCode, final ElectionType electionType, final Map<String, ElectionData> electionDataFiles,
            final LocalDate now, final Token token) {
        if (electionList != null) {
            Token electionTypeToken = Laconic.LOGGER.logMessage(token,
                    "Extracting and validating the election dates for election type %s.", electionType.getTermKey());
            for (Map.Entry<Integer, String> entry : electionList.getDates().entrySet()) {
                int electionNumber = entry.getKey();
                ElectionData electionData = electionDataFiles.getOrDefault(areaCode + "-" + electionNumber, null);
                elections.addElection(areaCode, electionType, electionNumber, entry.getValue(), electionData);
            }
            if (elections.getNextElection(areaCode, electionType, now) == null) {
                Laconic.LOGGER.logError("No election dates set in the future.", electionTypeToken);
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
        Laconic.LOGGER.logMessage("Validating for future election dates against the date %s.", now.toString(), token);
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
                addElectionList(elections, electionLists.getNational(), areaCode, ElectionType.NATIONAL,
                        electionDataFiles, now, areaToken);
                addElectionList(elections, electionLists.getPresidential(), areaCode, ElectionType.PRESIDENTIAL,
                        electionDataFiles, now, areaToken);
                addElectionList(elections, electionLists.getEuropean(), areaCode, ElectionType.EUROPEAN,
                        electionDataFiles, now, areaToken);
            }
        }
        return elections;
    }
}
