package net.filipvanlaenen.asapop.website;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.sortedtree.SortedTreeCollection;
import net.filipvanlaenen.nombrajkolektoj.longs.ModifiableLongCollection;
import net.filipvanlaenen.nombrajkolektoj.longs.OrderedLongCollection;
import net.filipvanlaenen.nombrajkolektoj.longs.SortedLongCollection;
import net.filipvanlaenen.tsvgj.DominantBaselineValue;
import net.filipvanlaenen.tsvgj.Line;
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
    private static final OrderedLongCollection POWERS_OF_TEN =
            OrderedLongCollection.createSequence(1L, n -> n * 10L, 15);
    private static final SortedLongCollection MAJOR_GRID_STRIDES = createMajorGridStrides();
    private static final SortedLongCollection MINOR_GRID_STRIDES = createMinorGridStrides();
    /**
     * The ID for the HTML element containing an entry's label.
     */
    private static final String LABEL_CLASS = "bar-chart-label";
    private static final String GRID_LINE_LABEL_CLASS = "bar-chart-grid-line-label";
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
    private static final String MAJOR_GRID_LINE_CLASS = "major-grid-line";
    private static final String MINOR_GRID_LINE_CLASS = "minor-grid-line";

    /**
     * Record type to contain entries for a pie chart.
     *
     * @param label    The class for this entry's label.
     * @param value    The value for this entry.
     * @param barClass The class for this entry's bar.
     */
    record Entry(String label, String symbol, long value, String barClass) implements Chart.Entry {
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

    private static SortedLongCollection createMajorGridStrides() {
        // TODO: Simply when Kronecker product has been implemented
        ModifiableLongCollection strides = ModifiableLongCollection.of(createMinorGridStrides());
        ModifiableLongCollection strides2 = ModifiableLongCollection.of(POWERS_OF_TEN);
        strides2.multiply(2L);
        strides.addAll(strides2);
        return SortedLongCollection.of(Comparator.naturalOrder(), strides);
    }

    private static SortedLongCollection createMinorGridStrides() {
        // TODO: Simply when Kronecker product has been implemented
        ModifiableLongCollection strides = ModifiableLongCollection.of(POWERS_OF_TEN);
        ModifiableLongCollection strides5 = ModifiableLongCollection.of(POWERS_OF_TEN);
        strides5.multiply(5L);
        strides.addAll(strides5);
        return SortedLongCollection.of(Comparator.naturalOrder(), strides);
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
        addMinorGridLines(svg, maximumValue);
        addMajorGridLinesAndLabels(svg, maximumValue);
        int i = 0;
        for (Entry entry : entries) {
            long value = entry.value();
            String label = entry.label();
            double x = LEFT_X + slotWidth * 0.05D + i * slotWidth;
            double height = CHART_CANVAS_HEIGHT * value / maximumValue;
            String barClass = entry.barClass();
            if (barClass == null) {
                barClass = "bar-chart-" + (i % TWELVE + 1);
            }
            Rect rect = new Rect().x(x).y(BOTTOM_Y - height).height(height).width(barWidth).clazz(barClass);
            String onMouseMoveEvent = "showBarChartTooltip(evt, '" + label + "', '" + value + "');";
            String onMouseOutEvent = "hideTooltip('" + TOOLTIP_ID + "');";
            rect.onmousemove(onMouseMoveEvent).onmouseout(onMouseOutEvent);
            svg.addElement(rect);
            String symbol = entry.symbol();
            if (symbol != null) {
                double labelX = LEFT_X + slotWidth * 0.55D + i * slotWidth;
                double labelFontSize = barWidth > TITLE_HEIGHT ? TITLE_HEIGHT : barWidth * 0.9D;
                double labelY = BOTTOM_Y - labelFontSize * 0.2D;
                Text labelText = new Text(symbol).x(labelX).y(labelY).fontSize(labelFontSize)
                        .textAnchor(TextAnchorValue.START).dominantBaseline(DominantBaselineValue.MIDDLE)
                        .transform(Transform.rotate(-90, labelX, labelY)).clazz(LABEL_CLASS)
                        .onmousemove(onMouseMoveEvent).onmouseout(onMouseOutEvent);
                svg.addElement(labelText);
            }
            i++;
        }
    }

    private void addMajorGridLinesAndLabels(Svg svg, long maximumValue) {
        long majorGridStride = MAJOR_GRID_STRIDES.getGreaterThanOrEqualTo(maximumValue / 5L);
        long gridValue = 0L;
        while (gridValue <= maximumValue) {
            double y = BOTTOM_Y - CHART_CANVAS_HEIGHT * gridValue / maximumValue;
            Line line = new Line().x1(LEFT_X).x2(RIGHT_X).y1(y).y2(y).strokeWidth(0.2D).clazz(MAJOR_GRID_LINE_CLASS);
            svg.addElement(line);
            String label = Long.toString(gridValue);
            if (majorGridStride % 1000L == 0) {
                label = Long.toString(gridValue / 1000L) + "K";
            }
            if (majorGridStride % 1000000L == 0) {
                label = Long.toString(gridValue / 1000L) + "M";
            }
            if (majorGridStride % 1000000000L == 0) {
                label = Long.toString(gridValue / 1000L) + "G";
            }
            if (majorGridStride % 1000000000000L == 0) {
                label = Long.toString(gridValue / 1000L) + "T";
            }
            if (majorGridStride % 1000000000000000L == 0) {
                label = Long.toString(gridValue / 1000L) + "P";
            }
            Text labelText = new Text(label).x(LEFT_X - TITLE_HEIGHT / 5D).y(y).fontSize(TITLE_HEIGHT / 3D)
                    .textAnchor(TextAnchorValue.END).dominantBaseline(DominantBaselineValue.MIDDLE)
                    .clazz(GRID_LINE_LABEL_CLASS);
            svg.addElement(labelText);
            gridValue += majorGridStride;
        }
    }

    private void addMinorGridLines(Svg svg, long maximumValue) {
        long majorGridStride = MAJOR_GRID_STRIDES.getGreaterThanOrEqualTo(maximumValue / 5L);
        long minorGridStride = majorGridStride > 1L ? MINOR_GRID_STRIDES.getLessThan(majorGridStride) : 1L;
        long gridValue = 0L;
        while (gridValue <= maximumValue) {
            if (gridValue % majorGridStride != 0) {
                double y = BOTTOM_Y - CHART_CANVAS_HEIGHT * gridValue / maximumValue;
                Line line = new Line().x1(LEFT_X).x2(RIGHT_X).y1(y).y2(y).strokeWidth(0.2D);
                line.clazz(MINOR_GRID_LINE_CLASS);
                svg.addElement(line);
            }
            gridValue += minorGridStride;
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
