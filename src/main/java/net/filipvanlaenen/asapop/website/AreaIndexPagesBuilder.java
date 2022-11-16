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
     * A map with the opinion polls.
     */
    private final Map<String, OpinionPolls> opinionPollsMap;
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat INTEGER = new DecimalFormat("0", symbols);
    private static final DecimalFormat ONE_DECIMAL = new DecimalFormat("0.0", symbols);

    AreaIndexPagesBuilder(final WebsiteConfiguration websiteConfiguration,
            final Map<String, OpinionPolls> opinionPollsMap) {
        this.websiteConfiguration = websiteConfiguration;
        this.opinionPollsMap = opinionPollsMap;
    }

    Map<Path, String> build() {
        Map<Path, String> result = new HashMap<Path, String>();
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            String areaCode = areaConfiguration.getAreaCode();
            if (areaCode != null) {
                result.put(Paths.get(areaCode, "index.html"), createAreaIndexPage(areaConfiguration));
            }
        }
        return result;
    }

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
        Set<ElectionConfiguration> electionConfigurations = areaConfiguration.getElectionConfigurations();
        if (electionConfigurations == null || electionConfigurations.isEmpty()) {
            P p = new P();
            section.addElement(p);
            p.addElement(new Span(" ").clazz("none"));
            p.addContent(".");
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
        section.addElement(new H2(" ").clazz("latest-opinion-polls"));
        OpinionPolls opinionPolls = opinionPollsMap.get(areaConfiguration.getAreaCode());
        if (opinionPolls == null || opinionPolls.getOpinionPolls().isEmpty()) {
            P p = new P();
            section.addElement(p);
            p.addElement(new Span(" ").clazz("none"));
            p.addContent(".");
        } else {
            List<OpinionPoll> opinionPollList = new ArrayList<OpinionPoll>(opinionPolls.getOpinionPolls());
            opinionPollList.sort(new Comparator<OpinionPoll>() {
                @Override
                public int compare(OpinionPoll op1, OpinionPoll op2) {
                    return op2.getEndDate().compareTo(op1.getEndDate());
                }
            });
            int numberOfOpinionPolls = opinionPollList.size();
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            List<OpinionPoll> latestOpinionPolls =
                    opinionPollList.subList(0, numberOfOpinionPolls > 10 ? 10 : numberOfOpinionPolls);
            Map<Set<ElectoralList>, Double> electoralListSetMax = new HashMap<Set<ElectoralList>, Double>();
            Map<Set<ElectoralList>, String> electoralListSetAbbreviation = new HashMap<Set<ElectoralList>, String>();
            for (OpinionPoll opinionPoll : latestOpinionPolls) {
                for (Set<ElectoralList> electoralListSet : opinionPoll.getMainResponseScenario()
                        .getElectoralListSets()) {
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
                public int compare(Set<ElectoralList> els1, Set<ElectoralList> els2) {
                    int maxComparison = electoralListSetMax.get(els2).compareTo(electoralListSetMax.get(els1));
                    if (maxComparison == 0) {
                        return electoralListSetAbbreviation.get(els1).compareTo(electoralListSetAbbreviation.get(els2));
                    } else {
                        return maxComparison;
                    }
                }
            });
            int numberOfElectoralListColumns = sortedElectoralListSets.size();
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            if (numberOfElectoralListColumns > 7) {
                numberOfElectoralListColumns = 7;
            }
            List<Set<ElectoralList>> largestElectoralListSets =
                    sortedElectoralListSets.subList(0, numberOfElectoralListColumns);
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
                    public int compare(String c1, String c2) {
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
                Double other = 100.0;
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
                String otherText =
                        precision == ResultValue.Precision.ONE ? INTEGER.format(other) : ONE_DECIMAL.format(other);
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
        body.addElement(createFooter());
        return html.asString();
    }
}
