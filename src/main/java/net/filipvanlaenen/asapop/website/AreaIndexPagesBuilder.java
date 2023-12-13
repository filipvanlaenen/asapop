package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.analysis.HighestAveragesAllocation;
import net.filipvanlaenen.asapop.model.Election;
import net.filipvanlaenen.asapop.model.ElectionDate;
import net.filipvanlaenen.asapop.model.ElectionType;
import net.filipvanlaenen.asapop.model.Elections;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.asapop.model.ResultValue.Precision;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.asapop.model.Unit;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.ElectoralSystem;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.kolektoj.ModifiableCollection;
import net.filipvanlaenen.txhtmlj.BR;
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
    private static final double ONE_HUNDRED = 100D;
    /**
     * A map converting scopes to election types.
     */
    private static final Map<Scope, ElectionType> ELECTION_TYPES_BY_SCOPE = Map.of(Scope.EUROPEAN,
            ElectionType.EUROPEAN, Scope.NATIONAL, ElectionType.NATIONAL, Scope.PRESIDENTIAL_FIRST_ROUND, null);

    /**
     * The elections.
     */
    private final Elections elections;
    /**
     * A map with the opinion polls.
     */
    private final Map<String, OpinionPolls> opinionPollsMap;
    /**
     * Today's day.
     */
    private final LocalDate now;

    /**
     * Private record to carry the data resulting from creating a new opinion poll row.
     *
     * @param row                           The opinion poll row.
     * @param publicationDateFootnote       Whether a publication date footnote should be added.
     * @param instantSeatProjectionFootnote Whether an instant seat projection footnote should be added.
     */
    private record OpinionPollRowData(TR row, boolean publicationDateFootnote, boolean instantSeatProjectionFootnote) {
    };

    /**
     * Constructor taking the website configuration and the opinion polls map as its parameters.
     *
     * @param websiteConfiguration The website configuration.
     * @param opinionPollsMap      A map with all the opinion polls.
     * @param elections            The elections.
     * @param now                  Today's day.
     */
    AreaIndexPagesBuilder(final WebsiteConfiguration websiteConfiguration,
            final Map<String, OpinionPolls> opinionPollsMap, final Elections elections, final LocalDate now) {
        super(websiteConfiguration);
        this.opinionPollsMap = opinionPollsMap;
        this.elections = elections;
        this.now = now;
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
            boolean instantSeatProjectionFootnote = false;
            for (OpinionPoll opinionPoll : latestOpinionPolls) {
                OpinionPollRowData opinionPollRow =
                        createOpinionPollRow(largestElectoralListSets, opinionPoll, areaConfiguration);
                tBody.addElement(opinionPollRow.row);
                publicationDateFootnote |= opinionPollRow.publicationDateFootnote;
                instantSeatProjectionFootnote |= opinionPollRow.instantSeatProjectionFootnote;
            }
            if (instantSeatProjectionFootnote) {
                section.addElement(createFootnote("i", "instant-seat-projection"));
            }
            if (publicationDateFootnote) {
                section.addElement(createFootnote("p", "publication-date"));
            }
        }
    }

    /**
     * If there are polling firms that are not included for this area configuration, adds a list of them to the section.
     *
     * @param section           The section to add the polling firms that are not included to.
     * @param areaConfiguration The area configuration.
     */
    private void addPollingFirmsNotIncluded(final Section section, final AreaConfiguration areaConfiguration) {
        Map<String, String> pollingFirmsNotIncluded = areaConfiguration.getPollingFirmsNotIncluded();
        if (pollingFirmsNotIncluded == null || pollingFirmsNotIncluded.isEmpty()) {
            return;
        }
        P p = new P();
        p.addElement(new Span(" ").clazz(
                pollingFirmsNotIncluded.size() == 1 ? "polling-firm-not-included" : "polling-firms-not-included"));
        p.addContent(":");
        section.addElement(p);
        UL ul = new UL();
        List<String> pollingFirmsNotIncludedSorted = new ArrayList<String>(pollingFirmsNotIncluded.keySet());
        Collections.sort(pollingFirmsNotIncludedSorted);
        for (String pollingFirmNotIncluded : pollingFirmsNotIncludedSorted) {
            LI li = new LI();
            li.addElement(new Span(pollingFirmNotIncluded));
            li.addContent(": ");
            li.addElement(new Span(" ").clazz("polling-firm-not-included-reason-"
                    + camelCaseToKebabCase(pollingFirmsNotIncluded.get(pollingFirmNotIncluded))));
            ul.addElement(li);
        }
        section.addElement(ul);
    }

    /**
     * Adds the upcoming elections as an unordered list to a section, or none if there are none.
     *
     * @param section           The section to add the upcoming election to.
     * @param areaConfiguration The configuration for the area.
     */
    private void addUpcomingElections(final Section section, final AreaConfiguration areaConfiguration) {
        String areaCode = areaConfiguration.getAreaCode();
        List<LI> upcomingElectionLIs = new ArrayList<LI>();
        addUpcomingElectionLI(upcomingElectionLIs, areaCode, ElectionType.PRESIDENTIAL);
        addUpcomingElectionLI(upcomingElectionLIs, areaCode, ElectionType.NATIONAL);
        addUpcomingElectionLI(upcomingElectionLIs, areaCode, ElectionType.EUROPEAN);
        if (upcomingElectionLIs.isEmpty()) {
            addNoneParagraph(section);
        } else {
            UL ul = new UL();
            section.addElement(ul);
            for (LI li : upcomingElectionLIs) {
                ul.addElement(li);
            }
        }
    }

    /**
     * Adds the upcoming elections of a given type for an area as LI elements to the provided list.
     *
     * @param upcomingElectionLIs The list to add the upcoming elections to as LI elements.
     * @param areaCode            The area code.
     * @param electionType        The election type.
     */
    private void addUpcomingElectionLI(final List<LI> upcomingElectionLIs, final String areaCode,
            final ElectionType electionType) {
        Election nextElection = elections.getNextElection(areaCode, electionType, now);
        if (nextElection != null) {
            LI li = new LI();
            ElectionDate nextElectionDate = nextElection.getNextElectionDate(now);
            String qualifierClass = nextElectionDate.getQualifierTermKey();
            if (qualifierClass != null) {
                li.addElement(new Span(" ").clazz(qualifierClass));
                li.addContent(" ");
            }
            li.addContent(nextElectionDate.getDateString());
            li.addContent(": ");
            li.addElement(new Span(" ").clazz(electionType.getTermKey()));
            upcomingElectionLIs.add(li);
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
                ResultValue resultValue = opinionPoll.getResult(ElectoralList.getIds(electoralListSet));
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
     * Converts a string from camel case to kebab case.
     *
     * @param string The camel case string to be converted.
     * @return The string converted to kebab case.
     */
    private String camelCaseToKebabCase(final String string) {
        return string.replaceAll("([A-Z])", "-$1").toLowerCase();
    }

    /**
     * Converts a result value to a number of votes that can be used for an instant seat projection.
     *
     * @param resultValue The result value to convert.
     * @return A number of votes that can be used for an instant seat projection.
     */
    private long convertResultValueToNumberOfVotes(final ResultValue resultValue) {
        return Math.round(resultValue.getNominalValue() * ONE_HUNDRED);
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
        addPollingFirmsNotIncluded(section, areaConfiguration);
        body.addElement(createFooter());
        return html.asString();
    }

    /**
     * Creates a footnote.
     *
     * @param letter    The letter for the footnote.
     * @param spanClass The span class for the footnote.
     * @return A paragraph containing a footnote.
     */
    private P createFootnote(final String letter, final String spanClass) {
        P p = new P();
        Sup sup = new Sup();
        p.addElement(sup);
        sup.addElement(new I(letter));
        p.addElement(new Span(" ").clazz(spanClass));
        return p;
    }

    /**
     * Creates an opinion poll row.
     *
     * @param largestElectoralListSets A list with electoral list sets, sorted from largest to smallest.
     * @param opinionPoll              The opinion poll for which a row should be created.
     * @param areaConfiguration        The area configuration.
     * @return A record containing the row and related data.
     */
    private OpinionPollRowData createOpinionPollRow(final List<Set<ElectoralList>> largestElectoralListSets,
            final OpinionPoll opinionPoll, final AreaConfiguration areaConfiguration) {
        TR opinionPollRow = new TR();
        ElectionType electionType = ELECTION_TYPES_BY_SCOPE.get(opinionPoll.getScope());
        int numberOfSeats = 0;
        HighestAveragesAllocation allocation = null;
        if (electionType != null) {
            Election nextElection =
                    elections.getNextElection(areaConfiguration.getAreaCode(), electionType, opinionPoll.getEndDate());
            if (nextElection != null && nextElection.electionData() != null
                    && nextElection.electionData().getElectoralSystem() != null) {
                ElectoralSystem electoralSystem = nextElection.electionData().getElectoralSystem();
                numberOfSeats = electoralSystem.getNumberOfSeats();
                Double threshold = electoralSystem.getThreshold();
                ModifiableCollection<Long> votes = ModifiableCollection.empty();
                for (ResultValue resultValue : opinionPoll.getMainResponseScenario().getResults()) {
                    votes.add(convertResultValueToNumberOfVotes(resultValue));
                }
                allocation = new HighestAveragesAllocation(numberOfSeats, threshold, votes);
            }
        }
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
        boolean unitIsSeats = Unit.SEATS == opinionPoll.getUnit();
        Double other = ONE_HUNDRED;
        Double otherSeats = opinionPoll.getMainResponseScenario().getSumOfResultsAndOther();
        Double scale = opinionPoll.getScale();
        ResultValue.Precision precision =
                ResultValue.Precision.getHighestPrecision(opinionPoll.getMainResponseScenario().getResults());
        boolean instantSeatProjectionIncluded = false;
        for (Set<ElectoralList> electoralListSet : largestElectoralListSets) {
            ResultValue resultValue = opinionPoll.getResult(ElectoralList.getIds(electoralListSet));
            if (resultValue == null) {
                opinionPollRow.addElement(new TD("—").clazz("result-value-td"));
            } else if (unitIsSeats) {
                opinionPollRow.addElement(new TD(resultValue.getText()).clazz("result-value-td"));
                otherSeats -= resultValue.getNominalValue();
            } else {
                double displayValue = resultValue.getNominalValue() / scale;
                String text = (scale == 1D) ? resultValue.getText() : precision.getFormat().format(displayValue);
                other -= displayValue;
                int decimalPointIndex = text.indexOf(".");
                TD valueTd = null;
                if (decimalPointIndex == -1) {
                    valueTd = new TD(text + "%").clazz("result-value-td");
                } else {
                    valueTd = new TD().clazz("result-value-td");
                    valueTd.addContent(text.substring(0, decimalPointIndex));
                    valueTd.addElement(new Span(" ").clazz("decimal-point"));
                    valueTd.addContent(text.substring(decimalPointIndex + 1) + "%");
                }
                if (allocation != null) {
                    valueTd.addElement(new BR());
                    valueTd.addContent(
                            allocation.getNumberOfSeatsString(convertResultValueToNumberOfVotes(resultValue)));
                    Sup sup = new Sup();
                    valueTd.addElement(sup);
                    sup.addElement(new I("i"));
                    instantSeatProjectionIncluded = true;
                }
                opinionPollRow.addElement(valueTd);
            }
        }
        if (unitIsSeats) {
            TD valueTd = new TD("(" + Precision.ONE.getFormat().format(otherSeats) + ")").clazz("result-value-td");
            opinionPollRow.addElement(valueTd);
        } else {
            // TODO: Add other seats if allocation.
            String otherText = precision.getFormat().format(other);
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
        return new OpinionPollRowData(opinionPollRow, publicationDateUsed, instantSeatProjectionIncluded);
    }
}
