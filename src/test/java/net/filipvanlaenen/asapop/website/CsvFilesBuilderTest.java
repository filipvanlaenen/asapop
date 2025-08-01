package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.AreaSubdivisionConfiguration;
import net.filipvanlaenen.asapop.yaml.CsvConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the <code>CsvFilesBuilder</code> class.
 */
public class CsvFilesBuilderTest {
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test CsvFilesBuilderTest.");

    /**
     * Creates a website configuration.
     *
     * @return A website configuration.
     */
    private WebsiteConfiguration createWebsiteConfiguration() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        CsvConfiguration csvConfiguration = new CsvConfiguration();
        csvConfiguration.setElectoralListIds(List.of("A", "B"));
        northMacedonia.setCsvConfiguration(csvConfiguration);
        AreaConfiguration belgium = new AreaConfiguration();
        belgium.setAreaCode("be");
        AreaSubdivisionConfiguration flanders = new AreaSubdivisionConfiguration();
        flanders.setAreaCode("vlg");
        CsvConfiguration flemishCsvConfiguration = new CsvConfiguration();
        flemishCsvConfiguration.setElectoralListIds(List.of("P", "Q"));
        flanders.setCsvConfiguration(flemishCsvConfiguration);
        AreaSubdivisionConfiguration[] belgianSubdivisions = new AreaSubdivisionConfiguration[] {flanders};
        belgium.setSubdivisions(belgianSubdivisions);
        websiteConfiguration.setAreaConfigurations(Set.of(belgium, northMacedonia, new AreaConfiguration()));
        return websiteConfiguration;
    }

    /**
     * Verifies that the CSV files are built correctly.
     */
    @Test
    public void websiteShouldBeBuiltCorrectly() {
        ModifiableMap<Path, String> map = ModifiableMap.<Path, String>empty();
        map.put(Paths.get("_csv", "be-vlg.csv"),
                "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,Sample Size Qualification,"
                        + "Participation,Precision,P,Q,Other\n");
        map.put(Paths.get("_csv", "fr_p13.csv"),
                "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,Sample Size Qualification,"
                        + "Participation,Precision,F,G,Other\n"
                        + "ACME,,2021-07-27,2021-07-28,Not Available,Not Available,Not Available,Not Available,1%,55%,"
                        + "40%,Not Available\n");
        map.put(Paths.get("_csv", "mk.csv"),
                "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,Sample Size Qualification,"
                        + "Participation,Precision,A,B,Other\n");
        Map<String, OpinionPolls> parliamentaryOpinionPollsMap =
                Map.of("be", new OpinionPolls(Collections.EMPTY_SET), "mk", new OpinionPolls(Collections.EMPTY_SET));
        ElectoralList.get("A").setAbbreviation("A");
        ElectoralList.get("B").setAbbreviation("B");
        ElectoralList.get("P").setAbbreviation("P");
        ElectoralList.get("Q").setAbbreviation("Q");
        OpinionPolls opinionPolls = RichOpinionPollsFile
                .parse(TOKEN, "•PF: ACME •FS: 2021-07-27 •FE: 2021-07-28 F:55 G:40", "F: FR001 •A:F", "G: FR002 •A:G")
                .getOpinionPolls();
        Map<String, OpinionPolls> presidentialOpinionPollsMap = Map.of("fr_p13", opinionPolls);
        CsvFilesBuilder builder = new CsvFilesBuilder(createWebsiteConfiguration(), parliamentaryOpinionPollsMap,
                presidentialOpinionPollsMap);
        assertTrue(map.containsSame(builder.build()));
    }
}
