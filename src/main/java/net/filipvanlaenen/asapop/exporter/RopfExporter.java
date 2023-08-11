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
import net.filipvanlaenen.asapop.model.ResultValue;
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

    private static Map<String, Integer> calculateMetadataFieldWidths(final Set<OpinionPoll> opinionPolls) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (OpinionPoll opinionPoll : opinionPolls) {
            updateMetadataFieldWidth(result, "A", opinionPoll.getArea());
            updateMetadataFieldWidth(result, "C", opinionPoll.getCommissioners());
            updateMetadataFieldWidth(result, "EX", opinionPoll.getExcluded());
            updateMetadataFieldWidth(result, "FE", opinionPoll.getFieldworkEnd());
            updateMetadataFieldWidth(result, "FS", opinionPoll.getFieldworkStart());
            updateMetadataFieldWidth(result, "N", opinionPoll.getNoResponses());
            updateMetadataFieldWidth(result, "O", opinionPoll.getOther());
            updateMetadataFieldWidth(result, "PD", opinionPoll.getPublicationDate());
            updateMetadataFieldWidth(result, "PF", opinionPoll.getPollingFirm());
            updateMetadataFieldWidth(result, "PFP", opinionPoll.getPollingFirmPartner());
            updateMetadataFieldWidth(result, "SC", opinionPoll.getScope());
            updateMetadataFieldWidth(result, "SS", opinionPoll.getSampleSize());
        }
        return result;
    }

    private static void updateMetadataFieldWidth(final Map<String, Integer> metadataFieldWidths, String key,
            Object value) {
        if (value != null) {
            updateMetadataFieldWidth(metadataFieldWidths, key, value.toString());
        }
    }

    private static void updateMetadataFieldWidth(final Map<String, Integer> metadataFieldWidths, String key,
            ResultValue value) {
        if (value != null) {
            updateMetadataFieldWidth(metadataFieldWidths, key, value.getText());
        }
    }

    private static void updateMetadataFieldWidth(final Map<String, Integer> metadataFieldWidths, String key,
            String value) {
        if (value != null && value.length() > 0) {
            int width = value.length();
            if (metadataFieldWidths.containsKey(key)) {
                metadataFieldWidths.put(key, Math.max(width, metadataFieldWidths.get(key)));
            } else {
                metadataFieldWidths.put(key, width);
            }
        }
    }

    private static void updateMetadataFieldWidth(final Map<String, Integer> metadataFieldWidths, String key,
            Set<String> values) {
        if (values != null && !values.isEmpty()) {
            updateMetadataFieldWidth(metadataFieldWidths, key, String.join(" •" + key + ": ", values));
        }
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
        Set<OpinionPoll> opinionPolls = richOpinionPollsFile.getOpinionPolls().getOpinionPolls();
        Map<String, Integer> metadataFieldWidths = calculateMetadataFieldWidths(opinionPolls);
        for (OpinionPoll opinionPoll : sortOpinionPolls(opinionPolls)) {
            sb.append(export(opinionPoll, idsToKeysMap, metadataFieldWidths));
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
    private static String export(final OpinionPoll opinionPoll, final Map<String, String> idsToKeysMap,
            final Map<String, Integer> metadataFieldWidths) {
        StringBuffer sb = new StringBuffer();
        sb.append(export("PF", metadataFieldWidths, opinionPoll.getPollingFirm()));
        sb.append(export("PFP", metadataFieldWidths, opinionPoll.getPollingFirmPartner()));
        sb.append(export("C", metadataFieldWidths, opinionPoll.getCommissioners()));
        sb.append(export("FS", metadataFieldWidths, opinionPoll.getFieldworkStart()));
        sb.append(export("FE", metadataFieldWidths, opinionPoll.getFieldworkEnd()));
        sb.append(export("PD", metadataFieldWidths, opinionPoll.getPublicationDate()));
        sb.append(export("SC", metadataFieldWidths, opinionPoll.getScope()));
        sb.append(export("A", metadataFieldWidths, opinionPoll.getArea()));
        sb.append(export("SS", metadataFieldWidths, opinionPoll.getSampleSize()));
        sb.append(export("EX", metadataFieldWidths, opinionPoll.getExcluded()));
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
        sb.append(export("O", metadataFieldWidths, opinionPoll.getOther()));
        sb.append(export("N", metadataFieldWidths, opinionPoll.getNoResponses()));
        return sb.toString().substring(1).stripTrailing();
    }

    /**
     * Exports a key with a value, or an empty string if the value is null.
     *
     * @param key   The key.
     * @param value The value.
     * @return A key with a value exported to a string, or an empty string if the value is null.
     */
    private static String export(final String key, final Map<String, Integer> metadataFieldWidths, final Object value) {
        if (value != null) {
            return export(key, metadataFieldWidths, value.toString());
        } else {
            return export(key, metadataFieldWidths, "");
        }
    }

    private static String export(final String key, final Map<String, Integer> metadataFieldWidths,
            final ResultValue resultValue) {
        if (resultValue != null) {
            return export(key, metadataFieldWidths, resultValue.getText());
        } else {
            return export(key, metadataFieldWidths, "");
        }
    }

    private static String export(final String key, final Map<String, Integer> metadataFieldWidths, final String value) {
        if (value != null && value.length() > 0) {
            return " •" + key + ": " + pad(value, metadataFieldWidths.get(key));
        } else if (metadataFieldWidths.containsKey(key)) {
            return pad("", key.length() + metadataFieldWidths.get(key) + FOUR);
        } else {
            return "";
        }
    }

    private static String export(final String key, final Map<String, Integer> metadataFieldWidths,
            final Set<String> values) {
        if (values != null && !values.isEmpty()) {
            List<String> sortedValues = new ArrayList<String>(values);
            Collections.sort(sortedValues);
            return export(key, metadataFieldWidths, String.join(" •" + key + ": ", sortedValues));
        } else {
            return export(key, metadataFieldWidths, "");
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
