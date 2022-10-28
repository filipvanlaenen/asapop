package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>ElectoralCalendarPageBuilder</code> class.
 */
public class ElectoralCalendarPageBuilderTest {
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
        ElectionConfiguration danishElection = new ElectionConfiguration();
        danishElection.setNextElectionDate("2022-11-01");
        danishElection.setType("Parliament");
        denmark.setElectionConfigurations(Set.of(danishElection));
        AreaConfiguration estonia = new AreaConfiguration();
        estonia.setAreaCode("ee");
        ElectionConfiguration estonianElectionParliament = new ElectionConfiguration();
        estonianElectionParliament.setNextElectionDate("2023-03-05");
        estonianElectionParliament.setType("Parliament");
        ElectionConfiguration estonianElectionPresident = new ElectionConfiguration();
        estonianElectionPresident.setNextElectionDate("≈2024-03");
        estonianElectionPresident.setType("President");
        estonia.setElectionConfigurations(Set.of(estonianElectionParliament, estonianElectionPresident));
        AreaConfiguration france = new AreaConfiguration();
        france.setAreaCode("fr");
        ElectionConfiguration frenchElectionParliament = new ElectionConfiguration();
        frenchElectionParliament.setNextElectionDate("2023-03-05");
        frenchElectionParliament.setType("Parliament");
        ElectionConfiguration frenchElectionPresident = new ElectionConfiguration();
        frenchElectionPresident.setNextElectionDate("2023-03-05");
        frenchElectionPresident.setType("President");
        ElectionConfiguration frenchElectionLocal = new ElectionConfiguration();
        frenchElectionLocal.setNextElectionDate("2023-03-05");
        frenchElectionLocal.setType("Local");
        france.setElectionConfigurations(
                Set.of(frenchElectionParliament, frenchElectionPresident, frenchElectionLocal));
        AreaConfiguration greenland = new AreaConfiguration();
        greenland.setAreaCode("gl");
        ElectionConfiguration greenlandElection = new ElectionConfiguration();
        greenlandElection.setNextElectionDate("≤2025-04-06");
        greenlandElection.setType("Parliament");
        greenland.setElectionConfigurations(Set.of(greenlandElection));
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
        expected.append("    <title>ASAPOP Website</title>\n");
        expected.append("    <style>header { display: block; width: 100%; }\n");
        expected.append(".header-left {\n");
        expected.append(
                "  display: inline-block; float: left; overflow: hidden; position: relative; text-align:" + " left;\n");
        expected.append("  width: 49%;\n");
        expected.append("}\n");
        expected.append(".header-right {\n");
        expected.append("  display: inline-block; float: right; overflow: hidden; position: relative;\n");
        expected.append("  text-align: right; width: 49%;\n");
        expected.append("}\n");
        expected.append(".privacy-statement { text-align: center; }\n");
        expected.append(".svg-chart-container-left {\n");
        expected.append("  display: inline-block; position: relative; width: 49%; vertical-align: middle;\n");
        expected.append("  overflow: hidden; float: left;\n");
        expected.append("}\n");
        expected.append(".svg-chart-container-right {\n");
        expected.append("  display: inline-block; position: relative; width: 49%; vertical-align: middle;\n");
        expected.append("  overflow: hidden; float: right;\n");
        expected.append("}\n");
        expected.append(".two-svg-charts-container { display: block; }\n");
        expected.append("@media screen and (max-width: 700px) {\n");
        expected.append("  .svg-chart-container-left { width: 100%; }\n");
        expected.append("  .svg-chart-container-right { float: none; width: 100%; }\n");
        expected.append("  .two-svg-charts-container { }\n");
        expected.append("}\n");
        expected.append("</style>\n");
        expected.append("    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\""
                + " type=\"application/javascript\"> </script>\n");
        expected.append(
                "    <script src=\"_js/internationalization.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("  </head>\n");
        expected.append("  <body onload=\"initializeLanguage();\">\n");
        expected.append("    <header>\n");
        expected.append("      <div class=\"header-left\">\n");
        expected.append("        <a class=\"main-page\" href=\"index.html\"> </a>\n");
        expected.append("      </div>\n");
        expected.append("      <div class=\"header-right\"><span class=\"csv-files\"> </span> · <span"
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
        expected.append("            <td class=\"_area_dk\"> </td>\n");
        expected.append("            <td class=\"parliament\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2023-03-05</td>\n");
        expected.append("            <td class=\"_area_ee\"> </td>\n");
        expected.append("            <td class=\"parliament\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2023-03-05</td>\n");
        expected.append("            <td class=\"_area_fr\"> </td>\n");
        expected.append("            <td class=\"local\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2023-03-05</td>\n");
        expected.append("            <td class=\"_area_fr\"> </td>\n");
        expected.append("            <td class=\"parliament\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>2023-03-05</td>\n");
        expected.append("            <td class=\"_area_fr\"> </td>\n");
        expected.append("            <td class=\"president\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td><span class=\"around\"> </span> 2024-03</td>\n");
        expected.append("            <td class=\"_area_ee\"> </td>\n");
        expected.append("            <td class=\"president\"> </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td><span class=\"no-later-than\"> </span> 2025-04-06</td>\n");
        expected.append("            <td class=\"_area_gl\"> </td>\n");
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
        assertEquals(expected.toString(),
                new ElectoralCalendarPageBuilder(createWebsiteConfiguration()).build().asString());
    }
}
