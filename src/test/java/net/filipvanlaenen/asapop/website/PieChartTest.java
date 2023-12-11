package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.website.PieChart.Entry;
import net.filipvanlaenen.kolektoj.Collection;

/**
 * Unit tests on the <code>PieChart</code> class.
 */
public class PieChartTest {
    /**
     * Verifies that the div element for the tooltip is correct.
     */
    @Test
    public void createTooltipDivShouldBeCorrect() {
        StringBuilder expected = new StringBuilder();
        expected.append(
                "<div class=\"tooltip\" id=\"pieChartTooltip\" style=\"position: absolute; display: none;\"> <span"
                        + " id=\"pieChartTooltipLabel\"> </span><br/><span id=\"pieChartTooltipNumerator\">"
                        + " </span>/<span id=\"pieChartTooltipDenominator\"> </span> (<span"
                        + " id=\"pieChartTooltipPercentage\"> </span>%)</div>");
        assertEquals(expected.toString(), PieChart.createTooltipDiv().asString());
    }

    /**
     * Verifies that an empty pie chart is produced when no entries are provided.
     */
    @Test
    public void shouldCreateAnEmptyPieChart() {
        PieChart pieChart = new PieChart("pie-chart", "pie-chart-title", Collection.empty());
        StringBuilder expected = new StringBuilder();
        expected.append("<div class=\"pie-chart\">\n");
        expected.append("  <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("    <text class=\"pie-chart-title\" dominant-baseline=\"middle\" font-size=\"20\""
                + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append("  </svg>\n");
        expected.append("</div>");
        assertEquals(expected.toString(), pieChart.getDiv().asString());
    }

    /**
     * Verifies that a pie chart is produced when one entry is provided.
     */
    @Test
    public void shouldCreateAPieChartForOneEntry() {
        PieChart pieChart =
                new PieChart("pie-chart", "pie-chart-title", Collection.of(new Entry("label1", "*", 1, "slice1")));
        StringBuilder expected = new StringBuilder();
        expected.append("<div class=\"pie-chart\">\n");
        expected.append("  <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("    <text class=\"pie-chart-title\" dominant-baseline=\"middle\" font-size=\"20\""
                + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append(
                "    <circle class=\"slice1\" cx=\"250\" cy=\"125\" onmousemove=\"showPieChartTooltip(evt, 'label1',"
                        + " '1', '1', '100');\" onmouseout=\"hideTooltip('pieChartTooltip');\" r=\"100\"/>\n");
        expected.append("    <text class=\"pieChartSymbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label1', '1', '1', '100');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"250\""
                + " y=\"125\">*</text>\n");
        expected.append("  </svg>\n");
        expected.append("</div>");
        assertEquals(expected.toString(), pieChart.getDiv().asString());
    }
}
