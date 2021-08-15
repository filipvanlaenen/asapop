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
     * @param electoralListKeys An array with the keys for the electoral lists to be exported.
     * @return A string containing the opinion polls in the PSV file format for EOPAOD.
     */
    public static String export(final OpinionPolls opinionPolls, final String... electoralListKeys) {
        StringBuffer sb = new StringBuffer();
        sb.append("Polling Firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        sb.append(" | Participation | Precision | ");
        sb.append(String.join(" | ", electoralListKeys));
        sb.append(" | Other");
        for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
            sb.append("\n");
            sb.append(export(opinionPoll, electoralListKeys));
        }
        return sb.toString();
    }

    /**
     * Exports the opinion poll.
     *
     * @param opinionPoll The opinion poll to export.
     * @param electoralListKeys An array with the keys of the electoral lists to be exported.
     * @return A string containing the opinion poll in the PSV file format for EOPAOD.
     */
    static String export(final OpinionPoll opinionPoll, final String... electoralListKeys) {
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
        StringBuffer sb = new StringBuffer();
        sb.append(String.join(" | ", elements));
        for (ResponseScenario responseScenario : opinionPoll.getAlternativeResponseScenarios()) {
            sb.append("\n");
            sb.append(export(responseScenario, opinionPoll, electoralListKeys));
        }
        return sb.toString();
    }

    /**
     * Exports the response scenario.
     *
     * @param responseScenario The response scenario to export.
     * @param opinionPoll The opinion poll this response scenario relates to.
     * @param electoralListKeys An array with the keys of the electoral lists to be exported.
     * @return A string containing the response scenario in the PSV file format for EOPAOD.
     */
    static String export(final ResponseScenario responseScenario, final OpinionPoll opinionPoll,
                         final String... electoralListKeys) {
        List<String> elements = new ArrayList<String>();
        elements.add(opinionPoll.getPollingFirm());
        elements.add(naIfNull(exportCommissionners(opinionPoll)));
        elements.addAll(exportDates(opinionPoll));
        if (responseScenario.getScope() == null) {
            elements.add(naIfNull(opinionPoll.getScope()));
        } else {
            elements.add(responseScenario.getScope());
        }
        elements.add(naIfNull(opinionPoll.getSampleSize()));
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
