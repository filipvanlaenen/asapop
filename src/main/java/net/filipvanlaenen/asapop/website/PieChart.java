package net.filipvanlaenen.asapop.website;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.array.OrderedArrayCollection;
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

class PieChart {
    record Entry(String labelClass, long value, String sliceClass) {
    }

    private static class EntryComparator implements Comparator<Entry> {
        @Override
        public int compare(Entry e1, Entry e2) {
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
    private static final double CENTER_X = ((double) SVG_CONTAINER_WIDTH) / 2;
    private static final double CENTER_Y = ((double) SVG_CONTAINER_HEIGHT) / 2;
    private static final double RADIUS = ((double) SVG_CONTAINER_HEIGHT) * 0.4D;
    private static final double TITLE_HEIGHT = (CENTER_Y - RADIUS) * 0.8D;
    private static final String TOOLTIP_ID = "pieChartTooltip";
    private static final String TOOLTIP_LABEL_ID = "pieChartTooltipLabel";
    private static final String TOOLTIP_DIVIDEND_ID = "pieChartTooltipDividend";
    private static final String TOOLTIP_DIVISOR_ID = "pieChartTooltipDivisor";
    private static final String TOOLTIP_PERCENTAGE_ID = "pieChartTooltipPercentage";
    private final String divClass;
    private final OrderedCollection<Entry> entries;
    private final String titleClass;

    PieChart(final String divClass, final String titleClass, final Collection<Entry> entries) {
        this(divClass, titleClass, new OrderedArrayCollection<Entry>(entries, new EntryComparator()));
    }

    PieChart(final String divClass, final String titleClass, final OrderedCollection<Entry> entries) {
        this.divClass = divClass;
        this.titleClass = titleClass;
        this.entries = entries;
    }

    static FlowContent createTooltipDiv() {
        Div div = new Div(" ").id(TOOLTIP_ID).clazz("tooltip").style("position: absolute; display: none;");
        div.addElement(new Span(" ").id(TOOLTIP_LABEL_ID));
        div.addElement(new BR());
        div.addElement(new Span(" ").id(TOOLTIP_DIVIDEND_ID));
        div.addContent("/");
        div.addElement(new Span(" ").id(TOOLTIP_DIVISOR_ID));
        div.addContent(" (");
        div.addElement(new Span(" ").id(TOOLTIP_PERCENTAGE_ID));
        div.addContent("%)");
        return div;
    }

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
        double startX = CENTER_X;
        double startY = CENTER_Y - RADIUS;
        double endX = CENTER_X;
        double endY = CENTER_Y - RADIUS;
        int i = 0;
        for (Entry entry : entries) {
            long value = entry.value();
            counter += value;
            String sliceClass = entry.sliceClass();
            if (sliceClass == null) {
                sliceClass = "pie-chart-" + ((i % 12) + 1);
            }
            if (value == sum) {
                Circle circle = new Circle().cx(CENTER_X).cy(CENTER_Y).r(RADIUS).clazz(sliceClass);
                circle.onmousemove("showPieChartTooltip(evt, '" + entry.labelClass() + "', '" + value + "', '" + sum
                        + "', '" + (Math.round(100D * value / sum)) + "');")
                        .onmouseout("hideTooltip('" + TOOLTIP_ID + "');");
                svg.addElement(circle);
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
                slice.onmousemove("showPieChartTooltip(evt, '" + entry.labelClass() + "', '" + value + "', '" + sum
                        + "', '" + (Math.round(100D * value / sum)) + "');")
                        .onmouseout("hideTooltip('" + TOOLTIP_ID + "');");
                svg.addElement(slice);
                startX = endX;
                startY = endY;
            }
            i++;
        }
        return div;
    }
}
