package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectionConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.H1;
import net.filipvanlaenen.txhtmlj.H2;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.I;
import net.filipvanlaenen.txhtmlj.LI;
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
import net.filipvanlaenen.txhtmlj.UL;

/**
 * Class building the index pages for all areas.
 */
class AreaIndexPagesBuilder extends PageBuilder {
    /**
     * Class representing an entry in the electoral calendar.
     */
    private final class Entry {
        /**
         * The election configuration for the entry.
         */
        private final ElectionConfiguration electionConfiguration;
        /**
         * The expected date.
         */
        private final ExpectedDate expectedDate;

        /**
         * Constructs an entry based on the election and area configuration.
         *
         * @param electionConfiguration The election configuration.
         */
        private Entry(final ElectionConfiguration electionConfiguration) {
            this.electionConfiguration = electionConfiguration;
            this.expectedDate = ExpectedDate.parse(electionConfiguration.getNextElectionDate());
        }

        /**
         * Returns the next election date.
         *
         * @return The next election date.
         */
        private ExpectedDate getNextElectionDate() {
            return expectedDate;
        }

        /**
         * Returns the type of the election as a class attribute value.
         *
         * @return The type of the election as a class attribute value.
         */
        private String getTypeAsClass() {
            return electionConfiguration.getType().toLowerCase().replaceAll(" ", "-");
        }
    }

