package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.AreaSubdivisionConfiguration;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.CsvConfiguration;
import net.filipvanlaenen.asapop.yaml.websiteconfiguration.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the <code>CsvFilesBuilder</code> class.
 */
public class CsvFilesBuilderTest {
    /**
     * The content for the opinion polls CSV file for North Macedonia, version 1.
     */
    private static final String POLLS_NORTH_MACEDONIA_CSV1 =
            "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,Sample Size Qualification,"
                    + "Participation,Precision,A,B,Other\n";
    /**
     * The content for the opinion polls CSV file for North Macedonia, version 2.
     */
    private static final String POLLS_NORTH_MACEDONIA_CSV2 =
            "Ephemeral Poll ID,Ephemeral Response Scenario ID,Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,"
                    + "Scope,Sample Size,Sample Size Qualification,Participation,Precision,A,B,Other\n";
    /**
     * The content for the opinion polls CSV file for France, version 1.
     */
    private static final String POLLS_FRANCE_P13_CSV1 =
            "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,Sample Size Qualification,"
                    + "Participation,Precision,F,G,H,I,Other\n"
                    + "ACME,,2021-07-27,2021-07-28,Not Available,Not Available,Not Available,Not Available,1%,55%,"
                    + "40%,2%,2%,Not Available\n";
    /**
     * The content for the opinion polls CSV file for France, version 2.
     */
    private static final String POLLS_FRANCE_P13_CSV2 =
            "Ephemeral Poll ID,Ephemeral Response Scenario ID,Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,"
                    + "Scope,Sample Size,Sample Size Qualification,Participation,Precision,F,G,H,I,Other\n"
                    + "1,1,ACME,,2021-07-27,2021-07-28,Not Available,Not Available,Not Available,Not Available,1%,55%,"
                    + "40%,2%,2%,Not Available\n";
    /**
     * The content for the opinion polls CSV file for Flanders, version 1.
     */
    private static final String POLLS_FLANDERS_CSV1 =
            "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,Sample Size Qualification,"
                    + "Participation,Precision,P,Q,Other\n";
    /**
     * The content for the opinion polls CSV file for Flanders, version 2.
     */
    private static final String POLLS_FLANDERS_CSV2 =
            "Ephemeral Poll ID,Ephemeral Response Scenario ID,Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,"
                    + "Scope,Sample Size,Sample Size Qualification,Participation,Precision,P,Q,Other\n";
    /**
     * The content for the electoral lists CSV file.
     */
    private static final String ELECTORAL_LISTS_CSV =
            "ID,Abbreviation,Romanized Abbreviation\n" + ",A,\n" + ",B,\n" + ",F,\n" + ",G,\n" + ",P,\n" + ",Q,\n";
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
        ElectoralList.clear();
        ModifiableMap<Path, String> expected = ModifiableMap.<Path, String>empty();
        expected.put(Paths.get("_csv", "be-vlg.csv"), POLLS_FLANDERS_CSV1);
        expected.put(Paths.get("_csv", "be-vlg.v1.csv"), POLLS_FLANDERS_CSV1);
        expected.put(Paths.get("_csv", "be-vlg.v2.csv"), POLLS_FLANDERS_CSV2);
        expected.put(Paths.get("_csv", "fr_p13.csv"), POLLS_FRANCE_P13_CSV1);
        expected.put(Paths.get("_csv", "fr_p13.v1.csv"), POLLS_FRANCE_P13_CSV1);
        expected.put(Paths.get("_csv", "fr_p13.v2.csv"), POLLS_FRANCE_P13_CSV2);
        expected.put(Paths.get("_csv", "mk.csv"), POLLS_NORTH_MACEDONIA_CSV1);
        expected.put(Paths.get("_csv", "mk.v1.csv"), POLLS_NORTH_MACEDONIA_CSV1);
        expected.put(Paths.get("_csv", "mk.v2.csv"), POLLS_NORTH_MACEDONIA_CSV2);
        expected.put(Paths.get("_csv", "electorallists.csv"), ELECTORAL_LISTS_CSV);
        expected.put(Paths.get("_csv", "electorallists.v1.csv"), ELECTORAL_LISTS_CSV);
        Map<String, OpinionPolls> parliamentaryOpinionPollsMap =
                Map.of("be", new OpinionPolls(Collections.EMPTY_SET), "mk", new OpinionPolls(Collections.EMPTY_SET));
        ElectoralList.get("A").setAbbreviation("A");
        ElectoralList.get("B").setAbbreviation("B");
        ElectoralList.get("P").setAbbreviation("P");
        ElectoralList.get("Q").setAbbreviation("Q");
        OpinionPolls opinionPolls = RichOpinionPollsFile
                .parse(TOKEN, "•PF: ACME •FS: 2021-07-27 •FE: 2021-07-28 F:55 G:40 H: 2 I: 2", "F: FR001 •A:F",
                        "G: FR002 •A:G", "H: FR1970A •A:H", "I: FR1970B •A:I")
                .getOpinionPollsDeprecated();
        Map<String, OpinionPolls> presidentialOpinionPollsMap = Map.of("fr_p13", opinionPolls);
        CsvFilesBuilder builder = new CsvFilesBuilder(createWebsiteConfiguration(), parliamentaryOpinionPollsMap,
                presidentialOpinionPollsMap);
        Map<Path, String> actual = builder.build();
        ModifiableMap<Path, String> unexpected = ModifiableMap.of(actual);
        unexpected.removeIf(e -> expected.containsKey(e.key()));
        assertTrue(unexpected.isEmpty(),
                unexpected.getKeys().stream().map(Object::toString).collect(Collectors.joining(", ")));
        assertTrue(actual.containsSame(expected));
    }
}
