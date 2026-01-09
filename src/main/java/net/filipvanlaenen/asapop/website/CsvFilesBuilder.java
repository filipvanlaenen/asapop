package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.filipvanlaenen.asapop.exporter.ElectoralListsCsvExporter;
import net.filipvanlaenen.asapop.exporter.EopaodCsvExporter;
import net.filipvanlaenen.asapop.model.Candidate;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.AreaSubdivisionConfiguration;
import net.filipvanlaenen.asapop.yaml.CsvConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;
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
        ModifiableMap<Path, String> result = ModifiableMap.<Path, String>empty();
        buildAndAddParliamentaryCsvFiles(result);
        buildAndAddPresidentialCsvFiles(result);
        buildAndAddElectoralListCsvFiles(result);
        return result;
    }

    /**
     * Build the CSV file with the electoral lists and adds it to the map.
     *
     * @param csvFilesMap The map with the CSV files.
     */
    private void buildAndAddElectoralListCsvFiles(final ModifiableMap<Path, String> csvFilesMap) {
        csvFilesMap.put(Paths.get("_csv", "electorallists.csv"), ElectoralListsCsvExporter.export());
        csvFilesMap.put(Paths.get("_csv", "electorallists.v1.csv"), ElectoralListsCsvExporter.export());
    }

    /**
     * Builds all the CSV files related to parliamentary elections and adds them to the map.
     *
     * @param csvFilesMap The map with the CSV files.
     */
    private void buildAndAddParliamentaryCsvFiles(final ModifiableMap<Path, String> csvFilesMap) {
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            String areaCode = areaConfiguration.getAreaCode();
            CsvConfiguration csvConfiguration = areaConfiguration.getCsvConfiguration();
            if (csvConfiguration != null) {
                OpinionPolls opinionPolls = parliamentaryOpinionPollsMap.get(areaCode);
                OrderedCollection<Set<String>> electoralListKeySets = csvConfiguration.getElectoralListIds().stream()
                        .map(key -> new HashSet<String>(Arrays.asList(key.split("\\+"))))
                        .collect(Collectors.toOrderedCollection());
                String outputContent = EopaodCsvExporter.export(opinionPolls, "--",
                        csvConfiguration.getIncludeAreaAsNational(), electoralListKeySets, OrderedCollection.empty());
                csvFilesMap.put(Paths.get("_csv", areaCode + ".csv"), outputContent);
                csvFilesMap.put(Paths.get("_csv", areaCode + ".v1.csv"), outputContent);
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
                                null, electoralListKeySets, OrderedCollection.empty());
                        csvFilesMap.put(Paths.get("_csv", areaCode + "-" + subdivisionAreaCode + ".csv"),
                                outputContent);
                        csvFilesMap.put(Paths.get("_csv", areaCode + "-" + subdivisionAreaCode + ".v1.csv"),
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
    private void buildAndAddPresidentialCsvFiles(final ModifiableMap<Path, String> csvFilesMap) {
        for (String presidentialElectionCode : presidentialOpinionPollsMap.getKeys()) {
            OpinionPolls opinionPolls = presidentialOpinionPollsMap.get(presidentialElectionCode);
            ModifiableSortedCollection<Set<String>> candidateKeys =
                    ModifiableSortedCollection.<Set<String>>empty(new Comparator<Set<String>>() {
                        @Override
                        public int compare(final Set<String> k1, final Set<String> k2) {
                            return k1.iterator().next().compareTo(k2.iterator().next());
                        }
                    });
            ModifiableSortedCollection<String> candidateKeys2 =
                    ModifiableSortedCollection.<String>empty(new Comparator<String>() {
                        @Override
                        public int compare(final String k1, final String k2) {
                            return k1.compareTo(k2);
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
                for (Candidate candidate : opinionPoll.getCandidates()) {
                    String candidateId = candidate.getId();
                    if (!candidateKeys2.contains(candidateId)) {
                        candidateKeys2.add(candidateId);
                    }
                }
            }
            String outputContent = EopaodCsvExporter.export(opinionPolls, null, null, candidateKeys, candidateKeys2);
            csvFilesMap.put(Paths.get("_csv", presidentialElectionCode + ".csv"), outputContent);
            csvFilesMap.put(Paths.get("_csv", presidentialElectionCode + ".v1.csv"), outputContent);
        }
    }
}