    /**
     * The magic number seven.
     */
    private static final int SEVEN = 7;
    /**
     * The magic number ten.
     */
    private static final int TEN = 10;
    /**
     * The magic number 100.0.
     */
    private static final double ONE_HUNDRED = 100.0;
    /**
     * The decimal format symbols for the US.
     */
    private static final DecimalFormatSymbols US_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.US);
    /**
     * The decimal format for integers, i.e. no decimals.
     */
    private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("0", US_FORMAT_SYMBOLS);
    /**
     * The decimal format for one decimal digit.
     */
    private static final DecimalFormat ONE_DECIMAL_FORMAT = new DecimalFormat("0.0", US_FORMAT_SYMBOLS);
    /**
     * A map with the opinion polls.
     */
    private final Map<String, OpinionPolls> opinionPollsMap;

    /**
     * Constructor taking the website configuration and the opinion polls map as its parameters.
     *
     * @param websiteConfiguration The website configuration.
     * @param opinionPollsMap      A map with all the opinion polls.
     */
    AreaIndexPagesBuilder(final WebsiteConfiguration websiteConfiguration,
            final Map<String, OpinionPolls> opinionPollsMap) {
        super(websiteConfiguration);
        this.opinionPollsMap = opinionPollsMap;
    }

    /**
     * Adds a paragraph with None to the section.
     *
     * @param section The section to add None to.
     */
    private void addNoneParagraph(final Section section) {
        P p = new P();
        section.addElement(p);
        p.addElement(new Span(" ").clazz("none"));
        p.addContent(".");
    }

    /**
     * Adds the most recent opinion polls as a table to a section, or none if there are none.
     *
     * @param section           The section to add the upcoming election to.
     * @param areaConfiguration The configuration for the area.
     */
    private void addOpinionPolls(final Section section, final AreaConfiguration areaConfiguration) {
        OpinionPolls opinionPolls = opinionPollsMap.get(areaConfiguration.getAreaCode());
        if (opinionPolls == null || opinionPolls.getOpinionPolls().isEmpty()) {
            addNoneParagraph(section);
        } else {
            List<OpinionPoll> latestOpinionPolls = calculateLatestOpinionPolls(opinionPolls);
            Map<Set<ElectoralList>, String> electoralListSetAbbreviation = new HashMap<Set<ElectoralList>, String>();
            List<Set<ElectoralList>> largestElectoralListSets =
                    calculateLargestElectoralListSetsAndAbbreviations(electoralListSetAbbreviation, latestOpinionPolls);
            Table table = new Table().clazz("opinion-polls-table");
            section.addElement(table);
            THead tHead = new THead();
            table.addElement(tHead);
            TR tr = new TR();
            tHead.addElement(tr);
            tr.addElement(new TH(" ").clazz("fieldwork-period"));
            tr.addElement(new TH(" ").clazz("polling-firm-commissioner"));
            for (Set<ElectoralList> electoralListSet : largestElectoralListSets) {
                tr.addElement(new TH(electoralListSetAbbreviation.get(electoralListSet)).clazz("electoral-lists-th"));
            }
            tr.addElement(new TH(" ").clazz("other"));
            TBody tBody = new TBody();
            table.addElement(tBody);
            boolean publicationDateFootnote = false;
            for (OpinionPoll opinionPoll : latestOpinionPolls) {
                TR opinionPollRow = new TR();
                tBody.addElement(opinionPollRow);
                String start = "";
                if (opinionPoll.getFieldworkStart() != null) {
                    start = opinionPoll.getFieldworkStart().toString();
                }
                boolean publicationDateUsed = false;
                String end = "";
                if (opinionPoll.getFieldworkEnd() != null) {
                    end = opinionPoll.getFieldworkEnd().toString();
                } else if (opinionPoll.getPublicationDate() != null) {
                    end = opinionPoll.getPublicationDate().toString();
                    publicationDateUsed = true;
                    publicationDateFootnote = true;
                }
                String fieldworkPeriod = start.equals(end) ? start : start + " – " + end;
                TD fieldworkTd = new TD(fieldworkPeriod);
                if (publicationDateUsed) {
                    Sup sup = new Sup();
                    fieldworkTd.addElement(sup);
                    sup.addElement(new I("p"));
                }
                opinionPollRow.addElement(fieldworkTd);
                String pollingFirm = opinionPoll.getPollingFirm();
                List<String> commissioners = new ArrayList<String>(opinionPoll.getCommissioners());
                commissioners.sort(new Comparator<String>() {
                    @Override
                    public int compare(final String c1, final String c2) {
                        return c1.compareToIgnoreCase(c2);
                    }
                });
                String commissionerText = String.join("–", commissioners);
                if (pollingFirm == null) {
                    opinionPollRow.addElement(new TD(commissionerText));
                } else if (commissionerText.equals("")) {
                    opinionPollRow.addElement(new TD(pollingFirm));
                } else {
                    opinionPollRow.addElement(new TD(pollingFirm + " / " + commissionerText));
                }
                Double other = ONE_HUNDRED;
                ResultValue.Precision precision = ResultValue.Precision.ONE;
                for (Set<ElectoralList> electoralListSet : largestElectoralListSets) {
                    ResultValue resultValue = opinionPoll.getResult(ElectoralList.getKeys(electoralListSet));
                    if (resultValue == null) {
                        opinionPollRow.addElement(new TD("—").clazz("result-value-td"));
                    } else {
                        precision = ResultValue.Precision.highest(precision, resultValue.getPrecision());
                        other -= resultValue.getNominalValue();
                        String text = resultValue.getText();
                        int decimalPointIndex = text.indexOf(".");
                        if (decimalPointIndex == -1) {
                            opinionPollRow.addElement(new TD(text + "%").clazz("result-value-td"));
                        } else {
                            TD valueTd = new TD().clazz("result-value-td");
                            valueTd.addContent(text.substring(0, decimalPointIndex));
                            valueTd.addElement(new Span(" ").clazz("decimal-point"));
                            valueTd.addContent(text.substring(decimalPointIndex + 1) + "%");
                            opinionPollRow.addElement(valueTd);
                        }
                    }
                }
                String otherText = precision == ResultValue.Precision.ONE ? INTEGER_FORMAT.format(other)
                        : ONE_DECIMAL_FORMAT.format(other);
                if (otherText.equals("-0")) {
                    otherText = "0";
                }
                if (otherText.equals("-0.0")) {
                    otherText = "0.0";
                }
                int decimalPointIndex = otherText.indexOf(".");
                if (decimalPointIndex == -1) {
                    opinionPollRow.addElement(new TD("(" + otherText + "%)").clazz("result-value-td"));
                } else {
                    TD valueTd = new TD("(").clazz("result-value-td");
                    valueTd.addContent(otherText.substring(0, decimalPointIndex));
                    valueTd.addElement(new Span(" ").clazz("decimal-point"));
                    valueTd.addContent(otherText.substring(decimalPointIndex + 1) + "%)");
                    opinionPollRow.addElement(valueTd);
                }
            }
            if (publicationDateFootnote) {
                P p = new P();
                section.addElement(p);
                Sup sup = new Sup();
                p.addElement(sup);
                sup.addElement(new I("p"));
                p.addElement(new Span(" ").clazz("publication-date"));
            }
        }
    }

    /**
     * Adds the upcoming elections as an unordered list to a section, or none if there are none.
     *
     * @param section           The section to add the upcoming election to.
     * @param areaConfiguration The configuration for the area.
     */
    private void addUpcomingElections(final Section section, final AreaConfiguration areaConfiguration) {
        Set<ElectionConfiguration> electionConfigurations = areaConfiguration.getElectionConfigurations();
        if (electionConfigurations == null || electionConfigurations.isEmpty()) {
            addNoneParagraph(section);
        } else {
            UL ul = new UL();
            section.addElement(ul);
            List<Entry> entries = new ArrayList<Entry>();
            for (ElectionConfiguration electionConfiguration : electionConfigurations) {
                if (electionConfiguration.getNextElectionDate() != null) {
                    entries.add(new Entry(electionConfiguration));
                }
            }
            entries.sort(new Comparator<Entry>() {
                @Override
                public int compare(final Entry e1, final Entry e2) {
                    int dateResult = e1.getNextElectionDate().compareTo(e2.getNextElectionDate());
                    if (dateResult != 0) {
                        return dateResult;
                    } else {
                        return e1.getTypeAsClass().compareTo(e2.getTypeAsClass());
                    }
                }
            });
            for (Entry entry : entries) {
                LI li = new LI();
                ul.addElement(li);
                ExpectedDate nextElectionDate = entry.getNextElectionDate();
                if (nextElectionDate.isApproximate()) {
                    li.addElement(new Span(" ").clazz("around"));
                    li.addContent(" ");
                } else if (nextElectionDate.isDeadline()) {
                    li.addElement(new Span(" ").clazz("no-later-than"));
                    li.addContent(" ");
                }
                li.addContent(nextElectionDate.getDateString());
                li.addContent(": ");
                li.addElement(new Span(" ").clazz(entry.getTypeAsClass()));
            }
        }
    }

    /**
     * Builds the index pages for all areas.
     *
     * @return A map with the index pages for all areas.
     */
    Map<Path, String> build() {
        Map<Path, String> result = new HashMap<Path, String>();
        for (AreaConfiguration areaConfiguration : getAreaConfigurations()) {
            String areaCode = areaConfiguration.getAreaCode();
            if (areaCode != null) {
                result.put(Paths.get(areaCode, "index.html"), createAreaIndexPage(areaConfiguration));
            }
        }
        return result;
    }

    /**
     * Calculates which electoral lists sets are the largest, and stores their abbreviations.
     *
     * @param electoralListSetAbbreviation A map where the abbreviations for the electoral list sets can be stored.
     * @param latestOpinionPolls           The opinion polls for which the largest electoral list sets should be
     *                                     calculated.
     * @return A list with the largest electoral list sets.
     */
    private List<Set<ElectoralList>> calculateLargestElectoralListSetsAndAbbreviations(
            final Map<Set<ElectoralList>, String> electoralListSetAbbreviation,
            final List<OpinionPoll> latestOpinionPolls) {
        Map<Set<ElectoralList>, Double> electoralListSetMax = new HashMap<Set<ElectoralList>, Double>();
        for (OpinionPoll opinionPoll : latestOpinionPolls) {
            for (Set<ElectoralList> electoralListSet : opinionPoll.getMainResponseScenario().getElectoralListSets()) {
                ResultValue resultValue = opinionPoll.getResult(ElectoralList.getKeys(electoralListSet));
                Double nominalValue = resultValue.getNominalValue();
                // EQMU: Changing the conditional boundary below produces an equivalent mutant.
                if (!electoralListSetMax.containsKey(electoralListSet)
                        || nominalValue > electoralListSetMax.get(electoralListSet)) {
                    electoralListSetMax.put(electoralListSet, nominalValue);
                }
                List<String> abbreviations =
                        electoralListSet.stream().map(el -> el.getAbbreviation()).collect(Collectors.toList());
                Collections.sort(abbreviations);
                electoralListSetAbbreviation.put(electoralListSet, String.join("–", abbreviations));
            }
        }
        List<Set<ElectoralList>> sortedElectoralListSets =
                new ArrayList<Set<ElectoralList>>(electoralListSetMax.keySet());
        sortedElectoralListSets.sort(new Comparator<Set<ElectoralList>>() {
            @Override
            public int compare(final Set<ElectoralList> els1, final Set<ElectoralList> els2) {
                int maxComparison = electoralListSetMax.get(els2).compareTo(electoralListSetMax.get(els1));
                if (maxComparison == 0) {
                    return electoralListSetAbbreviation.get(els1).compareTo(electoralListSetAbbreviation.get(els2));
                } else {
                    return maxComparison;
                }
            }
        });
        int numberOfElectoralListSets = sortedElectoralListSets.size();
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        return sortedElectoralListSets.subList(0,
                numberOfElectoralListSets > SEVEN ? SEVEN : numberOfElectoralListSets);
    }

    /**
     * Calculates the latest opinion polls.
     *
     * @param opinionPolls The opinion polls.
     * @return The latest opinion polls.
     */
    private List<OpinionPoll> calculateLatestOpinionPolls(final OpinionPolls opinionPolls) {
        List<OpinionPoll> opinionPollList = new ArrayList<OpinionPoll>(opinionPolls.getOpinionPolls());
        opinionPollList.sort(new Comparator<OpinionPoll>() {
            @Override
            public int compare(final OpinionPoll op1, final OpinionPoll op2) {
                return op2.getEndDate().compareTo(op1.getEndDate());
            }
        });
        int numberOfOpinionPolls = opinionPollList.size();
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        return opinionPollList.subList(0, numberOfOpinionPolls > TEN ? TEN : numberOfOpinionPolls);
    }

    /**
     * Creates the index page for an area.
     *
     * @param areaConfiguration The configuration for the area.
     * @return The index page for an area.
     */
    String createAreaIndexPage(final AreaConfiguration areaConfiguration) {
        Html html = new Html();
        html.addElement(createHead(1));
        Body body = new Body().onload("initializeLanguage();");
        html.addElement(body);
        body.addElement(createHeader(1));
        Section section = new Section();
        body.addElement(section);
        section.addElement(new H1(" ").clazz("_area_" + areaConfiguration.getAreaCode()));
        section.addElement(new H2(" ").clazz("upcoming-elections"));
        addUpcomingElections(section, areaConfiguration);
        section.addElement(new H2(" ").clazz("latest-opinion-polls"));
        addOpinionPolls(section, areaConfiguration);
        body.addElement(createFooter());
        return html.asString();
    }

}
