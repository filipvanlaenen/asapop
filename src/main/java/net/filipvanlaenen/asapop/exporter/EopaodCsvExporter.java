package net.filipvanlaenen.asapop.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.asapop.model.ResultValue.Precision;
import net.filipvanlaenen.asapop.model.Scope;

/**
 * Exporter to the EOPAOD CSV file format.
 */
public final class EopaodCsvExporter extends Exporter {
    /**
     * A map mapping scopes to CSV values.
     */
    static final Map<Scope, String> SCOPE_TO_PSV_STRING = Map.of(Scope.European, "European", Scope.National, "National",
            Scope.PresidentialFirstRound, "Presidential");

    /**
     * Private constructor.
     */
    private EopaodCsvExporter() {
    }

    /**
     * Returns the string if it isn't null, and the empty string otherwise.
     *
     * @param s The string.
     * @return The empty string if the string is null, and otherwise the string as provided.
     */
    private static String emptyIfNull(final String s) {
        return s == null ? "" : s;
    }

    /**
     * Escapes commas and quotes in a text block.
     *
     * @param text The text to process.
     * @return The original text properly escaped for commas and quotes according to the CSV standard.
     */
    private static String escapeCommasAndQuotes(final String text) {
        if (text.contains(",") || text.contains("\"")) {
            return "\"" + text.replaceAll("\"", "\"\"") + "\"";
        } else {
            return text;
        }
    }

