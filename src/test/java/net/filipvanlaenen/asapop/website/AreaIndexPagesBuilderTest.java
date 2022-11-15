package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>AreaIndexPagesBuilder</code> class.
 */
public class AreaIndexPagesBuilderTest {
    /**
     * Verifies that the index page for an area is built correctly when it is absent.
     */
    @Test
    public void absentAreaIndexPageShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        Map<String, OpinionPolls> opinionPollsMap = Collections.EMPTY_MAP;
        websiteConfiguration.setAreaConfigurations(Collections.EMPTY_SET);
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        assertEquals(createEmptyAreaIndexPage(),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap).createAreaIndexPage(northMacedonia));
    }

    /**
     * Verifies that the index page for an area is built correctly when no information is registered on the area.
     */
    @Test
    public void emptyAreaIndexPageShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", new OpinionPolls(Collections.EMPTY_SET));
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        northMacedonia.setElectionConfigurations(Collections.EMPTY_SET);
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia));
        assertEquals(createEmptyAreaIndexPage(),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap).createAreaIndexPage(northMacedonia));
    }

    /**
     * Verifies that the index page for an area with upcoming elections is built correctly.
     */
    @Test
    public void areaIndexPageWithUpcomingElectionsShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", new OpinionPolls(Collections.EMPTY_SET));
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        ElectionConfiguration parliament = new ElectionConfiguration();
        parliament.setNextElectionDate("2023-03-05");
        parliament.setType("Parliament");
        ElectionConfiguration regional = new ElectionConfiguration();
        regional.setNextElectionDate("2023-03-05");
        regional.setType("Regional");
        ElectionConfiguration local = new ElectionConfiguration();
        local.setNextElectionDate("2023-03-05");
        local.setType("Local");
        ElectionConfiguration president = new ElectionConfiguration();
        president.setNextElectionDate("≈2024-03");
        president.setType("President");
        ElectionConfiguration european = new ElectionConfiguration();
        european.setNextElectionDate("≤2025-03");
        european.setType("European");
        northMacedonia.setElectionConfigurations(Set.of(local, parliament, regional, president, european));
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia));
        assertEquals(createAreaIndexPageWithUpcomingElections(),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap).createAreaIndexPage(northMacedonia));
    }

    private String createEmptyAreaIndexPage() {
        StringBuilder expected = new StringBuilder();
        addTopPart(expected);
        expected.append("      <p><span class=\"none\"> </span>.</p>\n");
        addMiddlePart(expected);
        expected.append("      <p><span class=\"none\"> </span>.</p>\n");
        addBottomPart(expected);
        return expected.toString();
    }

    private String createAreaIndexPageWithUpcomingElections() {
        StringBuilder expected = new StringBuilder();
        addTopPart(expected);
        expected.append("      <ul>\n");
        expected.append("        <li>2023-03-05: <span class=\"local\"> </span></li>\n");
        expected.append("        <li>2023-03-05: <span class=\"parliament\"> </span></li>\n");
        expected.append("        <li>2023-03-05: <span class=\"regional\"> </span></li>\n");
        expected.append(
                "        <li><span class=\"around\"> </span> 2024-03: <span class=\"president\"> </span></li>\n");
        expected.append(
                "        <li><span class=\"no-later-than\"> </span> 2025-03: <span class=\"european\"> </span></li>\n");
        expected.append("      </ul>\n");
        addMiddlePart(expected);
        expected.append("      <p><span class=\"none\"> </span>.</p>\n");
        addBottomPart(expected);
        return expected.toString();
    }

    private void addMiddlePart(StringBuilder expected) {
        expected.append("      <h2 class=\"latest-opinion-polls\"> </h2>\n");
    }

    private void addBottomPart(StringBuilder expected) {
        expected.append("    </section>\n");
        expected.append("    <footer>\n");
        expected.append("      <div class=\"privacy-statement\"> </div>\n");
        expected.append("    </footer>\n");
        expected.append("  </body>\n");
        expected.append("</html>");
    }

    private void addTopPart(StringBuilder expected) {
        expected.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        expected.append("  <head>\n");
        expected.append("    <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"/>\n");
        expected.append("    <title>ASAPOP Website</title>\n");
        expected.append("    <link href=\"../_css/base.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("    <link href=\"../_css/skin.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\""
                + " type=\"application/javascript\"> </script>\n");
        expected.append(
                "    <script src=\"../_js/internationalization.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("  </head>\n");
        expected.append("  <body onload=\"initializeLanguage();\">\n");
        expected.append("    <header>\n");
        expected.append("      <div class=\"header-left\">\n");
        expected.append("        <a class=\"main-page\" href=\"../index.html\"> </a>\n");
        expected.append("      </div>\n");
        expected.append("      <div class=\"header-right\"><a class=\"electoral-calendar\" href=\"../calendar.html\">"
                + " </a> · <a class=\"csv-files\" href=\"../csv.html\"> </a> · <span class=\"language\"> </span>:"
                + " <select id=\"language-selector\" onchange=\"loadLanguage();\">\n");
        expected.append("  <option value=\"de\">Deutsch</option>\n");
        expected.append("  <option value=\"en\">English</option>\n");
        expected.append("  <option value=\"eo\">Esperanto</option>\n");
        expected.append("  <option value=\"fr\">français</option>\n");
        expected.append("  <option value=\"nl\">Nederlands</option>\n");
        expected.append("  <option value=\"no\">norsk</option>\n");
        expected.append("</select></div>\n");
        expected.append("    </header>\n");
        expected.append("    <section>\n");
        expected.append("      <h1 class=\"_area_mk\"> </h1>\n");
        expected.append("      <h2 class=\"upcoming-elections\"> </h2>\n");
    }
}
