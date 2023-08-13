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
import net.filipvanlaenen.asapop.model.ResponseScenario;
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
     * The magic number five.
     */
    private static final int FIVE = 5;

    /**
     * A record holding the widths of the electoral list fields.
     *
     * @param key                   The width of the electoral list's keys.
     * @param id                    The width of the electoral list's IDs.
     * @param abbreviation          The width of the electoral list's abbreviations.
     * @param romanizedAbbreviation The width of the electoral list's romanized abbreviations.
     * @param languageCode          The widths of the electoral list's language codes.
     */
    private record ElectoralListWidths(int key, int id, int abbreviation, int romanizedAbbreviation,
            Map<String, Integer> languageCode) {
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
        int romanizedAbbreviationWidth = 0;
        Map<String, Integer> languageCodeWidth = new HashMap<String, Integer>();
        for (ElectoralList electoralList : electoralLists) {
            idWidth = Math.max(idWidth, electoralList.getId().length());
            abbreviationWidth = Math.max(abbreviationWidth, electoralList.getAbbreviation().length());
            if (electoralList.getRomanizedAbbreviation() != null) {
                romanizedAbbreviationWidth =
                        Math.max(romanizedAbbreviationWidth, electoralList.getRomanizedAbbreviation().length());
            }
            for (String languageCode : electoralList.getLanguageCodes()) {
                if (languageCodeWidth.containsKey(languageCode)) {
                    languageCodeWidth.put(languageCode, Math.max(languageCodeWidth.get(languageCode),
                            electoralList.getName(languageCode).length()));
                } else {
                    languageCodeWidth.put(languageCode, electoralList.getName(languageCode).length());
                }
            }
        }
        return new ElectoralListWidths(electoralListKeyWidth, idWidth, abbreviationWidth, romanizedAbbreviationWidth,
                languageCodeWidth);
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
     * Calculates the widths of all the metadata fields over a set of opinion polls.
     *
     * @param opinionPolls The opinion polls.
     * @return A map with the widths of all the metadata fields.
     */
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
            // TODO: VS
            for (ResponseScenario responseScenario : opinionPoll.getAlternativeResponseScenarios()) {
                updateMetadataFieldWidth(result, "A", responseScenario.getArea());
                updateMetadataFieldWidth(result, "EX", responseScenario.getExcluded());
                updateMetadataFieldWidth(result, "N", responseScenario.getNoResponses());
                updateMetadataFieldWidth(result, "O", responseScenario.getOther());
                updateMetadataFieldWidth(result, "SC", responseScenario.getScope());
                updateMetadataFieldWidth(result, "SS", responseScenario.getSampleSize());
                // TODO: VS
            }
        }
        return result;
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
        if (electoralList.getRomanizedAbbreviation() != null) {
            sb.append(" •R: ");
            sb.append(pad(electoralList.getRomanizedAbbreviation(), electoralListWidths.romanizedAbbreviation));
        } else if (electoralListWidths.romanizedAbbreviation > 0) {
            sb.append(pad("", electoralListWidths.romanizedAbbreviation + FIVE));
        }
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
     * @param opinionPoll         The opinion poll to export.
     * @param idsToKeysMap        A map mapping the electoral list IDs to keys.
     * @param metadataFieldWidths The map with the metadata field widths.
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
        // TODO: VS
        StringBuffer result = new StringBuffer();
        result.append(sb.toString().substring(1).stripTrailing());
        for (ResponseScenario responseScenario : opinionPoll.getAlternativeResponseScenarios()) {
            result.append("\n");
            result.append(export(responseScenario, idsToKeysMap, metadataFieldWidths));
        }
        return result.toString();
    }

    /**
     * Exports a response scenario.
     *
     * @param responseScenario    The response scenario to export.
     * @param idsToKeysMap        A map mapping the electoral list IDs to keys.
     * @param metadataFieldWidths The map with the metadata field widths.
     * @return A string representation of the opinion poll.
     */
    private static String export(final ResponseScenario responseScenario, final Map<String, String> idsToKeysMap,
            final Map<String, Integer> metadataFieldWidths) {
        StringBuffer sb = new StringBuffer();
        sb.append(export("PF", metadataFieldWidths, ""));
        sb.append(export("PFP", metadataFieldWidths, ""));
        sb.append(export("C", metadataFieldWidths, ""));
        sb.append(export("FS", metadataFieldWidths, ""));
        sb.append(export("FE", metadataFieldWidths, ""));
        sb.append(export("PD", metadataFieldWidths, ""));
        sb.append(export("SC", metadataFieldWidths, responseScenario.getScope()));
        sb.append(export("A", metadataFieldWidths, responseScenario.getArea()));
        sb.append(export("SS", metadataFieldWidths, responseScenario.getSampleSize()));
        sb.append(export("EX", metadataFieldWidths, responseScenario.getExcluded()));
        List<Set<ElectoralList>> electoralListCombinations =
                new ArrayList<Set<ElectoralList>>(responseScenario.getElectoralListSets());
        Collections.sort(electoralListCombinations, new Comparator<Set<ElectoralList>>() {
            @Override
            public int compare(final Set<ElectoralList> arg0, final Set<ElectoralList> arg1) {
                double difference = responseScenario.getResult(ElectoralList.getIds(arg1)).getNominalValue()
                        - responseScenario.getResult(ElectoralList.getIds(arg0)).getNominalValue();
                return difference < 0 ? -1
                        : difference > 0 ? 1
                                : asKeysString(idsToKeysMap, arg0).compareTo(asKeysString(idsToKeysMap, arg1));
            }
        });
        for (Set<ElectoralList> electoralListCombination : electoralListCombinations) {
            sb.append(" ");
            sb.append(asKeysString(idsToKeysMap, electoralListCombination));
            sb.append(": ");
            sb.append(responseScenario.getResult(ElectoralList.getIds(electoralListCombination)).getText());
        }
        sb.append(export("O", metadataFieldWidths, responseScenario.getOther()));
        sb.append(export("N", metadataFieldWidths, responseScenario.getNoResponses()));
        // TODO: VS
        return "&" + sb.toString().substring(2).stripTrailing();
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
     * Exports a key with an object as a value, or a padded empty string if the object is null.
     *
     * @param key                 The key.
     * @param metadataFieldWidths The map with the metadata field widths.
     * @param object              The object.
     * @return A key with an object as a value exported to a string, or a padded empty string if the object is null.
     */
    private static String export(final String key, final Map<String, Integer> metadataFieldWidths,
            final Object object) {
        if (object != null) {
            return export(key, metadataFieldWidths, object.toString());
        } else {
            return export(key, metadataFieldWidths, "");
        }
    }

    /**
     * Exports a key with a result value, or a padded empty string if the value is null.
     *
     * @param key                 The key.
     * @param metadataFieldWidths The map with the metadata field widths.
     * @param resultValue         The result value.
     * @return A key with a result value exported to a string, or a padded empty string if the value is null.
     */
    private static String export(final String key, final Map<String, Integer> metadataFieldWidths,
            final ResultValue resultValue) {
        if (resultValue != null) {
            return export(key, metadataFieldWidths, resultValue.getText());
        } else {
            return export(key, metadataFieldWidths, "");
        }
    }

    /**
     * Exports a key with a set of strings as its value, or a padded empty string if the set is null or empty. The
     * strings will be sorted before they are exported.
     *
     * @param key                 The key.
     * @param metadataFieldWidths The map with the metadata field widths.
     * @param texts               The set of strings.
     * @return A key with a set of strings exported to a string, or a padded empty string if the set is null or empty.
     */
    private static String export(final String key, final Map<String, Integer> metadataFieldWidths,
            final Set<String> texts) {
        if (texts != null && !texts.isEmpty()) {
            List<String> sortedValues = new ArrayList<String>(texts);
            Collections.sort(sortedValues);
            return export(key, metadataFieldWidths, String.join(" •" + key + ": ", sortedValues));
        } else {
            return export(key, metadataFieldWidths, "");
        }
    }

    /**
     * Exports a key with a text, or a padded empty string if the text is null or empty but the key has a field width
     * registered, and an empty string otherwise.
     *
     * @param key                 The key.
     * @param metadataFieldWidths The map with the metadata field widths.
     * @param text                The text.
     * @return A key with a text, or a padded empty string if the text is null or empty but the key has a field width,
     *         and an empty string otherwise.
     */
    private static String export(final String key, final Map<String, Integer> metadataFieldWidths, final String text) {
        if (text != null && text.length() > 0) {
            return " •" + key + ": " + pad(text, metadataFieldWidths.get(key));
        } else if (metadataFieldWidths.containsKey(key)) {
            return pad("", key.length() + metadataFieldWidths.get(key) + FOUR);
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
        for (ResponseScenario responseScenario : opinionPoll.getAlternativeResponseScenarios()) {
            for (Set<ElectoralList> electoralListCombination : responseScenario.getElectoralListSets()) {
                electoralLists.addAll(electoralListCombination);
            }
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

    /**
     * Updates the map with the metadata field widths with the width of an object value for a key, unless the object is
     * null.
     *
     * @param metadataFieldWidths The map with the metadata field widths.
     * @param key                 The key.
     * @param object              The object value.
     */
    private static void updateMetadataFieldWidth(final Map<String, Integer> metadataFieldWidths, final String key,
            final Object object) {
        if (object != null) {
            updateMetadataFieldWidth(metadataFieldWidths, key, object.toString());
        }
    }

    /**
     * Updates the map with the metadata field widths with the width of a result value for a key, unless the result
     * value is null.
     *
     * @param metadataFieldWidths The map with the metadata field widths.
     * @param key                 The key.
     * @param resultValue         The result value.
     */
    private static void updateMetadataFieldWidth(final Map<String, Integer> metadataFieldWidths, final String key,
            final ResultValue resultValue) {
        if (resultValue != null) {
            updateMetadataFieldWidth(metadataFieldWidths, key, resultValue.getText());
        }
    }

    /**
     * Updates the map with the metadata field widths with the width of a set of texts for a key, unless the set is null
     * or empty.
     *
     * @param metadataFieldWidths The map with the metadata field widths.
     * @param key                 The key.
     * @param texts               The set of texts.
     */
    private static void updateMetadataFieldWidth(final Map<String, Integer> metadataFieldWidths, final String key,
            final Set<String> texts) {
        if (texts != null && !texts.isEmpty()) {
            updateMetadataFieldWidth(metadataFieldWidths, key, String.join(" •" + key + ": ", texts));
        }
    }

    /**
     * Updates the map with the metadata field widths with the width of a text for a key, unless the text is null or
     * empty.
     *
     * @param metadataFieldWidths The map with the metadata field widths.
     * @param key                 The key.
     * @param text                The text.
     */
    private static void updateMetadataFieldWidth(final Map<String, Integer> metadataFieldWidths, final String key,
            final String text) {
        if (text != null && text.length() > 0) {
            int width = text.length();
            if (metadataFieldWidths.containsKey(key)) {
                metadataFieldWidths.put(key, Math.max(width, metadataFieldWidths.get(key)));
            } else {
                metadataFieldWidths.put(key, width);
            }
        }
    }
}
