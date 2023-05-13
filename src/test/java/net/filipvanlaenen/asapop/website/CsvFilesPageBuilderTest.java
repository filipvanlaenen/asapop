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
        sweden.setAreaCode("se");
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        CsvConfiguration csvConfigurationNorthMacedonia = new CsvConfiguration();
        csvConfigurationNorthMacedonia.setElectoralListIds(List.of("A", "B"));
        northMacedonia.setCsvConfiguration(csvConfigurationNorthMacedonia);
        AreaConfiguration greenland = new AreaConfiguration();
        greenland.setAreaCode("gl");
        CsvConfiguration csvConfigurationGreenland = new CsvConfiguration();
        csvConfigurationGreenland.setElectoralListIds(List.of("A", "B"));
        greenland.setCsvConfiguration(csvConfigurationGreenland);
        AreaConfiguration serbia = new AreaConfiguration();
        serbia.setAreaCode("rs");
        CsvConfiguration csvConfigurationSerbia = new CsvConfiguration();
        csvConfigurationSerbia.setElectoralListIds(List.of("A", "B"));
        serbia.setCsvConfiguration(csvConfigurationSerbia);
        websiteConfiguration
                .setAreaConfigurations(Set.of(northMacedonia, greenland, serbia, sweden, new AreaConfiguration()));
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
        expected.append("        <a class=\"main-page\" href=\"index.html\"> </a>\n");
        expected.append("      </div>\n");
        expected.append("      <div class=\"header-right\"><span class=\"go-to\"> </span>: <select"
                + " id=\"area-selector\" onchange=\"moveToArea(0);\">\n");
        expected.append("  <option> </option>\n");
        expected.append("  <option class=\"_area_gl\" value=\"gl\"> </option>\n");
        expected.append("  <option class=\"_area_mk\" value=\"mk\"> </option>\n");
        expected.append("  <option class=\"_area_rs\" value=\"rs\"> </option>\n");
        expected.append("  <option class=\"_area_se\" value=\"se\"> </option>\n");
        expected.append("</select> · <a class=\"electoral-calendar\" href=\"calendar.html\"> </a> · <span"
                + " class=\"csv-files\"> </span> · <a class=\"statistics-page\" href=\"statistics.html\"> </a> ·"
                + " <span class=\"language\"> </span>: <select id=\"language-selector\""
                + " onchange=\"loadLanguage();\">\n");
        expected.append("  <option value=\"de\">Deutsch</option>\n");
        expected.append("  <option value=\"en\">English</option>\n");
        expected.append("  <option value=\"eo\">Esperanto</option>\n");
        expected.append("  <option value=\"fr\">français</option>\n");
        expected.append("  <option value=\"nl\">Nederlands</option>\n");
        expected.append("  <option value=\"no\">norsk</option>\n");
        expected.append("</select></div>\n");
        expected.append("    </header>\n");
        expected.append("    <section>\n");
        expected.append("      <h1 class=\"csv-files\"> </h1>\n");
        expected.append("      <table>\n");
        expected.append("        <thead>\n");
        expected.append("          <tr>\n");
        expected.append("            <th class=\"csv-file\"> </th>\n");
        expected.append("            <th class=\"country\"> </th>\n");
        expected.append("          </tr>\n");
        expected.append("        </thead>\n");
        expected.append("        <tbody>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>\n");
        expected.append("              <a href=\"_csv/gl.csv\">gl.csv</a>\n");
        expected.append("            </td>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_gl\" href=\"gl/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>\n");
        expected.append("              <a href=\"_csv/mk.csv\">mk.csv</a>\n");
        expected.append("            </td>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_mk\" href=\"mk/index.html\"> </a>\n");
        expected.append("            </td>\n");
        expected.append("          </tr>\n");
        expected.append("          <tr>\n");
        expected.append("            <td>\n");
        expected.append("              <a href=\"_csv/rs.csv\">rs.csv</a>\n");
        expected.append("            </td>\n");
        expected.append("            <td>\n");
        expected.append("              <a class=\"_area_rs\" href=\"rs/index.html\"> </a>\n");
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
