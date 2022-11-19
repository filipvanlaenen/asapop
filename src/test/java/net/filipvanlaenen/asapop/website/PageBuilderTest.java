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
        expected.append("  <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"/>\n");
        expected.append("  <title>ASAPOP Website</title>\n");
        expected.append("  <link href=\"_css/base.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("  <link href=\"_css/skin.css\" rel=\"stylesheet\" type=\"text/css\"/>\n");
        expected.append("  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\""
                + " type=\"application/javascript\"> </script>\n");
        expected.append(
                "  <script src=\"_js/internationalization.js\" type=\"application/javascript\">" + " </script>\n");
        expected.append("</head>");
        assertEquals(expected.toString(), new LocalPageBuilder().createHead().asString());
    }

    /**
     * Verifies that the header is built correctly when no link to the main page should be added.
     */
    @Test
    public void headerWithoutLinkToMainPageShouldBeBuiltCorrectly() {
        StringBuilder expected = new StringBuilder();
        expected.append("<header>\n");
        expected.append("  <div class=\"header-left\">\n");
        expected.append("    <span class=\"main-page\"> </span>\n");
        expected.append("  </div>\n");
        expected.append("  <div class=\"header-right\"><a class=\"electoral-calendar\" href=\"calendar.html\"> </a> ·"
                + " <a class=\"csv-files\" href=\"csv.html\"> </a> · <span class=\"language\"> </span>: <select"
                + " id=\"language-selector\" onchange=\"loadLanguage();\">\n");
        expected.append("  <option value=\"de\">Deutsch</option>\n");
        expected.append("  <option value=\"en\">English</option>\n");
        expected.append("  <option value=\"eo\">Esperanto</option>\n");
        expected.append("  <option value=\"fr\">français</option>\n");
        expected.append("  <option value=\"nl\">Nederlands</option>\n");
        expected.append("  <option value=\"no\">norsk</option>\n");
        expected.append("</select></div>\n");
        expected.append("</header>");
        assertEquals(expected.toString(), new LocalPageBuilder().createHeader(PageBuilder.HeaderLink.INDEX).asString());
    }

    /**
     * Verifies that the header is built correctly when no links should be added.
     */
    @Test
    public void headerWithoutLinksShouldBeBuiltCorrectly() {
        StringBuilder expected = new StringBuilder();
        expected.append("<header>\n");
        expected.append("  <div class=\"header-left\">\n");
        expected.append("    <a class=\"main-page\" href=\"index.html\"> </a>\n");
        expected.append("  </div>\n");
        expected.append("  <div class=\"header-right\"><a class=\"electoral-calendar\" href=\"calendar.html\"> </a> ·"
                + " <a class=\"csv-files\" href=\"csv.html\"> </a> · <span class=\"language\"> </span>:"
                + " <select id=\"language-selector\" onchange=\"loadLanguage();\">\n");
        expected.append("  <option value=\"de\">Deutsch</option>\n");
        expected.append("  <option value=\"en\">English</option>\n");
        expected.append("  <option value=\"eo\">Esperanto</option>\n");
        expected.append("  <option value=\"fr\">français</option>\n");
        expected.append("  <option value=\"nl\">Nederlands</option>\n");
        expected.append("  <option value=\"no\">norsk</option>\n");
        expected.append("</select></div>\n");
        expected.append("</header>");
        assertEquals(expected.toString(), new LocalPageBuilder().createHeader().asString());
    }

    /**
     * Verifies that the header for a page in a subdirectory is built correctly when no links should be added.
     */
    @Test
    public void headerInSubdirectoryWithoutLinksShouldBeBuiltCorrectly() {
        StringBuilder expected = new StringBuilder();
        expected.append("<header>\n");
        expected.append("  <div class=\"header-left\">\n");
        expected.append("    <a class=\"main-page\" href=\"../index.html\"> </a>\n");
        expected.append("  </div>\n");
        expected.append("  <div class=\"header-right\"><a class=\"electoral-calendar\" href=\"../calendar.html\"> </a>"
                + " · <a class=\"csv-files\" href=\"../csv.html\"> </a> · <span class=\"language\"> </span>:"
                + " <select id=\"language-selector\" onchange=\"loadLanguage();\">\n");
        expected.append("  <option value=\"de\">Deutsch</option>\n");
        expected.append("  <option value=\"en\">English</option>\n");
        expected.append("  <option value=\"eo\">Esperanto</option>\n");
        expected.append("  <option value=\"fr\">français</option>\n");
        expected.append("  <option value=\"nl\">Nederlands</option>\n");
        expected.append("  <option value=\"no\">norsk</option>\n");
        expected.append("</select></div>\n");
        expected.append("</header>");
        assertEquals(expected.toString(), new LocalPageBuilder().createHeader(1).asString());
    }
}
