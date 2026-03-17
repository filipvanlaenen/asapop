package net.filipvanlaenen.asapop.website;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.sortedtree.SortedTreeCollection;
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
    /**
     * Record type to contain entries for a pie chart.
     *
     * @param label    The class for this entry's label.
     * @param symbol   The symbol for this entry.
     * @param value    The value for this entry.
     * @param barClass The class for this entry's bar.
     */
    record Entry(String label, String symbol, long value, String barClass) implements Chart.Entry {
    }

    /**
     * The magic number four.
     */
    private static final int FOUR = 4;
    /**
     * The magic number ten.
     */
    private static final long TEN = 10L;
    /**
     * The magic number twelve.
     */
    private static final int TWELVE = 12;
    /**
     * The magic number one thousand.
     */
    private static final long ONE_THOUSAND = 1000L;
    /**
     * The magic number one million.
     */
    private static final long ONE_MILLION = 1_000_000L;
    /**
     * The magic number one billion.
     */
    private static final long ONE_BILLION = 1_000_000_000L;
    /**
     * The magic number one trillion.
     */
    private static final long ONE_TRILLION = 1_000_000_000_000L;
    /**
     * The magic number one quadrillion.
     */
    private static final long ONE_QUADRILLION = 1_000_000_000_000L;
    /**
     * The powers of ten covered by the grid lines.
     */
    private static final OrderedLongCollection POWERS_OF_TEN =
            OrderedLongCollection.createSequence(1L, n -> n * TEN, 15);
    /**
     * The strides for the major grid lines.
     */
    private static final SortedLongCollection MAJOR_GRID_STRIDES = SortedLongCollection.of(Comparator.naturalOrder(),
            OrderedLongCollection.ofMatrixDirectProduct(POWERS_OF_TEN, OrderedLongCollection.of(1L, 2L, 5L)));
    /**
     * The strides for the minor grid lines.
     */
    private static final SortedLongCollection MINOR_GRID_STRIDES = SortedLongCollection.of(Comparator.naturalOrder(),
            OrderedLongCollection.ofMatrixDirectProduct(POWERS_OF_TEN, OrderedLongCollection.of(1L, 5L)));
    /**
     * The maximum number of major grid lines (in addition to the grid line at zero).
     */
    private static final long MAXIMUM_NUMBER_OF_MAJOR_GRID_LINES = 5L;
    /**
     * The ID for the HTML element containing an entry's label.
     */
    private static final String LABEL_CLASS = "bar-chart-label";
    /**
     * Style class for the grid line labels.
     */
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
     * Style class for the major grid lines.
     */
    private static final String MAJOR_GRID_LINE_CLASS = "major-grid-line";
    /**
     * Style class for the minor grid lines.
     */
    private static final String MINOR_GRID_LINE_CLASS = "minor-grid-line";
    /**
     * Stroke width for the grid lines.
     */
    private static final double GRID_LINE_STROKE_WIDTH = 0.2D;
    /**
     * The ratio of the bar width relative to the slot width.
     */
    private static final double BAR_TO_SLOT_WIDTH_RATIO = 0.9D;

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
    protected void addChartElements(final Svg svg) {
        int n = entries.size();
        double slotWidth = CHART_CANVAS_WIDTH / n;
        double barWidth = slotWidth * BAR_TO_SLOT_WIDTH_RATIO;
        long maximumValue = entries.stream().mapToLong(Entry::value).max().getAsLong();
        addMinorGridLines(svg, maximumValue);
        addMajorGridLinesAndLabels(svg, maximumValue);
        int i = 0;
        for (Entry entry : entries) {
            long value = entry.value();
            String label = entry.label();
            double x = LEFT_X + slotWidth * (1D - BAR_TO_SLOT_WIDTH_RATIO) / 2D + i * slotWidth;
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

    /**
     * Adds the major grid lines and their labels.
     *
     * @param svg          The SVG object to add the major grid lines and their labels to.
     * @param maximumValue The maximum value of the entries.
     */
    private void addMajorGridLinesAndLabels(final Svg svg, final long maximumValue) {
        long majorGridStride = calculateMajorGridStride(maximumValue);
        long gridValue = 0L;
        while (gridValue <= maximumValue) {
            double y = BOTTOM_Y - CHART_CANVAS_HEIGHT * gridValue / maximumValue;
            Line line = new Line().x1(LEFT_X).x2(RIGHT_X).y1(y).y2(y).strokeWidth(GRID_LINE_STROKE_WIDTH)
                    .clazz(MAJOR_GRID_LINE_CLASS);
            svg.addElement(line);
            String label = Long.toString(gridValue);
            if (majorGridStride % ONE_THOUSAND == 0) {
                label = Long.toString(gridValue / ONE_THOUSAND) + "K";
            }
            if (majorGridStride % ONE_MILLION == 0) {
                label = Long.toString(gridValue / ONE_THOUSAND) + "M";
            }
            if (majorGridStride % ONE_BILLION == 0) {
                label = Long.toString(gridValue / ONE_THOUSAND) + "G";
            }
            if (majorGridStride % ONE_TRILLION == 0) {
                label = Long.toString(gridValue / ONE_THOUSAND) + "T";
            }
            if (majorGridStride % ONE_QUADRILLION == 0) {
                label = Long.toString(gridValue / ONE_THOUSAND) + "P";
            }
            Text labelText = new Text(label).x(LEFT_X - TITLE_HEIGHT / 5D).y(y).fontSize(TITLE_HEIGHT / 3D)
                    .textAnchor(TextAnchorValue.END).dominantBaseline(DominantBaselineValue.MIDDLE)
                    .clazz(GRID_LINE_LABEL_CLASS);
            svg.addElement(labelText);
            gridValue += majorGridStride;
        }
    }

    /**
     * Adds the minor grid lines.
     *
     * @param svg          The SVG object to add the minor grid lines.
     * @param maximumValue The maximum value of the entries.
     */
    private void addMinorGridLines(final Svg svg, final long maximumValue) {
        long majorGridStride = calculateMajorGridStride(maximumValue);
        long minorGridStride = majorGridStride > 1L ? MINOR_GRID_STRIDES.getLessThan(majorGridStride) : 1L;
        long gridValue = 0L;
        while (gridValue <= maximumValue) {
            if (gridValue % majorGridStride != 0) {
                double y = BOTTOM_Y - CHART_CANVAS_HEIGHT * gridValue / maximumValue;
                Line line = new Line().x1(LEFT_X).x2(RIGHT_X).y1(y).y2(y).strokeWidth(GRID_LINE_STROKE_WIDTH)
                        .strokeDashArray(1, FOUR).clazz(MINOR_GRID_LINE_CLASS);
                svg.addElement(line);
            }
            gridValue += minorGridStride;
        }
    }

    /**
     * Calculate the stride for the major grid lines based on the maximum value.
     *
     * @param maximumValue The maximum value for an entry in the bar chart.
     * @return The stride for the major grid lines.
     */
    private long calculateMajorGridStride(final long maximumValue) {
        return MAJOR_GRID_STRIDES.getGreaterThanOrEqualTo(maximumValue / MAXIMUM_NUMBER_OF_MAJOR_GRID_LINES);
    }
}
