package net.filipvanlaenen.asapop.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
            electoralLists.addAll(getElectoralLists(opinionPoll));
        }
        Map<String, String> idsToKeysMap = createIdsToKeys(electoralLists);
        for (OpinionPoll opinionPoll : richOpinionPollsFile.getOpinionPolls().getOpinionPolls()) {
            sb.append(export(opinionPoll, idsToKeysMap));
            sb.append("\n");
        }
        sb.append("\n");
        Map<String, String> keysToIdsMap =
                idsToKeysMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        List<String> keys = new ArrayList<String>(keysToIdsMap.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            sb.append(export(ElectoralList.get(keysToIdsMap.get(key)), idsToKeysMap));
            sb.append("\n");
        }
        return sb.toString();
    }

    private static Map<String, String> createIdsToKeys(Set<ElectoralList> electoralLists) {
        Map<String, String> result = new HashMap<String, String>();
        for (ElectoralList electoralList : electoralLists) {
            result.put(electoralList.getId(), electoralList.getAbbreviation()
                    .replaceAll("[^\\p{javaUpperCase}\\p{javaLowerCase}\\p{Digit}]", "").toUpperCase());
        }
        return result;
    }

    /**
     * Exports an electoral list.
     *
     * @param electoralList The electoral list to export.
     * @return A string representation of the election list.
     */
    private static String export(final ElectoralList electoralList, final Map<String, String> idsToKeysMap) {
        StringBuffer sb = new StringBuffer();
        String id = electoralList.getId();
        sb.append(idsToKeysMap.get(id));
        sb.append(": ");
        sb.append(id);
        sb.append(" •A: ");
        sb.append(electoralList.getAbbreviation());
        for (String languageCode : electoralList.getLanguageCodes()) {
            sb.append(" •");
            sb.append(languageCode);
            sb.append(": ");
            sb.append(electoralList.getName(languageCode));
        }
        return sb.toString();
    }

    /**
     * Exports an opinion poll.
     *
     * @param opinionPoll The opinion poll to export.
     * @return A string representation of the opinion poll.
     */
    private static String export(final OpinionPoll opinionPoll, final Map<String, String> idsToKeysMap) {
        StringBuffer sb = new StringBuffer();
        sb.append("•PF: ");
        sb.append(opinionPoll.getPollingFirm());
        sb.append(" •PD: ");
        sb.append(opinionPoll.getPublicationDate());
        for (Set<ElectoralList> electoralListCombination : opinionPoll.getMainResponseScenario()
                .getElectoralListSets()) {
            sb.append(" ");
            sb.append(String.join("+", electoralListCombination.stream().map(el -> idsToKeysMap.get(el.getId()))
                    .collect(Collectors.toList())));
            sb.append(": ");
            sb.append(opinionPoll.getMainResponseScenario().getResult(ElectoralList.getIds(electoralListCombination))
                    .getText());
        }
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
