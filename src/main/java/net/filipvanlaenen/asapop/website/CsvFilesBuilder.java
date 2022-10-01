package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.filipvanlaenen.asapop.exporter.EopaodCsvExporter;
import net.filipvanlaenen.asapop.model.OpinionPolls;

/**
 * Class building the CSV files.
 */
public class CsvFilesBuilder {
    /**
     * A map with the opinion polls.
     */
    private final Map<String, OpinionPolls> opinionPollsMap;

    /**
     * Constructor taking the map with the opinion polls as its parameter.
     *
     * @param opinionPollsMap The map with the opinion polls.
     */
    public CsvFilesBuilder(final Map<String, OpinionPolls> opinionPollsMap) {
        this.opinionPollsMap = opinionPollsMap;
    }

    /**
     * Builds all the CSV files for the website.
     *
     * @return A map with the CSV files and their paths.
     */
    public Map<Path, String> build() {
        Map<Path, String> result = new HashMap<Path, String>();
        for (Entry<String, OpinionPolls> entry : opinionPollsMap.entrySet()) {
            // TODO: Read from website configuration
            String[] electoralListKeys = new String[] {"СДСМ", "ВМРО", "ДУИ", "ASH", "ЛЕВ", "PDSH", "BESA"};
            String outputContent = EopaodCsvExporter.export(entry.getValue(), "--", electoralListKeys);
            result.put(Paths.get("_csv", entry.getKey() + ".csv"), outputContent);
        }
        return result;
    }
}
