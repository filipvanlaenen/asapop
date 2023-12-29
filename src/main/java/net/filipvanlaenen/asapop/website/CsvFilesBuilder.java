package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.exporter.EopaodCsvExporter;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.AreaSubdivisionConfiguration;
import net.filipvanlaenen.asapop.yaml.CsvConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

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
     * @param parliamentaryOpinionPollsMap The map with the opinion polls.
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
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            String areaCode = areaConfiguration.getAreaCode();
            CsvConfiguration csvConfiguration = areaConfiguration.getCsvConfiguration();
            if (csvConfiguration != null) {
                OpinionPolls opinionPolls = parliamentaryOpinionPollsMap.get(areaCode);
                List<Set<String>> electoralListKeySets = csvConfiguration.getElectoralListIds().stream()
                        .map(key -> new HashSet<String>(Arrays.asList(key.split("\\+")))).collect(Collectors.toList());
                String outputContent = EopaodCsvExporter.export(opinionPolls, null, electoralListKeySets);
                result.put(Paths.get("_csv", areaCode + ".csv"), outputContent);
            }
            AreaSubdivisionConfiguration[] subdivisions = areaConfiguration.getSubdivsions();
            if (subdivisions != null) {
                for (AreaSubdivisionConfiguration subdivision : subdivisions) {
                    csvConfiguration = subdivision.getCsvConfiguration();
                    if (csvConfiguration != null) {
                        OpinionPolls opinionPolls = parliamentaryOpinionPollsMap.get(areaCode);
                        List<Set<String>> electoralListKeySets = csvConfiguration.getElectoralListIds().stream()
                                .map(key -> new HashSet<String>(Arrays.asList(key.split("\\+"))))
                                .collect(Collectors.toList());
                        String subdivisionAreaCode = subdivision.getAreaCode();
                        String outputContent = EopaodCsvExporter.export(opinionPolls, subdivisionAreaCode.toUpperCase(),
                                electoralListKeySets);
                        result.put(Paths.get("_csv", areaCode + "-" + subdivisionAreaCode + ".csv"), outputContent);
                    }
                }
            }
        }
        for (String presidentialElectionCode : presidentialOpinionPollsMap.keySet()) {
            OpinionPolls opinionPolls = presidentialOpinionPollsMap.get(presidentialElectionCode);
            List<Set<String>> candidateKeys = new ArrayList<Set<String>>();
            for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
                for (Set<ElectoralList> candidateKeySet : opinionPoll.getElectoralListSets()) {
                    Set<String> candidateKey = new HashSet<String>();
                    candidateKey.add(candidateKeySet.iterator().next().getId());
                    if (!candidateKeys.contains(candidateKey)) {
                        candidateKeys.add(candidateKey);
                    }
                }
            }
            candidateKeys.sort(new Comparator<Set<String>>() {
                @Override
                public int compare(Set<String> k1, Set<String> k2) {
                    return k1.iterator().next().compareTo(k2.iterator().next());
                }
            });
            String outputContent = EopaodCsvExporter.export(opinionPolls, null, candidateKeys);
            result.put(Paths.get("_csv", presidentialElectionCode + ".csv"), outputContent);
        }
        return result;
    }
}
