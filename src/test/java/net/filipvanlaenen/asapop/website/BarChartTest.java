package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.website.BarChart.Entry;
import net.filipvanlaenen.kolektoj.Collection;

/**
 * Unit tests on the <code>BarChart</code> class.
 */
public class BarChartTest {
    /**
     * An entry with value 1.
     */
    private static final Entry ENTRY1 = new Entry("label1", "*", 1, "slice1");

    /**
     * Verifies that the div element for the tooltip is correct.
     */
    @Test
    public void createTooltipDivShouldBeCorrect() {
        StringBuilder expected = new StringBuilder();
        expected.append("<div class=\"tooltip\" id=\"barChartTooltip\" style=\"position: absolute; display: none;\">"
                + " <span id=\"barChartTooltipLabel\"> </span>: <span id=\"barChartTooltipNumber\"> </span></div>");
        assertEquals(expected.toString(), BarChart.createTooltipDiv().asString());
    }

    /**
     * Verifies that an empty bar chart is produced when no entries are provided.
     */
    @Test
    public void shouldCreateAnEmptyBarChart() {
        BarChart barChart = new BarChart("bar-chart", "bar-chart-title", Collection.of());
        StringBuilder expected = new StringBuilder();
        expected.append("<div class=\"bar-chart\">\n");
        expected.append("  <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("    <text class=\"bar-chart-title\" dominant-baseline=\"middle\" font-size=\"20\""
                + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append("    <line class=\"major-grid-line\" stroke-width=\"0.2\" x1=\"50\" x2=\"450\" y1=\"225\""
                + " y2=\"225\"/>\n");
        expected.append("    <text class=\"bar-chart-grid-line-label\" dominant-baseline=\"middle\""
                + " font-size=\"6.666667\" text-anchor=\"end\" x=\"46\" y=\"225\">0</text>\n");
        expected.append("    <line class=\"major-grid-line\" stroke-width=\"0.2\" x1=\"50\" x2=\"450\" y1=\"25\""
                + " y2=\"25\"/>\n");
        expected.append("    <text class=\"bar-chart-grid-line-label\" dominant-baseline=\"middle\""
                + " font-size=\"6.666667\" text-anchor=\"end\" x=\"46\" y=\"25\">1</text>\n");
        expected.append("  </svg>\n");
        expected.append("</div>");
        assertEquals(expected.toString(), barChart.getDiv().asString());
    }

    /**
     * Verifies that a bar chart is produced for one entry.
     */
    @Test
    public void shouldCreateABarChartForOneEntry() {
        BarChart barChart = new BarChart("bar-chart", "bar-chart-title", Collection.of(ENTRY1));
        StringBuilder expected = new StringBuilder();
        expected.append("<div class=\"bar-chart\">\n");
        expected.append("  <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("    <text class=\"bar-chart-title\" dominant-baseline=\"middle\" font-size=\"20\""
                + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append("    <line class=\"major-grid-line\" stroke-width=\"0.2\" x1=\"50\" x2=\"450\" y1=\"225\""
                + " y2=\"225\"/>\n");
        expected.append("    <text class=\"bar-chart-grid-line-label\" dominant-baseline=\"middle\""
                + " font-size=\"6.666667\" text-anchor=\"end\" x=\"46\" y=\"225\">0</text>\n");
        expected.append("    <line class=\"major-grid-line\" stroke-width=\"0.2\" x1=\"50\" x2=\"450\" y1=\"25\""
                + " y2=\"25\"/>\n");
        expected.append("    <text class=\"bar-chart-grid-line-label\" dominant-baseline=\"middle\""
                + " font-size=\"6.666667\" text-anchor=\"end\" x=\"46\" y=\"25\">1</text>\n");
        expected.append("    <rect class=\"slice1\" height=\"200\" onmousemove=\"showBarChartTooltip(evt, 'label1',"
                + " '1');\" onmouseout=\"hideTooltip('barChartTooltip');\" width=\"360\" x=\"70\" y=\"25\"/>\n");
        expected.append("    <text class=\"bar-chart-label\" dominant-baseline=\"middle\" font-size=\"20\""
                + " onmousemove=\"showBarChartTooltip(evt, 'label1', '1');\""
                + " onmouseout=\"hideTooltip('barChartTooltip');\" text-anchor=\"start\""
                + " transform=\"rotate(-90 270,221)\" x=\"270\" y=\"221\">*</text>\n");
        expected.append("  </svg>\n");
        expected.append("</div>");
        assertEquals(expected.toString(), barChart.getDiv().asString());
    }
}
