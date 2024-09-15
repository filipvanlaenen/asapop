package net.filipvanlaenen.asapop.website;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.sortedtree.SortedTreeCollection;
import net.filipvanlaenen.tsvgj.Circle;
import net.filipvanlaenen.tsvgj.DominantBaselineValue;
import net.filipvanlaenen.tsvgj.Path;
import net.filipvanlaenen.tsvgj.Path.LargeArcFlagValues;
import net.filipvanlaenen.tsvgj.Path.SweepFlagValues;
import net.filipvanlaenen.tsvgj.PreserveAspectRatioAlignValue;
import net.filipvanlaenen.tsvgj.PreserveAspectRatioMeetOrSliceValue;
import net.filipvanlaenen.tsvgj.Text;
import net.filipvanlaenen.tsvgj.TextAnchorValue;
import net.filipvanlaenen.txhtmlj.BR;
import net.filipvanlaenen.txhtmlj.Div;
import net.filipvanlaenen.txhtmlj.FlowContent;
import net.filipvanlaenen.txhtmlj.Span;
import net.filipvanlaenen.txhtmlj.Svg;

/**
 * Class building pie charts in SVG.
 */
class PieChart {
    /**
     * Record type to contain entries for a pie chart.
     *
     * @param labelClass The class for this entry's label.
     * @param symbol     The symbol for this entry.
     * @param value      The value for this entry.
     * @param sliceClass The class for this entry's slice in the pie chart.
     */
    record Entry(String labelClass, String symbol, long value, String sliceClass) {
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

    /**
     * The height of the SVG container.
     */
    private static final int SVG_CONTAINER_HEIGHT = 250;
    /**
     * The width of the SVG container.
     */
    private static final int SVG_CONTAINER_WIDTH = 500;
    /**
     * The X coordinate for the center of the pie chart.
     */
    private static final double CENTER_X = ((double) SVG_CONTAINER_WIDTH) / 2;
    /**
     * The Y coordinate for the center of the pie chart.
     */
    private static final double CENTER_Y = ((double) SVG_CONTAINER_HEIGHT) / 2;
    /**
     * The radius of the pie chart.
     */
    private static final double RADIUS = ((double) SVG_CONTAINER_HEIGHT) * 0.4D;
    /**
     * The radius for the placement of the symbols.
     */
    private static final double SYMBOL_RADIUS = RADIUS * 0.8D;
    /**
     * The height for the title of the pie chart.
     */
    private static final double TITLE_HEIGHT = (CENTER_Y - RADIUS) * 0.8D;
    /**
     * The height for the entry symbols on the pie chart.
     */
    private static final double SYMBOL_HEIGHT = TITLE_HEIGHT * 0.6D;
    /**
     * The ID for the HTML element containing the pie chart tooltip.
     */
    private static final String TOOLTIP_ID = "pieChartTooltip";
    /**
     * The ID for the HTML element containing the pie chart tooltip label.
     */
    private static final String TOOLTIP_LABEL_ID = "pieChartTooltipLabel";
    /**
     * The ID for the HTML element containing the tooltip for an entry's numerator.
     */
    private static final String TOOLTIP_NUMERATOR_ID = "pieChartTooltipNumerator";
    /**
     * The ID for the HTML element containing the tooltip for an entry's denominator.
     */
    private static final String TOOLTIP_DENOMINATOR_ID = "pieChartTooltipDenominator";
    /**
     * The ID for the HTML element containing the tooltip for an entry's percentage.
     */
    private static final String TOOLTIP_PERCENTAGE_ID = "pieChartTooltipPercentage";
    /**
     * The ID for the HTML element containing an entry's symbol.
     */
    private static final String SYMBOL_CLASS = "pieChartSymbol";
    /**
     * The magic number twelve.
     */
    private static final int TWELVE = 12;
    /**
     * The magic number one hundred.
     */
    private static final double ONE_HUNDRED = 100D;
    /**
     * The HTML class for the pie chart's div element.
     */
    private final String divClass;
    /**
     * An ordered collection with the entries for the pie chart.
     */
    private final OrderedCollection<Entry> entries;
    /**
     * The HTML class for the title.
     */
    private final String titleClass;

    /**
     * Constructor taking the HTML class for the pie chart's div element, the HTML class for the title and a collection
     * of entries as its parameters. The entries will be sorted according to their size.
     *
     * @param divClass   The HTML class for the pie chart's div element.
     * @param titleClass The HTML class for the title.
     * @param entries    The entries for the pie chart.
     */
    PieChart(final String divClass, final String titleClass, final Collection<Entry> entries) {
        this(divClass, titleClass, new SortedTreeCollection<Entry>(new EntryComparator(), entries));
    }

    /**
     * Constructor taking the HTML class for the pie chart's div element, the HTML class for the title and an ordered
     * collection of entries as its parameters.
     *
     * @param divClass   The HTML class for the pie chart's div element.
     * @param titleClass The HTML class for the title.
     * @param entries    The entries for the pie chart in the order in which they should be presented in the pie chart.
     */
    PieChart(final String divClass, final String titleClass, final OrderedCollection<Entry> entries) {
        this.divClass = divClass;
        this.titleClass = titleClass;
        this.entries = entries;
    }

