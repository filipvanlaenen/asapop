package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
}
