package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.sortedtree.SortedTreeCollection;
import net.filipvanlaenen.tsvgj.DominantBaselineValue;
import net.filipvanlaenen.tsvgj.Rect;
import net.filipvanlaenen.tsvgj.Svg;
import net.filipvanlaenen.tsvgj.Text;
import net.filipvanlaenen.tsvgj.TextAnchorValue;
import net.filipvanlaenen.tsvgj.Transform;
import net.filipvanlaenen.txhtmlj.Div;
import net.filipvanlaenen.txhtmlj.FlowContent;
import net.filipvanlaenen.txhtmlj.Span;

/**
 * Class building bar charts in SVG.
 */
class BarChart extends Chart {
    /**
     * The ID for the HTML element containing an entry's label.
     */
    private static final String LABEL_CLASS = "barChartLabel";
    /**
     * The ID for the HTML element containing the bar chart tooltip.
     */
    private static final String TOOLTIP_ID = "barChartTooltip";
    /**
     * The ID for the HTML element containing the bar chart tooltip label.
     */
    private static final String TOOLTIP_LABEL_ID = "barChartTooltipLabel";
    /**
     * The ID for the HTML element containing the tooltip for an entry's number.
     */
    private static final String TOOLTIP_NUMBER_ID = "barChartTooltipNumber";
    /**
     * The magic number twelve.
     */
    private static final int TWELVE = 12;

    /**
     * Record type to contain entries for a pie chart.
     *
     * @param label      The class for this entry's label.
     * @param value      The value for this entry.
     * @param sliceClass The class for this entry's slice in the pie chart.
     */
    record Entry(String label, long value, String sliceClass) implements Chart.Entry {
    }

    /**
     * An ordered collection with the entries for the pie chart.
     */
    private final OrderedCollection<Entry> entries;

    /**
     * Constructor taking the HTML class for the bar chart's div element, the HTML class for the title and a collection
     * of entries as its parameters. The entries will be sorted according to their size.
     *
     * @param divClass   The HTML class for the bar chart's div element.
     * @param titleClass The HTML class for the title.
     * @param entries    The entries for the bar chart.
     */
    BarChart(final String divClass, final String titleClass, final Collection<Entry> entries) {
        this(divClass, titleClass, new SortedTreeCollection<Entry>(new EntryComparator(), entries));
    }

    /**
     * Constructor taking the HTML class for the bar chart's div element, the HTML class for the title and an ordered
     * collection of entries as its parameters.
     *
     * @param divClass   The HTML class for the bar chart's div element.
     * @param titleClass The HTML class for the title.
     * @param entries    The entries for the bar chart in the order in which they should be presented in the bar chart.
     */
    BarChart(final String divClass, final String titleClass, final OrderedCollection<Entry> entries) {
        super(divClass, titleClass);
        this.entries = entries;
    }

    @Override
    protected void addChartElements(Svg svg) {
        int n = entries.size();
        double slotWidth = CHART_CANVAS_WIDTH / n;
        double barWidth = slotWidth * 0.9D;
        long maximumValue = entries.stream().mapToLong(Entry::value).max().getAsLong();
        int i = 0;
        for (Entry entry : entries) {
            long value = entry.value();
            String label = entry.label();
            double x = LEFT_X + slotWidth * 0.05D + i * slotWidth;
            double height = CHART_CANVAS_HEIGHT * value / maximumValue;
            String sliceClass = entry.sliceClass();
            if (sliceClass == null) {
                sliceClass = "bar-chart-" + (i % TWELVE + 1);
            }
            Rect rect = new Rect().x(x).y(BOTTOM_Y - height).height(height).width(barWidth).clazz(sliceClass);
            String onMouseMoveEvent = "showBarChartTooltip(evt, '" + label + "', '" + value + "');";
            String onMouseOutEvent = "hideTooltip('" + TOOLTIP_ID + "');";
            rect.onmousemove(onMouseMoveEvent).onmouseout(onMouseOutEvent);
            svg.addElement(rect);
            double labelX = LEFT_X + slotWidth * 0.5D + i * slotWidth;
            double labelFontSize = barWidth > TITLE_HEIGHT ? TITLE_HEIGHT : barWidth * 0.9D;
            double labelY = BOTTOM_Y - labelFontSize * 0.2D;
            Text symbol = new Text(label).x(labelX).y(labelY).fontSize(labelFontSize).textAnchor(TextAnchorValue.START)
                    .dominantBaseline(DominantBaselineValue.MIDDLE).transform(Transform.rotate(-90, labelX, labelY))
                    .clazz(LABEL_CLASS).onmousemove(onMouseMoveEvent).onmouseout(onMouseOutEvent);
            svg.addElement(symbol);

            i++;
        }
    }

    /**
     * Creates the div element for the tooltip.
     *
     * @return A div element with the tooltip.
     */
    static FlowContent createTooltipDiv() {
        Div div = new Div(" ").id(TOOLTIP_ID).clazz("tooltip").style("position: absolute; display: none;");
        div.addElement(new Span(" ").id(TOOLTIP_LABEL_ID));
        div.addContent(": ");
        div.addElement(new Span(" ").id(TOOLTIP_NUMBER_ID));
        return div;
    }
}
