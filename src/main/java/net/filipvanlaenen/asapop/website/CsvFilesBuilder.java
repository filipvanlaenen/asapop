package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.exporter.EopaodCsvExporter;
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
     * A map with the opinion polls.
     */
    private final Map<String, OpinionPolls> opinionPollsMap;
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;

    /**
     * Constructor taking the website configuration and the map with the opinion polls as its parameter.
     *
     * @param websiteConfiguration The website configuration.
     * @param opinionPollsMap      The map with the opinion polls.
     */
    public CsvFilesBuilder(final WebsiteConfiguration websiteConfiguration,
            final Map<String, OpinionPolls> opinionPollsMap) {
        this.websiteConfiguration = websiteConfiguration;
        this.opinionPollsMap = opinionPollsMap;
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
                OpinionPolls opinionPolls = opinionPollsMap.get(areaCode);
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
                        OpinionPolls opinionPolls = opinionPollsMap.get(areaCode);
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
        return result;
    }
}
