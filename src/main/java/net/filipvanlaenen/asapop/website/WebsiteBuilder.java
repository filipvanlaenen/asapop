package net.filipvanlaenen.asapop.website;

import java.time.LocalDate;

import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Class building the website.
 */
public class WebsiteBuilder {
    /**
     * The content of the base style sheet.
     */
    private final String baseStyleSheetContent;
    /**
     * The content of the custom style sheet.
     */
    private final String customStyleSheetContent;
    /**
     * The elections.
     */
    private final Elections elections;
    /**
     * The internationalization dictionary.
     */
    private final Internationalization internationalization;
    /**
     * The content of the navigation script.
     */
    private final String navigationScriptContent;
    /**
     * Today's day.
     */
    private final LocalDate now;
    /**
     * The map with the opinion polls related to parliamentary elections.
     */
    private final Map<String, OpinionPolls> parliamentaryOpinionPollsMap;
    /**
     * The map with the opinion polls related to presidential elections.
     */
    private final Map<String, OpinionPolls> presidentialOpinionPollsMap;
    /**
     * The content of the sorting script.
     */
    private final String sortingScriptContent;
    /**
     * The start of the year.
     */
    private final LocalDate startOfYear;
    /**
     * The tooptip script content.
     */
    private final String tooltipScriptContent;
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;

    /**
     * Constructor taking a website configuration, the internationalization terms and a map with the opinion polls as
     * its parameters.
     *
     * @param websiteConfiguration         The website configuration.
     * @param internationalization         The internationalization dictionary.
     * @param parliamentaryOpinionPollsMap The map with all the opinion polls related to parliamentary elections.
     * @param presidentialOpinionPollsMap  The map with all the opinion polls related to presidential elections.
     * @param elections                    The elections.
     * @param baseStyleSheetContent        The base style sheet content.
     * @param customStyleSheetContent      The custom style sheet content.
     * @param navigationScriptContent      The content of the navigation script.
     * @param sortingScriptContent         The content of the sorting script.
     * @param tooltipScriptContent         The content of the tooltip script.
     * @param now                          Today's date.
     */
    public WebsiteBuilder(final WebsiteConfiguration websiteConfiguration,
            final Internationalization internationalization,
            final Map<String, OpinionPolls> parliamentaryOpinionPollsMap,
            final Map<String, OpinionPolls> presidentialOpinionPollsMap, final Elections elections,
            final String baseStyleSheetContent, final String customStyleSheetContent,
            final String navigationScriptContent, final String sortingScriptContent, final String tooltipScriptContent,
            final LocalDate now) {
        this.websiteConfiguration = websiteConfiguration;
        this.internationalization = internationalization;
        this.parliamentaryOpinionPollsMap = parliamentaryOpinionPollsMap;
        this.presidentialOpinionPollsMap = presidentialOpinionPollsMap;
        this.elections = elections;
        this.baseStyleSheetContent = baseStyleSheetContent;
        this.customStyleSheetContent = customStyleSheetContent;
        this.navigationScriptContent = navigationScriptContent;
        this.sortingScriptContent = sortingScriptContent;
        this.tooltipScriptContent = tooltipScriptContent;
        this.now = now;
        this.startOfYear = now.withDayOfYear(1);
    }

    /**
     * Builds the website.
     *
     * @return The website.
     */
    public Website build() {
        Token token = Laconic.LOGGER.logMessage("Building the website.");
        Website website = new Website();
        JavaScriptsBuilder javaScriptsBuilder = new JavaScriptsBuilder(navigationScriptContent, sortingScriptContent,
                tooltipScriptContent, internationalization);
        website.putAll(javaScriptsBuilder.build());
        website.putAll(new StyleSheetsBuilder(baseStyleSheetContent, customStyleSheetContent).build());
        website.put("index.html", new IndexPageBuilder(websiteConfiguration, elections, now).build());
        website.put("calendar.html", new ElectoralCalendarPageBuilder(websiteConfiguration, elections, now).build());
        website.put("calendar.ical",
                new ICalendarFileBuilder(websiteConfiguration, elections, now, internationalization).build(token));
        website.put("csv.html", new CsvFilesPageBuilder(websiteConfiguration).build());
        website.put("statistics.html", new StatisticsPageBuilder(websiteConfiguration, internationalization,
                parliamentaryOpinionPollsMap, now, startOfYear).build());
        website.putAll(
                new CsvFilesBuilder(websiteConfiguration, parliamentaryOpinionPollsMap, presidentialOpinionPollsMap)
                        .build());
        website.putAll(new WidgetsBuilder(websiteConfiguration, parliamentaryOpinionPollsMap).build());
        website.putAll(
                new AreaIndexPagesBuilder(websiteConfiguration, parliamentaryOpinionPollsMap, elections, now).build());
        return website;
    }
}
