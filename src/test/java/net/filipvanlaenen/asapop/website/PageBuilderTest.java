package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>PageBuilder</code> class.
 */
public class PageBuilderTest {
    /**
     * Local subclass of PageBuilder to run the unit tests on.
     */
    class LocalPageBuilder extends PageBuilder {
    }

    /**
     * Verifies that the footer is built correctly.
     */
    @Test
    public void footerShouldBeBuiltCorrectly() {
        StringBuilder expected = new StringBuilder();
        expected.append("<footer>\n");
        expected.append("  <div class=\"privacy-statement\"> </div>\n");
        expected.append("</footer>");
        assertEquals(expected.toString(), new LocalPageBuilder().createFooter().asString());
    }

    /**
     * Verifies that the head is built correctly.
     */
    @Test
    public void headShouldBeBuiltCorrectly() {
        StringBuilder expected = new StringBuilder();
        expected.append("<head>\n");
        expected.append("  <title>ASAPOP Website</title>\n");
        expected.append("  <style>header { text-align: right; }\n");
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
        expected.append("  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\""
                + " type=\"application/javascript\"> </script>\n");
        expected.append(
                "  <script src=\"_js/internationalization.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("</head>");
        assertEquals(expected.toString(), new LocalPageBuilder().createHead().asString());
    }
    /**
     * Verifies that the header is built correctly.
     */
    @Test
    public void headerShouldBeBuiltCorrectly() {
        StringBuilder expected = new StringBuilder();
        expected.append("<header><span class=\"language\"> </span>: <select id=\"language-selector\""
                + " onchange=\"loadLanguage();\">\n");
        expected.append("  <option value=\"en\">English</option>\n");
        expected.append("  <option value=\"eo\">Esperanto</option>\n");
        expected.append("  <option value=\"fr\">français</option>\n");
        expected.append("  <option value=\"nl\">Nederlands</option>\n");
        expected.append("  <option value=\"no\">norsk</option>\n");
        expected.append("</select></header>");
        assertEquals(expected.toString(), new LocalPageBuilder().createHeader().asString());
    }
}
