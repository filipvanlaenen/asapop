package net.filipvanlaenen.asapop.exporter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.filipvanlaenen.asapop.model.DateOrMonth;
import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResultValue;

/**
 * Superclass for all exporters.
 */
public abstract class Exporter {
    /**
     * The magic number one hundred.
     */
    private static final int ONE_HUNDRED = 100;

    /**
     * Adds a string to a set, unless the string is null.
     *
     * @param <T> The type of the set items.
     * @param set The set to add to.
     * @param s   The string to add.
     */
    private static <T> void addToSetUnlessNull(final Set<T> set, final T s) {
        if (s != null) {
            set.add(s);
        }
    }

    /**
     * Verifies whether the provided area matches with the specified area. If the specified area is <code>null</code>,
     * or the specified area and the provided area are equal, or the specified area is <code>--</code> and the provided
     * area is <code>null</code>, the areas are said to match, and the method returns true.
     *
     * @param specifiedArea The area specified for export.
     * @param actualArea    The are of the opinion poll or response alternative.
     * @return True if the provided area matches with the specified area, false otherwise.
     */
    static boolean areaMatches(final String specifiedArea, final String actualArea) {
        return specifiedArea == null || specifiedArea.equals("--") && actualArea == null
                || specifiedArea.equals(actualArea);
    }

    /**
     * Calculates the precision of an opinion poll.
     *
     * @param opinionPoll          The opinion poll.
     * @param electoralListKeySets The sets of keys of the electoral lists to export.
     * @return The precision as a string.
     */
    static ResultValue.Precision calculatePrecision(final OpinionPoll opinionPoll,
            final List<Set<String>> electoralListKeySets) {
        return ResultValue.Precision.getHighestPrecision(extractResults(opinionPoll, electoralListKeySets));
    }

    /**
     * Calculates the precision of a response scenario.
     *
     * @param responseScenario The response scenario.
     * @return The precision as a string.
     */
    static ResultValue.Precision calculatePrecision(final ResponseScenario responseScenario) {
        Collection<ResultValue> allResults = new HashSet<ResultValue>(responseScenario.getResults());
        ResultValue other = responseScenario.getOther();
        if (other != null) {
            allResults.add(other);
        }
        return ResultValue.Precision.getHighestPrecision(allResults);
    }

    /**
     * Calculates the precision of a response scenario.
     *
     * @param responseScenario     The response scenario.
     * @param electoralListKeySets The sets of keys of the electoral lists to export.
     * @return The precision as a string.
     */
    static ResultValue.Precision calculatePrecision(final ResponseScenario responseScenario,
            final List<Set<String>> electoralListKeySets) {
        return ResultValue.Precision.getHighestPrecision(extractResults(responseScenario, electoralListKeySets));
    }

    /**
     * Compares two opinion polls for sorting.
     *
     * @param op1 The first opinion poll.
     * @param op2 The second opinion poll.
     * @return A comparison result usable for sorting.
     */
    static int compareOpinionPolls(final OpinionPoll op1, final OpinionPoll op2) {
        LocalDate endDate1 = op1.getEndDate();
        LocalDate endDate2 = op2.getEndDate();
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
     * Exports the commissioners.
     *
     * @param opinionPoll The opinion poll to export the commissioners from.
     * @return A string representing the commissioners of the opinion poll, or null if there are none.
     */
    static String exportCommissioners(final OpinionPoll opinionPoll) {
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
     * Exports the participation rate.
     *
     * @param opinionPoll The opinion poll to export the participation rate for.
     * @return A string representing the participation rate for the opinion poll.
     */
    static String exportParticipationRate(final OpinionPoll opinionPoll) {
        DecimalNumber excluded = opinionPoll.getExcluded();
        if (excluded == null) {
            return null;
        } else {
            return new DecimalNumber(ONE_HUNDRED - excluded.getValue(), excluded.getNumberOfDecimals()).toString();
        }
    }

    /**
     * Exports the polling firms.
     *
     * @param opinionPoll The opinion poll to export the polling firms from.
     * @return A string representing the polling firms of the opinion poll.
     */
    static String exportPollingFirms(final OpinionPoll opinionPoll) {
        if (opinionPoll.getPollingFirmPartner() == null) {
            return opinionPoll.getPollingFirm();
        } else {
            return sortAndConcatenateWithCommasAndAnd(
                    Set.of(opinionPoll.getPollingFirm(), opinionPoll.getPollingFirmPartner()));
        }
    }

    /**
     * Extracts all the results from an opinion poll.
     *
     * @param opinionPoll          The opinion poll to extract the results from.
     * @param electoralListKeySets The sets of keys of the electoral lists for which to extract the results.
     * @return A set of numbers representing the results.
     */
    private static Set<ResultValue> extractResults(final OpinionPoll opinionPoll,
            final List<Set<String>> electoralListKeySets) {
        Set<ResultValue> result = new HashSet<ResultValue>();
        for (Set<String> electoralListKeySet : electoralListKeySets) {
            addToSetUnlessNull(result, opinionPoll.getResult(electoralListKeySet));
        }
        addToSetUnlessNull(result, opinionPoll.getOther());
        return result;
    }

    /**
     * Extracts all the results from a response scenario.
     *
     * @param responseScenario     The response scenario to extract the results from.
     * @param electoralListKeySets The sets of keys of the electoral lists for which to extract the results.
     * @return A set of numbers representing the results.
     */
    private static Set<ResultValue> extractResults(final ResponseScenario responseScenario,
            final List<Set<String>> electoralListKeySets) {
        Set<ResultValue> result = new HashSet<ResultValue>();
        for (Set<String> electoralListKeySet : electoralListKeySets) {
            addToSetUnlessNull(result, responseScenario.getResult(electoralListKeySet));
        }
        addToSetUnlessNull(result, responseScenario.getOther());
        return result;
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
            return opinionPoll.getEndDate();
        } else {
            return opinionPoll.getFieldworkStart().getStart();
        }
    }

    /**
     * Returns the second object if the first object is <code>null</code>, or the first object otherwise.
     *
     * @param <T>    The type of the objects.
     * @param first  The first object.
     * @param second The second object.
     * @return The second object if the first object is <code>null</code>, or the first object otherwise.
     */
    static <T> T secondIfFirstNull(final T first, final T second) {
        return first == null ? second : first;
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
     * Sorts a set of opinion polls chronologically, first by end date, then by start date, then by sample size.
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
