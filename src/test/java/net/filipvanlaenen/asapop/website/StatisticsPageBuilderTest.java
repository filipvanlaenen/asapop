package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPollTestBuilder;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResponseScenarioTestBuilder;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>StatisticsPageBuilder</code> class.
 */
public class StatisticsPageBuilderTest {
    /**
     * The magic number seven.
     */
    private static final int SEVEN = 7;
    /**
     * The magic number eight.
     */
    private static final int EIGHT = 8;
    /**
     * The magic number 0.2 (or slightly below).
     */
    private static final double UP_TO_0_2 = 0.2D;
    /**
     * The magic number 0.5 (or slightly below).
     */
    private static final double UP_TO_0_5 = 0.5D;
    /**
     * The magic number 0.8 (or slightly below).
     */
    private static final double UP_TO_0_8 = 0.7999D;
    /**
     * The magic number 0.95 (or slightly below).
     */
    private static final double UP_TO_0_95 = 0.95D;
    /**
     * A magic number slightly above 0.95.
     */
    private static final double ABOVE_0_95 = 0.9501D;
    /**
     * A magic number slightly above 1.
     */
    private static final double ABOVE_ONE = 1.0001D;
    /**
     * The magic number 1,234.
     */
    private static final int LARGE_INTEGER_1234 = 1234;
    /**
     * The magic number 2,345.
     */
    private static final int LARGE_INTEGER_2345 = 2345;
    /**
     * Today's date.
     */
    private static final LocalDate NOW = LocalDate.of(2022, Month.DECEMBER, 7);
    /**
     * The start of the year 2022.
     */
    private static final LocalDate START_OF_YEAR = NOW.withDayOfYear(1);
    /**
     * A date to run the unit tests on.
     */
    private static final LocalDate DATE1 = LocalDate.parse("2021-12-28");
    /**
     * Another date to run the unit tests on.
     */
    private static final LocalDate DATE2 = LocalDate.parse("2022-06-29");

