package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.tsvgj.A;
import net.filipvanlaenen.tsvgj.Image;
import net.filipvanlaenen.tsvgj.PreserveAspectRatioAlignValue;
import net.filipvanlaenen.tsvgj.PreserveAspectRatioMeetOrSliceValue;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.Div;
import net.filipvanlaenen.txhtmlj.Footer;
import net.filipvanlaenen.txhtmlj.H1;
import net.filipvanlaenen.txhtmlj.Head;
import net.filipvanlaenen.txhtmlj.Header;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.JavaScriptMimeTypeValue;
import net.filipvanlaenen.txhtmlj.Option;
import net.filipvanlaenen.txhtmlj.Script;
import net.filipvanlaenen.txhtmlj.Section;
import net.filipvanlaenen.txhtmlj.Select;
import net.filipvanlaenen.txhtmlj.Span;
import net.filipvanlaenen.txhtmlj.Style;
import net.filipvanlaenen.txhtmlj.Svg;
import net.filipvanlaenen.txhtmlj.Title;

/**
 * Unit tests on the <code>WebsiteBuilder</code> class.
 */
public class WebsiteBuilderTest {
    /**
     * The magic number 250.
     */
    private static final int TWO_HUNDRED_FIFTY = 250;
    /**
     * The magic number 500.
     */
    private static final int FIVE_HUNDRED = 500;

    /**
     * Creates a website builder for testing purposes.
     *
     * @return A website builder for testing purposes.
     */
    private WebsiteBuilder createWebsiteBuilder() {
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
        websiteConfiguration.setAreaConfigurations(Set.of(sweden, latvia, bulgaria));
        return new WebsiteBuilder(websiteConfiguration);
    }

    /**
     * Verifies that the index page is built correctly.
     */
    @Test
    public void indexPageContentShouldBeBuiltCorrectly() {
        String expected = createIndexPageContent();
        assertEquals(expected, createWebsiteBuilder().buildIndexPageContent().asString());
    }

    /**
     * Verifies that the website is build correctly.
     */
    @Test
    public void websiteShouldBeBuiltCorrectly() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("index.html", createIndexPageContent());
        assertEquals(map, createWebsiteBuilder().build().asMap());
    }

    /**
     * Creates the content of the index page.
     *
     * @return The content of the index page.
     */
    private String createIndexPageContent() {
        Html html = new Html();
        Head head = new Head();
        html.addElement(head);
        head.addElement(new Title("ASAPOP Website"));
        StringBuffer style = new StringBuffer();
        style.append("header { text-align: right; }\n");
        style.append(".privacy-statement { text-align: center; }\n");
        style.append(".svg-chart-container-left {\n");
        style.append("  display:inline-block; position: relative; width: 49%; vertical-align: middle;\n");
        style.append("  overflow: hidden; float: left;\n");
        style.append("}\n");
        style.append(".svg-chart-container-right {\n");
        style.append("  display: inline-block; position: relative; width: 49%; vertical-align: middle;\n");
        style.append("  overflow: hidden; float: right;\n");
        style.append("}\n");
        style.append(".two-svg-charts-container { display: block; }\n");
        style.append("@media screen and (max-width: 700px) {\n");
        style.append("  .svg-chart-container-left { width: 100%; }\n");
        style.append("  .svg-chart-container-right { float: none; width: 100%; }\n");
        style.append("  .two-svg-charts-container { }\n");
        style.append("}\n");
        head.addElement(new Style(style.toString()));
        head.addElement(new Script(" ").type(JavaScriptMimeTypeValue.APPLICATION_JAVASCRIPT)
                .src("https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"));
        head.addElement(new Script(" ").type(JavaScriptMimeTypeValue.APPLICATION_JAVASCRIPT)
                .src("_js/internationalization.js"));
        Body body = new Body().onload("initializeLanguage();");
        html.addElement(body);
        Header header = new Header();
        header.addElement(new Span(" ").clazz("language"));
        Select languageSelector = new Select().id("language-selector").onchange("loadLanguage();");
        languageSelector.addElement(new Option("English").value("en"));
        languageSelector.addElement(new Option("Esperanto").value("eo"));
        languageSelector.addElement(new Option("français").value("fr"));
        languageSelector.addElement(new Option("Nederlands").value("nl"));
        languageSelector.addElement(new Option("norsk").value("no"));
        header.addElement(languageSelector);
        body.addElement(header);
        Section section = new Section();
        body.addElement(section);
        section.addElement(new H1("Upcoming Elections"));
        Div twoSvgChartsContainer = new Div().clazz("two-svg-charts-container");
        section.addElement(twoSvgChartsContainer);
        twoSvgChartsContainer.addElement(
                createDivWithImage("svg-chart-container-left", "https://filipvanlaenen.github.io/swedish_polls"));
        twoSvgChartsContainer.addElement(
                createDivWithImage("svg-chart-container-right", "https://filipvanlaenen.github.io/latvian_polls"));
        Footer footer = new Footer();
        body.addElement(footer);
        Div privacyNote = new Div().clazz("privacy-statement");
        footer.addElement(privacyNote);
        privacyNote.addContent("Privacy statement: this website is hosted on Google Cloud.");
        return html.asString();
    }

    /**
     * Creates a div element with the linked image in an svg element.
     *
     * @param clazz The class for the div element.
     * @param href  The hyperreference for the GitHub website.
     * @return A div element with the linked image in an svg element.
     */
    private Div createDivWithImage(final String clazz, final String href) {
        Div svgChartContainer = new Div().clazz(clazz);
        Svg svg = new Svg();
        svg.getSvg().viewBox(0, 0, FIVE_HUNDRED, TWO_HUNDRED_FIFTY).preserveAspectRatio(
                PreserveAspectRatioAlignValue.X_MIN_Y_MIN, PreserveAspectRatioMeetOrSliceValue.MEET);
        A a = new A().href(href);
        a.addElement(new Image().href(href + "/average.png").height(TWO_HUNDRED_FIFTY).width(FIVE_HUNDRED));
        svg.getSvg().addElement(a);
        svgChartContainer.addElement(svg);
        return svgChartContainer;
    }
}
