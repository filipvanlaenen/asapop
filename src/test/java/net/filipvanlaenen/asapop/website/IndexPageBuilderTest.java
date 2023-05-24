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
     * Today's date.
     */
    private static final LocalDate NOW = LocalDate.of(2022, Month.SEPTEMBER, 7);

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
        AreaConfiguration bulgaria = new AreaConfiguration();
        bulgaria.setAreaCode("bg");
        ElectionLists electionListsForBulgaria = new ElectionLists();
        ElectionList nationalElectionsInBulgaria = new ElectionList();
        nationalElectionsInBulgaria.setDates(Map.of(1, "2022-10-02"));
        nationalElectionsInBulgaria.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/bulgarian_polls");
        electionListsForBulgaria.setNational(nationalElectionsInBulgaria);
        bulgaria.setElections(electionListsForBulgaria);
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        websiteConfiguration.setAreaConfigurations(Set.of(sweden, latvia, bulgaria, northMacedonia, poland));
        return websiteConfiguration;
    }

    /**
     * Verifies that the index page is built correctly.
     */
    @Test
    public void indexPageContentShouldBeBuiltCorrectly() {
        StringBuilder expected = new StringBuilder();
        expected.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        expected.append("  <head>\n");
        expected.append("    <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"/>\n");
        expected.append("    <title>ASAPOP Website</title>\n");
        expected.append("    <link href=\"_css/base.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("    <link href=\"_css/skin.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\""
                + " type=\"application/javascript\"> </script>\n");
        expected.append(
                "    <script src=\"_js/internationalization.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("    <script src=\"_js/navigation.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("    <script src=\"_js/sorting.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("  </head>\n");
        expected.append("  <body onload=\"initializeLanguage();\">\n");
        expected.append("    <header>\n");
        expected.append("      <div class=\"header-left\">\n");
        expected.append("        <span class=\"main-page\"> </span>\n");
        expected.append("      </div>\n");
        expected.append("      <div class=\"header-right\"><span class=\"go-to\"> </span>: <select"
                + " id=\"area-selector\" onchange=\"moveToArea(0);\">\n");
        expected.append("  <option> </option>\n");
        expected.append("  <option class=\"_area_bg\" value=\"bg\"> </option>\n");
        expected.append("  <option class=\"_area_lv\" value=\"lv\"> </option>\n");
        expected.append("  <option class=\"_area_mk\" value=\"mk\"> </option>\n");
        expected.append("  <option class=\"_area_pl\" value=\"pl\"> </option>\n");
        expected.append("  <option class=\"_area_se\" value=\"se\"> </option>\n");
        expected.append("</select> · <a class=\"electoral-calendar\" href=\"calendar.html\"> </a> · <a"
                + " class=\"csv-files\" href=\"csv.html\"> </a> · <a class=\"statistics-page\""
                + " href=\"statistics.html\"> </a> · <span class=\"language\"> </span>: <select"
                + " id=\"language-selector\" onchange=\"loadLanguage();\">\n");
        expected.append("  <option value=\"de\">Deutsch</option>\n");
        expected.append("  <option value=\"en\">English</option>\n");
        expected.append("  <option value=\"eo\">Esperanto</option>\n");
        expected.append("  <option value=\"fr\">français</option>\n");
        expected.append("  <option value=\"nl\">Nederlands</option>\n");
        expected.append("  <option value=\"no\">norsk</option>\n");
        expected.append("</select></div>\n");
        expected.append("    </header>\n");
        expected.append("    <section>\n");
        expected.append("      <h1 class=\"upcoming-elections\"> </h1>\n");
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
        expected.append("    </section>\n");
        expected.append("    <footer>\n");
        expected.append("      <div class=\"privacy-statement\"> </div>\n");
        expected.append("    </footer>\n");
        expected.append("  </body>\n");
        expected.append("</html>");
        WebsiteConfiguration websiteConfiguration = createWebsiteConfiguration();
        Elections elections = ElectionsBuilder.extractElections(websiteConfiguration);
        assertEquals(expected.toString(),
                new IndexPageBuilder(websiteConfiguration, elections, NOW).build().asString());
    }
}
