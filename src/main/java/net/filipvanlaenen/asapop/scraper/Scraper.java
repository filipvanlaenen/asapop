package net.filipvanlaenen.asapop.scraper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.yaml.scrapeconfiguration.ColumnConfiguration;
import net.filipvanlaenen.asapop.yaml.scrapeconfiguration.TableConfiguration;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

public class Scraper {
    private static class Cell {
        private final String content;

        Cell(String content) {
            this.content = content;
        }

        int getRowSpan() {
            Pattern pattern = Pattern.compile(Pattern.quote("rowspan=\"") + "(\\d+)" + Pattern.quote("\""),
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            } else {
                return 1;
            }
        }

        String getContent() {
            return content;
        }
    }

    public final static Pattern TAG_CLOSING_PATTERN = Pattern.compile(Pattern.quote(">"));
    private final String page;

    public Scraper(String page) {
        this.page = page;
    }

    public static boolean containsHeaderWithText(final String page, final String text) {
        return containsTagPatternWithText(page, "h[1-4]", text);
    }

    public static boolean containsTagPatternWithText(final String page, final String tagPattern, final String text) {
        return getStartOfTagPatternWithText(page, tagPattern, text) != -1;
    }

    public static int getStartOfTagPatternWithText(final String page, final String tagPattern, final String text) {
        Pattern startPattern = Pattern.compile(Pattern.quote("<") + tagPattern, Pattern.CASE_INSENSITIVE);
        Pattern endPattern =
                Pattern.compile(Pattern.quote("</") + tagPattern + Pattern.quote(">"), Pattern.CASE_INSENSITIVE);
        Pattern textPattern = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE);
        int i = 0;
        while (i >= 0) {
            int tagStart = findFirstOccurrenceOf(page, i, startPattern);
            if (tagStart == -1) {
                return -1;
            } else {
                int tagStartClosing = findFirstOccurrenceOf(page, tagStart, TAG_CLOSING_PATTERN);
                int tagEnd = findFirstOccurrenceOf(page, tagStart, endPattern);
                String content = page.substring(tagStartClosing, tagEnd);
                Matcher matcher = textPattern.matcher(content);
                if (matcher.find()) {
                    return tagStart;
                }
                i = tagEnd + 1;
            }
        }
        return -1;
    }

    private OrderedCollection<String> extractTags(String fragment, String tagPattern) {
        return extractTags(fragment, tagPattern, tagPattern.length());
    }

    private OrderedCollection<String> extractTags(String fragment, String tagPattern, int patternLength) {
        Pattern startPattern = Pattern.compile(Pattern.quote("<") + tagPattern, Pattern.CASE_INSENSITIVE);
        Pattern endPattern =
                Pattern.compile(Pattern.quote("</") + tagPattern + Pattern.quote(">"), Pattern.CASE_INSENSITIVE);
        ModifiableOrderedCollection<String> result = ModifiableOrderedCollection.empty();
        int i = 0;
        while (i >= 0) {
            int tagStart = findFirstOccurrenceOf(fragment, i, startPattern);
            if (tagStart == -1) {
                return result;
            } else {
                int tagEnd = findFirstOccurrenceOf(fragment, tagStart, endPattern);
                result.add(fragment.substring(tagStart, tagEnd + patternLength + 3));
                i = tagEnd + 1;
            }
        }
        return result;
    }

    private int getStartOfTagPattern(String fragment, String tagPattern) {
        return getNextStartOfTagPattern(fragment, tagPattern, 0);
    }

    private int getNextStartOfTagPattern(String fragment, String tagPattern, int i) {
        Pattern startPattern = Pattern.compile(Pattern.quote("<") + tagPattern, Pattern.CASE_INSENSITIVE);
        return findFirstOccurrenceOf(fragment, i, startPattern);
    }

    private int getStartOfEndTagPattern(String fragment, String tagPattern) {
        Pattern startPattern =
                Pattern.compile(Pattern.quote("</") + tagPattern + Pattern.quote(">"), Pattern.CASE_INSENSITIVE);
        return findFirstOccurrenceOf(fragment, 0, startPattern);
    }

    public static boolean containsTagWithText(final String page, final String tag, final String text) {
        return containsTagPatternWithText(page, Pattern.quote(tag), text);
    }

    private static int findFirstOccurrenceOf(final String page, final int fromIndex, final Pattern pattern) {
        Matcher matcher = pattern.matcher(page);
        if (matcher.find(fromIndex)) {
            return matcher.start();
        } else {
            return -1;
        }
    }

    public String scrape(TableConfiguration tableConfiguration, final Token scrapeToken) {
        String fragment = reduceByHeader(page, tableConfiguration.getH2Content(), "h2", scrapeToken);
        fragment = reduceByHeader(fragment, tableConfiguration.getH3Content(), "h3", scrapeToken);
        fragment = reduceByHeader(fragment, tableConfiguration.getH4Content(), "h4", scrapeToken);
        fragment = reduceToTable(fragment, tableConfiguration.getTableIndex(), scrapeToken);
        OrderedCollection<String> rows = extractTags(fragment, "tr");
        OrderedCollection<OrderedCollection<String>> cellStrings = extractCellsFromRows(rows);
        Map<Integer, Map<Integer, Cell>> cells = parseCellStrings(cellStrings);
        int c = 0;
        for (ColumnConfiguration cc : tableConfiguration.getColumns()) {
            if (cells.containsKey(cc.getHeaderRow() - 1)) {
                if (cells.get(cc.getHeaderRow() - 1).containsKey(c)) {
                    // TODO: Match correctly, including whitespaces
                    if (!cells.get(cc.getHeaderRow() - 1).get(c).getContent().contains(cc.getHeaderContent())) {
                        Laconic.LOGGER.logError("Table does not contain header %s in the expected position.",
                                cc.getHeaderContent() + (cc.getHeaderRow() - 1) + "," + c, scrapeToken);
                    }
                } else {
                    Laconic.LOGGER.logError("Table does not contain %d column(s).", c + 1, scrapeToken);
                }
            } else {
                Laconic.LOGGER.logError("Table does not contain %d row(s).", cc.getHeaderRow(), scrapeToken);
            }
            c++;
        }
        // TODO: Parse the polling firm column
        // TODO: Parse the fieldwork period column
        // TODO: Parse the sample size column
        // TODO: Parse the result value columns
        // TODO: Parse the other column
        return "Scraping not implemented yet.";
    }

    private Map<Integer, Map<Integer, Cell>> parseCellStrings(
            OrderedCollection<OrderedCollection<String>> cellStrings) {
        ModifiableMap<Integer, ModifiableMap<Integer, Cell>> map = ModifiableMap.empty();
        int i = 0;
        for (OrderedCollection<String> row : cellStrings) {
            if (!map.containsKey(i)) {
                map.add(i, ModifiableMap.empty());
            }
            int j = 0;
            for (String c : row) {
                Cell cell = new Cell(c);
                while (map.get(i).containsKey(j)) {
                    j++;
                }
                int rw = cell.getRowSpan();
                // TODO: Handle colspan
                for (int k = 0; k < rw; k++) {
                    if (!map.containsKey(i + k)) {
                        map.add(i + k, ModifiableMap.empty());
                    }
                    map.get(i + k).add(j, cell);
                }
                j++;
            }
            i++;
        }
        ModifiableMap<Integer, Map<Integer, Cell>> result = ModifiableMap.empty();
        for (Entry<Integer, ModifiableMap<Integer, Cell>> e : map) {
            result.add(e.key(), Map.of(e.value()));
        }
        return result;
    }

    private OrderedCollection<OrderedCollection<String>> extractCellsFromRows(OrderedCollection<String> rows) {
        ModifiableOrderedCollection<OrderedCollection<String>> result = ModifiableOrderedCollection.empty();
        for (String row : rows) {
            result.add(extractTags(row, "t[dh]", 2));
        }
        return result;
    }

    private String reduceToTable(String fragment, Integer tableIndex, Token scrapeToken) {
        int n = tableIndex == null ? 1 : tableIndex;
        for (int i = 1; i < n; i++) {
            int j = getStartOfTagPattern(fragment, "table");
            if (j == -1) {
                Laconic.LOGGER.logError("Does not contain %d table(s) in the specified section.", n, scrapeToken);
                return fragment;
            }
            fragment = fragment.substring(j + 1);
        }
        int i = getStartOfTagPattern(fragment, "table");
        if (i == -1) {
            Laconic.LOGGER.logError("Does not contain %d table(s) in the specified section.", n, scrapeToken);
            return fragment;
        }
        int j = getStartOfEndTagPattern(fragment, "table");
        if (j == -1) {
            Laconic.LOGGER.logError("Broken table in the specified section.", n, scrapeToken);
            return fragment;
        }
        return fragment.substring(i, j + 8);
    }

    private String reduceByHeader(String fragment, String headerContent, String tag, final Token scrapeToken) {
        if (headerContent != null) {
            int i = getStartOfTagPatternWithText(fragment, tag, headerContent);
            if (i == -1) {
                Laconic.LOGGER.logError("Does not contain %s header content.", tag, scrapeToken);
                return fragment;
            } else {
                int j = getNextStartOfTagPattern(fragment, tag, i + 1);
                if (j == -1) {
                    return fragment.substring(i);
                } else {
                    return fragment.substring(i, j);
                }
            }
        }
        return fragment;
    }
}
