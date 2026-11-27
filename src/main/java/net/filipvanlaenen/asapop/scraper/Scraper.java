package net.filipvanlaenen.asapop.scraper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.exporter.RopfExporter;
import net.filipvanlaenen.asapop.model.DateMonthOrYear;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;
import net.filipvanlaenen.asapop.yaml.scrapeconfiguration.ColumnConfiguration;
import net.filipvanlaenen.asapop.yaml.scrapeconfiguration.PollingFirmAliases;
import net.filipvanlaenen.asapop.yaml.scrapeconfiguration.TableConfiguration;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.collectors.Collectors;
import net.filipvanlaenen.kolektoj.hash.HashCollection;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Class scraping a Wikipedia table against a collection of polls using a scraping configuration.
 */
public class Scraper {
    /**
     * A record capturing the data elements to determine the presence of a poll in a collection of polls.
     *
     * @param pollingFirm The name of the polling firm.
     * @param endDate     The end date of the poll.
     */
    private record PollKey(String pollingFirm, DateMonthOrYear endDate) {
    }

    /**
     * A record for a fieldwork period with a start date and an end date.
     *
     * @param startDate The start date of the fieldwork period.
     * @param endDate   The end date of the fieldwork period.
     */
    private record FieldworkPeriod(DateMonthOrYear startDate, DateMonthOrYear endDate) {
    }

