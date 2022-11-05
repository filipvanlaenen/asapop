package net.filipvanlaenen.asapop.website;

import java.util.Map;

import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;

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
     * The map with the opinion polls.
     */
    private final Map<String, OpinionPolls> opinionPollsMap;
    /**
     * The internationalization terms.
     */
    private final Terms terms;
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;

    /**
     * Constructor taking a website configuration, the internationalization terms and a map with the opinion polls as
     * its parameters.
     *
     * @param websiteConfiguration    The website configuration.
     * @param terms                   The internationalization terms.
     * @param opinionPollsMap         The map with all the opinion polls.
     * @param baseStyleSheetContent   The base style sheet content.
     * @param customStyleSheetContent The custom style sheet content.
     */
    public WebsiteBuilder(final WebsiteConfiguration websiteConfiguration, final Terms terms,
            final Map<String, OpinionPolls> opinionPollsMap, final String baseStyleSheetContent,
            final String customStyleSheetContent) {
        this.websiteConfiguration = websiteConfiguration;
        this.terms = terms;
        this.opinionPollsMap = opinionPollsMap;
        this.baseStyleSheetContent = baseStyleSheetContent;
        this.customStyleSheetContent = customStyleSheetContent;
    }

    /**
     * Builds the website.
     *
     * @return The website
     */
    public Website build() {
        Website website = new Website();
        website.put("index.html", new IndexPageBuilder(websiteConfiguration).build());
        website.put("calendar.html", new ElectoralCalendarPageBuilder(websiteConfiguration).build());
        website.put("csv.html", new CsvFilesPageBuilder(websiteConfiguration).build());
        CsvFilesBuilder csvFilesBuilder = new CsvFilesBuilder(websiteConfiguration, opinionPollsMap);
        website.putAll(csvFilesBuilder.build());
        JavaScriptsBuilder javaScriptsBuilder = new JavaScriptsBuilder(terms);
        website.putAll(javaScriptsBuilder.build());
        website.putAll(new StyleSheetsBuilder(baseStyleSheetContent, customStyleSheetContent).build());
        return website;
    }
}
