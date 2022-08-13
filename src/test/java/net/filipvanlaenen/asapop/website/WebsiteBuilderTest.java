package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

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
import net.filipvanlaenen.txhtmlj.Section;
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
     * Verifies that the index page is built correctly.
     */
    @Test
    public void indexPageContentShouldBeBuiltCorrectly() {
        String expected = createIndexPageContent();
        assertEquals(expected, new WebsiteBuilder().buildIndexPageContent().asString());
    }

    /**
     * Verifies that the website is build correctly.
     */
    @Test
    public void websiteShouldBeBuiltCorrectly() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("index.html", createIndexPageContent());
        assertEquals(map, new WebsiteBuilder().build().asMap());
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
        style.append(".privacy-note { text-align: center; }\n");
        style.append(".two-svg-charts-container { display: block; }\n");
        style.append(".svg-chart-container-left {\n");
        style.append("  display:inline-block; position: relative; width: 49%; vertical-align: middle;\n");
        style.append("  overflow: hidden; float: left;\n");
        style.append("}\n");
        style.append(".svg-chart-container-right {\n");
        style.append("  display: inline-block; position: relative; width: 49%; vertical-align: middle;\n");
        style.append("  overflow: hidden; float: right;\n");
        style.append("}\n");
        style.append("@media screen and (max-width: 700px) {\n");
        style.append("  .two-svg-charts-container { }\n");
        style.append("  .svg-chart-container-left { width: 100%; }\n");
        style.append("  .svg-chart-container-right { float: none; width: 100%; }\n");
        style.append("}\n");
        head.addElement(new Style(style.toString()));
        Body body = new Body();
        html.addElement(body);
        body.addElement(new Header());
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
        Div privacyNote = new Div().clazz("privacy-note");
        footer.addElement(privacyNote);
        privacyNote.addContent("Privacy note: this website is hosted on Google Cloud.");
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
