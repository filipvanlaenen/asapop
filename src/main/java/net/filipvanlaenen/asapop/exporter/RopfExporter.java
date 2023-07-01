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
    /**
     * The magic number four.
     */
    private static final int FOUR = 4;

    /**
     * A record holding the widths of the electoral list fields.
     *
     * @param key          The width of the electoral list keys.
     * @param id           The width of the electoral list IDs.
     * @param abbreviation The width of the electoral list abbreviations.
     * @param languageCode The widths of the electoral list language codes.
     */
    private record ElectoralListWidths(int key, int id, int abbreviation, Map<String, Integer> languageCode) {
    }

    /**
     * Private constructor.
     */
    private RopfExporter() {
    }

    /**
     * Converts a set of electoral lists into a string with the keys.
     *
     * @param idsToKeysMap             A map mapping the electoral list IDs to keys.
     * @param electoralListCombination A set of electoral lists.
     * @return A string with the electoral list keys.
     */
    private static String asKeysString(final Map<String, String> idsToKeysMap,
            final Set<ElectoralList> electoralListCombination) {
        return String.join("+",
                electoralListCombination.stream().map(el -> idsToKeysMap.get(el.getId())).collect(Collectors.toList()));
    }

    /**
     * Calculates the widths of the fields of the electoral lists.
     *
     * @param electoralLists    A set of electoral lists.
     * @param electoralListKeys The keys of the electoral lists.
     * @return A record holding the widths of the electoral list fields.
     */
    private static ElectoralListWidths calculateElectoralListWidths(final Set<ElectoralList> electoralLists,
            final List<String> electoralListKeys) {
        int electoralListKeyWidth = 0;
        for (String electoralListKey : electoralListKeys) {
            electoralListKeyWidth = Math.max(electoralListKeyWidth, electoralListKey.length());
        }
        int idWidth = 0;
        int abbreviationWidth = 0;
        Map<String, Integer> languageCodeWidth = new HashMap<String, Integer>();
        for (ElectoralList electoralList : electoralLists) {
            idWidth = Math.max(idWidth, electoralList.getId().length());
            abbreviationWidth = Math.max(abbreviationWidth, electoralList.getAbbreviation().length());
            for (String languageCode : electoralList.getLanguageCodes()) {
                if (languageCodeWidth.containsKey(languageCode)) {
                    languageCodeWidth.put(languageCode, Math.max(languageCodeWidth.get(languageCode),
                            electoralList.getName(languageCode).length()));
                } else {
                    languageCodeWidth.put(languageCode, electoralList.getName(languageCode).length());
                }
            }
        }
        return new ElectoralListWidths(electoralListKeyWidth, idWidth, abbreviationWidth, languageCodeWidth);
    }

    /**
     * Calculates the map mapping the electoral list IDs to the keys.
     *
     * @param electoralLists A set of electoral lists.
     * @return A map mapping the elector list IDs to the keys.
     */
    private static Map<String, String> calculateIdsToKeys(final Set<ElectoralList> electoralLists) {
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
        Map<String, String> idsToKeysMap = calculateIdsToKeys(electoralLists);
        for (OpinionPoll opinionPoll : richOpinionPollsFile.getOpinionPolls().getOpinionPolls()) {
            sb.append(export(opinionPoll, idsToKeysMap));
            sb.append("\n");
        }
        sb.append("\n");
        Map<String, String> keysToIdsMap =
                idsToKeysMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        List<String> electoralListKeys = new ArrayList<String>(keysToIdsMap.keySet());
        Collections.sort(electoralListKeys);
        ElectoralListWidths electoralListWidths = calculateElectoralListWidths(electoralLists, electoralListKeys);
        for (String key : electoralListKeys) {
            sb.append(export(ElectoralList.get(keysToIdsMap.get(key)), idsToKeysMap, electoralListWidths));
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Exports an electoral list.
     *
     * @param electoralList       The electoral list to export.
     * @param idsToKeysMap        A map mapping the electoral list IDs to keys.
     * @param electoralListWidths The widths of the electoral list fields.
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
        List<String> languageCodes = new ArrayList<String>(electoralListWidths.languageCode.keySet());
        Collections.sort(languageCodes);
        for (String languageCode : languageCodes) {
            if (electoralList.getName(languageCode) == null) {
                sb.append(pad("", electoralListWidths.languageCode().get(languageCode) + languageCode.length() + FOUR));
            } else {
                sb.append(" •");
                sb.append(languageCode);
                sb.append(": ");
                sb.append(
                        pad(electoralList.getName(languageCode), electoralListWidths.languageCode().get(languageCode)));
            }
        }
        return sb.toString().trim();
    }

    /**
     * Exports an opinion poll.
     *
     * @param opinionPoll  The opinion poll to export.
     * @param idsToKeysMap A map mapping the electoral list IDs to keys.
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

    /**
     * Exports a key with a value, or an empty string if the value is null.
     *
     * @param key   The key.
     * @param value The value.
     * @return A key with a value exported to a string, or an empty string if the value is null.
     */
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

    /**
     * Returns a string padded with spaces to the right.
     *
     * @param text  The text to be padded with spaces.
     * @param width The width.
     * @return A string containing the text padded with spaces to the right.
     */
    private static String pad(final String text, final int width) {
        return String.format("%1$-" + width + "s", text);
    }
}
