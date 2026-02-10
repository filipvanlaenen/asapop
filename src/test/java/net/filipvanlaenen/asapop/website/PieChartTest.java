package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.website.Chart.EntryComparator;
import net.filipvanlaenen.asapop.website.PieChart.Entry;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Unit tests on the <code>PieChart</code> class.
 */
public class PieChartTest {
    /**
     * An entry with value 0.
     */
    private static final Entry ENTRY0 = new Entry("label0", "0", 0, "slice0");
    /**
     * An entry with value 1.
     */
    private static final Entry ENTRY1 = new Entry("label1", "*", 1, "slice1");
    /**
     * An entry with value 2.
     */
    private static final Entry ENTRY2 = new Entry("label2", "+", 2, "slice2");
    /**
     * An entry with value 3.
     */
    private static final Entry ENTRY3 = new Entry("label3", "o", 3, null);

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
        PieChart pieChart = new PieChart("pie-chart", "pie-chart-title", Collection.of());
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
        PieChart pieChart = new PieChart("pie-chart", "pie-chart-title", Collection.of(ENTRY1));
        StringBuilder expected = new StringBuilder();
        expected.append("<div class=\"pie-chart\">\n");
        expected.append("  <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("    <text class=\"pie-chart-title\" dominant-baseline=\"middle\" font-size=\"20\""
                + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append(
                "    <circle class=\"slice1\" cx=\"250\" cy=\"125\" onmousemove=\"showPieChartTooltip(evt, 'label1',"
                        + " '1', '1', '100');\" onmouseout=\"hideTooltip('pieChartTooltip');\" r=\"100\"/>\n");
        expected.append("    <text class=\"pie-chart-symbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label1', '1', '1', '100');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"250\""
                + " y=\"125\">*</text>\n");
        expected.append("  </svg>\n");
        expected.append("</div>");
        assertEquals(expected.toString(), pieChart.getDiv().asString());
    }

    /**
     * Verifies that a pie chart is produced when two entries are provided.
     */
    @Test
    public void shouldCreateAPieChartForTwoEntries() {
        PieChart pieChart = new PieChart("pie-chart", "pie-chart-title", Collection.of(ENTRY1, ENTRY2));
        StringBuilder expected = createAPieChartWithTwoEntries();
        assertEquals(expected.toString(), pieChart.getDiv().asString());
    }

    /**
     * Verifies that adding an entry with value zero doesn't change the pie chart with two other entries.
     */
    @Test
    public void shouldCreateAPieChartForTwoEntriesIfTheThirdHasValueZero() {
        PieChart pieChart = new PieChart("pie-chart", "pie-chart-title", Collection.of(ENTRY0, ENTRY1, ENTRY2));
        StringBuilder expected = createAPieChartWithTwoEntries();
        assertEquals(expected.toString(), pieChart.getDiv().asString());
    }

    /**
     * Creates the result for a pie chart with two entries.
     *
     * @return The result for a pie chart with two entries.
     */
    private StringBuilder createAPieChartWithTwoEntries() {
        StringBuilder expected = new StringBuilder();
        expected.append("<div class=\"pie-chart\">\n");
        expected.append("  <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("    <text class=\"pie-chart-title\" dominant-baseline=\"middle\" font-size=\"20\""
                + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append("    <path class=\"slice2\" d=\"M 250 125 L 163.39746 175 A 100 100 0 1 0 250 25 Z\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label2', '2', '3', '67');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("    <text class=\"pie-chart-symbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label2', '2', '3', '67');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"319.282032\""
                + " y=\"165\">+</text>\n");
        expected.append("    <path class=\"slice1\" d=\"M 250 125 L 250 25 A 100 100 0 0 0 163.39746 175 Z\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label1', '1', '3', '33');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("    <text class=\"pie-chart-symbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label1', '1', '3', '33');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"180.717968\""
                + " y=\"85\">*</text>\n");
        expected.append("  </svg>\n");
        expected.append("</div>");
        return expected;
    }

    /**
     * Verifies that a pie chart is produced when three entries are provided in a specific order and one of them doesn't
     * have a slice class.
     */
    @Test
    public void shouldCreateAPieChartForThreeEntriesInSpecificedOrderAndAddingSliceClassForOneOfThem() {
        PieChart pieChart = new PieChart("pie-chart", "pie-chart-title", OrderedCollection.of(ENTRY1, ENTRY2, ENTRY3));
        StringBuilder expected = new StringBuilder();
        expected.append("<div class=\"pie-chart\">\n");
        expected.append("  <svg preserveAspectRatio=\"xMinYMin meet\" viewBox=\"0 0 500 250\">\n");
        expected.append("    <text class=\"pie-chart-title\" dominant-baseline=\"middle\" font-size=\"20\""
                + " text-anchor=\"middle\" x=\"250\" y=\"10\"> </text>\n");
        expected.append("    <path class=\"slice1\" d=\"M 250 125 L 336.60254 75 A 100 100 0 0 0 250 25 Z\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label1', '1', '6', '17');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("    <text class=\"pie-chart-symbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label1', '1', '6', '17');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"290\""
                + " y=\"55.717968\">*</text>\n");
        expected.append("    <path class=\"slice2\" d=\"M 250 125 L 250 225 A 100 100 0 0 0 336.60254 75 Z\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label2', '2', '6', '33');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("    <text class=\"pie-chart-symbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label2', '2', '6', '33');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"319.282032\""
                + " y=\"165\">+</text>\n");
        expected.append("    <path class=\"pie-chart-3\" d=\"M 250 125 L 250 25 A 100 100 0 0 0 250 225 Z\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label3', '3', '6', '50');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\"/>\n");
        expected.append("    <text class=\"pie-chart-symbol\" dominant-baseline=\"middle\" font-size=\"12\""
                + " onmousemove=\"showPieChartTooltip(evt, 'label3', '3', '6', '50');\""
                + " onmouseout=\"hideTooltip('pieChartTooltip');\" text-anchor=\"middle\" x=\"170\""
                + " y=\"125\">o</text>\n");
        expected.append("  </svg>\n");
        expected.append("</div>");
        assertEquals(expected.toString(), pieChart.getDiv().asString());
    }

    /**
     * Verifies that entries are compared correctly.
     */
    @Test
    public void entryComparatorShouldCompareCorrectly() {
        EntryComparator comparator = new EntryComparator();
        assertTrue(comparator.compare(ENTRY1, ENTRY2) > 0);
        assertTrue(comparator.compare(ENTRY2, ENTRY1) < 0);
        assertEquals(0, comparator.compare(ENTRY1, ENTRY1));
    }
}
