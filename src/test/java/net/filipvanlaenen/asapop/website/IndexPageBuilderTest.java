package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

/**
 * Unit tests on the <code>IndexPageBuilder</code> class.
 */
public class IndexPageBuilderTest {
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
        AreaConfiguration latvia = new AreaConfiguration();
        latvia.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/latvian_polls");
        latvia.setNextElectionDate("2022-10-01");
        AreaConfiguration bulgaria = new AreaConfiguration();
        bulgaria.setGitHubWebsiteUrl("https://filipvanlaenen.github.io/bulgarian_polls");
        bulgaria.setNextElectionDate("2022-10-02");
        AreaConfiguration northMacedonia = new AreaConfiguration();
        northMacedonia.setAreaCode("mk");
        websiteConfiguration.setAreaConfigurations(Set.of(sweden, latvia, bulgaria, northMacedonia));
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
        expected.append("        <span class=\"main-page\"> </span>\n");
        expected.append("      </div>\n");
        expected.append("      <div class=\"header-right\"><a class=\"csv-files\" href=\"csv.html\"> </a> · <span"
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
        assertEquals(expected.toString(), new IndexPageBuilder(createWebsiteConfiguration()).build().asString());
    }
}