    private final static Pattern GREATER_THAN_PATTERN = Pattern.compile(Pattern.quote(">"));
    public static final String ALL_OK = "Found no unmatched polls or unparsed rows.";
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
                int tagStartClosing = findFirstOccurrenceOf(page, tagStart, GREATER_THAN_PATTERN);
                int tagEnd = findFirstOccurrenceOf(page, tagStart, endPattern);
                String content = page.substring(tagStartClosing + 1, tagEnd);
                Matcher matcher = textPattern.matcher(content);
                if (matcher.find()) {
                    return tagStart;
                }
                i = tagEnd + 1;
            }
        }
        return -1;
    }

    static OrderedCollection<String> extractEmptyTags(String fragment, String tagPattern) {
        return extractEmptyTags(fragment, tagPattern, tagPattern.length());
    }

    private static OrderedCollection<String> extractEmptyTags(String fragment, String tagPattern, int patternLength) {
        Pattern startPattern = Pattern.compile(Pattern.quote("<") + tagPattern, Pattern.CASE_INSENSITIVE);
        Pattern endPattern = Pattern.compile(Pattern.quote("/>"), Pattern.CASE_INSENSITIVE);
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

    private static OrderedCollection<String> extractTags(String fragment, String tagPattern) {
        return extractTags(fragment, tagPattern, tagPattern.length());
    }

    private static OrderedCollection<String> extractTags(String fragment, String tagPattern, int patternLength) {
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

    private static int getStartOfTagPattern(String fragment, String tagPattern) {
        return getNextStartOfTagPattern(fragment, tagPattern, 0);
    }

    private static int getNextStartOfTagPattern(String fragment, String tagPattern, int i) {
        Pattern startPattern = Pattern.compile(Pattern.quote("<") + tagPattern, Pattern.CASE_INSENSITIVE);
        return findFirstOccurrenceOf(fragment, i, startPattern);
    }

    private static int getStartOfEndTagPattern(String fragment, String tagPattern) {
        Pattern startPattern =
                Pattern.compile(Pattern.quote("</") + tagPattern + Pattern.quote(">"), Pattern.CASE_INSENSITIVE);
        return findFirstOccurrenceOf(fragment, 0, startPattern);
    }

    public static boolean containsTagWithText(final String page, final String tag, final String text) {
        return containsTagPatternWithText(page, Pattern.quote(tag), text);
    }

    static int findFirstOccurrenceOf(final String fragment, final int fromIndex, final Pattern pattern) {
        Matcher matcher = pattern.matcher(fragment);
        if (matcher.find(fromIndex)) {
            return matcher.start();
        } else {
            return -1;
        }
    }

    public String scrape(TableConfiguration tableConfiguration, RichOpinionPollsFile richOpinionPollsFile,
            final Token scrapeToken) {
        String fragment = reduceByHeader(page, tableConfiguration.getH2Content(), "h2", scrapeToken);
        fragment = reduceByHeader(fragment, tableConfiguration.getH3Content(), "h3", scrapeToken);
        fragment = reduceByHeader(fragment, tableConfiguration.getH4Content(), "h4", scrapeToken);
        fragment = reduceByHeader(fragment, tableConfiguration.getH5Content(), "h5", scrapeToken);
        fragment = reduceToTable(fragment, tableConfiguration.getTableIndex(), scrapeToken);
        OrderedCollection<String> rows = extractTags(fragment, "tr");
        OrderedCollection<OrderedCollection<String>> cellStrings = extractCellsFromRows(rows);
        Map<Integer, Map<Integer, Cell>> cells = parseCellStrings(cellStrings);
        int columnIndex = 0;
        boolean scrapeConfigurationError = false;
        for (ColumnConfiguration cc : tableConfiguration.getColumns()) {
            Token validateColumnHeaderToken = Laconic.LOGGER.logMessage(scrapeToken,
                    "Validating the header of the column at position %d.", columnIndex + 1);
            int rowIndex = cc.getHeaderRow() - 1;
            if (cells.containsKey(rowIndex)) {
                Laconic.LOGGER.logMessage("Validating the header of the column on row %d.", rowIndex + 1,
                        validateColumnHeaderToken);
                if (cells.get(rowIndex).containsKey(columnIndex)) {
                    if (cc.getHeaderContent() != null) {
                        String header = cells.get(rowIndex).get(columnIndex).getFlatDisplayText();
                        Laconic.LOGGER.logMessage("Validating the header “%s”.", header, validateColumnHeaderToken);
                        if (!header.equals(cc.getHeaderContent())) {
                            Laconic.LOGGER.logError("Header text isn't equal to “%s”.", cc.getHeaderContent(),
                                    validateColumnHeaderToken);
                            scrapeConfigurationError = true;
                        }
                    } else if (cc.getHeaderImage() != null) {
                        String image = cells.get(rowIndex).get(columnIndex).getImage();
                        Laconic.LOGGER.logMessage("Validating the header image “%s”.", image,
                                validateColumnHeaderToken);
                        if (!image.equals(cc.getHeaderImage())) {
                            Laconic.LOGGER.logError("Header image isn't equal to “%s”.", cc.getHeaderImage(),
                                    validateColumnHeaderToken);
                            scrapeConfigurationError = true;
                        }
                    } else {
                        Laconic.LOGGER.logError("Column configuration doesn't contain content or image to validate.",
                                validateColumnHeaderToken);
                        scrapeConfigurationError = true;
                    }
                } else {
                    Laconic.LOGGER.logError("Table does not contain %d column(s).", columnIndex + 1,
                            validateColumnHeaderToken);
                    scrapeConfigurationError = true;
                }
            } else {
                Laconic.LOGGER.logError("Table does not contain %d row(s).", rowIndex + 1, validateColumnHeaderToken);
                scrapeConfigurationError = true;
            }
            columnIndex++;
        }
        ModifiableMap<String, String> pollingFirmAliases = ModifiableMap.empty();
        if (tableConfiguration.getPollingFirmAliases() != null) {
            PollingFirmAliases[] aliases = tableConfiguration.getPollingFirmAliases();
            for (PollingFirmAliases a : aliases) {
                for (String s : a.getAliases()) {
                    pollingFirmAliases.add(s, a.getName());
                }
            }
        }
        Integer headRows = tableConfiguration.getHeadRows();
        int firstRow = headRows == null ? 0 : headRows;
        Integer footRows = tableConfiguration.getFootRows();
        int lastRow = cells.size() - (footRows == null ? 0 : footRows);
        int unmatched = 0;
        String unmatchedPolls = "";
        Collection<String> excludedPollingFirms =
                tableConfiguration.getExcludedPollingFirms() == null ? Collection.empty()
                        : new HashCollection<>(tableConfiguration.getExcludedPollingFirms());
        Collection<PollKey> unreconciledPolls =
                new HashCollection<PollKey>(tableConfiguration.getUnreconciledPolls() == null ? Collection.empty()
                        : Collection.of(tableConfiguration.getUnreconciledPolls()).stream()
                                .map(u -> new PollKey(u.getPollingFirm(), DateMonthOrYear.parse(u.getDate())))
                                .collect(Collectors.toCollection()));
        Collection<PollKey> currentPolls = new HashCollection<PollKey>(richOpinionPollsFile.getOpinionPolls().stream()
                .map(p -> new PollKey(p.getPollingFirm(),
                        p.getFieldworkEnd() == null ? DateMonthOrYear.of(p.getPublicationDate()) : p.getFieldworkEnd()))
                .collect(Collectors.toCollection()));
        OrderedCollection<ColumnConfiguration> columns = OrderedCollection.of(tableConfiguration.getColumns());
        int pollingFirmIndex = 0;
        for (int i = 0; i < columns.size(); i++) {
            if (Collection.of("pollingFirm", "pollingFirmAndCommissioner").contains(columns.getAt(i).getColumnType())) {
                pollingFirmIndex = i;
            }
        }
        Set<ElectoralList> ids = new HashSet<ElectoralList>();
        for (ColumnConfiguration cc : columns) {
            if ("resultValue".equals(cc.getColumnType())) {
                for (String i : cc.getResultKeys().split("\\+")) {
                    ids.add(ElectoralList.get(i));
                }
            }
        }
        java.util.Map<String, String> idsToKeysMap = RopfExporter.calculateIdsToKeys(ids, false);
        Integer defaultYear = tableConfiguration.getDefaultYear();
        boolean firstLineOfResultsOnly = Boolean.TRUE == tableConfiguration.getFirstLineOfResultsOnly();
        String defaultYearString = defaultYear == null ? null : Integer.toString(defaultYear);
        for (int i = firstRow; i < lastRow; i++) {
            Token parseRowToken = Laconic.LOGGER.logMessage(scrapeToken, "Parsing the row at position %d.", i + 1);
            boolean allResultValuesIdentical = true;
            Cell lastResultValueCell = null;
            Map<Integer, Cell> row = cells.get(i);
            if (!row.isEmpty() && !row.get(pollingFirmIndex).getFlatDisplayText().toLowerCase().contains("election")) {
                String commissioner = null;
                String pollingFirm = null;
                DateMonthOrYear startDate = null;
                DateMonthOrYear endDate = null;
                String noResponses = null;
                String other = null;
                String sampleSize = null;
                String excluded = null;
                ModifiableMap<String, String> results = ModifiableMap.empty();
                int j = 0;
                int remainingColSpan = 0;
                String lastKey = null;
                for (ColumnConfiguration cc : columns) {
                    Token parseCellToken =
                            Laconic.LOGGER.logMessage(parseRowToken, "Parsing the cell at position %d.", j + 1);
                    String columnType = cc.getColumnType();
                    Cell cell = row.containsKey(j) ? row.get(j) : Cell.EMPTY;
                    if ("pollingFirm".equals(columnType)) {
                        pollingFirm = cell.getFlatDisplayText();
                        Laconic.LOGGER.logMessage("Found the name “%s” for the polling firm.", pollingFirm,
                                parseCellToken);
                        if (pollingFirmAliases.containsKey(pollingFirm)) {
                            pollingFirm = pollingFirmAliases.get(pollingFirm);
                            Laconic.LOGGER.logMessage("Converted the alias of the polling firm to the name “%s”.",
                                    pollingFirm, parseCellToken);
                        }
                    } else if ("pollingFirmAndCommissioner".equals(columnType)) {
                        String pollingFirmAndCommissioner = cell.getFlatDisplayText();
                        if (pollingFirmAndCommissioner.contains(cc.getSeparator())) {
                            String[] r = pollingFirmAndCommissioner.split(Pattern.quote(cc.getSeparator()));
                            pollingFirm = r[0];
                            commissioner = r[1];
                        } else {
                            pollingFirm = pollingFirmAndCommissioner;
                        }
                        Laconic.LOGGER.logMessage("Found the name “%s” for the polling firm.", pollingFirm,
                                parseCellToken);
                        if (pollingFirmAliases.containsKey(pollingFirm)) {
                            pollingFirm = pollingFirmAliases.get(pollingFirm);
                            Laconic.LOGGER.logMessage("Converted the alias of the polling firm to the name “%s”.",
                                    pollingFirm, parseCellToken);
                        }
                    } else if ("fieldworkPeriod".equals(columnType)) {
                        if (!cell.isEmpty()) {
                            String fieldwordPeriod = cell.getFlatDisplayText();
                            Laconic.LOGGER.logMessage("Parsing the fieldwork period text “%s”.", fieldwordPeriod,
                                    parseCellToken);
                            FieldworkPeriod period = parseFieldwordPeriod(fieldwordPeriod, defaultYearString);
                            if (period == null) {
                                Laconic.LOGGER.logError("Could not extract a fieldwork period.", parseCellToken);
                                scrapeConfigurationError = true;
                            } else {
                                startDate = period.startDate;
                                endDate = period.endDate();
                                Laconic.LOGGER.logMessage("Found %s for the fieldwork end date.", endDate.toString(),
                                        parseCellToken);
                            }
                        }
                    } else if ("sampleSize".equals(columnType)) {
                        if (!cell.isEmpty()) {
                            sampleSize = cell.getFlatDisplayText().replaceAll(",", "");
                        }
                    } else if ("turnout".equals(columnType)) {
                        if (!cell.isEmpty()) {
                            String turnout = cell.getFlatDisplayText().replaceAll(",", "").split("–")[0];
                            double t = Double.parseDouble(turnout);
                            excluded = String.format(Locale.US, "%.1f", 100D - t);
                        }
                    } else if ("resultValue".equals(columnType)) {
                        if (lastResultValueCell == null) {
                            lastResultValueCell = cell;
                        }
                        allResultValuesIdentical = cell == lastResultValueCell;
                        if (remainingColSpan > 0) {
                            if (!cell.isEmpty()) {
                                String key = RopfExporter.asKeysString(idsToKeysMap,
                                        ElectoralList.get(Set.of(cc.getResultKeys().split("\\+"))));
                                String result = results.remove(lastKey);
                                lastKey = lastKey + "+" + key;
                                results.add(lastKey, result);
                            }
                            remainingColSpan--;
                        } else if (!cell.isEmpty()) {
                            lastKey = RopfExporter.asKeysString(idsToKeysMap,
                                    ElectoralList.get(Set.of(cc.getResultKeys().split("\\+"))));
                            String resultValue = firstLineOfResultsOnly ? cell.getFirstLineOfDisplayText()
                                    : cell.getFlatDisplayText();
                            if (!"?".equals(resultValue)) {
                                results.add(lastKey, resultValue);
                                remainingColSpan = cell.getColspan() - 1;
                            }
                        }
                        if (remainingColSpan == 0) {
                            lastKey = null;
                        }
                    } else if ("other".equals(columnType)) {
                        if (!cell.isEmpty()) {
                            other = cell.getFlatDisplayText();
                        }
                    } else if ("noResponses".equals(columnType)) {
                        if (!cell.isEmpty()) {
                            noResponses = cell.getFlatDisplayText();
                        }
                    }
                    j++;
                }
                if (!allResultValuesIdentical && pollingFirm != null && !pollingFirm.isBlank() && endDate != null) {
                    PollKey pk = new PollKey(pollingFirm, endDate);
                    if (!excludedPollingFirms.contains(pollingFirm) && !unreconciledPolls.contains(pk)
                            && !currentPolls.contains(pk)) {
                        unmatched++;
                        unmatchedPolls += "\n  •PF: " + pollingFirm
                                + (commissioner == null ? "" : " •C: " + commissioner)
                                + (startDate == null ? "" : " •FS: " + startDate) + " •FE: " + endDate + " •SC: "
                                + tableConfiguration.getScope() + (sampleSize == null ? "" : " •SS: " + sampleSize)
                                + (excluded == null ? "" : " •EX: " + excluded)
                                + results.stream().map(e -> " " + e.key() + ": " + e.value()).collect(
                                        java.util.stream.Collectors.joining())
                                + (other == null ? "" : " •O: " + other)
                                + (noResponses == null ? "" : " •N: " + noResponses);
                    }
                }
            }
        }
        if (scrapeConfigurationError)

        {
            if (unmatched == 0) {
                return "Scrape configuration error.";
            } else {
                return "Scrape configuration error. Found also " + unmatched + " unmatched poll(s):" + unmatchedPolls;
            }
        } else if (unmatched == 0) {
            return ALL_OK;
        } else {
            return "Found " + unmatched + " unmatched poll(s):" + unmatchedPolls;
        }
    }

    private FieldworkPeriod parseFieldwordPeriod(final String fragment, final String defaultYear) {
        Pattern pattern = Pattern
                .compile("((\\d{1,2})(\\s+([A-Za-z]{3}))?\\s*[–-]\\s*)?(\\d{1,2})\\s+([A-Za-z]{3})(\\s+(\\d{4}))?");
        Matcher matcher = pattern.matcher(fragment);
        if (matcher.matches()) {
            String startDay = matcher.group(2);
            String startMonth = matcher.group(4);
            String endDay = matcher.group(5);
            String endMonth = matcher.group(6);
            String year = matcher.group(8);
            if (startDay == null) {
                startDay = endDay;
            }
            if (startMonth == null) {
                startMonth = endMonth;
            }
            if (year == null) {
                year = defaultYear;
            }
            String startDateStr = startDay + " " + startMonth + " " + year;
            String endDateStr = endDay + " " + endMonth + " " + year;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            return new FieldworkPeriod(DateMonthOrYear.of(startDate), DateMonthOrYear.of(endDate));
        } else {
            Pattern pattern2 = Pattern.compile("\\?\\s*[–-]\\s*(\\d{1,2})\\s+([A-Za-z]{3})(\\s+(\\d{4}))?");
            Matcher matcher2 = pattern2.matcher(fragment);
            if (matcher2.matches()) {
                String endDay = matcher2.group(1);
                String endMonth = matcher2.group(2);
                String year = matcher2.group(4);
                if (year == null) {
                    year = defaultYear;
                }
                String endDateStr = endDay + " " + endMonth + " " + year;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
                LocalDate endDate = LocalDate.parse(endDateStr, formatter);
                return new FieldworkPeriod(null, DateMonthOrYear.of(endDate));
            } else {
                String text = fragment.replaceAll("Mid[–-]", "").replaceAll("Late[–-]", "");
                Pattern monthPattern =
                        Pattern.compile("((([A-Za-z]{3})(\\s+(\\d{4}))?)?\\s*[–-]\\s*)?([A-Za-z]{3})(\\s+(\\d{4}))?");
                Matcher monthMatcher = monthPattern.matcher(text);
                if (monthMatcher.matches()) {
                    String startMonth = monthMatcher.group(3);
                    String startYear = monthMatcher.group(5);
                    String endMonth = monthMatcher.group(6);
                    String year = monthMatcher.group(8);
                    if (startMonth == null) {
                        startMonth = endMonth;
                    }
                    if (year == null) {
                        year = defaultYear;
                    }
                    if (startYear == null) {
                        startYear = year;
                    }
                    String startDateStr = startMonth + " " + startYear;
                    String endDateStr = endMonth + " " + year;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
                    YearMonth startDate = YearMonth.parse(startDateStr, formatter);
                    YearMonth endDate = YearMonth.parse(endDateStr, formatter);
                    return new FieldworkPeriod(DateMonthOrYear.of(startDate), DateMonthOrYear.of(endDate));
                }
            }
        }
        return null;
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
                int rs = cell.getRowspan();
                int cs = cell.getColspan();
                for (int k = 0; k < rs; k++) {
                    if (!map.containsKey(i + k)) {
                        map.add(i + k, ModifiableMap.empty());
                    }
                    for (int l = 0; l < cs; l++) {
                        map.get(i + k).add(j + l, cell);
                    }
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
