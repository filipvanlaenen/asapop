package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionList;
import net.filipvanlaenen.asapop.yaml.ElectionLists;
import net.filipvanlaenen.asapop.yaml.ElectionsBuilder;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>IndexPageBuilder</code> class.
 */
public class IndexPageBuilderTest {
    /**
     * First today's date.
     */
    private static final LocalDate NOW1 = LocalDate.of(2022, Month.SEPTEMBER, 7);
    /**
     * Second today's date.
     */
    private static final LocalDate NOW3 = LocalDate.of(2022, Month.OCTOBER, 2);

    /**
     * Creates a website configuration.
     *
     * @return A website configuration.
     */
    private WebsiteConfiguration createWebsiteConfiguration() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        AreaConfiguration sweden = new AreaConfiguration();
        sweden.setAreaCode("se");
        ElectionLists electionListsForSweden = new ElectionLists();
        ElectionList nationalElectionsInSweden = new ElectionList();
        nationalElectionsInSweden.setDates(Map.of(1, "2022-09-11"));
        nationalElectionsInSweden.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/swedish_polls");
        electionListsForSweden.setNational(nationalElectionsInSweden);
        ElectionList europeanElectionsInSweden = new ElectionList();
        europeanElectionsInSweden.setDates(Map.of(1, "2022-09-10"));
        europeanElectionsInSweden.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/swedish_ep_polls");
        electionListsForSweden.setEuropean(europeanElectionsInSweden);
        sweden.setElections(electionListsForSweden);
        AreaConfiguration latvia = new AreaConfiguration();
        latvia.setAreaCode("lv");
        ElectionLists electionListsForLatvia = new ElectionLists();
        ElectionList nationalElectionsInLatvia = new ElectionList();
        nationalElectionsInLatvia.setDates(Map.of(1, "2022-10-01"));
        nationalElectionsInLatvia.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/latvian_polls");
        electionListsForLatvia.setNational(nationalElectionsInLatvia);
        latvia.setElections(electionListsForLatvia);
        AreaConfiguration poland = new AreaConfiguration();
        poland.setAreaCode("pl");
        ElectionLists electionListsForPoland = new ElectionLists();
        ElectionList nationalElectionsInPoland = new ElectionList();
        nationalElectionsInPoland.setDates(Map.of(1, "2022-10-01"));
        nationalElectionsInPoland.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/polish_polls");
        electionListsForPoland.setNational(nationalElectionsInPoland);
        poland.setElections(electionListsForPoland);
        AreaConfiguration netherlands = new AreaConfiguration();
        netherlands.setAreaCode("nl");
        ElectionLists electionListsForNetherlands = new ElectionLists();
        ElectionList nationalElectionsInNetherlands = new ElectionList();
        nationalElectionsInNetherlands.setDates(Map.of(1, "2022-10-01"));
        nationalElectionsInNetherlands.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/dutch_polls");
        electionListsForNetherlands.setNational(nationalElectionsInNetherlands);
        netherlands.setElections(electionListsForNetherlands);
        AreaConfiguration bulgaria = new AreaConfiguration();
        bulgaria.setAreaCode("bg");
        ElectionLists electionListsForBulgaria = new ElectionLists();
        ElectionList nationalElectionsInBulgaria = new ElectionList();
        nationalElectionsInBulgaria.setDates(Map.of(1, "2022-10-03"));
        nationalElectionsInBulgaria.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/bulgarian_polls");
        electionListsForBulgaria.setNational(nationalElectionsInBulgaria);
        bulgaria.setElections(electionListsForBulgaria);
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        websiteConfiguration
                .setAreaConfigurations(Set.of(sweden, latvia, bulgaria, netherlands, northMacedonia, poland));
        return websiteConfiguration;
    }

    /**
     * Verifies that the index page is built correctly when there are two next elections with a GitHub reference.
     */
    @Test
    public void indexPageContentShouldBeBuiltCorrectlyForTwoNextElectionsWithGitHubReference() {
        StringBuilder expected = new StringBuilder();
        addTop(expected);
        expected.append("      <div class=\"two-svg-charts-container\">\n");
        expected.append("        <div class=\"svg-chart-container-left\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("            <a href=\"https://filipvanlaenen.github.io/swedish_polls\">\n");
        expected.append("              <image height=\"250\""
                + " href=\"https://filipvanlaenen.github.io/swedish_polls/average.png\" width=\"500\"/>\n");
        expected.append("            </a>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("        <div class=\"svg-chart-container-right\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("            <a href=\"https://filipvanlaenen.github.io/latvian_polls\">\n");
        expected.append("              <image height=\"250\""
                + " href=\"https://filipvanlaenen.github.io/latvian_polls/average.png\" width=\"500\"/>\n");
        expected.append("            </a>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("      </div>\n");
        addBottom(expected);
        WebsiteConfiguration websiteConfiguration = createWebsiteConfiguration();
        Elections elections = ElectionsBuilder.extractElections(websiteConfiguration);
        assertEquals(expected.toString(),
                new IndexPageBuilder(websiteConfiguration, elections, NOW1).build().asString());
    }

    /**
     * Verifies that the index page is built correctly when there is only one next election with a GitHub reference.
     */
    @Test
    public void indexPageContentShouldBeBuiltCorrectlyForOneNextElectionWithGitHubReference() {
        StringBuilder expected = new StringBuilder();
        addTop(expected);
        expected.append("      <div class=\"two-svg-charts-container\">\n");
        expected.append("        <div class=\"svg-chart-container-left\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("            <a href=\"https://filipvanlaenen.github.io/bulgarian_polls\">\n");
        expected.append("              <image height=\"250\""
                + " href=\"https://filipvanlaenen.github.io/bulgarian_polls/average.png\" width=\"500\"/>\n");
        expected.append("            </a>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("        <div class=\"svg-chart-container-right\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("            <a href=\"\">\n");
        expected.append("              <image height=\"250\" href=\"/average.png\" width=\"500\"/>\n");
        expected.append("            </a>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("      </div>\n");
        addBottom(expected);
        WebsiteConfiguration websiteConfiguration = createWebsiteConfiguration();
        Elections elections = ElectionsBuilder.extractElections(websiteConfiguration);
        assertEquals(expected.toString(),
                new IndexPageBuilder(websiteConfiguration, elections, NOW3).build().asString());
    }

    /**
     * Adds the bottom of the page to the StringBuilder.
     *
     * @param stringBuilder The StringBuilder to which the bottom of the page should be added.
     */
    private void addBottom(final StringBuilder stringBuilder) {
        stringBuilder.append("    </section>\n");
        stringBuilder.append("    <footer>\n");
        stringBuilder.append("      <div class=\"privacy-statement\"> </div>\n");
        stringBuilder.append("    </footer>\n");
        stringBuilder.append("  </body>\n");
        stringBuilder.append("</html>");
    }

    /**
     * Adds the top of the page to the StringBuilder.
     *
     * @param stringBuilder The StringBuilder to which the top of the page should be added.
     */
    private void addTop(final StringBuilder stringBuilder) {
        stringBuilder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        stringBuilder.append("  <head>\n");
        stringBuilder.append("    <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"/>\n");
        stringBuilder.append("    <title>ASAPOP Website</title>\n");
        stringBuilder.append("    <link href=\"_css/base.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        stringBuilder.append("    <link href=\"_css/skin.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        stringBuilder.append("    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\""
                + " type=\"application/javascript\"> </script>\n");
        stringBuilder.append(
                "    <script src=\"_js/internationalization.js\" type=\"application/javascript\">" + " </script>\n");
        stringBuilder.append("    <script src=\"_js/navigation.js\" type=\"application/javascript\">" + " </script>\n");
        stringBuilder.append("    <script src=\"_js/sorting.js\" type=\"application/javascript\">" + " </script>\n");
        stringBuilder.append("  </head>\n");
        stringBuilder.append("  <body onload=\"initializeLanguage();\">\n");
        stringBuilder.append("    <header>\n");
        stringBuilder.append("      <div class=\"header-left\">\n");
        stringBuilder.append("        <span class=\"main-page\"> </span>\n");
        stringBuilder.append("      </div>\n");
        stringBuilder.append("      <div class=\"header-right\"><span class=\"go-to\"> </span>: <select"
                + " id=\"area-selector\" onchange=\"moveToArea(0);\">\n");
        stringBuilder.append("  <option> </option>\n");
        stringBuilder.append("  <option class=\"_area_bg\" value=\"bg\"> </option>\n");
        stringBuilder.append("  <option class=\"_area_lv\" value=\"lv\"> </option>\n");
        stringBuilder.append("  <option class=\"_area_mk\" value=\"mk\"> </option>\n");
        stringBuilder.append("  <option class=\"_area_nl\" value=\"nl\"> </option>\n");
        stringBuilder.append("  <option class=\"_area_pl\" value=\"pl\"> </option>\n");
        stringBuilder.append("  <option class=\"_area_se\" value=\"se\"> </option>\n");
        stringBuilder.append("</select> · <a class=\"electoral-calendar\" href=\"calendar.html\"> </a> · <a"
                + " class=\"csv-files\" href=\"csv.html\"> </a> · <a class=\"statistics-page\""
                + " href=\"statistics.html\"> </a> · <span class=\"language\"> </span>: <select"
                + " id=\"language-selector\" onchange=\"loadLanguage();\">\n");
        stringBuilder.append("  <option value=\"de\">Deutsch</option>\n");
        stringBuilder.append("  <option value=\"en\">English</option>\n");
        stringBuilder.append("  <option value=\"eo\">Esperanto</option>\n");
        stringBuilder.append("  <option value=\"fr\">français</option>\n");
        stringBuilder.append("  <option value=\"nl\">Nederlands</option>\n");
        stringBuilder.append("  <option value=\"no\">norsk</option>\n");
        stringBuilder.append("</select></div>\n");
        stringBuilder.append("    </header>\n");
        stringBuilder.append("    <section>\n");
        stringBuilder.append("      <h1 class=\"upcoming-elections\"> </h1>\n");
    }
}
