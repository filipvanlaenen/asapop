package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
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
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the <code>WebsiteBuilder</code> class.
 */
public class WebsiteBuilderTest {
    /**
     * Today's date.
     */
    private static final LocalDate NOW = LocalDate.of(2022, Month.SEPTEMBER, 7);
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test WebsiteBuilderTest.");
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
     * Creates the internationalization dictionary.
     *
     * @return The internationalization dictionary.
     */
    private Internationalization createInternationalization() {
        Internationalization internationalization = new Internationalization();
        internationalization.addTranslations("language", Map.of("en", "Language"));
        return internationalization;
    }

    /**
     * Creates a website configuration.
     *
     * @return A website configuration.
     */
    private WebsiteConfiguration createWebsiteConfiguration() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Test");
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
        ElectoralList.clear();
        ModifiableMap<Path, String> expected = ModifiableMap.<Path, String>empty();
        WebsiteConfiguration websiteConfiguration = createWebsiteConfiguration();
        Internationalization internationalization = createInternationalization();
        Elections elections = ElectionsBuilder.extractAndValidateElections(websiteConfiguration, Map.empty(), NOW);
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", new OpinionPolls(Collections.EMPTY_SET));
        ElectoralList.get("A").setAbbreviation("A");
        ElectoralList.get("B").setAbbreviation("B");
        expected.put(Paths.get("index.html"),
                new IndexPageBuilder(websiteConfiguration, elections, NOW).build().asString());
        expected.put(Paths.get("calendar.html"),
                new ElectoralCalendarPageBuilder(websiteConfiguration, elections, NOW).build().asString());
        expected.put(Paths.get("calendar.ical"),
                new ICalendarFileBuilder(websiteConfiguration, elections, NOW, internationalization).build(TOKEN));
        expected.put(Paths.get("csv.html"), new CsvFilesPageBuilder(websiteConfiguration).build().asString());
        expected.put(Paths.get("statistics.html"),
                new StatisticsPageBuilder(websiteConfiguration, internationalization, opinionPollsMap, NOW).build()
                        .asString());
        expected.put(Paths.get("lv", "index.html"),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, elections, NOW)
                        .createAreaIndexPage(latvia));
        expected.put(Paths.get("mk", "index.html"),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, elections, NOW)
                        .createAreaIndexPage(northMacedonia));
        expected.put(Paths.get("se", "index.html"),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, elections, NOW)
                        .createAreaIndexPage(sweden));
        expected.put(Paths.get("_js", "internationalization.js"),
                new InternationalizationScriptBuilder(createInternationalization()).build());
        String navigationScriptContent = "function moveToArea(level) {}";
        expected.put(Paths.get("_js", "navigation.js"), navigationScriptContent);
        String sortingScriptContent = "function sortTable(table) {}";
        expected.put(Paths.get("_js", "sorting.js"), sortingScriptContent);
        String tooltipScriptContent = "function tooltip(text) {}";
        expected.put(Paths.get("_js", "tooltip.js"), tooltipScriptContent);
        expected.put(Paths.get("_csv", "mk.csv"),
                "Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size,"
                        + "Sample Size Qualification,Participation,Precision,A,B,Other\n");
        expected.put(Paths.get("_csv", "electorallists.csv"),
                "ID,Abbreviation,Romanized Abbreviation\n" + ",A,\n" + ",B,\n");
        expected.put(Paths.get("_widgets", "tables", "mk.html"),
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "  <head>\n"
                        + "    <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"/>\n"
                        + "    <style>body {\n" + "  background-color: #F9F9F9;\n" + "}\n\n" + "table {\n"
                        + "  font-family: sans-serif;\n" + "}\n\n" + "th {\n" + "  min-width: 65px;\n" + "}</style>\n"
                        + "  </head>\n" + "  <body>\n" + "    <table>\n" + "      <thead>\n" + "        <tr>\n"
                        + "          <th>Fieldwork Period</th>\n" + "          <th>Polling Firm</th>\n"
                        + "          <th>Commissioner(s)</th>\n" + "          <th>Sample Size</th>\n"
                        + "          <th>Other</th>\n" + "        </tr>\n" + "      </thead>\n" + "      <tbody/>\n"
                        + "    </table>\n" + "  </body>\n" + "</html>");
        String baseStyleSheetContent = "header { display: block; width: 100%; }";
        expected.put(Paths.get("_css", "base.css"), baseStyleSheetContent);
        String customStyleSheetContent = "body { font-family: serif; background: #FFFFFF; color: #0E3651; }";
        expected.put(Paths.get("_css", "skin.css"), customStyleSheetContent);
        WebsiteBuilder builder = new WebsiteBuilder(createWebsiteConfiguration(), internationalization, opinionPollsMap,
                Map.empty(), elections, baseStyleSheetContent, customStyleSheetContent, navigationScriptContent,
                sortingScriptContent, tooltipScriptContent, NOW);
        assertTrue(expected.containsSame(builder.build().asMap()));
    }
}
