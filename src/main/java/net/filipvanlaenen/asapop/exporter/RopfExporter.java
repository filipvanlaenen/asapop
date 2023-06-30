package net.filipvanlaenen.asapop.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private record ElectoralListWidths(int key, int id, int abbreviation, Map<String, Integer> languageWidth) {
    }

    /**
     * Private constructor.
     */
    private RopfExporter() {
    }

    private static String asKeysString(final Map<String, String> idsToKeysMap,
            final Set<ElectoralList> electoralListCombination) {
        return String.join("+",
                electoralListCombination.stream().map(el -> idsToKeysMap.get(el.getId())).collect(Collectors.toList()));
    }

    private static ElectoralListWidths calculateElectoralListWidths(final Set<ElectoralList> electoralLists,
            final List<String> keys) {
        int keyWidth = 0;
        for (String key : keys) {
            keyWidth = Math.max(keyWidth, key.length());
        }
        int idWidth = 0;
        int abbreviationWidth = 0;
        Map<String, Integer> languageWidth = new HashMap<String, Integer>();
        for (ElectoralList electoralList : electoralLists) {
            idWidth = Math.max(idWidth, electoralList.getId().length());
            abbreviationWidth = Math.max(abbreviationWidth, electoralList.getAbbreviation().length());
            for (String languageCode : electoralList.getLanguageCodes()) {
                if (languageWidth.containsKey(languageCode)) {
                    languageWidth.put(languageCode,
                            Math.max(languageWidth.get(languageCode), electoralList.getName(languageCode).length()));
                } else {
                    languageWidth.put(languageCode, electoralList.getName(languageCode).length());
                }
            }
        }
        return new ElectoralListWidths(keyWidth, idWidth, abbreviationWidth, languageWidth);
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
        ElectoralListWidths electoralListWidths = calculateElectoralListWidths(electoralLists, keys);
        for (String key : keys) {
            sb.append(export(ElectoralList.get(keysToIdsMap.get(key)), idsToKeysMap, electoralListWidths));
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Exports an electoral list.
     *
     * @param electoralList The electoral list to export.
     * @return A string representation of the election list.
     */
    private static String export(final ElectoralList electoralList, final Map<String, String> idsToKeysMap,
            final ElectoralListWidths electoralListWidths) {
        StringBuffer sb = new StringBuffer();
        String id = electoralList.getId();
        sb.append(pad(idsToKeysMap.get(id) + ":", electoralListWidths.key + 1));
        sb.append(" ");
        sb.append(pad(id, electoralListWidths.id));
        sb.append(" •A: ");
        sb.append(pad(electoralList.getAbbreviation(), electoralListWidths.abbreviation));
        List<String> languageCodes = new ArrayList<String>(electoralListWidths.languageWidth.keySet());
        Collections.sort(languageCodes);
        for (String languageCode : languageCodes) {
            if (electoralList.getName(languageCode) == null) {
                sb.append(pad("", electoralListWidths.languageWidth().get(languageCode) + languageCode.length() + 4));
            } else {
                sb.append(" •");
                sb.append(languageCode);
                sb.append(": ");
                sb.append(pad(electoralList.getName(languageCode),
                        electoralListWidths.languageWidth().get(languageCode)));
            }
        }
        return sb.toString().trim();
    }

    /**
     * Exports an opinion poll.
     *
     * @param opinionPoll The opinion poll to export.
     * @return A string representation of the opinion poll.
     */
    private static String export(final OpinionPoll opinionPoll, final Map<String, String> idsToKeysMap) {
        StringBuffer sb = new StringBuffer();
        sb.append(export("PF", opinionPoll.getPollingFirm()));
        sb.append(export("PFP", opinionPoll.getPollingFirmPartner()));
        for (String commissioner : opinionPoll.getCommissioners()) {
            sb.append(" •C: ");
            sb.append(commissioner);
        }
        sb.append(export("FS", opinionPoll.getFieldworkStart()));
        sb.append(export("FE", opinionPoll.getFieldworkEnd()));
        sb.append(export("PD", opinionPoll.getPublicationDate()));
        sb.append(export("SC", opinionPoll.getScope()));
        sb.append(export("SS", opinionPoll.getSampleSize()));
        sb.append(export("EX", opinionPoll.getExcluded()));
        List<Set<ElectoralList>> electoralListCombinations =
                new ArrayList<Set<ElectoralList>>(opinionPoll.getMainResponseScenario().getElectoralListSets());
        Collections.sort(electoralListCombinations, new Comparator<Set<ElectoralList>>() {
            @Override
            public int compare(final Set<ElectoralList> arg0, final Set<ElectoralList> arg1) {
                double difference = opinionPoll.getResult(ElectoralList.getIds(arg1)).getNominalValue()
                        - opinionPoll.getResult(ElectoralList.getIds(arg0)).getNominalValue();
                return difference < 0 ? -1
                        : difference > 0 ? 1
                                : asKeysString(idsToKeysMap, arg0).compareTo(asKeysString(idsToKeysMap, arg1));
            }
        });
        for (Set<ElectoralList> electoralListCombination : electoralListCombinations) {
            sb.append(" ");
            sb.append(asKeysString(idsToKeysMap, electoralListCombination));
            sb.append(": ");
            sb.append(opinionPoll.getMainResponseScenario().getResult(ElectoralList.getIds(electoralListCombination))
                    .getText());
        }
        sb.append(export("O", opinionPoll.getOther()));
        sb.append(export("N", opinionPoll.getNoResponses()));
        return sb.toString().trim();
    }

    private static String export(final String key, final Object value) {
        if (value != null) {
            return " •" + key + ": " + value;
        } else {
            return "";
        }
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

    private static String pad(final String text, final int width) {
        return String.format("%1$-" + width + "s", text);
    }
}
