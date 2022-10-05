package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.CsvConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>CsvFilesPageBuilder</code> class.
 */
public class CsvFilesPageBuilderTest {
    /**
     * Creates a website configuration.
     *
     * @return A website configuration.
     */
    private WebsiteConfiguration createWebsiteConfiguration() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        AreaConfiguration sweden = new AreaConfiguration();
        sweden.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/swedish_polls");
        sweden.setNextElectionDate("2022-09-11");
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        CsvConfiguration csvConfiguration = new CsvConfiguration();
        csvConfiguration.setElectoralListKeys(List.of("A", "B"));
        northMacedonia.setCsvConfiguration(csvConfiguration);
        websiteConfiguration.setAreaConfigurations(Set.of(sweden, northMacedonia));
        return websiteConfiguration;
    }

    /**
     * Verifies that the CSV files page is built correctly.
     */
    @Test
    public void csvFilesPageIsBuiltCorrectly() {
        StringBuilder expected = new StringBuilder();
        expected.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        expected.append("  <head>\n");
        expected.append("    <title>ASAPOP Website</title>\n");
        expected.append("    <style>header { text-align: right; }\n");
        expected.append(".privacy-statement { text-align: center; }\n");
        expected.append(".svg-chart-container-left {\n");
        expected.append("  display:inline-block; position: relative; width: 49%; vertical-align: middle;\n");
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
        expected.append("    <header><span class=\"language\"> </span>: <select id=\"language-selector\""
                + " onchange=\"loadLanguage();\">\n");
        expected.append("  <option value=\"en\">English</option>\n");
        expected.append("  <option value=\"eo\">Esperanto</option>\n");
        expected.append("  <option value=\"fr\">français</option>\n");
        expected.append("  <option value=\"nl\">Nederlands</option>\n");
        expected.append("  <option value=\"no\">norsk</option>\n");
        expected.append("</select></header>\n");
        expected.append("    <section>\n");
        expected.append("      <h1 class=\"csv-files\"> </h1>\n");
        expected.append("      <table>\n");
        expected.append("        <thead>\n");
        expected.append("          <tr>\n");
        expected.append("            <th class=\"country\"> </th>\n");
        expected.append("            <th class=\"csv-file\"> </th>\n");
        expected.append("          </tr>\n");
        expected.append("        </thead>\n");
        expected.append("        <tbody>\n");
        expected.append("          <tr>\n");
        expected.append("            <td class=\"_area_mk\"> </td>\n");
        expected.append("            <td>\n");
        expected.append("              <a href=\"_csv/mk.csv\">mk.csv</a>\n");
        expected.append("            </td>\n");
        expected.append("          </tr>\n");
        expected.append("        </tbody>\n");
        expected.append("      </table>\n");
        expected.append("    </section>\n");
        expected.append("    <footer>\n");
        expected.append("      <div class=\"privacy-statement\"> </div>\n");
        expected.append("    </footer>\n");
        expected.append("  </body>\n");
        expected.append("</html>");
        assertEquals(expected.toString(), new CsvFilesPageBuilder(createWebsiteConfiguration()).build().asString());
    }
}
