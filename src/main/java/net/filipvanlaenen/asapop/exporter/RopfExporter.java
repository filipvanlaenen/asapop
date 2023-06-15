package net.filipvanlaenen.asapop.exporter;

import java.util.HashSet;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;

/**
 * Exporter to the ROPF file format.
 */
public final class RopfExporter extends Exporter {
    /**
     * Private constructor.
     */
    private RopfExporter() {
    }

    /**
     * Exports the rich opinion polls file.
     *
     * @param richOpinionPollsFile The rich opinion polls file to export.
     * @return A string representing of the rich opinion polls file.
     */
    public static String export(final RichOpinionPollsFile richOpinionPollsFile) {
        StringBuffer sb = new StringBuffer();
        Set<ElectoralList> electoralLists = new HashSet<ElectoralList>();
        for (OpinionPoll opinionPoll : richOpinionPollsFile.getOpinionPolls().getOpinionPolls()) {
            sb.append(export(opinionPoll));
            sb.append("\n");
            electoralLists.addAll(getElectoralLists(opinionPoll));
        }
        for (ElectoralList electoralList : electoralLists) {
            sb.append("\n");
            sb.append(export(electoralList));
        }
        return sb.toString();
    }

    /**
     * Exports an electoral list.
     *
     * @param electoralList The electoral list to export.
     * @return A string representation of the election list.
     */
    private static String export(ElectoralList electoralList) {
        StringBuffer sb = new StringBuffer();
        sb.append(": ");
        sb.append(electoralList.getId());
        sb.append(" •A: ");
        sb.append(electoralList.getAbbreviation());
        return sb.toString();
    }

    /**
     * Exports an opinion poll.
     *
     * @param opinionPoll The opinion poll to export.
     * @return A string representation of the opinion poll.
     */
    private static String export(final OpinionPoll opinionPoll) {
        StringBuffer sb = new StringBuffer();
        sb.append("•PF: ");
        sb.append(opinionPoll.getPollingFirm());
        return sb.toString();
    }

    /**
     * Returns a set with all the electoral lists mentioned in an opinion poll.
     *
     * @param opinionPoll The opinion poll.
     * @return A set with all the electoral lists mentioned in the opinion poll.
     */
    private static Set<ElectoralList> getElectoralLists(final OpinionPoll opinionPoll) {
        Set<ElectoralList> electoralLists = new HashSet<ElectoralList>();
        for (Set<ElectoralList> electoralListCombination : opinionPoll.getElectoralListSets()) {
            electoralLists.addAll(electoralListCombination);
        }
        return electoralLists;
    }
}
