package net.filipvanlaenen.asapop.website;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.asapop.model.Unit;
import net.filipvanlaenen.asapop.yaml.AreaConfiguration;
import net.filipvanlaenen.asapop.yaml.AreaSubdivisionConfiguration;
import net.filipvanlaenen.asapop.yaml.WebsiteConfiguration;
import net.filipvanlaenen.txhtmlj.Body;
import net.filipvanlaenen.txhtmlj.Head;
import net.filipvanlaenen.txhtmlj.Html;
import net.filipvanlaenen.txhtmlj.HttpEquivValue;
import net.filipvanlaenen.txhtmlj.Link;
import net.filipvanlaenen.txhtmlj.LinkTypeValue;
import net.filipvanlaenen.txhtmlj.Meta;
import net.filipvanlaenen.txhtmlj.Style;
import net.filipvanlaenen.txhtmlj.TBody;
import net.filipvanlaenen.txhtmlj.TD;
import net.filipvanlaenen.txhtmlj.TH;
import net.filipvanlaenen.txhtmlj.THead;
import net.filipvanlaenen.txhtmlj.TR;
import net.filipvanlaenen.txhtmlj.Table;

final class WidgetsBuilder {
    /**
     * The magic number fifty.
     */
    private static final int FIFTY = 50;
    private static final String[] STYLES = new String[] {
            "https://fonts.googleapis.com/css?family=Alegreya+Sans%3A400%2C700%2C400italic%2C700italic%2C600%2C600italic%7CAlegreya+Sans%3A400%2C500%2C600%2C700%2C400italic%2C700italic&ver=1",
            "https://europeelects.eu/wp-content/themes/chaplin/style.css?ver=2.6.7"};

    /**
     * A map with the opinion polls related to parliamentary elections.
     */
    private final Map<String, OpinionPolls> parliamentaryOpinionPollsMap;
    /**
     * The configuration for the website.
     */
    private final WebsiteConfiguration websiteConfiguration;

    WidgetsBuilder(final WebsiteConfiguration websiteConfiguration,
            final Map<String, OpinionPolls> parliamentaryOpinionPollsMap) {
        this.websiteConfiguration = websiteConfiguration;
        this.parliamentaryOpinionPollsMap = parliamentaryOpinionPollsMap;
    }

    Map<Path, String> build() {
        Map<Path, String> result = new HashMap<Path, String>();
        for (AreaConfiguration areaConfiguration : websiteConfiguration.getAreaConfigurations()) {
            String areaCode = areaConfiguration.getAreaCode();
            if (areaConfiguration.getCsvConfiguration() != null) {
                OpinionPolls opinionPolls = parliamentaryOpinionPollsMap.get(areaCode);
                List<OpinionPoll> latestOpinionPolls = calculateLatestOpinionPolls(opinionPolls);
                result.put(Paths.get("_widgets", "tables", areaCode + ".html"),
                        createHtmlTableFragment(latestOpinionPolls));
            }
            AreaSubdivisionConfiguration[] subdivisions = areaConfiguration.getSubdivsions();
            if (subdivisions != null) {
                for (AreaSubdivisionConfiguration subdivision : subdivisions) {
                    if (subdivision.getCsvConfiguration() != null) {
                        OpinionPolls opinionPolls = parliamentaryOpinionPollsMap.get(areaCode);
                        List<OpinionPoll> latestOpinionPolls = calculateLatestOpinionPolls(opinionPolls);
                        String subdivisionAreaCode = subdivision.getAreaCode();
                        result.put(Paths.get("_widgets", "tables", areaCode + "-" + subdivisionAreaCode + ".html"),
                                createHtmlTableFragment(latestOpinionPolls));
                    }
                }
            }
        }
        return result;
    }

    private String createHtmlTableFragment(final List<OpinionPoll> opinionPolls) {
        Html html = new Html();
        Head head = new Head();
        html.addElement(head);
        head.addElement(new Meta().httpEquiv(HttpEquivValue.CONTENT_TYPE).content("text/html; charset=UTF-8"));
        for (String style : STYLES) {
            head.addElement(new Link().rel(LinkTypeValue.STYLESHEET).href(style).type("text/css"));
        }
        head.addElement(new Style("body { background-color: #F9F9F9; }\n"
                + "table{font-family: Alegreya Sans,-apple-system,BlinkMacSystemFont,'Helvetica Neue',Helvetica,sans-serif;}\n"
                + "th{min-width:65px}"));
        Body body = new Body();
        html.addElement(body);
        Table table = new Table();
        body.addElement(table);
        THead tHead = new THead();
        table.addElement(tHead);
        TR tr = new TR();
        tHead.addElement(tr);
        tr.addElement(new TH("Fieldwork Period"));
        tr.addElement(new TH("Polling Firm"));
        tr.addElement(new TH("Commissioner(s)"));
        tr.addElement(new TH("Sample Size"));
        Map<Set<ElectoralList>, String> electoralListSetAbbreviation = new HashMap<Set<ElectoralList>, String>();
        List<Set<ElectoralList>> electoralListSets =
                calculateElectoralListSetsAndAbbreviations(electoralListSetAbbreviation, opinionPolls);
        for (Set<ElectoralList> electoralListSet : electoralListSets) {
            tr.addElement(new TH(electoralListSetAbbreviation.get(electoralListSet)));
        }
        tr.addElement(new TH("Other"));
        TBody tBody = new TBody();
        table.addElement(tBody);
        for (OpinionPoll opinionPoll : opinionPolls) {
            tBody.addElement(createOpinionPollRow(electoralListSets, opinionPoll));
        }
        return html.asString();
    }

