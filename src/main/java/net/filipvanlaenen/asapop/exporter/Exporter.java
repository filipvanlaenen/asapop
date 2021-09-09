package net.filipvanlaenen.asapop.exporter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.filipvanlaenen.asapop.model.DateOrMonth;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.ResponseScenario;

/**
 * Superclass for all exporters.
 */
public abstract class Exporter {
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
     * Verifies whether the provided area matches with the specified area. If the specified area is <code>null</code>,
     * or the specified area and the provided area are equal, or the specified area is <code>--</code> and the
     * provided area is <code>null</code>, the areas are said to match, and the method returns true.
     *
     * @param specifiedArea The area specified for export.
     * @param actualArea The are of the opinion poll or response alternative.
     * @return True if the provided area matches with the specified area, false otherwise.
     */
    static boolean areaMatches(final String specifiedArea, final String actualArea) {
        return specifiedArea == null || (specifiedArea.equals("--") && actualArea == null) || specifiedArea.equals(actualArea);
    }

    /**
     * Calculates the precision of an opinion poll.
     *
     * @param opinionPoll The opinion poll.
     * @param electoralListKeys The keys of the electoral lists to export.
     * @return The precision as a string.
     */
    static String calculatePrecision(final OpinionPoll opinionPoll, final String... electoralListKeys) {
        return calculatePrecision(extractResults(opinionPoll, electoralListKeys));
    }

    /**
     * Calculates the precision of a response scenario.
     *
     * @param responseScenario The response scenario.
     * @param electoralListKeys The keys of the electoral lists to export.
     * @return The precision as a string.
     */
    static String calculatePrecision(final ResponseScenario responseScenario, final String... electoralListKeys) {
        return calculatePrecision(extractResults(responseScenario, electoralListKeys));
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
     * Compares two opinion polls for sorting.
     *
     * @param op1 The first opinion poll.
     * @param op2 The second opinion poll.
     * @return A comparison result usable for sorting.
     */
    static int compareOpinionPolls(final OpinionPoll op1, final OpinionPoll op2) {
        LocalDate endDate1 = getOpinionPollEndDate(op1);
        LocalDate endDate2 = getOpinionPollEndDate(op2);
        if (endDate1.equals(endDate2)) {
            LocalDate startDate1 = getOpinionPollStartDate(op1);
            LocalDate startDate2 = getOpinionPollStartDate(op2);
            if (startDate1.equals(startDate2)) {
                return op2.getSampleSizeValue() - op1.getSampleSizeValue();
            } else {
                return startDate2.compareTo(startDate1);
            }
        } else {
            return endDate2.compareTo(endDate1);
        }
    }

    /**
     * Exports the commissionners.
     *
     * @param opinionPoll The opinion poll to export the commissioners from.
     * @return A string representing the commissioners of the opinion poll, or null if there are none.
     */
    static String exportCommissionners(final OpinionPoll opinionPoll) {
        if (opinionPoll.getCommissioners().isEmpty()) {
            return null;
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
    static List<String> exportDates(final OpinionPoll opinionPoll) {
        List<String> elements = new ArrayList<String>();
        DateOrMonth fieldworkStart = opinionPoll.getFieldworkStart();
        DateOrMonth fieldworkEnd = opinionPoll.getFieldworkEnd();
        LocalDate publicationDate = opinionPoll.getPublicationDate();
        if (fieldworkStart == null) {
            if (fieldworkEnd == null) {
                elements.add(publicationDate.toString());
                elements.add(publicationDate.toString());
            } else {
                elements.add(fieldworkEnd.getStart().toString());
                elements.add(fieldworkEnd.getEnd().toString());
            }
        } else {
            if (fieldworkEnd == null) {
                elements.add(fieldworkStart.getStart().toString());
                elements.add(publicationDate.toString());
            } else {
                elements.add(fieldworkStart.getStart().toString());
                elements.add(fieldworkEnd.getEnd().toString());
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
     * Returns the end date of an opinion poll, for sorting.
     *
     * @param opinionPoll The opinion poll.
     * @return The end date of the opinion poll, either the fieldword end date or the publication date.
     */
    private static LocalDate getOpinionPollEndDate(final OpinionPoll opinionPoll) {
        if (opinionPoll.getFieldworkEnd() == null) {
            return opinionPoll.getPublicationDate();
        } else {
            return opinionPoll.getFieldworkEnd().getEnd();
        }
    }

    /**
     * Returns the start date of an opinion poll, for sorting.
     *
     * @param opinionPoll The opinion poll.
     * @return The start date of the opinion poll, either the fieldwork start date or the result of calling
     *         <code>getOpinionPollEndDate</code>.
     */
    private static LocalDate getOpinionPollStartDate(final OpinionPoll opinionPoll) {
        if (opinionPoll.getFieldworkStart() == null) {
            return getOpinionPollEndDate(opinionPoll);
        } else {
            return opinionPoll.getFieldworkStart().getStart();
        }
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

    /**
     * Sorts a set of opinion polls chronologically, first by end date, then by start date, than by sample size.
     *
     * @param opinionPolls The set of opinion polls to be sorted.
     * @return A sorted list with the opinion polls.
     */
    static List<OpinionPoll> sortOpinionPolls(final Set<OpinionPoll> opinionPolls) {
        List<OpinionPoll> result = new ArrayList<OpinionPoll>(opinionPolls);
        result.sort((op1, op2) -> compareOpinionPolls(op1, op2));
        return result;
    }
}
