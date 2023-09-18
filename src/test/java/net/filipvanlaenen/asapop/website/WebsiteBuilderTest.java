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

import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.CsvConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionList;
import net.filipvanlaenen.asapop.yaml.ElectionLists;
import net.filipvanlaenen.asapop.yaml.ElectionsBuilder;
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
    private static final LocalDate NOW = LocalDate.of(2022, Month.SEPTEMBER, 7);
    /**
     * The start of the year 2022.
     */
    private static final LocalDate START_OF_YEAR = NOW.withDayOfYear(1);
    /**
     * The area configuration for Latvia.
     */
    private AreaConfiguration latvia;
    /**
     * The area configuration for North Macedonia.
     */
    private AreaConfiguration northMacedonia;
    /**
     * The area configuration for Sweden.
     */
    private AreaConfiguration sweden;

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
        sweden = new AreaConfiguration();
        sweden.setAreaCode("se");
        ElectionLists electionListsForSweden = new ElectionLists();
        ElectionList nationalElectionsInSweden = new ElectionList();
        nationalElectionsInSweden.setDates(Map.of(1, "2022-09-11"));
        nationalElectionsInSweden.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/swedish_polls");
        electionListsForSweden.setNational(nationalElectionsInSweden);
        sweden.setElections(electionListsForSweden);
        latvia = new AreaConfiguration();
        latvia.setAreaCode("lv");
        ElectionLists electionListsForLatvia = new ElectionLists();
        ElectionList nationalElectionsInLatvia = new ElectionList();
        nationalElectionsInLatvia.setDates(Map.of(1, "2022-10-01"));
        nationalElectionsInLatvia.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/latvian_polls");
        electionListsForLatvia.setNational(nationalElectionsInLatvia);
        latvia.setElections(electionListsForLatvia);
        AreaConfiguration bulgaria = new AreaConfiguration();
        ElectionLists electionListsForBulgaria = new ElectionLists();
        ElectionList nationalElectionsInBulgaria = new ElectionList();
        nationalElectionsInBulgaria.setDates(Map.of(1, "2022-10-02"));
        nationalElectionsInBulgaria.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/bulgarian_polls");
        electionListsForBulgaria.setNational(nationalElectionsInBulgaria);
        bulgaria.setElections(electionListsForBulgaria);
        northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        CsvConfiguration csvConfiguration = new CsvConfiguration();
        csvConfiguration.setElectoralListIds(List.of("A", "B"));
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
        Terms terms = createTerms();
        Elections elections = ElectionsBuilder.extractElections(websiteConfiguration);
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", new OpinionPolls(Collections.EMPTY_SET));
        ElectoralList.get("A").setAbbreviation("A");
        ElectoralList.get("B").setAbbreviation("B");
        map.put(Paths.get("index.html"), new IndexPageBuilder(websiteConfiguration, elections, NOW).build().asString());
        map.put(Paths.get("calendar.html"),
                new ElectoralCalendarPageBuilder(websiteConfiguration, elections, NOW).build().asString());
        map.put(Paths.get("csv.html"), new CsvFilesPageBuilder(websiteConfiguration).build().asString());
        map.put(Paths.get("statistics.html"),
                new StatisticsPageBuilder(websiteConfiguration, terms, opinionPollsMap, NOW, START_OF_YEAR).build()
                        .asString());
        map.put(Paths.get("lv", "index.html"),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, elections, NOW)
                        .createAreaIndexPage(latvia));
        map.put(Paths.get("mk", "index.html"),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, elections, NOW)
                        .createAreaIndexPage(northMacedonia));
        map.put(Paths.get("se", "index.html"),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, elections, NOW)
                        .createAreaIndexPage(sweden));
        map.put(Paths.get("_js", "internationalization.js"),
                new InternationalizationScriptBuilder(createTerms()).build());
        String navigationScriptContent = "function moveToArea(level) {}";
        map.put(Paths.get("_js", "navigation.js"), navigationScriptContent);
        String sortingScriptContent = "function sortTable(table) {}";
        map.put(Paths.get("_js", "sorting.js"), sortingScriptContent);
        String tooltipScriptContent = "function tooltip(text) {}";
        map.put(Paths.get("_js", "tooltip.js"), tooltipScriptContent);
        map.put(Paths.get("_csv", "mk.csv"),
                "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,"
                        + "Sample Size Qualification,Participation,Precision,A,B,Other\n");
        String baseStyleSheetContent = "header { display: block; width: 100%; }";
        map.put(Paths.get("_css", "base.css"), baseStyleSheetContent);
        String customStyleSheetContent = "body { font-family: serif; background: #FFFFFF; color: #0E3651; }";
        map.put(Paths.get("_css", "skin.css"), customStyleSheetContent);
        WebsiteBuilder builder = new WebsiteBuilder(createWebsiteConfiguration(), terms, opinionPollsMap, elections,
                baseStyleSheetContent, customStyleSheetContent, navigationScriptContent, sortingScriptContent,
                tooltipScriptContent, NOW);
        assertEquals(map, builder.build().asMap());
    }
}
