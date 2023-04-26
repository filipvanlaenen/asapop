package net.filipvanlaenen.asapop.website;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.model.Election;
import net.filipvanlaenen.asapop.model.ElectionType;
import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionList;
import net.filipvanlaenen.asapop.yaml.ElectionLists;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.tsvgj.A;
import net.filipvanlaenen.tsvgj.Image;
import net.filipvanlaenen.tsvgj.PreserveAspectRatioAlignValue;
import net.filipvanlaenen.tsvgj.PreserveAspectRatioMeetOrSliceValue;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.Div;
import net.filipvanlaenen.txhtmlj.H1;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.Section;
import net.filipvanlaenen.txhtmlj.Svg;

/**
 * Class building the index page.
 */
final class IndexPageBuilder extends PageBuilder {
    /**
     * The height of the SVG container.
     */
    private static final int SVG_CONTAINER_HEIGHT = 250;
    /**
     * The width of the SVG container.
     */
    private static final int SVG_CONTAINER_WIDTH = 500;
    /**
     * The elections.
     */
    private final Elections elections;
    /**
     * The list with GitHub website URLs, sorted by next election date.
     */
    private List<String> gitHubWebsiteUrlsByNextElectionDate;
    /**
     * Today's day.
     */
    private final LocalDate now;

    /**
     * Constructor taking the website configuration as its parameter.
     *
     * @param websiteConfiguration The website configuration.
     * @param elections            The elections.
     * @param now                  The today's day.
     */
    IndexPageBuilder(final WebsiteConfiguration websiteConfiguration, final Elections elections, final LocalDate now) {
        super(websiteConfiguration);
        this.elections = elections;
        this.now = now;
    }

    /**
     * Builds the content of the index page.
     *
     * @return The content of the index page
     */
    Html build() {
        Html html = new Html();
        html.addElement(createHead());
        Body body = new Body().onload("initializeLanguage();");
        html.addElement(body);
        body.addElement(createHeader(PageBuilder.HeaderLink.INDEX));
        Section section = new Section();
        body.addElement(section);
        section.addElement(new H1(" ").clazz("upcoming-elections"));
        Div twoSvgChartsContainer = new Div().clazz("two-svg-charts-container");
        section.addElement(twoSvgChartsContainer);
        twoSvgChartsContainer
                .addElement(createDivWithImage("svg-chart-container-left", getGitHubWebsiteUrlByNextElectionDate(0)));
        twoSvgChartsContainer
                .addElement(createDivWithImage("svg-chart-container-right", getGitHubWebsiteUrlByNextElectionDate(1)));
        body.addElement(createFooter());
        return html;
    }

    /**
     * Calculates the list of GitHub website URLs sorted by the next election date for the areas.
     */
    private void calculateGitHubWebsiteUrlsSortedByNextElectionDate() {
        List<Election> nextNationalElections = elections.getNextElections(now).stream()
                .filter(ne -> ne.electionType() == ElectionType.NATIONAL).collect(Collectors.toList());
        nextNationalElections.sort(new Comparator<Election>() {
            @Override
            public int compare(final Election e1, final Election e2) {
                int dateResult = e1.getNextElectionDate(now).compareTo(e2.getNextElectionDate(now));
                if (dateResult != 0) {
                    return dateResult;
                }
                return e1.areaCode().compareTo(e2.areaCode());
            }
        });
        gitHubWebsiteUrlsByNextElectionDate = new ArrayList<String>();
        for (Election nextElection : nextNationalElections) {
            for (AreaConfiguration areaConfiguration : getAreaConfigurations()) {
                String areaCode = areaConfiguration.getAreaCode();
                if (areaCode != null && areaCode.equals(nextElection.areaCode())) {
                    ElectionLists electionLists = areaConfiguration.getElections();
                    if (electionLists != null) {
                        ElectionList nationalElectionList = electionLists.getNational();
                        if (nationalElectionList != null) {
                            String gitHubWebsiteUrl = nationalElectionList.getGitHubWebsiteUrl();
                            if (gitHubWebsiteUrl != null) {
                                gitHubWebsiteUrlsByNextElectionDate.add(gitHubWebsiteUrl);
                            }
                        }
                    }
                }
            }
            if (gitHubWebsiteUrlsByNextElectionDate.size() > 1) {
                break;
            }
        }
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
            calculateGitHubWebsiteUrlsSortedByNextElectionDate();
        }
        return gitHubWebsiteUrlsByNextElectionDate.get(index);
    }
}
