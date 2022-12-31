package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>StatisticsPageBuilder</code> class.
 */
public class StatisticsPageBuilderTest {
    /**
     * The magic number 1,234.
     */
    private static final int LARGE_INTEGER_1234 = 1234;
    /**
     * The magic number 2,345.
     */
    private static final int LARGE_INTEGER_2345 = 2345;
    /**
     * The start of the year 2022.
     */
    private static final LocalDate START_OF_YEAR = LocalDate.of(2022, Month.JANUARY, 1);
    /**
     * A date to run the unit tests on.
     */
    private static final LocalDate DATE1 = LocalDate.parse("2021-12-28");
    /**
     * Another date to run the unit tests on.
     */
    private static final LocalDate DATE2 = LocalDate.parse("2022-12-29");

    /**
     * Creates an opinion polls map.
     *
     * @return An opinion polls map.
     */
    private Map<String, OpinionPolls> createOpinionPollsMap() {
        OpinionPoll poll1 = new OpinionPoll.Builder().addCommissioner("The Times").setPublicationDate(DATE1)
                .addWellformedResult("A", "55").build();
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().addWellformedResult("A", "56").build();
        poll1.addAlternativeResponseScenario(responseScenario1);
        OpinionPoll poll2 = new OpinionPoll.Builder().addCommissioner("The Post").setPublicationDate(DATE2)
                .addWellformedResult("A", "57").addWellformedResult("B", "56").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().addWellformedResult("A", "56").build();
        poll2.addAlternativeResponseScenario(responseScenario2);
        OpinionPoll poll3 = new OpinionPoll.Builder().addCommissioner("The Times").setPublicationDate(DATE2)
                .addWellformedResult("A", "55").build();
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
     * Verifies that the electoral calendar page is built correctly.
     */
    @Test
    public void electoralCalendarPageIsBuiltCorrectly() {
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
        expected.append("      <table class=\"statistics-table\">\n");
        expected.append("        <thead>\n");
        expected.append("          <tr>\n");
        expected.append("            <th class=\"country\"> </th>\n");
        expected.append("            <th class=\"number-of-opinion-polls-th\"><span class=\"number-of-opinion-polls\">"
                + " </span> (<span class=\"year-to-date\"> </span>)</th>\n");
        expected.append("            <th class=\"number-of-response-scenarios-th\"><span"
                + " class=\"number-of-response-scenarios\"> </span> (<span class=\"year-to-date\"> </span>)</th>\n");
        expected.append("            <th class=\"number-of-result-values-th\"><span"
                + " class=\"number-of-result-values\"> </span> (<span class=\"year-to-date\"> </span>)</th>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td class=\"total\"> </td>\n");
        expected.append("            <td class=\"statistics-total-td\">5 (3)</td>\n");
        expected.append("            <td class=\"statistics-total-td\">8 (4)</td>\n");
        expected.append("            <td class=\"statistics-total-td\">9 (5)</td>\n");
        expected.append("          </tr>\n");
        expected.append("        </thead>\n");
        expected.append("        <tbody>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_dk\" href=\"dk/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"statistics-value-td\">3 (2)</td>\n");
        expected.append("            <td class=\"statistics-value-td\">5 (3)</td>\n");
        expected.append("            <td class=\"statistics-value-td\">6 (4)</td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_ee\" href=\"ee/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"statistics-value-td\">2 (1)</td>\n");
        expected.append("            <td class=\"statistics-value-td\">3 (1)</td>\n");
        expected.append("            <td class=\"statistics-value-td\">3 (1)</td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_no\" href=\"no/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_se\" href=\"se/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("            <td class=\"statistics-value-td\">—</td>\n");
        expected.append("          </tr>\n");
        expected.append("        </tbody>\n");
        expected.append("      </table>\n");
        expected.append("    </section>\n");
        expected.append("    <footer>\n");
        expected.append("      <div class=\"privacy-statement\"> </div>\n");
        expected.append("    </footer>\n");
        expected.append("  </body>\n");
        expected.append("</html>");
        assertEquals(expected.toString(),
                new StatisticsPageBuilder(createWebsiteConfiguration(), createOpinionPollsMap(), START_OF_YEAR).build()
                        .asString());
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
}