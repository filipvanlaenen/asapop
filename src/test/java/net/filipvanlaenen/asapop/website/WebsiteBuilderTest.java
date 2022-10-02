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
import net.filipvanlaenen.asapop.yaml.CsvConfiguration;
import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>WebsiteBuilder</code> class.
 */
public class WebsiteBuilderTest {
    /**
     * Creates a set of terms for the internationalization script builder.
     *
     * @return A set of terms for the internationalization script builder.
     */
    private Terms createTerms() {
        Terms terms = new Terms();
        Term term = new Term();
        term.setKey("language");
        term.setTranslations(Map.of("en", "Language"));
        terms.setTerms(Set.of(term));
        return terms;
    }

    /**
     * Creates a website configuration.
     *
     * @return A website configuration.
     */
    private WebsiteConfiguration createWebsiteConfiguration() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        AreaConfiguration sweden = new AreaConfiguration();
        sweden.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/swedish_polls");
        sweden.setNextElectionDate("2022-09-11");
        AreaConfiguration latvia = new AreaConfiguration();
        latvia.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/latvian_polls");
        latvia.setNextElectionDate("2022-10-01");
        AreaConfiguration bulgaria = new AreaConfiguration();
        bulgaria.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/bulgarian_polls");
        bulgaria.setNextElectionDate("2022-10-02");
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        CsvConfiguration csvConfiguration = new CsvConfiguration();
        csvConfiguration.setElectoralListKeys(List.of("A", "B"));
        northMacedonia.setCsvConfiguration(csvConfiguration);
        websiteConfiguration.setAreaConfigurations(Set.of(sweden, latvia, bulgaria, northMacedonia));
        return websiteConfiguration;
    }

    /**
     * Verifies that the website is built correctly.
     */
    @Test
    public void websiteShouldBeBuiltCorrectly() {
        Map<Path, String> map = new HashMap<Path, String>();
        WebsiteConfiguration websiteConfiguration = createWebsiteConfiguration();
        map.put(Paths.get("index.html"), new IndexPageBuilder(websiteConfiguration).build().asString());
        map.put(Paths.get("csv.html"), new CsvFilesPageBuilder(websiteConfiguration).build().asString());
        map.put(Paths.get("_js", "internationalization.js"),
                new InternationalizationScriptBuilder(createTerms()).build());
        map.put(Paths.get("_csv", "mk.csv"),
                "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,"
                        + "Sample Size Qualification,Participation,Precision,A,B,Other");
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", new OpinionPolls(Collections.EMPTY_SET));
        ElectoralList.get("A").setAbbreviation("A");
        ElectoralList.get("B").setAbbreviation("B");
        WebsiteBuilder builder = new WebsiteBuilder(createWebsiteConfiguration(), createTerms(), opinionPollsMap);
        assertEquals(map, builder.build().asMap());
    }
}
