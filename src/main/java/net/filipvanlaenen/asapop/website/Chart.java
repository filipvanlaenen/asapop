package net.filipvanlaenen.asapop.website;

import java.util.Comparator;

import net.filipvanlaenen.tsvgj.DominantBaselineValue;
import net.filipvanlaenen.tsvgj.PreserveAspectRatioAlignValue;
import net.filipvanlaenen.tsvgj.PreserveAspectRatioMeetOrSliceValue;
import net.filipvanlaenen.tsvgj.Text;
import net.filipvanlaenen.tsvgj.TextAnchorValue;
import net.filipvanlaenen.txhtmlj.Div;
import net.filipvanlaenen.txhtmlj.Svg;

abstract class Chart {
    /**
     * The height of the SVG container.
     */
    protected static final int SVG_CONTAINER_HEIGHT = 250;
    /**
     * The width of the SVG container.
     */
    protected static final int SVG_CONTAINER_WIDTH = 500;
    /**
     * The X coordinate for the center of the chart.
     */
    protected static final double CENTER_X = ((double) SVG_CONTAINER_WIDTH) / 2;
    /**
     * The Y coordinate for the center of the chart.
     */
    protected static final double CENTER_Y = ((double) SVG_CONTAINER_HEIGHT) / 2;
    /**
     * The height of the canvas for the chart.
     */
    protected static final double CHART_CANVAS_HEIGHT = ((double) SVG_CONTAINER_HEIGHT) * 0.8D;
    /**
     * The height for the title of the chart.
     */
    protected static final double TITLE_HEIGHT = (SVG_CONTAINER_HEIGHT - CHART_CANVAS_HEIGHT) * 0.4D;

    /**
     * The HTML class for the chart's div element.
     */
    private final String divClass;
    /**
     * The HTML class for the title.
     */
    private final String titleClass;

    interface Entry {
        long value();
    }

    /**
     * Constructor taking the HTML class for the chart's div element and the HTML class for the title.
     *
     * @param divClass   The HTML class for the chart's div element.
     * @param titleClass The HTML class for the title.
     */
    Chart(final String divClass, final String titleClass) {
        this.divClass = divClass;
        this.titleClass = titleClass;
    }

    /**
     * Comparator to compare entries.
     */
    static class EntryComparator implements Comparator<Entry> {
        @Override
        public int compare(final Entry e1, final Entry e2) {
            if (e1.value() < e2.value()) {
                return 1;
            } else if (e1.value() > e2.value()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    protected abstract void addChartElements(net.filipvanlaenen.tsvgj.Svg svg);

    /**
     * Creates a div element with the chart.
     *
     * @return A div element with the chart.
     */
    Div getDiv() {
        Div div = new Div().clazz(divClass);
        Svg htmlSvg = new Svg();
        net.filipvanlaenen.tsvgj.Svg svg = htmlSvg.getSvg();
        svg.viewBox(0, 0, SVG_CONTAINER_WIDTH, SVG_CONTAINER_HEIGHT).preserveAspectRatio(
                PreserveAspectRatioAlignValue.X_MIN_Y_MIN, PreserveAspectRatioMeetOrSliceValue.MEET);
        Text title = new Text(" ").x(CENTER_X).y(TITLE_HEIGHT / 2).fontSize(TITLE_HEIGHT)
                .textAnchor(TextAnchorValue.MIDDLE).dominantBaseline(DominantBaselineValue.MIDDLE).clazz(titleClass);
        svg.addElement(title);
        addChartElements(svg);
        div.addElement(htmlSvg);
        return div;
    }
}
