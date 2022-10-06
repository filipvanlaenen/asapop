package net.filipvanlaenen.asapop.exporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResultValue;
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
     * @param opinionPolls      The opinion polls to export.
     * @param area              The area to filter opinion polls and response scenarios on.
     * @param electoralListKeys An array with the keys for the electoral lists to be exported.
     * @return A string containing the opinion polls in the CSV file format for EOPAOD.
     */
    public static String export(final OpinionPolls opinionPolls, final String area, final String... electoralListKeys) {
        StringBuffer sb = new StringBuffer();
        sb.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        sb.append(",Sample Size Qualification,Participation,Precision");
        for (String key : electoralListKeys) {
            sb.append(",");
            sb.append(ElectoralList.get(key).getAbbreviation());
        }
        sb.append(",Other\n");
        for (OpinionPoll opinionPoll : sortOpinionPolls(opinionPolls.getOpinionPolls())) {
            String lines = export(opinionPoll, area, electoralListKeys);
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
     * @param opinionPoll       The opinion poll to export.
     * @param area              The area to filter opinion polls on.
     * @param electoralListKeys An array with the keys of the electoral lists to be exported.
     * @return A string containing the opinion poll in the CSV file format for EOPAOD.
     */
    static String export(final OpinionPoll opinionPoll, final String area, final String... electoralListKeys) {
        List<String> lines = new ArrayList<String>();
        if (areaMatches(area, opinionPoll.getArea())) {
            List<String> elements = new ArrayList<String>();
            elements.add(escapeCommasAndQuotes(exportPollingFirms(opinionPoll)));
            elements.add(escapeCommasAndQuotes(emptyIfNull(exportCommissioners(opinionPoll))));
            elements.addAll(exportDates(opinionPoll));
            elements.add(notAvailableIfNull(exportScope(opinionPoll.getScope())));
            elements.add(notAvailableIfNull(opinionPoll.getSampleSize()));
            elements.add(opinionPoll.getSampleSize() == null ? "Not Available" : "Provided");
            elements.add(notAvailableIfNull(exportParticipationRatePercentage(opinionPoll)));
            elements.add(calculatePrecision(opinionPoll, electoralListKeys) + "%");
            for (String electoralListKey : electoralListKeys) {
                elements.add(percentageOrNotAvailable(opinionPoll.getResult(electoralListKey)));
            }
            elements.add(percentageOrNotAvailable(opinionPoll.getOther()));
            lines.add(String.join(",", elements));
        }
        for (ResponseScenario responseScenario : opinionPoll.getAlternativeResponseScenarios()) {
            String responseScenarioLine = export(responseScenario, opinionPoll, area, electoralListKeys);
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
     * @param responseScenario  The response scenario to export.
     * @param opinionPoll       The opinion poll this response scenario relates to.
     * @param area              The area to filter response scenarios on.
     * @param electoralListKeys An array with the keys of the electoral lists to be exported.
     * @return A string containing the response scenario in the PSV file format for EOPAOD.
     */
    static String export(final ResponseScenario responseScenario, final OpinionPoll opinionPoll, final String area,
            final String... electoralListKeys) {
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
        elements.add(notAvailableIfNull(exportParticipationRatePercentage(opinionPoll)));
        elements.add(calculatePrecision(responseScenario, electoralListKeys) + "%");
        for (String electoralListKey : electoralListKeys) {
            elements.add(percentageOrNotAvailable(responseScenario.getResult(electoralListKey)));
        }
        elements.add(percentageOrNotAvailable(responseScenario.getOther()));
        return String.join(",", elements);
    }

    /**
     * Exports the participation rate of the opinion poll as a percentage.
     *
     * @param opinionPoll The opinion poll for which to export the participation rate as a percentage.
     * @return A string representing the participation rate as a percentage.
     */
    private static String exportParticipationRatePercentage(final OpinionPoll opinionPoll) {
        String participationRate = exportParticipationRate(opinionPoll);
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
     * Returns the string with a percentage sign added if it isn't null, and the string "Not Available" otherwise.
     *
     * @param s The string.
     * @return "Not Available" if the string is null, and otherwise the string with a percentage sign added.
     */
    private static String percentageOrNotAvailable(final ResultValue s) {
        return s == null ? "Not Available" : s.getPrimitiveText() + "%";
    }
}
