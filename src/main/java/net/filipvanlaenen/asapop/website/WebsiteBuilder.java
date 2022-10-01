package net.filipvanlaenen.asapop.website;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.Terms;
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
 * Class building the website.
 */
public class WebsiteBuilder {
    /**
     * The height of the SVG container.
     */
    private static final int SVG_CONTAINER_HEIGHT = 250;
    /**
     * The width of the SVG container.
     */
    private static final int SVG_CONTAINER_WIDTH = 500;
    /**
     * The list with GitHub website URLs, sorted by next election date.
     */
    private List<String> gitHubWebsiteUrlsByNextElectionDate;
    private final Map<String, OpinionPolls> opinionPollsMap;
    private final Terms terms;
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;

    /**
     * Constructor taking a website configuration as its parameter.
     *
     * @param websiteConfiguration The website configuration.
     */
    public WebsiteBuilder(final WebsiteConfiguration websiteConfiguration, final Terms terms,
            final Map<String, OpinionPolls> opinionPollsMap) {
        this.websiteConfiguration = websiteConfiguration;
        this.terms = terms;
        this.opinionPollsMap = opinionPollsMap;
    }

    /**
     * Builds the website.
     *
     * @return The website
     */
    public Website build() {
        Website website = new Website();
        website.put("index.html", buildIndexPageContent());
        CsvFilesBuilder csvFilesBuilder = new CsvFilesBuilder(opinionPollsMap);
        website.putAll(csvFilesBuilder.build());
        JavaScriptsBuilder javaScriptsBuilder = new JavaScriptsBuilder(terms);
        website.putAll(javaScriptsBuilder.build());
        return website;
    }

    /**
     * Builds the content of the index page.
     *
     * @return The content of the index page
     */
    Html buildIndexPageContent() {
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
        header.addContent(": ");
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
        section.addElement(new H1(" ").clazz("upcoming-elections"));
        Div twoSvgChartsContainer = new Div().clazz("two-svg-charts-container");
        section.addElement(twoSvgChartsContainer);
        twoSvgChartsContainer
                .addElement(createDivWithImage("svg-chart-container-left", getGitHubWebsiteUrlByNextElectionDate(0)));
        twoSvgChartsContainer
                .addElement(createDivWithImage("svg-chart-container-right", getGitHubWebsiteUrlByNextElectionDate(1)));
        Footer footer = new Footer();
        body.addElement(footer);
        footer.addElement(new Div(" ").clazz("privacy-statement"));
        return html;
    }

    /**
     * Calculates the list of GitHub website URLs sorted by the next election date for the areas.
     */
    private void calculateGitHubWebsiteUrlsByNextElectionDate() {
        List<AreaConfiguration> areaConfigurations =
                new ArrayList<AreaConfiguration>(websiteConfiguration.getAreaConfigurations());
        areaConfigurations.sort(Comparator.comparing(AreaConfiguration::getNextElectionDate));
        gitHubWebsiteUrlsByNextElectionDate = areaConfigurations.stream()
                .map(areaConfigutation -> areaConfigutation.getGitHubWebsiteUrl()).collect(Collectors.toList());
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
        svg.getSvg().viewBox(0, 0, SVG_CONTAINER_WIDTH, SVG_CONTAINER_HEIGHT).preserveAspectRatio(
                PreserveAspectRatioAlignValue.X_MIN_Y_MIN, PreserveAspectRatioMeetOrSliceValue.MEET);
        A a = new A().href(href);
        a.addElement(new Image().href(href + "/average.png").height(SVG_CONTAINER_HEIGHT).width(SVG_CONTAINER_WIDTH));
        svg.getSvg().addElement(a);
        svgChartContainer.addElement(svg);
        return svgChartContainer;
    }

    /**
     * Returns the GitHub website URL at a given index, with the websites being sorted by the next election date for the
     * area.
     *
     * @param index The index for which the GitHub website URL should be returned.
     * @return The GitHub website URL at a given index, with the websites being sorted by the next election date for the
     *         area.
     */
    private String getGitHubWebsiteUrlByNextElectionDate(final int index) {
        if (gitHubWebsiteUrlsByNextElectionDate == null) {
            calculateGitHubWebsiteUrlsByNextElectionDate();
        }
        return gitHubWebsiteUrlsByNextElectionDate.get(index);
    }
}
