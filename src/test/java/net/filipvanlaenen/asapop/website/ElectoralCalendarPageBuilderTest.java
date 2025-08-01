package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.Election;
import net.filipvanlaenen.asapop.model.ElectionDate;
import net.filipvanlaenen.asapop.model.ElectionType;
import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.asapop.website.ElectoralCalendarPageBuilder.ElectionComparator;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionList;
import net.filipvanlaenen.asapop.yaml.ElectionLists;
import net.filipvanlaenen.asapop.yaml.ElectionsBuilder;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.Map;

/**
 * Unit tests on the <code>ElectoralCalendarPageBuilder</code> class.
 */
public class ElectoralCalendarPageBuilderTest {
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
        websiteConfiguration.setName("Test");
        AreaConfiguration sweden = new AreaConfiguration();
        sweden.setAreaCode("se");
        AreaConfiguration denmark = new AreaConfiguration();
        denmark.setAreaCode("dk");
        ElectionLists electionListsForDenmark = new ElectionLists();
        ElectionList nationalElectionsInDenmark = new ElectionList();
        nationalElectionsInDenmark.setDates(Map.of(1, "2022-11-01"));
        electionListsForDenmark.setNational(nationalElectionsInDenmark);
        denmark.setElections(electionListsForDenmark);
        AreaConfiguration estonia = new AreaConfiguration();
        estonia.setAreaCode("ee");
        ElectionLists electionListsForEstonia = new ElectionLists();
        ElectionList nationalElectionsInEstonia = new ElectionList();
        nationalElectionsInEstonia.setDates(Map.of(1, "2023-03-05"));
        electionListsForEstonia.setNational(nationalElectionsInEstonia);
        ElectionList presidentialElectionsInEstonia = new ElectionList();
        presidentialElectionsInEstonia.setDates(Map.of(1, "≈2024-03"));
        electionListsForEstonia.setPresidential(presidentialElectionsInEstonia);
        estonia.setElections(electionListsForEstonia);
        AreaConfiguration france = new AreaConfiguration();
        france.setAreaCode("fr");
        ElectionLists electionListsForFrance = new ElectionLists();
        ElectionList nationalElectionsInFrance = new ElectionList();
        nationalElectionsInFrance.setDates(Map.of(1, "2023-03-05"));
        electionListsForFrance.setNational(nationalElectionsInFrance);
        ElectionList presidentialElectionsInFrance = new ElectionList();
        presidentialElectionsInFrance.setDates(Map.of(1, "2023-03-05"));
        electionListsForFrance.setPresidential(presidentialElectionsInFrance);
        ElectionList europeanElectionsInFrance = new ElectionList();
        europeanElectionsInFrance.setDates(Map.of(1, "2023-03-05"));
        electionListsForFrance.setEuropean(europeanElectionsInFrance);
        france.setElections(electionListsForFrance);
        AreaConfiguration greenland = new AreaConfiguration();
        greenland.setAreaCode("gl");
        ElectionLists electionListsForGreenland = new ElectionLists();
        ElectionList nationalElectionsInGreenland = new ElectionList();
        nationalElectionsInGreenland.setDates(Map.of(1, "≤2025-04-06"));
        electionListsForGreenland.setNational(nationalElectionsInGreenland);
        ElectionList presidentialElectionsInGreenland = new ElectionList();
        presidentialElectionsInGreenland.setDates(Map.of(1, "2023-03-05"));
        electionListsForGreenland.setPresidential(presidentialElectionsInGreenland);
        greenland.setElections(electionListsForGreenland);
        websiteConfiguration
                .setAreaConfigurations(Set.of(denmark, estonia, france, greenland, sweden, new AreaConfiguration()));
        return websiteConfiguration;
    }

    /**
     * Verifies that the electoral calendar page is built correctly.
     */
    @Test
    public void electoralCalendarPageIsBuiltCorrectly() {
        StringBuilder expected = new StringBuilder();
        expected.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        expected.append("  <head>\n");
        expected.append("    <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"/>\n");
        expected.append("    <title>ASAPOP Website Test</title>\n");
        expected.append("    <link href=\"_css/base.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("    <link href=\"_css/skin.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\""
                + " type=\"application/javascript\"> </script>\n");
        expected.append(
                "    <script src=\"_js/internationalization.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("    <script src=\"_js/navigation.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("    <script src=\"_js/sorting.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("    <script src=\"_js/tooltip.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("  </head>\n");
        expected.append("  <body onload=\"initializeLanguage();\">\n");
        expected.append("    <header>\n");
        expected.append("      <div class=\"header-left\">\n");
        expected.append("        <a class=\"main-page\" href=\"index.html\"> </a>\n");
        expected.append("      </div>\n");
        expected.append("      <div class=\"header-right\"><span class=\"go-to\"> </span>: <select"
                + " id=\"area-selector\" onchange=\"moveToArea(0);\">\n");
        expected.append("  <option> </option>\n");
        expected.append("  <option class=\"_area_dk\" value=\"dk\"> </option>\n");
        expected.append("  <option class=\"_area_ee\" value=\"ee\"> </option>\n");
        expected.append("  <option class=\"_area_fr\" value=\"fr\"> </option>\n");
        expected.append("  <option class=\"_area_gl\" value=\"gl\"> </option>\n");
        expected.append("  <option class=\"_area_se\" value=\"se\"> </option>\n");
        expected.append("</select> · <span class=\"electoral-calendar\"> </span> · <a class=\"csv-files\""
                + " href=\"csv.html\"> </a> · <a class=\"statistics-page\" href=\"statistics.html\"> </a> · <span"
                + " class=\"language\"> </span>: <select id=\"language-selector\" onchange=\"loadLanguage();\">\n");
        expected.append("  <option value=\"de\">Deutsch</option>\n");
        expected.append("  <option value=\"en\">English</option>\n");
        expected.append("  <option value=\"eo\">Esperanto</option>\n");
        expected.append("  <option value=\"fr\">français</option>\n");
        expected.append("  <option value=\"nl\">Nederlands</option>\n");
        expected.append("  <option value=\"no\">norsk</option>\n");
        expected.append("</select></div>\n");
        expected.append("    </header>\n");
        expected.append("    <section>\n");
        expected.append("      <h1 class=\"electoral-calendar\"> </h1>\n");
        expected.append("      <p><span class=\"import-this-calendar-as-an\"> </span> <a class=\"icalendar-file\""
                + " href=\"calendar.ical\"> </a>.</p>\n");
        expected.append("      <table>\n");
        expected.append("        <thead>\n");
        expected.append("          <tr>\n");
        expected.append("            <th class=\"date\"> </th>\n");
        expected.append("            <th class=\"country\"> </th>\n");
        expected.append("            <th class=\"election-type\"> </th>\n");
        expected.append("          </tr>\n");
        expected.append("        </thead>\n");
        expected.append("        <tbody>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2022-11-01</td>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_dk\" href=\"dk/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"parliament\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2023-03-05</td>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_ee\" href=\"ee/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"parliament\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2023-03-05</td>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_fr\" href=\"fr/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"president\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2023-03-05</td>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_fr\" href=\"fr/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"parliament\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2023-03-05</td>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_fr\" href=\"fr/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"european-parliament\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2023-03-05</td>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_gl\" href=\"gl/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"president\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td><span class=\"around\"> </span> 2024-03</td>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_ee\" href=\"ee/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"president\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td><span class=\"no-later-than\"> </span> 2025-04-06</td>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_gl\" href=\"gl/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"parliament\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("        </tbody>\n");
        expected.append("      </table>\n");
        expected.append("    </section>\n");
        expected.append("    <footer>\n");
        expected.append("      <div class=\"privacy-statement\"> </div>\n");
        expected.append("    </footer>\n");
        expected.append("  </body>\n");
        expected.append("</html>");
        WebsiteConfiguration websiteConfiguration = createWebsiteConfiguration();
        Elections elections = ElectionsBuilder.extractAndValidateElections(websiteConfiguration, Map.empty(), NOW);
        assertEquals(expected.toString(),
                new ElectoralCalendarPageBuilder(websiteConfiguration, elections, NOW).build().asString());
    }

    /**
     * Verifies that an election with an earlier date comes first.
     */
    @Test
    public void electionComparatorShouldSortElectionWithEarlierDateFirst() {
        Election e1 = new Election("AA", ElectionType.NATIONAL, 1, List.of(ElectionDate.parse("2023-12-13")),
                Collections.EMPTY_LIST, null);
        Election e2 = new Election("AA", ElectionType.NATIONAL, 1, List.of(ElectionDate.parse("2023-12-14")),
                Collections.EMPTY_LIST, null);
        assertTrue(new ElectionComparator(NOW).compare(e1, e2) < 0);
        assertTrue(new ElectionComparator(NOW).compare(e2, e1) > 0);
    }

    /**
     * Verifies that for elections with the same date, they are sorted according to area code.
     */
    @Test
    public void electionComparatorShouldSortElectionWithSameDatesAccordingToAreaCode() {
        Election e1 = new Election("AA", ElectionType.NATIONAL, 1, List.of(ElectionDate.parse("2023-12-13")),
                Collections.EMPTY_LIST, null);
        Election e2 = new Election("AB", ElectionType.NATIONAL, 1, List.of(ElectionDate.parse("2023-12-13")),
                Collections.EMPTY_LIST, null);
        assertTrue(new ElectionComparator(NOW).compare(e1, e2) < 0);
        assertTrue(new ElectionComparator(NOW).compare(e2, e1) > 0);
    }

    /**
     * Verifies that for elections with the same date and area code, they are sorted according to the election type.
     */
    @Test
    public void electionComparatorShouldSortElectionWithSameDatesAndAreaCodesAccordingToElectionType() {
        Election e1 = new Election("AA", ElectionType.NATIONAL, 1, List.of(ElectionDate.parse("2023-12-13")),
                Collections.EMPTY_LIST, null);
        Election e2 = new Election("AA", ElectionType.EUROPEAN, 1, List.of(ElectionDate.parse("2023-12-13")),
                Collections.EMPTY_LIST, null);
        assertTrue(new ElectionComparator(NOW).compare(e1, e2) < 0);
        assertTrue(new ElectionComparator(NOW).compare(e2, e1) > 0);
    }
}
