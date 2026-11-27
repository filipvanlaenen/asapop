package net.filipvanlaenen.asapop.scraper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Cell</code> class.
 */
public class CellTest {
    private final static String COMPLICATED_CELL_CONTENT =
            "<td style=\"background:#FCCBCD\" about=\"#mwt113\" typeof=\"mw:ExpandedAttrs\" id=\"mwAVc\""
                    + " data-mw='{\"attribs\":[[{\"txt\":\"style\",\"html\":\"&lt;span about=\\\"#mwt112\\\""
                    + " typeof=\\\"mw:Transclusion\\\" id=\\\"mwAVY\\\""
                    + " data-mw=&apos;{\\\"parts\\\":[{\\\"template\\\":{\\\"target\\\":{\\\"wt\\\":\\\"Party"
                    + " shading/PSOE\\\",\\\"href\\\":\\\"./Template:Party_shading/PSOE\\\"},\\\"params\\\":{},"
                    + "\\\"i\\\":0}}]}&apos;>style=\\\"background:#FCCBCD\\\"&lt;/span>\"},{\"html\":\"\"}]]}'><b"
                    + " id=\"mwAVg\">29.8</b><br id=\"mwAVk\"/><span style=\"font-family:Verdana;font-size:75%;\""
                    + " about=\"#mwt111\" typeof=\"mw:Transclusion\" id=\"mwAVo\""
                    + " data-mw='{\"parts\":[{\"template\":{\"target\":{\"wt\":\"font\",\"href\":\"./Template:Font\"},"
                    + "\"params\":{\"size\":{\"wt\":\"75%\"},\"font\":{\"wt\":\"Verdana\"},\"text\":{\"wt\":\"120\"}},"
                    + "\"i\":0}}]}'>120</span></td>";

    /**
     * Verifies that <code>getColspan</code> returns 1 when no colspan is specified.
     */
    @Test
    public void getColspanShouldReturnOneByDefault() {
        assertEquals(1, Cell.EMPTY.getColspan());
    }

    /**
     * Verifies that <code>getColspan</code> returns the correct number when colspan is specified.
     */
    @Test
    public void getColspanShouldReturnCorrectNumberWhenSpecificed() {
        assertEquals(2, new Cell("<td colspan=\"2\">Foo</td>").getColspan());
    }

    /**
     * Verifies that <code>getDisplayText</code> returns an empty string for an empty cell.
     */
    @Test
    public void getDisplayTextShouldReturnEmptyStringForEmptyCell() {
        assertEquals("", Cell.EMPTY.getDisplayText());
    }

    /**
     * Verifies that <code>getDisplayText</code> returns a simple string for a simple cell with some simple text.
     */
    @Test
    public void getDisplayTextShouldReturnSimpleStringFromSimpleCellWithSimpleText() {
        assertEquals("Foo", new Cell("<td>Foo</td>").getDisplayText());
    }

    /**
     * Verifies that <code>getDisplayText</code> returns a two line string for a simple cell with a two line text.
     */
    @Test
    public void getDisplayTextShouldReturnTwoLineStringFromSimpleCellWithTwoLineText() {
        assertEquals("Foo\nBar", new Cell("<td>Foo<br/>Bar</td>").getDisplayText());
    }

    /**
     * Verifies that <code>getDisplayText</code> returns a two line string for a complicated cell.
     */
    @Test
    public void getDisplayTextShouldReturnTwoLineStringFromComplicatedCell() {
        assertEquals("29.8\n120", new Cell(COMPLICATED_CELL_CONTENT).getDisplayText());
    }

    /**
     * Verifies that <code>getFirstLineOfDisplayText</code> returns an empty string for an empty cell.
     */
    @Test
    public void getFirstLineOfDisplayTextShouldReturnEmptyStringForEmptyCell() {
        assertEquals("", Cell.EMPTY.getFirstLineOfDisplayText());
    }

    /**
     * Verifies that <code>getFirstLineOfDisplayText</code> returns a simple string for a simple cell with some simple
     * text.
     */
    @Test
    public void getFirstLineOfDisplayTextShouldReturnSimpleStringFromSimpleCellWithSimpleText() {
        assertEquals("Foo", new Cell("<td>Foo</td>").getFirstLineOfDisplayText());
    }

    /**
     * Verifies that <code>getFirstLineOfDisplayText</code> returns the first line for a simple cell with a two line
     * text.
     */
    @Test
    public void getFirstLineOfDisplayTextShouldReturnFirstLineFromSimpleCellWithTwoLineText() {
        assertEquals("Foo", new Cell("<td>Foo<br/>Bar</td>").getFirstLineOfDisplayText());
    }

    /**
     * Verifies that <code>getFlatDisplayText</code> returns an empty string for an empty cell.
     */
    @Test
    public void getFlatDisplayTextShouldReturnEmptyStringForEmptyCell() {
        assertEquals("", Cell.EMPTY.getFlatDisplayText());
    }

    /**
     * Verifies that <code>getFlatDisplayText</code> returns a simple string for a simple cell with some simple text.
     */
    @Test
    public void getFlatDisplayTextShouldReturnSimpleStringFromSimpleCellWithSimpleText() {
        assertEquals("Foo", new Cell("<td>Foo</td>").getFlatDisplayText());
    }

    /**
     * Verifies that <code>getFlatDisplayText</code> returns the first line for a simple cell with a two line text.
     */
    @Test
    public void getFlatDisplayTextShouldReturnFlatTextFromSimpleCellWithTwoLineText() {
        assertEquals("Foo Bar", new Cell("<td>Foo<br/>Bar</td>").getFlatDisplayText());
    }

    /**
     * Verifies that <code>getRolspan</code> returns 1 when no rowspan is specified.
     */
    @Test
    public void getRowspanShouldReturnOneByDefault() {
        assertEquals(1, Cell.EMPTY.getRowspan());
    }

    /**
     * Verifies that <code>getRowspan</code> returns the correct number when rowspan is specified.
     */
    @Test
    public void getRowspanShouldReturnCorrectNumberWhenSpecificed() {
        assertEquals(2, new Cell("<td rowspan=\"2\">Foo</td>").getRowspan());
    }
}
