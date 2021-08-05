package net.filipvanlaenen.asapop.exporter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;

/**
 * Exporter to the EOPAOD PSV file format.
 */
public class EopaodPsvExporter {
    /**
     * Exports the opinion polls.
     *
     * @param opinionPolls The opinion polls to export.
     * @param electoralListKeys An array with the keys for the electoral lists to be exported.
     * @return A string containing the opinion polls in the PSV file format for EOPAOD.
     */
    public static String export(final OpinionPolls opinionPolls, final String... electoralListKeys) {
        StringBuffer sb = new StringBuffer();
        sb.append("Polling firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        sb.append(" | Participation | Precision | ");
        sb.append(String.join(" | ", electoralListKeys));
        sb.append(" | Other");
        for (OpinionPoll opinionPoll : opinionPolls.getOpinionPollsList()) {
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
        elements.add(opinionPoll.getCommissioners().isEmpty() ? "N/A" : String.join(", ", opinionPoll.getCommissioners()));
        elements.addAll(exportDates(opinionPoll));
        elements.add(naIfNull(opinionPoll.getScope()));
        elements.add(naIfNull(opinionPoll.getSampleSize()));
        elements.add("N/A");
        elements.add("N/A");
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
     * Exports the response scenario as a string in the PSV file format for EOPAOD.
     *
     * @param opinionPoll The opinion poll this response scenario relates to.
     * @param electoralListKeys An array with the keys of the electoral lists to be exported.
     * @return A string containing the response scenario in the PSV file format for EOPAOD.
     */
    static String export(final ResponseScenario responseScenario, final OpinionPoll opinionPoll, final String... electoralListKeys) {
        List<String> elements = new ArrayList<String>();
        elements.add(opinionPoll.getPollingFirm());
        elements.add(opinionPoll.getCommissioners().isEmpty() ? "N/A" : String.join(", ", opinionPoll.getCommissioners()));
        elements.addAll(exportDates(opinionPoll));
        elements.add(responseScenario.getScope() == null ? naIfNull(opinionPoll.getScope()) : responseScenario.getScope());
        elements.add(naIfNull(opinionPoll.getSampleSize()));
        elements.add("N/A");
        elements.add("N/A");
        for (String electoralListKey : electoralListKeys) {
            elements.add(naIfNull(responseScenario.getResult(electoralListKey)));
        }
        elements.add(naIfNull(responseScenario.getOther()));
        return String.join(" | ", elements);
    }

    /**
     * Exports the dates from the opinion poll to two data elements.
     *
     * @param opinionPoll The opinion poll to export the dates from.
     * @return A list with two elements containing the dates for the EOPAOD PSV file.
     */
    private static List<String> exportDates(final OpinionPoll opinionPoll) {
        List<String> elements = new ArrayList<String>();
        LocalDate fieldworkStart = opinionPoll.getFieldworkStart();
        LocalDate fieldworkEnd = opinionPoll.getFieldworkEnd();
        LocalDate publicationDate = opinionPoll.getPublicationDate();
        if (fieldworkStart == null) {
            if (fieldworkEnd == null) {
                elements.add(publicationDate.toString());
                elements.add(publicationDate.toString());
            } else {
                elements.add(fieldworkEnd.toString());
                elements.add(fieldworkEnd.toString());
            }
        } else {
            if (fieldworkEnd == null) {
                elements.add(fieldworkStart.toString());
                elements.add(publicationDate.toString());
            } else {
                elements.add(fieldworkStart.toString());
                elements.add(fieldworkEnd.toString());
            }
        }
        return elements;
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
