package net.filipvanlaenen.asapop.exporter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;

/**
 * Exporter to the EOPAOD PSV file format.
 */
public final class EopaodPsvExporter {
    /**
     * Private constructor.
     */
    private EopaodPsvExporter() {
    }

    /**
     * Adds a string to a set, unless the string is null.
     *
     * @param set The set to add to.
     * @param s The string to add.
     */
    private static void addToSetUnlessNull(final Set<String> set, final String s) {
        if (s != null) {
            set.add(s);
        }
    }

    /**
     * Calculates the precision of a set of numbers.
     *
     * @param numbers A set of numbers.
     * @return The precision as a string.
     */
    private static String calculatePrecision(final Set<String> numbers) {
        String result = "1";
        for (String number : numbers) {
            if (number.contains(".")) {
                if (number.endsWith(".5")) {
                    if (result.equals("1")) {
                        result = "0.5";
                    }
                } else if (!number.endsWith(".0")) {
                    result = "0.1";
                }
            }
        }
        return result;
    }

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
        elements.add(exportCommissionners(opinionPoll));
        elements.addAll(exportDates(opinionPoll));
        elements.add(naIfNull(opinionPoll.getScope()));
        elements.add(naIfNull(opinionPoll.getSampleSize()));
        elements.add("N/A");
        elements.add(calculatePrecision(extractResults(opinionPoll, electoralListKeys)));
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
     * @param responseScenario The response scenario to export.
     * @param opinionPoll The opinion poll this response scenario relates to.
     * @param electoralListKeys An array with the keys of the electoral lists to be exported.
     * @return A string containing the response scenario in the PSV file format for EOPAOD.
     */
    static String export(final ResponseScenario responseScenario, final OpinionPoll opinionPoll,
                         final String... electoralListKeys) {
        List<String> elements = new ArrayList<String>();
        elements.add(opinionPoll.getPollingFirm());
        elements.add(exportCommissionners(opinionPoll));
        elements.addAll(exportDates(opinionPoll));
        if (responseScenario.getScope() == null) {
            elements.add(naIfNull(opinionPoll.getScope()));
        } else {
            elements.add(responseScenario.getScope());
        }
        elements.add(naIfNull(opinionPoll.getSampleSize()));
        elements.add("N/A");
        elements.add(calculatePrecision(extractResults(responseScenario, electoralListKeys)));
        for (String electoralListKey : electoralListKeys) {
            elements.add(naIfNull(responseScenario.getResult(electoralListKey)));
        }
        elements.add(naIfNull(responseScenario.getOther()));
        return String.join(" | ", elements);
    }

    /**
     * Exports the commissionners.
     *
     * @param opinionPoll The opinion poll to export the commissioners from.
     * @return A string representing the commissioners of the opinion poll.
     */
    private static String exportCommissionners(final OpinionPoll opinionPoll) {
        if (opinionPoll.getCommissioners().isEmpty()) {
            return "N/A";
        } else {
            return sortAndConcatenateWithCommasAndAnd(opinionPoll.getCommissioners());
        }
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
     * Extracts all the results from an opinion poll.
     *
     * @param opinionPoll The opinion poll to extract the results from.
     * @param electoralListKeys The keys of the electoral lists for which to extract the results.
     * @return A set of numbers representing the results.
     */
    private static Set<String> extractResults(final OpinionPoll opinionPoll, final String... electoralListKeys) {
        Set<String> result = new HashSet<String>();
        for (String electoralListKey : electoralListKeys) {
            addToSetUnlessNull(result, opinionPoll.getResult(electoralListKey));
        }
        addToSetUnlessNull(result, opinionPoll.getOther());
        return result;
    }

    /**
     * Extracts all the results from a response scenario.
     *
     * @param responseScenario The response scenario to extract the results from.
     * @param electoralListKeys The keys of the electoral lists for which to extract the results.
     * @return A set of numbers representing the results.
     */
    private static Set<String> extractResults(final ResponseScenario responseScenario,
                                              final String... electoralListKeys) {
        Set<String> result = new HashSet<String>();
        for (String electoralListKey : electoralListKeys) {
            addToSetUnlessNull(result, responseScenario.getResult(electoralListKey));
        }
        addToSetUnlessNull(result, responseScenario.getOther());
        return result;
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

    /**
     * Sorts and concatenates with commas and an and a set of strings.
     *
     * @param strings The set of strings to be sorted and concatenated.
     * @return A string listing the elements of the set in alphabetical order.
     */
    private static String sortAndConcatenateWithCommasAndAnd(final Set<String> strings) {
        List<String> sortedStrings = new ArrayList<String>(strings);
        sortedStrings.sort(String::compareToIgnoreCase);
        String lastString = sortedStrings.remove(sortedStrings.size() - 1);
        if (sortedStrings.isEmpty()) {
            return lastString;
        } else {
            return String.join(", ", sortedStrings) + " and " + lastString;
        }
    }
}
