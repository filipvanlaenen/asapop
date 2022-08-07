package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.tsvgj.PreserveAspectRatioAlignValue;
import net.filipvanlaenen.tsvgj.PreserveAspectRatioMeetOrSliceValue;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.Div;
import net.filipvanlaenen.txhtmlj.Head;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.Style;
import net.filipvanlaenen.txhtmlj.Svg;
import net.filipvanlaenen.txhtmlj.Title;

/**
 * Class building the website.
 */
public class WebsiteBuilder {
    /**
     * Builds the website.
     *
     * @return The website
     */
    public Website build() {
        Website website = new Website();
        website.put("index.html", buildIndexPageContent());
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
        Div twoSvgChartsContainer = new Div().clazz("two-svg-charts-container");
        Div svgChartContainerLeft = new Div().clazz("svg-chart-container-left");
        Svg svgLeft = new Svg();
        svgLeft.getSvg().viewBox(0, 0, 500, 250).preserveAspectRatio(PreserveAspectRatioAlignValue.X_MIN_Y_MIN,
                PreserveAspectRatioMeetOrSliceValue.MEET);
        svgChartContainerLeft.addElement(svgLeft);
        twoSvgChartsContainer.addElement(svgChartContainerLeft);
        Div svgChartContainerRight = new Div().clazz("svg-chart-container-right");
        Svg svgRight = new Svg();
        svgRight.getSvg().viewBox(0, 0, 500, 250).preserveAspectRatio(PreserveAspectRatioAlignValue.X_MIN_Y_MIN,
                PreserveAspectRatioMeetOrSliceValue.MEET);
        svgChartContainerRight.addElement(svgRight);
        twoSvgChartsContainer.addElement(svgChartContainerRight);
        body.addElement(twoSvgChartsContainer);
        Div privacyNote = new Div().clazz("privacy-note");
        privacyNote.addContent("Privacy note: this website is hosted on Google Cloud.");
        body.addElement(privacyNote);
        return html;
    }
}