    /**
     * Creates the div element for the tooltip.
     *
     * @return A div element with the tooltip.
     */
    static FlowContent createTooltipDiv() {
        Div div = new Div(" ").id(TOOLTIP_ID).clazz("tooltip").style("position: absolute; display: none;");
        div.addElement(new Span(" ").id(TOOLTIP_LABEL_ID));
        div.addElement(new BR());
        div.addElement(new Span(" ").id(TOOLTIP_NUMERATOR_ID));
        div.addContent("/");
        div.addElement(new Span(" ").id(TOOLTIP_DENOMINATOR_ID));
        div.addContent(" (");
        div.addElement(new Span(" ").id(TOOLTIP_PERCENTAGE_ID));
        div.addContent("%)");
        return div;
    }

    /**
     * Creates a div element with the pie chart.
     *
     * @return A div element with the pie chart.
     */
    Div getDiv() {
        long sum = entries.stream().map(e -> e.value()).reduce(0L, Long::sum);
        Div div = new Div().clazz(divClass);
        Svg htmlSvg = new Svg();
        net.filipvanlaenen.tsvgj.Svg svg = htmlSvg.getSvg();
        svg.viewBox(0, 0, SVG_CONTAINER_WIDTH, SVG_CONTAINER_HEIGHT).preserveAspectRatio(
                PreserveAspectRatioAlignValue.X_MIN_Y_MIN, PreserveAspectRatioMeetOrSliceValue.MEET);
        Text title = new Text(" ").x(CENTER_X).y(TITLE_HEIGHT / 2).fontSize(TITLE_HEIGHT)
                .textAnchor(TextAnchorValue.MIDDLE).dominantBaseline(DominantBaselineValue.MIDDLE).clazz(titleClass);
        svg.addElement(title);
        div.addElement(htmlSvg);
        if (sum == 0L) {
            return div;
        }
        long counter = 0L;
        double halfwayCounter = 0D;
        double startX = CENTER_X;
        double startY = CENTER_Y - RADIUS;
        double endX = CENTER_X;
        double endY = CENTER_Y - RADIUS;
        int i = 0;
        for (Entry entry : entries) {
            long value = entry.value();
            halfwayCounter = counter + value / 2D;
            counter += value;
            String sliceClass = entry.sliceClass();
            if (sliceClass == null) {
                sliceClass = "pie-chart-" + (i % TWELVE + 1);
            }
            String onMouseMoveEvent = "showPieChartTooltip(evt, '" + entry.labelClass() + "', '" + value + "', '" + sum
                    + "', '" + (Math.round(ONE_HUNDRED * value / sum)) + "');";
            String onMouseOutEvent = "hideTooltip('" + TOOLTIP_ID + "');";
            if (value == sum) {
                Circle circle = new Circle().cx(CENTER_X).cy(CENTER_Y).r(RADIUS).clazz(sliceClass);
                circle.onmousemove(onMouseMoveEvent).onmouseout(onMouseOutEvent);
                svg.addElement(circle);
                Text symbol = new Text(entry.symbol()).x(CENTER_X).y(CENTER_Y).fontSize(SYMBOL_HEIGHT)
                        .textAnchor(TextAnchorValue.MIDDLE).dominantBaseline(DominantBaselineValue.MIDDLE)
                        .clazz(SYMBOL_CLASS).onmousemove(onMouseMoveEvent).onmouseout(onMouseOutEvent);
                svg.addElement(symbol);
            } else if (value > 0L) {
                endX = CENTER_X + Math.sin(2 * Math.PI * counter / sum) * RADIUS;
                endY = CENTER_Y - Math.cos(2 * Math.PI * counter / sum) * RADIUS;
                Path slice = new Path().clazz(sliceClass);
                slice.moveTo(CENTER_X, CENTER_Y);
                slice.lineTo(endX, endY);
                slice.arcTo(RADIUS, RADIUS, 0,
                        2 * value > sum ? LargeArcFlagValues.LARGE_ARC : LargeArcFlagValues.SMALL_ARC,
                        SweepFlagValues.NEGATIVE_ANGLE, startX, startY);
                slice.closePath();
                slice.onmousemove(onMouseMoveEvent).onmouseout(onMouseOutEvent);
                svg.addElement(slice);
                double symbolX = CENTER_X + Math.sin(2 * Math.PI * halfwayCounter / sum) * SYMBOL_RADIUS;
                double symbolY = CENTER_Y - Math.cos(2 * Math.PI * halfwayCounter / sum) * SYMBOL_RADIUS;
                Text symbol = new Text(entry.symbol()).x(symbolX).y(symbolY).fontSize(SYMBOL_HEIGHT)
                        .textAnchor(TextAnchorValue.MIDDLE).dominantBaseline(DominantBaselineValue.MIDDLE)
                        .clazz(SYMBOL_CLASS).onmousemove(onMouseMoveEvent).onmouseout(onMouseOutEvent);
                svg.addElement(symbol);
                startX = endX;
                startY = endY;
            }
            i++;
        }
        return div;
    }
}
