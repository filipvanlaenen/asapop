package net.filipvanlaenen.asapop.website;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.array.ModifiableArrayCollection;
import net.filipvanlaenen.kolektoj.array.ModifiableOrderedArrayCollection;
import net.filipvanlaenen.txhtmlj.A;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.Div;
import net.filipvanlaenen.txhtmlj.H1;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.P;
import net.filipvanlaenen.txhtmlj.Section;
import net.filipvanlaenen.txhtmlj.Span;
import net.filipvanlaenen.txhtmlj.Sup;
import net.filipvanlaenen.txhtmlj.TBody;
import net.filipvanlaenen.txhtmlj.TD;
import net.filipvanlaenen.txhtmlj.TH;
import net.filipvanlaenen.txhtmlj.THead;
import net.filipvanlaenen.txhtmlj.TR;
import net.filipvanlaenen.txhtmlj.Table;

/**
 * Class building the page with statistics.
 */
final class StatisticsPageBuilder extends PageBuilder {
    /**
     * Enumeration modeling the qualification of currency.
     */
    enum CurrencyQualification {
        /**
         * Up-to-date.
         */
        UP_TO_DATE("■", "up-to-date", 0.8D),
        /**
         * Probably up-to-date.
         */
        PROBABLY_UP_TO_DATE("●", "probably-up-to-date", 0.5D),
        /**
         * Possibly out-of-date.
         */
        POSSIBLY_OUT_OF_DATE("●", "possibly-out-of-date", 0.2D),
        /**
         * Probably out-of-date.
         */
        PROBABLY_OUT_OF_DATE("▲", "probably-out-of-date", 0.05D),
        /**
         * Out-of-date.
         */
        OUT_OF_DATE("▲", "out-of-date", 0D);

        /**
         * The magic number seven.
         */
        private static final int SEVEN = 7;

        /**
         * The symbol.
         */
        private final String symbol;
        /**
         * The term.
         */
        private final String term;
        /**
         * The (lower) threshold.
         */
        private final double threshold;

        /**
         * Constructor taking the symbol, the span element class and the (lower) threshold as its parameters.
         *
         * @param symbol    The symbol.
         * @param term      The term.
         * @param threshold The (lower) threshold.
         */
        CurrencyQualification(final String symbol, final String term, final double threshold) {
            this.symbol = symbol;
            this.term = term;
            this.threshold = threshold;
        }

        /**
         * Creates a span element for the currency qualification.
         *
         * @return A span element for the currency qualification.
         */
        private Span createSpan() {
            return new Span(symbol).clazz(getColorClazz());
        }

        /**
         * Calculates the currency qualification using the number of opinion polls per day and the number of days since
         * the last opinion poll.
         *
         * @param numberOfOpinionPollsPerDay The number of opinion polls per day.
         * @param daysSinceLastOpinionPoll   The number of days since the last opinion poll.
         * @return The currency qualification.
         */
        static CurrencyQualification calculateCurrencyQualification(final double numberOfOpinionPollsPerDay,
                final long daysSinceLastOpinionPoll) {
            if (daysSinceLastOpinionPoll <= SEVEN) {
                return UP_TO_DATE;
            }
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            if (numberOfOpinionPollsPerDay > 1D) {
                return OUT_OF_DATE;
            }
            double probability = Math.pow(1D - numberOfOpinionPollsPerDay, daysSinceLastOpinionPoll - SEVEN);
            for (CurrencyQualification qualification : values()) {
                if (probability >= qualification.threshold) {
                    return qualification;
                }
            }
            // EQMU: Replacing the return value with null produces an equivalent mutant because the code is unreachable.
            return OUT_OF_DATE;
        }

        /**
         * Returns the class for the color.
         *
         * @return The class for the color.
         */
        String getColorClazz() {
            return term + "-color";
        }

        String getSymbol() {
            return symbol;
        }

        String getTerm() {
            return term;
        }
    }

