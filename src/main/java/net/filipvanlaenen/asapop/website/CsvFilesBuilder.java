package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.exporter.EopaodCsvExporter;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.AreaSubdivisionConfiguration;
import net.filipvanlaenen.asapop.yaml.CsvConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.ModifiableSortedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.collectors.Collectors;

/**
 * Class building the CSV files.
 */
public class CsvFilesBuilder {
    /**
     * A map with the opinion polls related to parliamentary elections.
     */
    private final Map<String, OpinionPolls> parliamentaryOpinionPollsMap;
    /**
     * The map with the opinion polls related to presidential elections.
     */
    private final Map<String, OpinionPolls> presidentialOpinionPollsMap;
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;

    /**
     * Constructor taking the website configuration and the map with the opinion polls as its parameter.
     *
     * @param websiteConfiguration         The website configuration.
     * @param parliamentaryOpinionPollsMap The map with the opinion polls related to parliamentary elections.
     * @param presidentialOpinionPollsMap  The map with the opinion polls related to presidential elections.
     */
    public CsvFilesBuilder(final WebsiteConfiguration websiteConfiguration,
            final Map<String, OpinionPolls> parliamentaryOpinionPollsMap,
            final Map<String, OpinionPolls> presidentialOpinionPollsMap) {
        this.websiteConfiguration = websiteConfiguration;
        this.parliamentaryOpinionPollsMap = parliamentaryOpinionPollsMap;
        this.presidentialOpinionPollsMap = presidentialOpinionPollsMap;
    }

    /**
     * Builds all the CSV files for the website.
     *
     * @return A map with the CSV files and their paths.
     */
    public Map<Path, String> build() {
        Map<Path, String> result = new HashMap<Path, String>();
        buildAndAddParliamentaryCsvFiles(result);
        buildAndAddPresidentialCsvFiles(result);
        return result;
    }

    /**
     * Builds all the CSV files related to parliamentary elections and adds them to the map.
     *
     * @param csvFilesMap The map with the CSV files.
     */
    private void buildAndAddParliamentaryCsvFiles(final Map<Path, String> csvFilesMap) {
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            String areaCode = areaConfiguration.getAreaCode();
            CsvConfiguration csvConfiguration = areaConfiguration.getCsvConfiguration();
            if (csvConfiguration != null) {
                OpinionPolls opinionPolls = parliamentaryOpinionPollsMap.get(areaCode);
                OrderedCollection<Set<String>> electoralListKeySets = csvConfiguration.getElectoralListIds().stream()
                        .map(key -> new HashSet<String>(Arrays.asList(key.split("\\+"))))
                        .collect(Collectors.toOrderedCollection());
                String outputContent = EopaodCsvExporter.export(opinionPolls, "--",
                        csvConfiguration.getIncludeAreaAsNational(), electoralListKeySets);
                csvFilesMap.put(Paths.get("_csv", areaCode + ".csv"), outputContent);
            }
            AreaSubdivisionConfiguration[] subdivisions = areaConfiguration.getSubdivsions();
            if (subdivisions != null) {
                for (AreaSubdivisionConfiguration subdivision : subdivisions) {
                    csvConfiguration = subdivision.getCsvConfiguration();
                    if (csvConfiguration != null) {
                        OpinionPolls opinionPolls = parliamentaryOpinionPollsMap.get(areaCode);
                        OrderedCollection<Set<String>> electoralListKeySets = csvConfiguration.getElectoralListIds()
                                .stream().map(key -> new HashSet<String>(Arrays.asList(key.split("\\+"))))
                                .collect(Collectors.toOrderedCollection());
                        String subdivisionAreaCode = subdivision.getAreaCode();
                        String outputContent = EopaodCsvExporter.export(opinionPolls, subdivisionAreaCode.toUpperCase(),
                                null, electoralListKeySets);
                        csvFilesMap.put(Paths.get("_csv", areaCode + "-" + subdivisionAreaCode + ".csv"),
                                outputContent);
                    }
                }
            }
        }
    }

    /**
     * Builds all the CSV files related to presidential elections and adds them to the map.
     *
     * @param csvFilesMap The map with the CSV files.
     */
    private void buildAndAddPresidentialCsvFiles(final Map<Path, String> csvFilesMap) {
        for (String presidentialElectionCode : presidentialOpinionPollsMap.keySet()) {
            OpinionPolls opinionPolls = presidentialOpinionPollsMap.get(presidentialElectionCode);
            ModifiableSortedCollection<Set<String>> candidateKeys =
                    ModifiableSortedCollection.<Set<String>>empty(new Comparator<Set<String>>() {
                        @Override
                        public int compare(final Set<String> k1, final Set<String> k2) {
                            return k1.iterator().next().compareTo(k2.iterator().next());
                        }
                    });
            for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
                for (Set<ElectoralList> candidateKeySet : opinionPoll.getElectoralListSets()) {
                    Set<String> candidateKey = new HashSet<String>();
                    candidateKey.add(candidateKeySet.iterator().next().getId());
                    if (!candidateKeys.contains(candidateKey)) {
                        candidateKeys.add(candidateKey);
                    }
                }
            }
            String outputContent = EopaodCsvExporter.export(opinionPolls, null, null, candidateKeys);
            csvFilesMap.put(Paths.get("_csv", presidentialElectionCode + ".csv"), outputContent);
        }
    }
}
