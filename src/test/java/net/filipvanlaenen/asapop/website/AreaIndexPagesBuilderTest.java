package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionList;
import net.filipvanlaenen.asapop.yaml.ElectionLists;
import net.filipvanlaenen.asapop.yaml.ElectionsBuilder;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the <code>AreaIndexPagesBuilder</code> class.
 */
public class AreaIndexPagesBuilderTest {
    /**
     * The magic number three.
     */
    private static final int THREE = 3;
    /**
     * Today's date.
     */
    private static final LocalDate NOW = LocalDate.of(2022, Month.DECEMBER, 7);
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test AreaIndexPagesBuilderTest.");

    /**
     * Verifies that the correct pages are built.
     */
    @Test
    public void buildShouldBuildTheCorrectPages() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Test");
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        AreaConfiguration serbia = new AreaConfiguration();
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia, serbia));
        AreaIndexPagesBuilder builder =
                new AreaIndexPagesBuilder(websiteConfiguration, Map.empty(), new Elections(), NOW);
        Map<Path, String> expected = Map.of(Paths.get("mk", "index.html"), builder.createAreaIndexPage(northMacedonia));
        assertTrue(expected.containsSame(builder.build()));
    }

    /**
     * Verifies that the index page for an area is built correctly when it is absent.
     */
    @Test
    public void absentAreaIndexPageShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Test");
        websiteConfiguration.setAreaConfigurations(Collections.EMPTY_SET);
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        assertEquals(createEmptyAreaIndexPage(false),
                new AreaIndexPagesBuilder(websiteConfiguration, Map.empty(), new Elections(), NOW)
                        .createAreaIndexPage(northMacedonia));
    }

    /**
     * Verifies that the index page for an area is built correctly when no information is registered on the area.
     */
    @Test
    public void emptyAreaIndexPageShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Test");
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", new OpinionPolls(Collections.EMPTY_SET));
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia));
        assertEquals(createEmptyAreaIndexPage(true),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, new Elections(), NOW)
                        .createAreaIndexPage(northMacedonia));
    }

    /**
     * Verifies that the index page for an area with a small opinion poll is built correctly.
     */
    @Test
    public void areaIndexPageWithASmallOpinionPollShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Test");
        OpinionPolls opinionPolls = RichOpinionPollsFile
                .parse(TOKEN, "•PF: ACME •FS: 2021-07-27 •FE: 2021-07-28 A:55 B:40", "A: MK001 •A:AP", "B: MK002 •A:BL")
                .getOpinionPolls();
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", opinionPolls);
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia));
        assertEquals(createAreaIndexPageWithASmallOpinionPoll(),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, new Elections(), NOW)
                        .createAreaIndexPage(northMacedonia));
    }

    /**
     * Verifies that the index page for an area with a small opinion poll with the results expressed as number of seats
     * is built correctly.
     */
    @Test
    public void areaIndexPageWithAnOpinionPollWithResultsInNumberOfSeatsShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Test");
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse(TOKEN,
                "•PF: ACME •FS: 2021-07-27 •FE: 2021-07-28 •U: S A:55 B:40 C: 10 D: 9 E: 8 F: 7 G: 6 H: 5 I: 4",
                "A: MK001 •A:AP", "B: MK002 •A:BL", "C: MK003 •A:C", "D: MK004 •A:D", "E: MK005 •A:E", "F: MK006 •A:F",
                "G: MK007 •A:G", "H: MK008 •A:H", "I: MK009 •A:I").getOpinionPolls();
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", opinionPolls);
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia));
        assertEquals(createAreaIndexPageWithAnOpinionPollWithResultsInNumberOfSeats(),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, new Elections(), NOW)
                        .createAreaIndexPage(northMacedonia));
    }

    /**
     * Verifies that the index page for an area with a small opinion poll and one polling firm not included is built
     * correctly.
     */
    @Test
    public void areaIndexPageWithASmallOpinionPollAndOnePollingFirmNotIncludedShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Test");
        OpinionPolls opinionPolls = RichOpinionPollsFile
                .parse(TOKEN, "•PF: ACME •FS: 2021-07-27 •FE: 2021-07-28 A:55 B:40", "A: MK001 •A:AP", "B: MK002 •A:BL")
                .getOpinionPolls();
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", opinionPolls);
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        northMacedonia.setPollingFirmsNotIncluded(Map.of("BCME", "foo"));
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia));
        assertEquals(createAreaIndexPageWithASmallOpinionPoll("BCME", "foo"),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, new Elections(), NOW)
                        .createAreaIndexPage(northMacedonia));
    }

    /**
     * Verifies that the index page for an area with a small opinion poll and many polling firms not included is built
     * correctly.
     */
    @Test
    public void areaIndexPageWithASmallOpinionPollAndManyPollingFirmsNotIncludedShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Test");
        OpinionPolls opinionPolls = RichOpinionPollsFile
                .parse(TOKEN, "•PF: ACME •FS: 2021-07-27 •FE: 2021-07-28 A:55 B:40", "A: MK001 •A:AP", "B: MK002 •A:BL")
                .getOpinionPolls();
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", opinionPolls);
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        ModifiableMap<String, String> pollingFirmsNotIncluded = ModifiableMap.empty();
        pollingFirmsNotIncluded.put("FCME", "foo-bar");
        pollingFirmsNotIncluded.put("ECME", "qux");
        pollingFirmsNotIncluded.put("DCME", "bar");
        pollingFirmsNotIncluded.put("CCME", "foo-bar");
        pollingFirmsNotIncluded.put("BCME", "foo");
        pollingFirmsNotIncluded.put("GCME", "foo");
        northMacedonia.setPollingFirmsNotIncluded(pollingFirmsNotIncluded);
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia));
        assertEquals(
                createAreaIndexPageWithASmallOpinionPoll("BCME", "foo", "CCME", "foo-bar", "DCME", "bar", "ECME", "qux",
                        "FCME", "foo-bar", "GCME", "foo"),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, new Elections(), NOW)
                        .createAreaIndexPage(northMacedonia));
    }

    /**
     * Verifies that the index page for an area with many and large opinion polls is built correctly.
     */
    @Test
    public void areaIndexPageWithManyAndLargeOpinionPollsShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Test");
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse(TOKEN,
                "•PF: ACME •FS: 2021-07-27 •FE: 2021-07-28 A:55 B:10 C:5 D:5 E+I+J+K:5 F:5 G:5 H:5",
                "•C: The Times •FS: 2022-10-16 •PD: 2022-10-26 A:55 B:40 D:4",
                "•PF: ACME •FE: 2022-10-17 A:55 B:42.1 D:2.9",
                "•PF: ACME •C: The Times •C: The Post •C: The Independent •FS: 2022-11-12 •FE: 2022-11-16 A:55 B:40.1",
                "•PF: ACME •FE: 2023-02-04 A:55 B:20 D:3 •N:13", "A: MK001 •A:AP", "B: MK002 •A:BL", "C: MK003 •A:C",
                "D: MK004 •A:D", "E: MK005 •A:E", "F: MK006 •A:F", "G: MK007 •A:G", "H: MK008 •A:H", "I: MK009 •A:I",
                "J: MK010 •A:J", "K: MK011 •A:K").getOpinionPolls();
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", opinionPolls);
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia));
        assertEquals(createAreaIndexPageWithManyAndLargeOpinionPolls(),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, new Elections(), NOW)
                        .createAreaIndexPage(northMacedonia));
    }

    /**
     * Verifies that the index page for an area with upcoming elections is built correctly.
     */
    @Test
    public void areaIndexPageWithUpcomingElectionsShouldBeBuiltCorrectly() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Test");
        Map<String, OpinionPolls> opinionPollsMap = Map.of("mk", new OpinionPolls(Collections.EMPTY_SET));
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        ElectionLists electionLists = new ElectionLists();
        ElectionList nationalElections = new ElectionList();
        nationalElections.setDates(java.util.Map.of(1, "2023-03-05"));
        electionLists.setNational(nationalElections);
        ElectionList presidentialElections = new ElectionList();
        presidentialElections.setDates(java.util.Map.of(2, "≈2024-03"));
        electionLists.setPresidential(presidentialElections);
        ElectionList europeanElections = new ElectionList();
        europeanElections.setDates(java.util.Map.of(THREE, "≤2025-03"));
        electionLists.setEuropean(europeanElections);
        northMacedonia.setElections(electionLists);
        websiteConfiguration.setAreaConfigurations(Set.of(northMacedonia));
        Elections elections = ElectionsBuilder.extractAndValidateElections(websiteConfiguration, Map.empty(), NOW);
        assertEquals(createAreaIndexPageWithUpcomingElections(),
                new AreaIndexPagesBuilder(websiteConfiguration, opinionPollsMap, elections, NOW)
                        .createAreaIndexPage(northMacedonia));
    }

    /**
     * Creates an empty area index page.
     *
     * @param includeNavigationToArea True if the navigation to an area should be included.
     * @return An empty area index page.
     */
    private String createEmptyAreaIndexPage(final boolean includeNavigationToArea) {
        StringBuilder expected = new StringBuilder();
        addTopPart(expected, includeNavigationToArea);
        expected.append("      <p><span class=\"none\"> </span>.</p>\n");
        addMiddlePart(expected);
        expected.append("      <p><span class=\"none\"> </span>.</p>\n");
        addBottomPart(expected);
        return expected.toString();
    }

    /**
     * Creates an index page for an area with only one small opinion poll.
     *
     * @param pollingFirmsNotIncluded A string array with key/value pairs representing polling firm names and reasons
     *                                for why they're not included.
     * @return An index page for an area with only one small opinion poll.
     */
    private String createAreaIndexPageWithASmallOpinionPoll(final String... pollingFirmsNotIncluded) {
        StringBuilder expected = new StringBuilder();
        addTopPart(expected, true);
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
        if (pollingFirmsNotIncluded.length > 0) {
            if (pollingFirmsNotIncluded.length > 2) {
                expected.append("      <p><span class=\"polling-firms-not-included\"> </span>:</p>\n");
            } else {
                expected.append("      <p><span class=\"polling-firm-not-included\"> </span>:</p>\n");
            }
            expected.append("      <ul>\n");
            for (int i = 0; i < pollingFirmsNotIncluded.length / 2; i++) {
                expected.append("        <li><span>");
                expected.append(pollingFirmsNotIncluded[i * 2]);
                expected.append("</span>: <span class=\"polling-firm-not-included-reason-");
                expected.append(pollingFirmsNotIncluded[i * 2 + 1]);
                expected.append("\"> </span></li>\n");
            }
            expected.append("      </ul>\n");
        }
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
        addTopPart(expected, true);
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
        expected.append("            <td> – 2023-02-04</td>\n");
        expected.append("            <td>ACME</td>\n");
        expected.append("            <td class=\"result-value-td\">63%</td>\n");
        expected.append("            <td class=\"result-value-td\">23%</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">3%</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">—</td>\n");
        expected.append("            <td class=\"result-value-td\">(10%)</td>\n");
        expected.append("          </tr>\n");
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
        addTopPart(expected, true);
        expected.append("      <ul>\n");
        expected.append(
                "        <li><span class=\"around\"> </span> 2024-03: <span class=\"president\"> </span></li>\n");
        expected.append("        <li>2023-03-05: <span class=\"parliament\"> </span></li>\n");
        expected.append(
                "        <li><span class=\"no-later-than\"> </span> 2025-03: <span class=\"european-parliament\">"
                        + " </span></li>\n");
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
     * @param includeNavigationToArea True if the navigation to an area should be included.
     * @param expected                The StringBuilder instance to add the top part to.
     */
    private void addTopPart(final StringBuilder expected, final boolean includeNavigationToArea) {
        expected.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        expected.append("  <head>\n");
        expected.append("    <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"/>\n");
        expected.append("    <title>ASAPOP Website Test</title>\n");
        expected.append("    <link href=\"../_css/base.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("    <link href=\"../_css/skin.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\""
                + " type=\"application/javascript\"> </script>\n");
        expected.append(
                "    <script src=\"../_js/internationalization.js\" type=\"application/javascript\"> </script>\n");
        expected.append("    <script src=\"../_js/navigation.js\" type=\"application/javascript\"> </script>\n");
        expected.append("    <script src=\"../_js/sorting.js\" type=\"application/javascript\"> </script>\n");
        expected.append("    <script src=\"../_js/tooltip.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("  </head>\n");
        expected.append("  <body onload=\"initializeLanguage();\">\n");
        expected.append("    <header>\n");
        expected.append("      <div class=\"header-left\">\n");
        expected.append("        <a class=\"main-page\" href=\"../index.html\"> </a>\n");
        expected.append("      </div>\n");
        expected.append("      <div class=\"header-right\"><span class=\"go-to\"> </span>: <select"
                + " id=\"area-selector\" onchange=\"moveToArea(1);\">\n");
        expected.append("  <option> </option>\n");
        if (includeNavigationToArea) {
            expected.append("  <option class=\"_area_mk\" value=\"mk\"> </option>\n");
        }
        expected.append("</select> · <a class=\"electoral-calendar\" href=\"../calendar.html\"> </a> ·"
                + " <a class=\"csv-files\" href=\"../csv.html\"> </a> · <a class=\"statistics-page\""
                + " href=\"../statistics.html\"> </a> · <span class=\"language\"> </span>: <select"
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
        expected.append("      <h1 class=\"_area_mk\"> </h1>\n");
        expected.append("      <h2 class=\"upcoming-elections\"> </h2>\n");
    }

    /**
     * Creates an index page for an area with an opinion poll with results in number of seats.
     *
     * @return An index page for an area with an opinion poll with results in number of seats.
     */
    private String createAreaIndexPageWithAnOpinionPollWithResultsInNumberOfSeats() {
        StringBuilder expected = new StringBuilder();
        addTopPart(expected, true);
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
        expected.append("            <th class=\"electoral-lists-th\">E</th>\n");
        expected.append("            <th class=\"electoral-lists-th\">F</th>\n");
        expected.append("            <th class=\"electoral-lists-th\">G</th>\n");
        expected.append("            <th class=\"other\"> </th>\n");
        expected.append("          </tr>\n");
        expected.append("        </thead>\n");
        expected.append("        <tbody>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2021-07-27 – 2021-07-28</td>\n");
        expected.append("            <td>ACME</td>\n");
        expected.append("            <td class=\"result-value-td\">55</td>\n");
        expected.append("            <td class=\"result-value-td\">40</td>\n");
        expected.append("            <td class=\"result-value-td\">10</td>\n");
        expected.append("            <td class=\"result-value-td\">9</td>\n");
        expected.append("            <td class=\"result-value-td\">8</td>\n");
        expected.append("            <td class=\"result-value-td\">7</td>\n");
        expected.append("            <td class=\"result-value-td\">6</td>\n");
        expected.append("            <td class=\"result-value-td\">(9)</td>\n");
        expected.append("          </tr>\n");
        expected.append("        </tbody>\n");
        expected.append("      </table>\n");
        addBottomPart(expected);
        return expected.toString();
    }
}
