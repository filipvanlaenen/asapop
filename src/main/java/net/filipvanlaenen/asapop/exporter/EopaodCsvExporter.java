package net.filipvanlaenen.asapop.exporter;

import java.util.ArrayList;
import java.util.List;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResultValue;

/**
 * Exporter to the EOPAOD CSV file format.
 */
public final class EopaodCsvExporter extends Exporter {
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
            return "\"" + text.replaceAll("\"",  "\"\"") + "\"";
        } else {
            return text;
        }
    }

    /**
     * Exports the opinion polls.
     *
     * @param opinionPolls The opinion polls to export.
     * @param area The area to filter opinion polls and response scenarios on.
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
        sb.append(",Other");
        for (OpinionPoll opinionPoll : sortOpinionPolls(opinionPolls.getOpinionPolls())) {
            String lines = export(opinionPoll, area, electoralListKeys);
            if (lines != null) {
                sb.append("\n");
                sb.append(lines);
            }
        }
        return sb.toString();
    }

    /**
     * Exports the opinion poll.
     *
     * @param opinionPoll The opinion poll to export.
     * @param area The area to filter opinion polls on.
     * @param electoralListKeys An array with the keys of the electoral lists to be exported.
     * @return A string containing the opinion poll in the CSV file format for EOPAOD.
     */
    static String export(final OpinionPoll opinionPoll, final String area, final String... electoralListKeys) {
        List<String> lines = new ArrayList<String>();
        if (areaMatches(area, opinionPoll.getArea())) {
            List<String> elements = new ArrayList<String>();
            elements.add(escapeCommasAndQuotes(opinionPoll.getPollingFirm()));
            elements.add(escapeCommasAndQuotes(emptyIfNull(exportCommissionners(opinionPoll))));
            elements.addAll(exportDates(opinionPoll));
            elements.add(notAvailableIfNull(exportScope(opinionPoll.getScope())));
            elements.add(notAvailableIfNull(opinionPoll.getSampleSize()));
            elements.add(opinionPoll.getSampleSize() == null ? "Not Available" : "Provided");
            elements.add("Not Available");
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
     * @param responseScenario The response scenario to export.
     * @param opinionPoll The opinion poll this response scenario relates to.
     * @param area The area to filter response scenarios on.
     * @param electoralListKeys An array with the keys of the electoral lists to be exported.
     * @return A string containing the response scenario in the PSV file format for EOPAOD.
     */
    static String export(final ResponseScenario responseScenario, final OpinionPoll opinionPoll, final String area,
                         final String... electoralListKeys) {
        if (!areaMatches(area, secondIfFirstNull(responseScenario.getArea(), opinionPoll.getArea()))) {
            return null;
        }
        List<String> elements = new ArrayList<String>();
        elements.add(escapeCommasAndQuotes(opinionPoll.getPollingFirm()));
        elements.add(escapeCommasAndQuotes(emptyIfNull(exportCommissionners(opinionPoll))));
        elements.addAll(exportDates(opinionPoll));
        elements.add(notAvailableIfNull(exportScope(secondIfFirstNull(responseScenario.getScope(),
                                                                      opinionPoll.getScope()))));
        String sampleSize = secondIfFirstNull(responseScenario.getSampleSize(), opinionPoll.getSampleSize());
        elements.add(notAvailableIfNull(sampleSize));
        elements.add(sampleSize == null ? "Not Available" : "Provided");
        elements.add("Not Available");
        elements.add(calculatePrecision(responseScenario, electoralListKeys) + "%");
        for (String electoralListKey : electoralListKeys) {
            elements.add(percentageOrNotAvailable(responseScenario.getResult(electoralListKey)));
        }
        elements.add(percentageOrNotAvailable(responseScenario.getOther()));
        return String.join(",", elements);
    }

    /**
     * Exports the scope to the terms used by the EOPAOD CSV format.
     *
     * @param scope The scope to convert.
     * @return A term used by EOPAOD CSV format for the scope.
     */
    private static String exportScope(final String scope) {
        if ("N".equals(scope)) {
            return "National";
        } else if ("E".equals(scope)) {
            return "European";
        } else {
            return null;
        }
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
        return s == null ? "Not Available" : s.getText() + "%";
    }
}
