package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>AreaIndexPagesBuilder</code> class.
 */
public class AreaIndexPagesBuilderTest {
    /**
     * Verifies that the correct pages are built.
     */
    @Test
    public void buildShouldBuilderTheCorrectPages() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        AreaConfiguration serbia = new AreaConfiguration();
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia, serbia));
        Map<String, OpinionPolls> opinionPollsMap = Collections.EMPTY_MAP;
        AreaIndexPagesBuilder builder = new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap);
        Map<Path, String> expected = Map.of(Paths.get("mk", "index.html"), builder.createAreaIndexPage(northMacedonia));
        assertEquals(expected, builder.build());
    }

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
     * Verifies that the index page for an area with a small opinion poll is built correctly.
     */
    @Test
    public void areaIndexPageWithASmallOpinionPollShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        OpinionPolls opinionPolls = RichOpinionPollsFile
                .parse("•PF: ACME •FS: 2021-07-27 •FE: 2021-07-28 A:55 B:40", "A: •A:AP", "B: •A:BL").getOpinionPolls();
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", opinionPolls);
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        northMacedonia.setElectionConfigurations(Collections.EMPTY_SET);
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia));
        assertEquals(createAreaIndexPageWithASmallOpinionPoll(),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap).createAreaIndexPage(northMacedonia));
    }

    /**
     * Verifies that the index page for an area with many and large opinion polls is built correctly.
     */
    @Test
    public void areaIndexPageWithManyAndLargeOpinionPollsShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse(
                "•PF: ACME •FS: 2021-07-27 •FE: 2021-07-28 A:55 B:10 C:5 D:5 E+I+J+K:5 F:5 G:5 H:5",
                "•C: The Times •FS: 2022-10-16 •PD: 2022-10-26 A:55 B:40 D:4",
                "•PF: ACME •FE: 2022-10-17 A:55 B:42.1 D:2.9",
                "•PF: ACME •C: The Times •C: The Post •C: The Independent •FS: 2022-11-12 •FE: 2022-11-16 A:55 B:40.1",
                "A: •A:AP", "B: •A:BL", "C: •A:C", "D: •A:D", "E: •A:E", "F: •A:F", "G: •A:G", "H: •A:H", "I: •A:I",
                "J: •A:J", "K: •A:K").getOpinionPolls();
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", opinionPolls);
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        northMacedonia.setElectionConfigurations(Collections.EMPTY_SET);
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia));
        assertEquals(createAreaIndexPageWithManyAndLargeOpinionPolls(),
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

    /**
     * Creates an empty area index page.
     *
     * @return An empty area index page.
     */
    private String createEmptyAreaIndexPage() {
        StringBuilder expected = new StringBuilder();
        addTopPart(expected);
        expected.append("      <p><span class=\"none\"> </span>.</p>\n");
        addMiddlePart(expected);
        expected.append("      <p><span class=\"none\"> </span>.</p>\n");
        addBottomPart(expected);
        return expected.toString();
    }

    /**
     * Creates an index page for an area with only one small opinion poll.
     *
     * @return An index page for an area with only one small opinion poll.
     */
    private String createAreaIndexPageWithASmallOpinionPoll() {
        StringBuilder expected = new StringBuilder();
        addTopPart(expected);
        expected.append("      <p><span class=\"none\"> </span>.</p>\n");
        addMiddlePart(expected);
        expected.append("      <table class=\"opinion-polls-table\">\n");
        expected.append("        <thead>\n");
        expected.append("          <tr>\n");
        expected.append("            <th class=\"fieldwork-period\"> </th>\n");
        expected.append("            <th class=\"polling-firm-commissioner\"> </th>\n");
        expected.append("            <th class=\"electoral-lists-th\">AP</th>\n");
        expected.append("            <th class=\"electoral-lists-th\">BL</th>\n");
        expected.append("            <th class=\"other\"> </th>\n");
        expected.append("          </tr>\n");
        expected.append("        </thead>\n");
        expected.append("        <tbody>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2021-07-27 – 2021-07-28</td>\n");
        expected.append("            <td>ACME</td>\n");
        expected.append("            <td class=\"result-value-td\">55%</td>\n");
        expected.append("            <td class=\"result-value-td\">40%</td>\n");
        expected.append("            <td class=\"result-value-td\">(5%)</td>\n");
        expected.append("          </tr>\n");
        expected.append("        </tbody>\n");
        expected.append("      </table>\n");
        addBottomPart(expected);
        return expected.toString();
    }

    /**
     * Creates an index page for an area with many and large opinion polls.
     *
     * @return An index page for an area with many and large opinion polls.
     */
    private String createAreaIndexPageWithManyAndLargeOpinionPolls() {
        StringBuilder expected = new StringBuilder();
        addTopPart(expected);
        expected.append("      <p><span class=\"none\"> </span>.</p>\n");
        addMiddlePart(expected);
        expected.append("      <table class=\"opinion-polls-table\">\n");
        expected.append("        <thead>\n");
        expected.append("          <tr>\n");
        expected.append("            <th class=\"fieldwork-period\"> </th>\n");
        expected.append("            <th class=\"polling-firm-commissioner\"> </th>\n");
        expected.append("            <th class=\"electoral-lists-th\">AP</th>\n");
        expected.append("            <th class=\"electoral-lists-th\">BL</th>\n");
        expected.append("            <th class=\"electoral-lists-th\">C</th>\n");
        expected.append("            <th class=\"electoral-lists-th\">D</th>\n");
        expected.append("            <th class=\"electoral-lists-th\">E–I–J–K</th>\n");
        expected.append("            <th class=\"electoral-lists-th\">F</th>\n");
        expected.append("            <th class=\"electoral-lists-th\">G</th>\n");
        expected.append("            <th class=\"other\"> </th>\n");
        expected.append("          </tr>\n");
        expected.append("        </thead>\n");
        expected.append("        <tbody>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2022-11-12 – 2022-11-16</td>\n");
        expected.append("            <td>ACME / The Independent–The Post–The Times</td>\n");
        expected.append("            <td class=\"result-value-td\">55%</td>\n");
        expected.append("            <td class=\"result-value-td\">40<span class=\"decimal-point\"> </span>1%</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">(4<span class=\"decimal-point\"> </span>9%)</td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2022-10-16 – 2022-10-26<sup>\n");
        expected.append("  <i>p</i>\n");
        expected.append("</sup></td>\n");
        expected.append("            <td>The Times</td>\n");
        expected.append("            <td class=\"result-value-td\">55%</td>\n");
        expected.append("            <td class=\"result-value-td\">40%</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">4%</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">(1%)</td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td> – 2022-10-17</td>\n");
        expected.append("            <td>ACME</td>\n");
        expected.append("            <td class=\"result-value-td\">55%</td>\n");
        expected.append("            <td class=\"result-value-td\">42<span class=\"decimal-point\"> </span>1%</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">2<span class=\"decimal-point\"> </span>9%</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">(0<span class=\"decimal-point\"> </span>0%)</td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2021-07-27 – 2021-07-28</td>\n");
        expected.append("            <td>ACME</td>\n");
        expected.append("            <td class=\"result-value-td\">55%</td>\n");
        expected.append("            <td class=\"result-value-td\">10%</td>\n");
        expected.append("            <td class=\"result-value-td\">5%</td>\n");
        expected.append("            <td class=\"result-value-td\">5%</td>\n");
        expected.append("            <td class=\"result-value-td\">5%</td>\n");
        expected.append("            <td class=\"result-value-td\">5%</td>\n");
        expected.append("            <td class=\"result-value-td\">5%</td>\n");
        expected.append("            <td class=\"result-value-td\">(10%)</td>\n");
        expected.append("          </tr>\n");
        expected.append("        </tbody>\n");
        expected.append("      </table>\n");
        expected.append("      <p>\n");
        expected.append("        <sup>\n");
        expected.append("          <i>p</i>\n");
        expected.append("        </sup>\n");
        expected.append("        <span class=\"publication-date\"> </span>\n");
        expected.append("      </p>\n");
        addBottomPart(expected);
        return expected.toString();
    }

    /**
     * Creates an index page for an area with upcoming elections.
     *
     * @return An index page for an area with upcoming elections.
     */
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

    /**
     * Adds the middle part of an area index page to a StringBuilder instance.
     *
     * @param expected The StringBuilder instance to add the middle part to.
     */
    private void addMiddlePart(final StringBuilder expected) {
        expected.append("      <h2 class=\"latest-opinion-polls\"> </h2>\n");
    }

    /**
     * Adds the bottom part of an area index page to a StringBuilder instance.
     *
     * @param expected The StringBuilder instance to add the bottom part to.
     */
    private void addBottomPart(final StringBuilder expected) {
        expected.append("    </section>\n");
        expected.append("    <footer>\n");
        expected.append("      <div class=\"privacy-statement\"> </div>\n");
        expected.append("    </footer>\n");
        expected.append("  </body>\n");
        expected.append("</html>");
    }

    /**
     * Adds the top part of an area index page to a StringBuilder instance.
     *
     * @param expected The StringBuilder instance to add the top part to.
     */
    private void addTopPart(final StringBuilder expected) {
        expected.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        expected.append("  <head>\n");
        expected.append("    <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"/>\n");
        expected.append("    <title>ASAPOP Website</title>\n");
        expected.append("    <link href=\"../_css/base.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("    <link href=\"../_css/skin.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\""
                + " type=\"application/javascript\"> </script>\n");
        expected.append(
                "    <script src=\"../_js/internationalization.js\" type=\"application/javascript\"> </script>\n");
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
