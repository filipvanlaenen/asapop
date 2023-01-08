package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
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
import net.filipvanlaenen.asapop.yaml.ElectionConfiguration;
import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>WebsiteBuilder</code> class.
 */
public class WebsiteBuilderTest {
    /**
     * Today's date.
     */
    private static final LocalDate NOW = LocalDate.of(2022, Month.DECEMBER, 7);
    /**
     * The start of the year 2022.
     */
    private static final LocalDate START_OF_YEAR = NOW.withDayOfYear(1);
    /**
     * The area configuration for North Macedonia.
     */
    private AreaConfiguration northMacedonia;

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
        ElectionConfiguration swedishElection = new ElectionConfiguration();
        swedishElection.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/swedish_polls");
        swedishElection.setNextElectionDate("2022-09-11");
        sweden.setElectionConfigurations(Set.of(swedishElection));
        AreaConfiguration latvia = new AreaConfiguration();
        ElectionConfiguration latvianElection = new ElectionConfiguration();
        latvianElection.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/latvian_polls");
        latvianElection.setNextElectionDate("2022-10-01");
        latvia.setElectionConfigurations(Set.of(latvianElection));
        AreaConfiguration bulgaria = new AreaConfiguration();
        ElectionConfiguration bulgarianElection = new ElectionConfiguration();
        bulgarianElection.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/bulgarian_polls");
        bulgarianElection.setNextElectionDate("2022-10-02");
        bulgaria.setElectionConfigurations(Set.of(bulgarianElection));
        northMacedonia = new AreaConfiguration();
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
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", new OpinionPolls(Collections.EMPTY_SET));
        ElectoralList.get("A").setAbbreviation("A");
        ElectoralList.get("B").setAbbreviation("B");
        map.put(Paths.get("index.html"), new IndexPageBuilder(websiteConfiguration).build().asString());
        map.put(Paths.get("calendar.html"), new ElectoralCalendarPageBuilder(websiteConfiguration).build().asString());
        map.put(Paths.get("csv.html"), new CsvFilesPageBuilder(websiteConfiguration).build().asString());
        map.put(Paths.get("statistics.html"),
                new StatisticsPageBuilder(websiteConfiguration, opinionPollsMap, NOW, START_OF_YEAR).build()
                        .asString());
        map.put(Paths.get("mk", "index.html"),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap).createAreaIndexPage(northMacedonia));
        map.put(Paths.get("_js", "internationalization.js"),
                new InternationalizationScriptBuilder(createTerms()).build());
        String navigationScriptContent = "function moveToArea(level) {}";
        map.put(Paths.get("_js", "navigation.js"), navigationScriptContent);
        map.put(Paths.get("_csv", "mk.csv"),
                "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,"
                        + "Sample Size Qualification,Participation,Precision,A,B,Other\n");
        String baseStyleSheetContent = "header { display: block; width: 100%; }";
        map.put(Paths.get("_css", "base.css"), baseStyleSheetContent);
        String customStyleSheetContent = "body { font-family: serif; background: #FFFFFF; color: #0E3651; }";
        map.put(Paths.get("_css", "skin.css"), customStyleSheetContent);
        WebsiteBuilder builder = new WebsiteBuilder(createWebsiteConfiguration(), createTerms(), opinionPollsMap,
                baseStyleSheetContent, customStyleSheetContent, navigationScriptContent, NOW);
        assertEquals(map, builder.build().asMap());
    }
}
