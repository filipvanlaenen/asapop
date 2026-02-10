package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPollTestBuilder;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.OpinionPollsStore;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResponseScenarioTestBuilder;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.ModifiableMap;

/**
 * Unit tests on the <code>StatisticsPageBuilder</code> class.
 */
public class StatisticsPageBuilderTest {
    /**
     * The magic number seven.
     */
    private static final int SEVEN = 7;
    /**
     * The magic number eight.
     */
    private static final int EIGHT = 8;
    /**
     * The magic number 0.2 (or slightly below).
     */
    private static final double UP_TO_0_2 = 0.2D;
    /**
     * The magic number 0.5 (or slightly below).
     */
    private static final double UP_TO_0_5 = 0.5D;
    /**
     * The magic number 0.8 (or slightly below).
     */
    private static final double UP_TO_0_8 = 0.7999D;
    /**
     * The magic number 0.95 (or slightly below).
     */
    private static final double UP_TO_0_95 = 0.95D;
    /**
     * A magic number slightly above 0.95.
     */
    private static final double ABOVE_0_95 = 0.9501D;
    /**
     * A magic number slightly above 1.
     */
    private static final double ABOVE_ONE = 1.0001D;
    /**
     * The magic number 1,234.
     */
    private static final int LARGE_INTEGER_1234 = 1234;
    /**
     * The magic number 2,345.
     */
    private static final int LARGE_INTEGER_2345 = 2345;
    /**
     * Today's date.
     */
    private static final LocalDate NOW = LocalDate.of(2022, Month.DECEMBER, 7);
    /**
     * A date to run the unit tests on.
     */
    private static final LocalDate DATE1 = LocalDate.parse("2021-12-28");
    /**
     * Another date to run the unit tests on.
     */
    private static final LocalDate DATE2 = LocalDate.parse("2022-06-29");

    /**
     * Creates an opinion polls map.
     *
     * @return An opinion polls map.
     */
    private Map<String, OpinionPolls> createOpinionPollsMap() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55").addCommissioner("The Times")
                .setPublicationDate(DATE1).build();
        ResponseScenario responseScenario1 = new ResponseScenarioTestBuilder().addResult("A", "56").build();
        poll1.addAlternativeResponseScenario(responseScenario1);
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "57").addResult("B", "56")
                .addCommissioner("The Post").setPublicationDate(DATE2).build();
        ResponseScenario responseScenario2 = new ResponseScenarioTestBuilder().addResult("A", "56").build();
        poll2.addAlternativeResponseScenario(responseScenario2);
        OpinionPoll poll3 = new OpinionPollTestBuilder().addResult("A", "55").addCommissioner("The Times")
                .setPublicationDate(DATE2).build();
        return Map.of("dk", new OpinionPolls(Set.of(poll1, poll2, poll3)), "ee",
                new OpinionPolls(Set.of(poll1, poll3)));
    }

    /**
     * Creates a website configuration.
     *
     * @return A website configuration.
     */
    private WebsiteConfiguration createWebsiteConfiguration() {
        WebsiteConfiguration websiteConfiguration = new WebsiteConfiguration();
        websiteConfiguration.setName("Test");
        AreaConfiguration sweden = new AreaConfiguration();
        sweden.setAreaCode("se");
        AreaConfiguration denmark = new AreaConfiguration();
        denmark.setAreaCode("dk");
        AreaConfiguration estonia = new AreaConfiguration();
        estonia.setAreaCode("ee");
        AreaConfiguration norway = new AreaConfiguration();
        norway.setAreaCode("no");
        websiteConfiguration.setAreaConfigurations(Set.of(denmark, estonia, sweden, norway, new AreaConfiguration()));
        return websiteConfiguration;
    }

    /**
     * Creates the internationalization dictionary.
     *
     * @return The internationalization dictionary.
     */
    private Internationalization createInternationalization() {
        Internationalization internationalization = new Internationalization();
        for (String areaCode : new String[] {"dk", "ee", "no", "se"}) {
            String key = "_area_" + areaCode;
            ModifiableMap<String, String> translations = ModifiableMap.<String, String>empty();
            for (Language language : Language.values()) {
                translations.put(language.getId(), areaCode + "-" + language.getId());
            }
            internationalization.addTranslations(key, translations);
        }
        return internationalization;
    }

    /**
     * Verifies that the statistics page is built correctly.
     */
    @Test
    public void statisticsPageIsBuiltCorrectly() throws IOException {
        Map<String, OpinionPolls> opinionPollsMap = createOpinionPollsMap();
        for (Entry<String, OpinionPolls> ops : opinionPollsMap) {
            for (OpinionPoll op : ops.value().getOpinionPolls()) {
                OpinionPollsStore.addAll(ops.key(), Collection.of(op));
            }
        }
        String expected = new String(
                getClass().getClassLoader().getResourceAsStream("StatisticsPageBuilderTest.html").readAllBytes());
        assertEquals(expected, new StatisticsPageBuilder(createWebsiteConfiguration(), createInternationalization(),
                opinionPollsMap, NOW).build().asString());
    }

    /**
     * Verifies that TD element is produced correctly for large numbers.
     */
    @Test
    public void shouldProduceTdElementForLargeNumbersWithThousandsSeparators() {
        assertEquals(
                "<td>2<span class=\"thousands-separator\"> </span>345"
                        + " (1<span class=\"thousands-separator\"> </span>234)</td>",
                StatisticsPageBuilder.createNumberAndYearToDateTd(LARGE_INTEGER_2345, LARGE_INTEGER_1234).asString());
    }

    /**
     * Verifies that if the most recent opinion poll was seven days ago, the currency qualification is up-to-date.
     */
    @Test
    public void mostRecentOpinionPollSevenDaysAgoShouldBeUpToDate() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.UP_TO_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(ABOVE_ONE, SEVEN));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * more than one, the currency qualification is out-of-date.
     */
    @Test
    public void moreThanOneOpinionPollPerDayShouldBeOutOfDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.OUT_OF_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(ABOVE_ONE, EIGHT));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * 0.2, the currency qualification is up-to-date.
     */
    @Test
    public void averageUpTo02OpinionPollsPerDayShouldBeUpToDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.UP_TO_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(UP_TO_0_2, EIGHT));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * 0.5, the currency qualification is probably up-to-date.
     */
    @Test
    public void averageUpYo05OpinionPollsPerDayShouldBeProbablyUpToDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.PROBABLY_UP_TO_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(UP_TO_0_5, EIGHT));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * 0.8, the currency qualification is possibly out-of-date.
     */
    @Test
    public void averageUpTo08OpinionPollsPerDayShouldBePossiblyOutOfToDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.POSSIBLY_OUT_OF_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(UP_TO_0_8, EIGHT));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * 0.95, the currency qualification is probably out-of-date.
     */
    @Test
    public void averageUpTo095OpinionPollsPerDayShouldBeProbablyOutOfToDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.PROBABLY_OUT_OF_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(UP_TO_0_95, EIGHT));
    }

    /**
     * Verifies that if the most recent opinion poll was eight days ago, and the number of opinion polls per days is
     * 0.96, the currency qualification is out-of-date.
     */
    @Test
    public void averageAbove095OpinionPollsPerDayShouldBeOutOfToDateAfterEightDays() {
        assertEquals(StatisticsPageBuilder.CurrencyQualification.OUT_OF_DATE,
                StatisticsPageBuilder.CurrencyQualification.calculateCurrencyQualification(ABOVE_0_95, EIGHT));
    }
}