    /**
     * Creates an opinion polls map.
     *
     * @return An opinion polls map.
     */
    private Map<String, OpinionPolls> createOpinionPollsMap() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55").addCommissioner("The Times")
                .setPublicationDate(DATE1).build();
        ResponseScenario responseScenario1 = new ResponseScenarioTestBuilder().addResult("A", "56").build();
        poll1.addAlternativeResponseScenario(responseScenario1);
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "57").addResult("B", "56")
                .addCommissioner("The Post").setPublicationDate(DATE2).build();
        ResponseScenario responseScenario2 = new ResponseScenarioTestBuilder().addResult("A", "56").build();
        poll2.addAlternativeResponseScenario(responseScenario2);
        OpinionPoll poll3 = new OpinionPollTestBuilder().addResult("A", "55").addCommissioner("The Times")
                .setPublicationDate(DATE2).build();
        return Map.of("dk", new OpinionPolls(Set.of(poll1, poll2, poll3)), "ee",
                new OpinionPolls(Set.of(poll1, poll3)));
    }

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
        AreaConfiguration estonia = new AreaConfiguration();
        estonia.setAreaCode("ee");
        AreaConfiguration norway = new AreaConfiguration();
        norway.setAreaCode("no");
        websiteConfiguration.setAreaConfigurations(Set.of(denmark, estonia, sweden, norway, new AreaConfiguration()));
        return websiteConfiguration;
    }

    /**
     * Creates the internationalization dictionary.
     *
     * @return The internationalization dictionary.
     */
    private Internationalization createInternationalization() {
        Internationalization internationalization = new Internationalization();
        for (String areaCode : new String[] {"dk", "ee", "no", "se"}) {
            String key = "_area_" + areaCode;
            Map<String, String> translations = new HashMap<String, String>();
            for (Language language : Language.values()) {
                translations.put(language.getId(), areaCode + "-" + language.getId());
            }
            internationalization.addTranslations(key, translations);
        }
        return internationalization;
    }

    /**
     * Adds the top part of a statistics page to a StringBuilder instance.
     *
     * @param expected The StringBuilder instance to add the top part to.
     */
    private void addTopPart(final StringBuilder expected) {
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
        expected.append("  <body onload=\"initializeLanguage(); sortTable('statistics-table', 2, 'area-name',"
                + " 'alphanumeric-internationalized')\">\n");
        expected.append("    <header>\n");
        expected.append("      <div class=\"header-left\">\n");
        expected.append("        <a class=\"main-page\" href=\"index.html\"> </a>\n");
        expected.append("      </div>\n");
        expected.append("      <div class=\"header-right\"><span class=\"go-to\"> </span>: <select"
                + " id=\"area-selector\" onchange=\"moveToArea(0);\">\n");
        expected.append("  <option> </option>\n");
        expected.append("  <option class=\"_area_dk\" value=\"dk\"> </option>\n");
        expected.append("  <option class=\"_area_ee\" value=\"ee\"> </option>\n");
        expected.append("  <option class=\"_area_no\" value=\"no\"> </option>\n");
        expected.append("  <option class=\"_area_se\" value=\"se\"> </option>\n");
        expected.append("</select> · <a class=\"electoral-calendar\" href=\"calendar.html\"> </a> · <a"
                + " class=\"csv-files\" href=\"csv.html\"> </a> · <span class=\"statistics-page\"> </span> · <span"
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
        expected.append("      <h1 class=\"statistics\"> </h1>\n");
    }

    /**
     * Verifies that the statistics page is built correctly.
     */
    @Test
    public void statisticsPageIsBuiltCorrectly() {
        StringBuilder expected = new StringBuilder();
        addTopPart(expected);
        expected.append("      <table class=\"statistics-table\" id=\"statistics-table\">\n");
        expected.append("        <thead>\n");
        expected.append("          <tr>\n");
        expected.append("            <th class=\"country\" onclick=\"sortTable('statistics-table', 2, 'area-name',"
                + " 'alphanumeric-internationalized')\"> </th>\n");
        expected.append("            <th class=\"number-of-opinion-polls-th\"><span class=\"number-of-opinion-polls\""
                + " onclick=\"sortTable('statistics-table', 2, 'number-of-opinion-polls', 'numeric')\"> </span>"
                + " (<span class=\"year-to-date\" onclick=\"sortTable('statistics-table', 2,"
                + " 'number-of-opinion-polls-ytd', 'numeric')\"> </span>)</th>\n");
        expected.append("            <th class=\"number-of-response-scenarios-th\"><span"
                + " class=\"number-of-response-scenarios\" onclick=\"sortTable('statistics-table', 2,"
                + " 'number-of-response-scenarios', 'numeric')\"> </span> (<span class=\"year-to-date\""
                + " onclick=\"sortTable('statistics-table', 2, 'number-of-response-scenarios-ytd', 'numeric')\">"
                + " </span>)</th>\n");
        expected.append("            <th class=\"number-of-result-values-th\"><span"
                + " class=\"number-of-result-values\" onclick=\"sortTable('statistics-table', 2,"
                + " 'number-of-result-values', 'numeric')\"> </span> (<span class=\"year-to-date\""
                + " onclick=\"sortTable('statistics-table', 2, 'number-of-result-values-ytd', 'numeric')\">"
                + " </span>)</th>\n");
        expected.append("            <th class=\"most-recent-date-th\">\n");
        expected.append("              <span class=\"most-recent-date\" onclick=\"sortTable('statistics-table', 2,"
                + " 'most-recent-date', 'alphanumeric')\"> </span>\n");
        expected.append("              <sup>\n");
        expected.append("                <a href=\"#footnote-1\">1</a>\n");
        expected.append("              </sup>\n");
        expected.append("            </th>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td class=\"total\"> </td>\n");
        expected.append("            <td class=\"statistics-total-td\">5 (3)</td>\n");
        expected.append("            <td class=\"statistics-total-td\">8 (4)</td>\n");
        expected.append("            <td class=\"statistics-total-td\">9 (5)</td>\n");
        expected.append("            <td class=\"statistics-total-td\">2022-06-29</td>\n");
        expected.append("          </tr>\n");
        expected.append("        </thead>\n");
        expected.append("        <tbody>\n");
        expected.append(
                "          <tr data-area-name-de=\"dk-de\" data-area-name-en=\"dk-en\" data-area-name-eo=\"dk-eo\""
                        + " data-area-name-fr=\"dk-fr\" data-area-name-nl=\"dk-nl\" data-area-name-no=\"dk-no\""
                        + " data-most-recent-date=\"4-2022-06-29\" data-number-of-opinion-polls=\"3\""
                        + " data-number-of-opinion-polls-ytd=\"2\" data-number-of-response-scenarios=\"5\""
                        + " data-number-of-response-scenarios-ytd=\"3\" data-number-of-result-values=\"6\""
                        + " data-number-of-result-values-ytd=\"4\">\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_dk\" href=\"dk/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"statistics-value-td\">3 (2)</td>\n");
        expected.append("            <td class=\"statistics-value-td\">5 (3)</td>\n");
        expected.append("            <td class=\"statistics-value-td\">6 (4)</td>\n");
        expected.append("            <td class=\"statistics-value-td\"><span"
                + " class=\"probably-up-to-date-color\">●</span> 2022-06-29</td>\n");
        expected.append("          </tr>\n");
        expected.append(
                "          <tr data-area-name-de=\"ee-de\" data-area-name-en=\"ee-en\" data-area-name-eo=\"ee-eo\""
                        + " data-area-name-fr=\"ee-fr\" data-area-name-nl=\"ee-nl\" data-area-name-no=\"ee-no\""
                        + " data-most-recent-date=\"4-2022-06-29\" data-number-of-opinion-polls=\"2\""
                        + " data-number-of-opinion-polls-ytd=\"1\" data-number-of-response-scenarios=\"3\""
                        + " data-number-of-response-scenarios-ytd=\"1\" data-number-of-result-values=\"3\""
                        + " data-number-of-result-values-ytd=\"1\">\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_ee\" href=\"ee/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"statistics-value-td\">2 (1)</td>\n");
        expected.append("            <td class=\"statistics-value-td\">3 (1)</td>\n");
        expected.append("            <td class=\"statistics-value-td\">3 (1)</td>\n");
        expected.append("            <td class=\"statistics-value-td\"><span"
                + " class=\"probably-up-to-date-color\">●</span> 2022-06-29</td>\n");
        expected.append("          </tr>\n");
        expected.append(
                "          <tr data-area-name-de=\"no-de\" data-area-name-en=\"no-en\" data-area-name-eo=\"no-eo\""
                        + " data-area-name-fr=\"no-fr\" data-area-name-nl=\"no-nl\" data-area-name-no=\"no-no\""
                        + " data-most-recent-date=\"-1\" data-number-of-opinion-polls=\"-1\""
                        + " data-number-of-opinion-polls-ytd=\"-1\" data-number-of-response-scenarios=\"-1\""
                        + " data-number-of-response-scenarios-ytd=\"-1\" data-number-of-result-values=\"-1\""
                        + " data-number-of-result-values-ytd=\"-1\">\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_no\" href=\"no/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("          </tr>\n");
        expected.append(
                "          <tr data-area-name-de=\"se-de\" data-area-name-en=\"se-en\" data-area-name-eo=\"se-eo\""
                        + " data-area-name-fr=\"se-fr\" data-area-name-nl=\"se-nl\" data-area-name-no=\"se-no\""
                        + " data-most-recent-date=\"-1\" data-number-of-opinion-polls=\"-1\""
                        + " data-number-of-opinion-polls-ytd=\"-1\" data-number-of-response-scenarios=\"-1\""
                        + " data-number-of-response-scenarios-ytd=\"-1\" data-number-of-result-values=\"-1\""
                        + " data-number-of-result-values-ytd=\"-1\">\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_se\" href=\"se/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("          </tr>\n");
        expected.append("        </tbody>\n");
        expected.append("      </table>\n");
        expected.append("      <p id=\"footnote-1\"><sup>1</sup> <span class=\"qualification-of-currency\"> </span>:"
                + " <span class=\"up-to-date-color\">■</span> <span class=\"up-to-date\"> </span> (P ≥ 80 %), <span"
                + " class=\"probably-up-to-date-color\">●</span> <span class=\"probably-up-to-date\"> </span> (80 %"
                + " &gt; P ≥ 50 %), <span class=\"possibly-out-of-date-color\">●</span> <span"
                + " class=\"possibly-out-of-date\"> </span> (50 % &gt; P ≥ 20 %), <span"
                + " class=\"probably-out-of-date-color\">▲</span> <span class=\"probably-out-of-date\"> </span> (20 %"
                + " &gt; P ≥ 5 %), <span class=\"out-of-date-color\">▲</span> <span class=\"out-of-date\"> </span> (5"
                + " % &gt; P).</p>\n");
        expected.append("      <div class=\"two-svg-charts-container\">\n");
        expected.append("        <div class=\"svg-chart-container-right\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("            <text class=\"currency\" dominant-baseline=\"middle\" font-size=\"20\""
                + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append("            <circle class=\"probably-up-to-date-color\" cx=\"250\" cy=\"125\""
                + " onmousemove=\"showPieChartTooltip(evt, 'probably-up-to-date', '2', '2', '100');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" r=\"100\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, 'probably-up-to-date', '2', '2', '100');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"250\""
                + " y=\"125\">●</text>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("        <div class=\"svg-chart-container-left\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("            <text class=\"currency\" dominant-baseline=\"middle\" font-size=\"20\""
                + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append(
                "            <path class=\"probably-up-to-date-color\" d=\"M 250 125 L 250 225 A 100 100 0 0 0 250 25"
                        + " Z\" onmousemove=\"showPieChartTooltip(evt, 'probably-up-to-date', '2', '4', '50');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, 'probably-up-to-date', '2', '4', '50');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"330\""
                + " y=\"125\">●</text>\n");
        expected.append(
                "            <path class=\"no-opinion-polls\" d=\"M 250 125 L 250 25 A 100 100 0 0 0 250 225 Z\""
                        + " onmousemove=\"showPieChartTooltip(evt, 'no-opinion-polls', '2', '4', '50');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, 'no-opinion-polls', '2', '4', '50');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"170\""
                + " y=\"125\">–</text>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("      </div>\n");
        expected.append("      <div class=\"two-svg-charts-container\">\n");
        expected.append("        <div class=\"svg-chart-container-left\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append(
                "            <text class=\"number-of-opinion-polls\" dominant-baseline=\"middle\" font-size=\"20\""
                        + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append(
                "            <path class=\"pie-chart-1\" d=\"M 250 125 L 191.221475 205.901699 A 100 100 0 1 0 250 25"
                        + " Z\" onmousemove=\"showPieChartTooltip(evt, '_area_dk', '3', '5', '60');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_dk', '3', '5', '60');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"326.084521\""
                + " y=\"149.72136\">DK</text>\n");
        expected.append(
                "            <path class=\"pie-chart-2\" d=\"M 250 125 L 250 25 A 100 100 0 0 0 191.221475 205.901699"
                        + " Z\" onmousemove=\"showPieChartTooltip(evt, '_area_ee', '2', '5', '40');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_ee', '2', '5', '40');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"173.915479\""
                + " y=\"100.27864\">EE</text>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("        <div class=\"svg-chart-container-right\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append(
                "            <text class=\"number-of-opinion-polls-ytd\" dominant-baseline=\"middle\" font-size=\"20\""
                        + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append(
                "            <path class=\"pie-chart-1\" d=\"M 250 125 L 163.39746 175 A 100 100 0 1 0 250 25 Z\""
                        + " onmousemove=\"showPieChartTooltip(evt, '_area_dk', '2', '3', '67');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_dk', '2', '3', '67');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"319.282032\""
                + " y=\"165\">DK</text>\n");
        expected.append(
                "            <path class=\"pie-chart-2\" d=\"M 250 125 L 250 25 A 100 100 0 0 0 163.39746 175 Z\""
                        + " onmousemove=\"showPieChartTooltip(evt, '_area_ee', '1', '3', '33');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_ee', '1', '3', '33');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"180.717968\""
                + " y=\"85\">EE</text>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("      </div>\n");
        expected.append("      <div class=\"two-svg-charts-container\">\n");
        expected.append("        <div class=\"svg-chart-container-left\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("            <text class=\"number-of-response-scenarios\" dominant-baseline=\"middle\""
                + " font-size=\"20\" text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append(
                "            <path class=\"pie-chart-1\" d=\"M 250 125 L 179.289322 195.710678 A 100 100 0 1 0 250 25"
                        + " Z\" onmousemove=\"showPieChartTooltip(evt, '_area_dk', '5', '8', '63');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_dk', '5', '8', '63');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"323.910363\""
                + " y=\"155.614675\">DK</text>\n");
        expected.append(
                "            <path class=\"pie-chart-2\" d=\"M 250 125 L 250 25 A 100 100 0 0 0 179.289322 195.710678"
                        + " Z\" onmousemove=\"showPieChartTooltip(evt, '_area_ee', '3', '8', '38');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_ee', '3', '8', '38');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"176.089637\""
                + " y=\"94.385325\">EE</text>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("        <div class=\"svg-chart-container-right\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("            <text class=\"number-of-response-scenarios-ytd\" dominant-baseline=\"middle\""
                + " font-size=\"20\" text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append("            <path class=\"pie-chart-1\" d=\"M 250 125 L 150 125 A 100 100 0 1 0 250 25 Z\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_dk', '3', '4', '75');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_dk', '3', '4', '75');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"306.568542\""
                + " y=\"181.568542\">DK</text>\n");
        expected.append("            <path class=\"pie-chart-2\" d=\"M 250 125 L 250 25 A 100 100 0 0 0 150 125 Z\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_ee', '1', '4', '25');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_ee', '1', '4', '25');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"193.431458\""
                + " y=\"68.431458\">EE</text>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("      </div>\n");
        expected.append("      <div class=\"two-svg-charts-container\">\n");
        expected.append("        <div class=\"svg-chart-container-left\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append(
                "            <text class=\"number-of-result-values\" dominant-baseline=\"middle\" font-size=\"20\""
                        + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append(
                "            <path class=\"pie-chart-1\" d=\"M 250 125 L 163.39746 175 A 100 100 0 1 0 250 25 Z\""
                        + " onmousemove=\"showPieChartTooltip(evt, '_area_dk', '6', '9', '67');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_dk', '6', '9', '67');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"319.282032\""
                + " y=\"165\">DK</text>\n");
        expected.append(
                "            <path class=\"pie-chart-2\" d=\"M 250 125 L 250 25 A 100 100 0 0 0 163.39746 175 Z\""
                        + " onmousemove=\"showPieChartTooltip(evt, '_area_ee', '3', '9', '33');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_ee', '3', '9', '33');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"180.717968\""
                + " y=\"85\">EE</text>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("        <div class=\"svg-chart-container-right\">\n");
        expected.append("          <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("            <text class=\"number-of-result-values-ytd\" dominant-baseline=\"middle\""
                + " font-size=\"20\" text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append(
                "            <path class=\"pie-chart-1\" d=\"M 250 125 L 154.894348 94.098301 A 100 100 0 1 0 250 25"
                        + " Z\" onmousemove=\"showPieChartTooltip(evt, '_area_dk', '4', '5', '80');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_dk', '4', '5', '80');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"297.02282\""
                + " y=\"189.72136\">DK</text>\n");
        expected.append(
                "            <path class=\"pie-chart-2\" d=\"M 250 125 L 250 25 A 100 100 0 0 0 154.894348 94.098301"
                        + " Z\" onmousemove=\"showPieChartTooltip(evt, '_area_ee', '1', '5', '20');\""
                        + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("            <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, '_area_ee', '1', '5', '20');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"202.97718\""
                + " y=\"60.27864\">EE</text>\n");
        expected.append("          </svg>\n");
        expected.append("        </div>\n");
        expected.append("      </div>\n");
        expected.append("    </section>\n");
        expected.append("    <footer>\n");
        expected.append("      <div class=\"privacy-statement\"> </div>\n");
        expected.append("    </footer>\n");
        expected.append(
                "    <div class=\"tooltip\" id=\"pieChartTooltip\" style=\"position: absolute; display: none;\">"
                        + " <span id=\"pieChartTooltipLabel\"> </span><br/><span id=\"pieChartTooltipNumerator\">"
                        + " </span>/<span id=\"pieChartTooltipDenominator\"> </span> (<span"
                        + " id=\"pieChartTooltipPercentage\"> </span>%)</div>\n");
        expected.append("  </body>\n");
        expected.append("</html>");
        assertEquals(expected.toString(), new StatisticsPageBuilder(createWebsiteConfiguration(),
                createInternationalization(), createOpinionPollsMap(), NOW, START_OF_YEAR).build().asString());
    }

    /**
     * Verifies that TD element is produced correctly for large numbers.
     */
    @Test
    public void shouldProduceTdElementForLargeNumbersWithThousandsSeparators() {
        assertEquals(
                "<td>2<span class=\"thousands-separator\"> </span>345"
                        + " (1<span class=\"thousands-separator\"> </span>234)</td>",
                StatisticsPageBuilder.createNumberAndYearToDateTd(LARGE_INTEGER_2345, LARGE_INTEGER_1234).asString());
    }

    /**
     * Verifies that if the most recent opinion poll was seven days ago, the currency qualification is up-to-date.
     */
    @Test
    public void mostRecentOpinionPollSevenDaysAgoShouldBeUpToDate() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.UP_TO_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(ABOVE_ONE, SEVEN));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * more than one, the currency qualification is out-of-date.
     */
    @Test
    public void moreThanOneOpinionPollPerDayShouldBeOutOfDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.OUT_OF_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(ABOVE_ONE, EIGHT));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * 0.2, the currency qualification is up-to-date.
     */
    @Test
    public void averageUpTo02OpinionPollsPerDayShouldBeUpToDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.UP_TO_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(UP_TO_0_2, EIGHT));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * 0.5, the currency qualification is probably up-to-date.
     */
    @Test
    public void averageUpYo05OpinionPollsPerDayShouldBeProbablyUpToDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.PROBABLY_UP_TO_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(UP_TO_0_5, EIGHT));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * 0.8, the currency qualification is possibly out-of-date.
     */
    @Test
    public void averageUpTo08OpinionPollsPerDayShouldBePossiblyOutOfToDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.POSSIBLY_OUT_OF_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(UP_TO_0_8, EIGHT));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * 0.95, the currency qualification is probably out-of-date.
     */
    @Test
    public void averageUpTo095OpinionPollsPerDayShouldBeProbablyOutOfToDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.PROBABLY_OUT_OF_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(UP_TO_0_95, EIGHT));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * 0.96, the currency qualification is out-of-date.
     */
    @Test
    public void averageAbove095OpinionPollsPerDayShouldBeOutOfToDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.OUT_OF_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(ABOVE_0_95, EIGHT));
    }
}