    /**
     * The decimal format symbols for the US.
     */
    private static final DecimalFormatSymbols US_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.US);
    /**
     * The decimal format for integers, i.e. no decimals.
     */
    private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#,###", US_FORMAT_SYMBOLS);
    /**
     * The number of days in three years.
     */
    private static final int THREE_YEARS_AS_DAYS = 1 + 3 * 365;

    /**
     * Today's day.
     */
    private final LocalDate now;
    /**
     * A map with the opinion polls.
     */
    private final Map<String, OpinionPolls> opinionPollsMap;
    /**
     * The start of the year.
     */
    private final LocalDate startOfYear;
    /**
     * The terms.
     */
    private final Terms terms;

    /**
     * Constructor taking the website configuration and the map with the opinion polls as its parameter.
     *
     * @param websiteConfiguration The website configuration.
     * @param terms                The terms.
     * @param opinionPollsMap      The map with the opinion polls.
     * @param now                  Today's day.
     * @param startOfYear          The start of the year.
     */
    StatisticsPageBuilder(final WebsiteConfiguration websiteConfiguration, final Terms terms,
            final Map<String, OpinionPolls> opinionPollsMap, final LocalDate now, final LocalDate startOfYear) {
        super(websiteConfiguration);
        this.opinionPollsMap = opinionPollsMap;
        this.terms = terms;
        this.now = now;
        this.startOfYear = startOfYear;
    }

    /**
     * Builds the content of the statistics page.
     *
     * @return The content of the statistics page
     */
    Html build() {
        Html html = new Html();
        html.addElement(createHead());
        Body body = new Body().onload("initializeLanguage(); sortTable('statistics-table', 2, 'area-name',"
                + " 'alphanumeric-internationalized')");
        html.addElement(body);
        body.addElement(createHeader(PageBuilder.HeaderLink.STATISTICS));
        Section section = new Section();
        body.addElement(section);
        section.addElement(new H1(" ").clazz("statistics"));
        Table table = new Table().clazz("statistics-table").id("statistics-table");
        section.addElement(table);
        THead tHead = new THead();
        table.addElement(tHead);
        tHead.addElement(createTableHeaderRow());
        TBody tBody = new TBody();
        table.addElement(tBody);
        TR totalTr = new TR();
        tHead.addElement(totalTr);
        List<AreaConfiguration> sortedAreaConfigurations =
                getAreaConfigurations().stream().filter(ac -> ac.getAreaCode() != null).collect(Collectors.toList());
        sortedAreaConfigurations.sort(new Comparator<AreaConfiguration>() {
            @Override
            public int compare(final AreaConfiguration ac0, final AreaConfiguration ac1) {
                return ac0.getAreaCode().compareTo(ac1.getAreaCode());
            }
        });
        int totalNumberOfOpinionPolls = 0;
        int totalNumberOfOpinionPollsYtd = 0;
        int totalNumberOfResponseScenarios = 0;
        int totalNumberOfResponseScenariosYtd = 0;
        int totalNumberOfResultValues = 0;
        int totalNumberOfResultValuesYtd = 0;
        LocalDate totalMostRecentDate = LocalDate.EPOCH;
        int numberOfCurrencyQualifications = CurrencyQualification.values().length;
        List<CurrencyQualification> currencyQualifications = new ArrayList<CurrencyQualification>();
        long absent = 0L;
        ModifiableCollection<PieChart.Entry> numberOfOpinionPollsEntries =
                new ModifiableArrayCollection<PieChart.Entry>();
        ModifiableCollection<PieChart.Entry> numberOfOpinionPollsYtdEntries =
                new ModifiableArrayCollection<PieChart.Entry>();
        ModifiableCollection<PieChart.Entry> numberOfResponseScenariosEntries =
                new ModifiableArrayCollection<PieChart.Entry>();
        ModifiableCollection<PieChart.Entry> numberOfResponseScenariosYtdEntries =
                new ModifiableArrayCollection<PieChart.Entry>();
        ModifiableCollection<PieChart.Entry> numberOfResultValuesEntries =
                new ModifiableArrayCollection<PieChart.Entry>();
        ModifiableCollection<PieChart.Entry> numberOfResultValuesYtdEntries =
                new ModifiableArrayCollection<PieChart.Entry>();
        for (AreaConfiguration areaConfiguration : sortedAreaConfigurations) {
            String areaCode = areaConfiguration.getAreaCode();
            TR areaTr = new TR();
            String areaKey = "_area_" + areaCode;
            Optional<Term> term = terms.getTerms().stream().filter(t -> t.getKey().equals(areaKey)).findFirst();
            if (term.isPresent()) {
                Map<String, String> translations = term.get().getTranslations();
                for (Language language : Language.values()) {
                    String languageId = language.getId();
                    areaTr.data("area-name-" + languageId, translations.get(languageId));
                }
            }
            tBody.addElement(areaTr);
            TD tdAreaName = new TD();
            areaTr.addElement(tdAreaName);
            String areaClass = "_area_" + areaCode;
            String areaSymbol = areaCode.toUpperCase();
            tdAreaName.addElement(new A(" ").clazz(areaClass).href(areaCode + "/index.html"));
            if (opinionPollsMap.containsKey(areaCode)) {
                OpinionPolls opinionPolls = opinionPollsMap.get(areaCode);
                int numberOfOpinionPolls = opinionPolls.getNumberOfOpinionPolls();
                areaTr.data("number-of-opinion-polls", Integer.toString(numberOfOpinionPolls));
                numberOfOpinionPollsEntries.add(new PieChart.Entry(areaClass, areaSymbol, numberOfOpinionPolls, null));
                int numberOfOpinionPollsYtd = opinionPolls.getNumberOfOpinionPolls(startOfYear);
                areaTr.data("number-of-opinion-polls-ytd", Integer.toString(numberOfOpinionPollsYtd));
                numberOfOpinionPollsYtdEntries
                        .add(new PieChart.Entry(areaClass, areaSymbol, numberOfOpinionPollsYtd, null));
                int numberOfResponseScenarios = opinionPolls.getNumberOfResponseScenarios();
                areaTr.data("number-of-response-scenarios", Integer.toString(numberOfResponseScenarios));
                numberOfResponseScenariosEntries
                        .add(new PieChart.Entry(areaClass, areaSymbol, numberOfResponseScenarios, null));
                int numberOfResponseScenariosYtd = opinionPolls.getNumberOfResponseScenarios(startOfYear);
                areaTr.data("number-of-response-scenarios-ytd", Integer.toString(numberOfResponseScenariosYtd));
                numberOfResponseScenariosYtdEntries
                        .add(new PieChart.Entry(areaClass, areaSymbol, numberOfResponseScenariosYtd, null));
                int numberOfResultValues = opinionPolls.getNumberOfResultValues();
                areaTr.data("number-of-result-values", Integer.toString(numberOfResultValues));
                numberOfResultValuesEntries.add(new PieChart.Entry(areaClass, areaSymbol, numberOfResultValues, null));
                int numberOfResultValuesYtd = opinionPolls.getNumberOfResultValues(startOfYear);
                areaTr.data("number-of-result-values-ytd", Integer.toString(numberOfResultValuesYtd));
                numberOfResultValuesYtdEntries
                        .add(new PieChart.Entry(areaClass, areaSymbol, numberOfResultValuesYtd, null));
                LocalDate mostRecentDate = opinionPolls.getMostRecentDate();
                LocalDate threeYearBeforeMostRecentDate = mostRecentDate.minusDays(THREE_YEARS_AS_DAYS);
                int numberOfOpinionPollsLastThreeYears =
                        opinionPolls.getNumberOfOpinionPolls(threeYearBeforeMostRecentDate);
                double numberOfOpinionPollsPerDay = ((double) numberOfOpinionPollsLastThreeYears) / THREE_YEARS_AS_DAYS;
                long daysSinceLastOpinionPoll = ChronoUnit.DAYS.between(mostRecentDate, now);
                CurrencyQualification currencyQualification = CurrencyQualification
                        .calculateCurrencyQualification(numberOfOpinionPollsPerDay, daysSinceLastOpinionPoll);
                currencyQualifications.add(currencyQualification);
                totalNumberOfOpinionPolls += numberOfOpinionPolls;
                totalNumberOfOpinionPollsYtd += numberOfOpinionPollsYtd;
                totalNumberOfResponseScenarios += numberOfResponseScenarios;
                totalNumberOfResponseScenariosYtd += numberOfResponseScenariosYtd;
                totalNumberOfResultValues += numberOfResultValues;
                totalNumberOfResultValuesYtd += numberOfResultValuesYtd;
                totalMostRecentDate =
                        mostRecentDate.isAfter(totalMostRecentDate) ? mostRecentDate : totalMostRecentDate;
                areaTr.addElement(createNumberAndYearToDateTd(numberOfOpinionPolls, numberOfOpinionPollsYtd)
                        .clazz("statistics-value-td"));
                areaTr.addElement(createNumberAndYearToDateTd(numberOfResponseScenarios, numberOfResponseScenariosYtd)
                        .clazz("statistics-value-td"));
                areaTr.addElement(createNumberAndYearToDateTd(numberOfResultValues, numberOfResultValuesYtd)
                        .clazz("statistics-value-td"));
                TD mostRecentDateTd = new TD().clazz("statistics-value-td");
                mostRecentDateTd.addElement(currencyQualification.createSpan());
                mostRecentDateTd.addContent(" " + mostRecentDate.toString());
                areaTr.data("most-recent-date",
                        Integer.toString(numberOfCurrencyQualifications - currencyQualification.ordinal()) + "-"
                                + mostRecentDate.toString());
                areaTr.addElement(mostRecentDateTd);
            } else {
                areaTr.data("number-of-opinion-polls", "-1");
                areaTr.data("number-of-opinion-polls-ytd", "-1");
                areaTr.data("number-of-response-scenarios", "-1");
                areaTr.data("number-of-response-scenarios-ytd", "-1");
                areaTr.data("number-of-result-values", "-1");
                areaTr.data("number-of-result-values-ytd", "-1");
                areaTr.data("most-recent-date", "-1");
                areaTr.addElement(new TD("—").clazz("statistics-value-td"));
                areaTr.addElement(new TD("—").clazz("statistics-value-td"));
                areaTr.addElement(new TD("—").clazz("statistics-value-td"));
                areaTr.addElement(new TD("—").clazz("statistics-value-td"));
                absent++;
            }
        }
        totalTr.addElement(new TD(" ").clazz("total"));
        totalTr.addElement(createNumberAndYearToDateTd(totalNumberOfOpinionPolls, totalNumberOfOpinionPollsYtd)
                .clazz("statistics-total-td"));
        totalTr.addElement(
                createNumberAndYearToDateTd(totalNumberOfResponseScenarios, totalNumberOfResponseScenariosYtd)
                        .clazz("statistics-total-td"));
        totalTr.addElement(createNumberAndYearToDateTd(totalNumberOfResultValues, totalNumberOfResultValuesYtd)
                .clazz("statistics-total-td"));
        totalTr.addElement(new TD(totalMostRecentDate.toString()).clazz("statistics-total-td"));
        section.addElement(createCurrencyFootnote());
        section.addElement(createCurrencyCharts(currencyQualifications, absent));
        Div numberOfOpinionPollsCharts = new Div().clazz("two-svg-charts-container");
        numberOfOpinionPollsCharts.addElement(
                new PieChart("svg-chart-container-left", "number-of-opinion-polls", numberOfOpinionPollsEntries)
                        .getDiv());
        numberOfOpinionPollsCharts.addElement(
                new PieChart("svg-chart-container-right", "number-of-opinion-polls-ytd", numberOfOpinionPollsYtdEntries)
                        .getDiv());
        section.addElement(numberOfOpinionPollsCharts);
        Div numberOfResponseScenariosCharts = new Div().clazz("two-svg-charts-container");
        numberOfResponseScenariosCharts.addElement(new PieChart("svg-chart-container-left",
                "number-of-response-scenarios", numberOfResponseScenariosEntries).getDiv());
        numberOfResponseScenariosCharts.addElement(new PieChart("svg-chart-container-right",
                "number-of-response-scenarios-ytd", numberOfResponseScenariosYtdEntries).getDiv());
        section.addElement(numberOfResponseScenariosCharts);
        Div numberOfResultValusCharts = new Div().clazz("two-svg-charts-container");
        numberOfResultValusCharts.addElement(
                new PieChart("svg-chart-container-left", "number-of-result-values", numberOfResultValuesEntries)
                        .getDiv());
        numberOfResultValusCharts.addElement(
                new PieChart("svg-chart-container-right", "number-of-result-values-ytd", numberOfResultValuesYtdEntries)
                        .getDiv());
        section.addElement(numberOfResultValusCharts);
        body.addElement(createFooter());
        body.addElement(PieChart.createTooltipDiv());
        return html;
    }

    private Div createCurrencyCharts(final List<CurrencyQualification> currencyQualifications,
            final long noOpinionPolls) {
        Map<CurrencyQualification, Long> currencyQualificationsMap =
                currencyQualifications.stream().collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        ModifiableOrderedCollection<PieChart.Entry> entries = new ModifiableOrderedArrayCollection<PieChart.Entry>();
        for (CurrencyQualification cq : CurrencyQualification.values()) {
            if (currencyQualificationsMap.containsKey(cq)) {
                entries.add(new PieChart.Entry(cq.getTerm(), cq.getSymbol(), currencyQualificationsMap.get(cq),
                        cq.getColorClazz()));
            }
        }
        Div twoSvgChartsContainer = new Div().clazz("two-svg-charts-container");
        twoSvgChartsContainer.addElement(new PieChart("svg-chart-container-right", "currency", entries).getDiv());
        entries.add(new PieChart.Entry("no-opinion-polls", "–", noOpinionPolls, "no-opinion-polls"));
        twoSvgChartsContainer.addElement(new PieChart("svg-chart-container-left", "currency", entries).getDiv());
        return twoSvgChartsContainer;
    }

    /**
     * Creates the footnote about the currency.
     *
     * @return A P element with a footnote about the currency.
     */
    private P createCurrencyFootnote() {
        P footnote = new P().id("footnote-1");
        footnote.addElement(new Sup("1"));
        footnote.addContent(" ");
        footnote.addElement(new Span(" ").clazz("qualification-of-currency"));
        footnote.addContent(": ");
        footnote.addElement(CurrencyQualification.UP_TO_DATE.createSpan());
        footnote.addContent(" ");
        footnote.addElement(new Span(" ").clazz(CurrencyQualification.UP_TO_DATE.getTerm()));
        footnote.addContent(" (P ≥ 80 %), ");
        footnote.addElement(CurrencyQualification.PROBABLY_UP_TO_DATE.createSpan());
        footnote.addContent(" ");
        footnote.addElement(new Span(" ").clazz(CurrencyQualification.PROBABLY_UP_TO_DATE.getTerm()));
        footnote.addContent(" (80 % > P ≥ 50 %), ");
        footnote.addElement(CurrencyQualification.POSSIBLY_OUT_OF_DATE.createSpan());
        footnote.addContent(" ");
        footnote.addElement(new Span(" ").clazz(CurrencyQualification.POSSIBLY_OUT_OF_DATE.getTerm()));
        footnote.addContent(" (50 % > P ≥ 20 %), ");
        footnote.addElement(CurrencyQualification.PROBABLY_OUT_OF_DATE.createSpan());
        footnote.addContent(" ");
        footnote.addElement(new Span(" ").clazz(CurrencyQualification.PROBABLY_OUT_OF_DATE.getTerm()));
        footnote.addContent(" (20 % > P ≥ 5 %), ");
        footnote.addElement(CurrencyQualification.OUT_OF_DATE.createSpan());
        footnote.addContent(" ");
        footnote.addElement(new Span(" ").clazz(CurrencyQualification.OUT_OF_DATE.getTerm()));
        footnote.addContent(" (5 % > P).");
        return footnote;
    }

    /**
     * Creates a TD element with a number and its year-to-date number.
     *
     * @param number The number to be included in a TD elemement.
     * @param ytd    The year-to-date number to be included in a TD element.
     * @return A TD element with a number and its year-to-date number.
     */
    static TD createNumberAndYearToDateTd(final int number, final int ytd) {
        TD td = new TD();
        String text = INTEGER_FORMAT.format(number) + " (" + INTEGER_FORMAT.format(ytd) + ")";
        int thousandsSeparatorIndex = text.indexOf(",");
        while (thousandsSeparatorIndex != -1) {
            td.addContent(text.substring(0, thousandsSeparatorIndex));
            td.addElement(new Span(" ").clazz("thousands-separator"));
            text = text.substring(thousandsSeparatorIndex + 1);
            thousandsSeparatorIndex = text.indexOf(",");
        }
        td.addContent(text);
        return td;
    }

    /**
     * Creates the table header row.
     *
     * @return The table header row.
     */
    private TR createTableHeaderRow() {
        TR tr = new TR();
        tr.addElement(new TH(" ").clazz("country")
                .onclick("sortTable('statistics-table', 2, 'area-name', 'alphanumeric-internationalized')"));
        TH thOpinionPolls = new TH().clazz("number-of-opinion-polls-th");
        thOpinionPolls.addElement(new Span(" ").clazz("number-of-opinion-polls")
                .onclick("sortTable('statistics-table', 2, 'number-of-opinion-polls', 'numeric')"));
        thOpinionPolls.addContent(" (");
        thOpinionPolls.addElement(new Span(" ").clazz("year-to-date")
                .onclick("sortTable('statistics-table', 2, 'number-of-opinion-polls-ytd', 'numeric')"));
        thOpinionPolls.addContent(")");
        tr.addElement(thOpinionPolls);
        TH thResponseScenarios = new TH().clazz("number-of-response-scenarios-th");
        thResponseScenarios.addElement(new Span(" ").clazz("number-of-response-scenarios")
                .onclick("sortTable('statistics-table', 2, 'number-of-response-scenarios', 'numeric')"));
        thResponseScenarios.addContent(" (");
        thResponseScenarios.addElement(new Span(" ").clazz("year-to-date")
                .onclick("sortTable('statistics-table', 2, 'number-of-response-scenarios-ytd', 'numeric')"));
        thResponseScenarios.addContent(")");
        tr.addElement(thResponseScenarios);
        TH thResultValue = new TH().clazz("number-of-result-values-th");
        thResultValue.addElement(new Span(" ").clazz("number-of-result-values")
                .onclick("sortTable('statistics-table', 2, 'number-of-result-values', 'numeric')"));
        thResultValue.addContent(" (");
        thResultValue.addElement(new Span(" ").clazz("year-to-date")
                .onclick("sortTable('statistics-table', 2, 'number-of-result-values-ytd', 'numeric')"));
        thResultValue.addContent(")");
        tr.addElement(thResultValue);
        TH thMostRecentDate = new TH().clazz("most-recent-date-th");
        thMostRecentDate.addElement(new Span(" ").clazz("most-recent-date")
                .onclick("sortTable('statistics-table', 2, 'most-recent-date', 'alphanumeric')"));
        Sup footnoteLink = new Sup();
        footnoteLink.addElement(new A("1").href("#footnote-1"));
        thMostRecentDate.addElement(footnoteLink);
        tr.addElement(thMostRecentDate);
        return tr;
    }
}
