package net.filipvanlaenen.asapop.exporter;

import java.util.ArrayList;
import java.util.List;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;

/**
 * Exporter to the EOPAOD PSV file format.
 */
public final class EopaodPsvExporter extends Exporter {
    /**
     * Private constructor.
     */
    private EopaodPsvExporter() {
    }

    /**
     * Exports the opinion polls.
     *
     * @param opinionPolls The opinion polls to export.
     * @param area The area to filter opinion polls and response scenarios on.
     * @param electoralListKeys An array with the keys for the electoral lists to be exported.
     * @return A string containing the opinion polls in the PSV file format for EOPAOD.
     */
    public static String export(final OpinionPolls opinionPolls, final String area, final String... electoralListKeys) {
        StringBuffer sb = new StringBuffer();
        sb.append("Polling Firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        sb.append(" | Participation | Precision | ");
        sb.append(String.join(" | ", electoralListKeys));
        sb.append(" | Other");
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
     * @return A string containing the opinion poll in the PSV file format for EOPAOD.
     */
    static String export(final OpinionPoll opinionPoll, final String area, final String... electoralListKeys) {
        List<String> lines = new ArrayList<String>();
        if (areaMatches(area, opinionPoll.getArea())) {
            List<String> elements = new ArrayList<String>();
            elements.add(opinionPoll.getPollingFirm());
            elements.add(naIfNull(exportCommissionners(opinionPoll)));
            elements.addAll(exportDates(opinionPoll));
            elements.add(naIfNull(opinionPoll.getScope()));
            elements.add(naIfNull(opinionPoll.getSampleSize()));
            elements.add("N/A");
            elements.add(calculatePrecision(opinionPoll, electoralListKeys));
            for (String electoralListKey : electoralListKeys) {
                elements.add(naIfNull(opinionPoll.getResult(electoralListKey)));
            }
            elements.add(naIfNull(opinionPoll.getOther()));
            lines.add(String.join(" | ", elements));
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
        elements.add(opinionPoll.getPollingFirm());
        elements.add(naIfNull(exportCommissionners(opinionPoll)));
        elements.addAll(exportDates(opinionPoll));
        elements.add(naIfNull(secondIfFirstNull(responseScenario.getScope(), opinionPoll.getScope())));
        elements.add(naIfNull(secondIfFirstNull(responseScenario.getSampleSize(), opinionPoll.getSampleSize())));
        elements.add("N/A");
        elements.add(calculatePrecision(responseScenario, electoralListKeys));
        for (String electoralListKey : electoralListKeys) {
            elements.add(naIfNull(responseScenario.getResult(electoralListKey)));
        }
        elements.add(naIfNull(responseScenario.getOther()));
        return String.join(" | ", elements);
    }

    /**
     * Returns the string if it isn't null, and the string "N/A" otherwise.
     *
     * @param s The string.
     * @return "N/A" if the string is null, and otherwise the string as provided.
     */
    private static String naIfNull(final String s) {
        return s == null ? "N/A" : s;
    }
}
