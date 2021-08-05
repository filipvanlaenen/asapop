package net.filipvanlaenen.asapop.exporter;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;

/**
 * Exporter to the EOPAOD PSV file format.
 */
public class EopaodPsvExporter {
    /**
     * Exports the opinion polls as a string in the PSV file format for EOPAOD.
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
            sb.append(opinionPoll.toEopaodPsvString(electoralListKeys));
        }
        return sb.toString();
    }
}
