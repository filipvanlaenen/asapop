package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.AreaSubdivisionConfiguration;
import net.filipvanlaenen.asapop.yaml.CsvConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>CsvFilesBuilder</code> class.
 */
public class CsvFilesBuilderTest {
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
        Map<Path, String> map = new HashMap<Path, String>();
        map.put(Paths.get("_csv", "be-vlg.csv"),
                "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,"
                        + "Sample Size Qualification,Participation,Precision,P,Q,Other\n");
        map.put(Paths.get("_csv", "mk.csv"),
                "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,"
                        + "Sample Size Qualification,Participation,Precision,A,B,Other\n");
        Map<String, OpinionPolls> opinionPollsMap =
                Map.of("be", new OpinionPolls(Collections.EMPTY_SET), "mk", new OpinionPolls(Collections.EMPTY_SET));
        ElectoralList.get("A").setAbbreviation("A");
        ElectoralList.get("B").setAbbreviation("B");
        ElectoralList.get("P").setAbbreviation("P");
        ElectoralList.get("Q").setAbbreviation("Q");
        CsvFilesBuilder builder =
                new CsvFilesBuilder(createWebsiteConfiguration(), opinionPollsMap, Collections.EMPTY_MAP);
        assertEquals(map, builder.build());
    }
}