    /**
     * Exports the opinion polls.
     *
     * @param opinionPolls         The opinion polls to export.
     * @param area                 The area to filter opinion polls and response scenarios on.
     * @param electoralListKeySets A list with the sets of keys for the electoral lists to be exported.
     * @return A string containing the opinion polls in the CSV file format for EOPAOD.
     */
    public static String export(final OpinionPolls opinionPolls, final String area,
            final List<Set<String>> electoralListKeySets) {
        StringBuffer sb = new StringBuffer();
        sb.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        sb.append(",Sample Size Qualification,Participation,Precision");
        for (Set<String> electoralListKeySet : electoralListKeySets) {
            sb.append(",");
            Set<ElectoralList> electoralLists = ElectoralList.get(electoralListKeySet);
            List<String> sortedKeys = new ArrayList<String>(electoralLists.stream()
                    .map(electoralList -> electoralList.getRomanizedAbbreviation() == null
                            ? electoralList.getAbbreviation()
                            : electoralList.getRomanizedAbbreviation())
                    .collect(Collectors.toList()));
            Collections.sort(sortedKeys);
            sb.append(String.join("+", sortedKeys));
        }
        sb.append(",Other\n");
        for (OpinionPoll opinionPoll : sortOpinionPolls(opinionPolls.getOpinionPolls())) {
            String lines = export(opinionPoll, area, electoralListKeySets);
            if (lines != null) {
                sb.append(lines);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Exports the opinion poll.
     *
     * @param opinionPoll          The opinion poll to export.
     * @param area                 The area to filter opinion polls on.
     * @param electoralListKeySets A list with the sets of keys of the electoral lists to be exported.
     * @return A string containing the opinion poll in the CSV file format for EOPAOD.
     */
    static String export(final OpinionPoll opinionPoll, final String area,
            final List<Set<String>> electoralListKeySets) {
        List<String> lines = new ArrayList<String>();
        if (areaMatches(area, opinionPoll.getArea())) {
            List<String> elements = new ArrayList<String>();
            elements.add(escapeCommasAndQuotes(emptyIfNull(exportPollingFirms(opinionPoll))));
            elements.add(escapeCommasAndQuotes(emptyIfNull(exportCommissioners(opinionPoll))));
            elements.addAll(exportDates(opinionPoll));
            elements.add(notAvailableIfNull(exportScope(opinionPoll.getScope())));
            elements.add(notAvailableIfNull(opinionPoll.getSampleSize()));
            elements.add(opinionPoll.getSampleSize() == null ? "Not Available" : "Provided");
            elements.add(notAvailableIfNull(exportParticipationRatePercentage(opinionPoll)));
            Precision precision = calculatePrecision(opinionPoll, electoralListKeySets);
            elements.add(precision + "%");
            Double scale = opinionPoll.getScale();
            for (Set<String> electoralListKeySet : electoralListKeySets) {
                elements.add(percentageOrNotAvailable(opinionPoll.getResult(electoralListKeySet), precision, scale));
            }
            elements.add(percentageOrNotAvailable(opinionPoll.getOther(), precision, scale));
            lines.add(String.join(",", elements));
        }
        for (ResponseScenario responseScenario : opinionPoll.getAlternativeResponseScenarios()) {
            String responseScenarioLine = export(responseScenario, opinionPoll, area, electoralListKeySets);
            if (responseScenarioLine != null) {
                lines.add(responseScenarioLine);
            }
        }
        if (lines.isEmpty()) {
            return null;
        } else {
            return String.join("\n", lines);
        }
    }

    /**
     * Exports the response scenario.
     *
     * @param responseScenario     The response scenario to export.
     * @param opinionPoll          The opinion poll this response scenario relates to.
     * @param area                 The area to filter response scenarios on.
     * @param electoralListKeySets A list with the sets of keys of the electoral lists to be exported.
     * @return A string containing the response scenario in the PSV file format for EOPAOD.
     */
    static String export(final ResponseScenario responseScenario, final OpinionPoll opinionPoll, final String area,
            final List<Set<String>> electoralListKeySets) {
        if (!areaMatches(area, secondIfFirstNull(responseScenario.getArea(), opinionPoll.getArea()))) {
            return null;
        }
        List<String> elements = new ArrayList<String>();
        elements.add(escapeCommasAndQuotes(exportPollingFirms(opinionPoll)));
        elements.add(escapeCommasAndQuotes(emptyIfNull(exportCommissioners(opinionPoll))));
        elements.addAll(exportDates(opinionPoll));
        elements.add(notAvailableIfNull(
                exportScope(secondIfFirstNull(responseScenario.getScope(), opinionPoll.getScope()))));
        String sampleSize = secondIfFirstNull(responseScenario.getSampleSize(), opinionPoll.getSampleSize());
        elements.add(notAvailableIfNull(sampleSize));
        elements.add(sampleSize == null ? "Not Available" : "Provided");
        elements.add(notAvailableIfNull(exportParticipationRatePercentage(responseScenario, opinionPoll)));
        Precision precision = calculatePrecision(responseScenario, electoralListKeySets);
        elements.add(precision + "%");
        Double scale = responseScenario.getScale();
        for (Set<String> electoralListKeySet : electoralListKeySets) {
            elements.add(percentageOrNotAvailable(responseScenario.getResult(electoralListKeySet), precision, scale));
        }
        elements.add(percentageOrNotAvailable(responseScenario.getOther(), precision, scale));
        return String.join(",", elements);
    }

    /**
     * Exports the participation rate of the opinion poll as a percentage.
     *
     * @param opinionPoll The opinion poll for which to export the participation rate as a percentage.
     * @return A string representing the participation rate as a percentage.
     */
    private static String exportParticipationRatePercentage(final OpinionPoll opinionPoll) {
        String participationRate = exportParticipationRate(opinionPoll.getMainResponseScenario(), opinionPoll);
        if (participationRate == null) {
            return null;
        } else {
            return participationRate + "%";
        }
    }

    /**
     * Exports the participation rate of the response scenario as a percentage.
     *
     * @param responseScenario The response scenario for which to export the participation rate as a percentage.
     * @param opinionPoll      The opinion poll for which to export the participation rate as a percentage.
     * @return A string representing the participation rate as a percentage.
     */
    private static String exportParticipationRatePercentage(final ResponseScenario responseScenario,
            final OpinionPoll opinionPoll) {
        String participationRate = exportParticipationRate(responseScenario, opinionPoll);
        if (participationRate == null) {
            return null;
        } else {
            return participationRate + "%";
        }
    }

    /**
     * Exports the scope to the terms used by the EOPAOD PSV format.
     *
     * @param scope The scope to convert.
     * @return A term used by EOPAOD PSV format for the scope.
     */
    private static String exportScope(final Scope scope) {
        return scope == null ? null : SCOPE_TO_PSV_STRING.get(scope);
    }

    /**
     * Returns the string if it isn't null, and the string "Not Available" otherwise.
     *
     * @param s The string.
     * @return "Not Available" if the string is null, and otherwise the string as provided.
     */
    private static String notAvailableIfNull(final String s) {
        return s == null ? "Not Available" : s;
    }

    /**
     * Returns the result value with a percentage sign added if it isn't null, and the string "Not Available" otherwise.
     *
     * @param resultValue The result value.
     * @param precision   The precision to be used.
     * @param scale       The scale to be used.
     * @return "Not Available" if the string is null, and otherwise the string with a percentage sign added.
     */
    private static String percentageOrNotAvailable(final ResultValue resultValue, final Precision precision,
            final Double scale) {
        if (resultValue == null) {
            return "Not Available";
        } else if (scale == 1D) {
            return resultValue.getPrimitiveText() + "%";
        } else {
            return precision.getFormat().format(resultValue.getNominalValue() / scale) + "%";
        }
    }
}
