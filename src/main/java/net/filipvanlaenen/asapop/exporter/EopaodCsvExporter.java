package net.filipvanlaenen.asapop.exporter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;

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
     * Exports the opinion polls.
     *
     * @param opinionPolls The opinion polls to export.
     * @param electoralListKeys An array with the keys for the electoral lists to be exported.
     * @return A string containing the opinion polls in the CSV file format for EOPAOD.
     */
    public static String export(final OpinionPolls opinionPolls, final String... electoralListKeys) {
        StringBuffer sb = new StringBuffer();
        sb.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        sb.append(",Sample Size Qualification,Participation,Precision");
        for (String key : electoralListKeys) {
            sb.append(",");
            sb.append(ElectoralList.get(key).getAbbreviation());
        }
        sb.append(",Other");
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
     * @return A string containing the opinion poll in the CSV file format for EOPAOD.
     */
    static String export(final OpinionPoll opinionPoll, final String... electoralListKeys) {
        List<String> elements = new ArrayList<String>();
        elements.add(opinionPoll.getPollingFirm());
        elements.add(notAvailableIfNull(exportCommissionners(opinionPoll)));
        elements.addAll(exportDates(opinionPoll));
        elements.add(notAvailableIfNull(opinionPoll.getScope()));
        elements.add(notAvailableIfNull(opinionPoll.getSampleSize()));
        elements.add("Not Available");
        elements.add("Not Available");
        elements.add(calculatePrecision(extractResults(opinionPoll, electoralListKeys)));
        for (String electoralListKey : electoralListKeys) {
            elements.add(notAvailableIfNull(opinionPoll.getResult(electoralListKey)));
        }
        elements.add(notAvailableIfNull(opinionPoll.getOther()));
        StringBuffer sb = new StringBuffer();
        sb.append(String.join(",", elements));
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
        elements.add(notAvailableIfNull(exportCommissionners(opinionPoll)));
        elements.addAll(exportDates(opinionPoll));
        if (responseScenario.getScope() == null) {
            elements.add(notAvailableIfNull(opinionPoll.getScope()));
        } else {
            elements.add(responseScenario.getScope());
        }
        elements.add(notAvailableIfNull(opinionPoll.getSampleSize()));
        elements.add("Not Available");
        elements.add("Not Available");
        elements.add(calculatePrecision(extractResults(responseScenario, electoralListKeys)));
        for (String electoralListKey : electoralListKeys) {
            elements.add(notAvailableIfNull(responseScenario.getResult(electoralListKey)));
        }
        elements.add(notAvailableIfNull(responseScenario.getOther()));
        return String.join(",", elements);
    }

    /**
     * Returns the string if it isn't null, and the string "Not Available" otherwise.
     *
     * @param s The string.
     * @return "Not Available" if the string is null, and otherwise the string as provided.
     */
    static String notAvailableIfNull(final String s) {
        return s == null ? "Not Available" : s;
    }
}
