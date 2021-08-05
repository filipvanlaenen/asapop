package net.filipvanlaenen.asapop.exporter;

import java.time.LocalDate;

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
        StringBuffer sb = new StringBuffer();
        sb.append(opinionPoll.getPollingFirm());
        sb.append(" | ");
        sb.append(opinionPoll.getCommissioners().isEmpty() ? "N/A" : String.join(", ", opinionPoll.getCommissioners()));
        sb.append(" | ");
        LocalDate fieldworkStart = opinionPoll.getFieldworkStart();
        LocalDate fieldworkEnd = opinionPoll.getFieldworkEnd();
        LocalDate publicationDate = opinionPoll.getPublicationDate();
        if (fieldworkStart == null) {
            if (fieldworkEnd == null) {
                sb.append(publicationDate.toString());
                sb.append(" | ");
                sb.append(publicationDate.toString());
            } else {
                sb.append(fieldworkEnd.toString());
                sb.append(" | ");
                sb.append(fieldworkEnd.toString());
            }
        } else {
            if (fieldworkEnd == null) {
                sb.append(fieldworkStart.toString());
                sb.append(" | ");
                sb.append(publicationDate.toString());
            } else {
                sb.append(fieldworkStart.toString());
                sb.append(" | ");
                sb.append(fieldworkEnd.toString());
            }
        }
        sb.append(" | ");
        sb.append(naIfNull(opinionPoll.getScope()));
        sb.append(" | ");
        sb.append(naIfNull(opinionPoll.getSampleSize()));
        sb.append(" | ");
        sb.append("N/A");
        sb.append(" | ");
        sb.append("N/A");
        sb.append(" | ");
        for (String electoralListKey : electoralListKeys) {
            sb.append(naIfNull(opinionPoll.getResult(electoralListKey)));
            sb.append(" | ");
        }
        sb.append(naIfNull(opinionPoll.getOther()));
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
        StringBuffer sb = new StringBuffer();
        sb.append(opinionPoll.getPollingFirm());
        sb.append(" | ");
        sb.append(opinionPoll.getCommissioners().isEmpty() ? "N/A" : String.join(", ", opinionPoll.getCommissioners()));
        sb.append(" | ");
        LocalDate fieldworkStart = opinionPoll.getFieldworkStart();
        LocalDate fieldworkEnd = opinionPoll.getFieldworkEnd();
        LocalDate publicationDate = opinionPoll.getPublicationDate();
        if (opinionPoll.getFieldworkStart() == null) {
            if (opinionPoll.getFieldworkEnd() == null) {
                sb.append(opinionPoll.getPublicationDate().toString());
                sb.append(" | ");
                sb.append(opinionPoll.getPublicationDate().toString());
            } else {
                sb.append(opinionPoll.getFieldworkEnd().toString());
                sb.append(" | ");
                sb.append(opinionPoll.getFieldworkEnd().toString());
            }
        } else {
            if (opinionPoll.getFieldworkEnd() == null) {
                sb.append(opinionPoll.getFieldworkStart().toString());
                sb.append(" | ");
                sb.append(opinionPoll.getPublicationDate().toString());
            } else {
                sb.append(opinionPoll.getFieldworkStart().toString());
                sb.append(" | ");
                sb.append(opinionPoll.getFieldworkEnd().toString());
            }
        }
        sb.append(" | ");
        sb.append(responseScenario.getScope() == null ? naIfNull(opinionPoll.getScope()) : responseScenario.getScope());
        sb.append(" | ");
        sb.append(naIfNull(opinionPoll.getSampleSize()));
        sb.append(" | ");
        sb.append("N/A");
        sb.append(" | ");
        sb.append("N/A");
        sb.append(" | ");
        for (String electoralListKey : electoralListKeys) {
            sb.append(naIfNull(responseScenario.getResult(electoralListKey)));
            sb.append(" | ");
        }
        sb.append(naIfNull(responseScenario.getOther()));
        return sb.toString();
    }

    private static String naIfNull(final String s) {
        return s == null ? "N/A" : s;
    }
}