    private List<Set<ElectoralList>> calculateElectoralListSetsAndAbbreviations(
            final Map<Set<ElectoralList>, String> electoralListSetAbbreviation, final List<OpinionPoll> opinionPolls) {
        Map<Set<ElectoralList>, Double> electoralListSetMax = new HashMap<Set<ElectoralList>, Double>();
        for (OpinionPoll opinionPoll : opinionPolls) {
            for (Set<ElectoralList> electoralListSet : opinionPoll.getMainResponseScenario().getElectoralListSets()) {
                ResultValue resultValue = opinionPoll.getResult(ElectoralList.getIds(electoralListSet));
                Double nominalValue = resultValue.getNominalValue();
                // EQMU: Changing the conditional boundary below produces an equivalent mutant.
                if (!electoralListSetMax.containsKey(electoralListSet)
                        || nominalValue > electoralListSetMax.get(electoralListSet)) {
                    electoralListSetMax.put(electoralListSet, nominalValue);
                }
                List<String> abbreviations = electoralListSet.stream()
                        .map(el -> el.getRomanizedAbbreviation() != null ? el.getRomanizedAbbreviation()
                                : el.getAbbreviation())
                        .collect(Collectors.toList());
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
        return sortedElectoralListSets;
    }

    private TR createOpinionPollRow(final List<Set<ElectoralList>> largestElectoralListSets,
            final OpinionPoll opinionPoll) {
        TR opinionPollRow = new TR();
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
        if (publicationDateUsed) {
            opinionPollRow.addElement(new TD(end + " (published)"));
        } else {
            opinionPollRow.addElement(new TD(fieldworkPeriod));
        }
        String pollingFirm = opinionPoll.getPollingFirm();
        if (pollingFirm == null) {
            opinionPollRow.addElement(new TD("—"));
        } else {
            opinionPollRow.addElement(new TD(pollingFirm));
        }
        List<String> commissioners = new ArrayList<String>(opinionPoll.getCommissioners());
        commissioners.sort(new Comparator<String>() {
            @Override
            public int compare(final String c1, final String c2) {
                return c1.compareToIgnoreCase(c2);
            }
        });
        String commissionerText = String.join("–", commissioners);
        if (commissionerText.equals("")) {
            opinionPollRow.addElement(new TD("—"));
        } else {
            opinionPollRow.addElement(new TD(commissionerText));
        }
        if (opinionPoll.getSampleSize() == null) {
            opinionPollRow.addElement(new TD("—"));
        } else {
            opinionPollRow.addElement(new TD(opinionPoll.getSampleSize().toString()));
        }
        boolean unitIsSeats = Unit.SEATS == opinionPoll.getUnit();
        Double otherSeats = opinionPoll.getMainResponseScenario().getSumOfResultsAndOther();
        Double scale = opinionPoll.getScale();
        ResultValue.Precision precision =
                ResultValue.Precision.getHighestPrecision(opinionPoll.getMainResponseScenario().getResults());
        for (Set<ElectoralList> electoralListSet : largestElectoralListSets) {
            ResultValue resultValue = opinionPoll.getResult(ElectoralList.getIds(electoralListSet));
            if (resultValue == null) {
                opinionPollRow.addElement(new TD("—"));
            } else if (unitIsSeats) {
                opinionPollRow.addElement(new TD(resultValue.getText()));
                otherSeats -= resultValue.getNominalValue();
            } else {
                double displayValue = resultValue.getNominalValue() / scale;
                String text = (scale == 1D) ? resultValue.getText() : precision.getFormat().format(displayValue);
                TD valueTd = null;
                valueTd = new TD(text + "%");
                opinionPollRow.addElement(valueTd);
            }
        }
        ResultValue other = opinionPoll.getOther();
        if (other == null) {
            opinionPollRow.addElement(new TD("—"));
        } else if (unitIsSeats) {
            opinionPollRow.addElement(new TD(other.getText()));
        } else {
            double displayValue = other.getNominalValue() / scale;
            String text = (scale == 1D) ? other.getText() : precision.getFormat().format(displayValue);
            TD valueTd = null;
            valueTd = new TD(text + "%");
            opinionPollRow.addElement(valueTd);
        }
        return opinionPollRow;
    }

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
        return opinionPollList.subList(0, numberOfOpinionPolls > FIFTY ? FIFTY : numberOfOpinionPolls);
    }

}
