package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.exporter.EopaodCsvExporter;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
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
     * Constructor taking the map with the opinion polls as its parameter.
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
                List<Set<String>> electoralListKeySets = csvConfiguration.getElectoralListKeys().stream()
                        .map(key -> Set.of(key)).collect(Collectors.toList());
                String outputContent = EopaodCsvExporter.export(opinionPolls, "--", electoralListKeySets);
                result.put(Paths.get("_csv", areaCode + ".csv"), outputContent);
            }
        }
        return result;
    }
}
