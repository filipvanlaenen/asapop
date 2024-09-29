package net.filipvanlaenen.asapop.exporter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.model.DateMonthOrYear;
import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.ElectoralList;
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
     * Verifies whether the provided area matches with the specified area.
     *
     * <ol>
     * <li>If the specified area is equal to <code>null</code>, any actual area matches.</li>
     * <li>If the specified area is equal to <code>--</code>, the actual area has to be equal to <code>null</code> or
     * the area specified that be included as national in order to match.</li>
     * <li>Otherwise, the actual area only matches if it's equal to the specified area.</li>
     * </ol>
     *
     * @param specifiedArea         The area specified for export.
     * @param includeAreaAsNational The area that also should match as national.
     * @param actualArea            The are of the opinion poll or response alternative.
     * @return True if the provided area matches with the specified area, false otherwise.
     */
    static boolean areaMatches(final String specifiedArea, final String includeAreaAsNational,
            final String actualArea) {
        return specifiedArea == null
                || "--".equals(specifiedArea) && (actualArea == null || actualArea.equals(includeAreaAsNational))
                || specifiedArea.equals(actualArea);
    }

    /**
     * Calculates the precision of an opinion poll.
     *
     * @param opinionPoll         The opinion poll.
     * @param electoralListIdSets The sets of IDs of the electoral lists to export.
     * @return The precision as a string.
     */
    static ResultValue.Precision calculatePrecision(final OpinionPoll opinionPoll,
            final List<Set<String>> electoralListIdSets) {
        return ResultValue.Precision.getHighestPrecision(extractResults(opinionPoll, electoralListIdSets));
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
     * @param responseScenario    The response scenario.
     * @param electoralListIdSets The sets of IDs of the electoral lists to export.
     * @return The precision as a string.
     */
    static ResultValue.Precision calculatePrecision(final ResponseScenario responseScenario,
            final List<Set<String>> electoralListIdSets) {
        return ResultValue.Precision.getHighestPrecision(extractResults(responseScenario, electoralListIdSets));
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
                Integer sampleSizeValue1 = op1.getSampleSizeValue();
                sampleSizeValue1 = sampleSizeValue1 == null ? 0 : sampleSizeValue1;
                Integer sampleSizeValue2 = op2.getSampleSizeValue();
                sampleSizeValue2 = sampleSizeValue2 == null ? 0 : sampleSizeValue2;
                if (sampleSizeValue1.equals(sampleSizeValue2)) {
                    String pollingFirm1 = op1.getPollingFirm();
                    String pollingFirm2 = op2.getPollingFirm();
                    if (Objects.equals(pollingFirm1, pollingFirm2)) {
                        return 0;
                    } else if (pollingFirm1 == null) {
                        return 1;
                    } else if (pollingFirm2 == null) {
                        return -1;
                    } else {
                        return pollingFirm1.compareTo(pollingFirm2);
                    }
                } else {
                    return sampleSizeValue2 - sampleSizeValue1;
                }
            } else {
                return startDate2.compareTo(startDate1);
            }
        } else {
            return endDate2.compareTo(endDate1);
        }
    }

    /**
     * Converts a set of IDs for electoral lists to a string with their abbreviations.
     *
     * @param electoralListIdSet The IDs for the electoral lists.
     * @return A string with the abbreviations of the electoral lists.
     */
    static String electoralListIdsToAbbreviations(final Set<String> electoralListIdSet) {
        Set<ElectoralList> electoralLists = ElectoralList.get(electoralListIdSet);
        List<String> sortedAbbreviations =
                new ArrayList<String>(electoralLists.stream()
                        .map(electoralList -> electoralList.getRomanizedAbbreviation() == null
                                ? electoralList.getAbbreviation()
                                : electoralList.getRomanizedAbbreviation())
                        .collect(Collectors.toList()));
        Collections.sort(sortedAbbreviations);
        return String.join("+", sortedAbbreviations);
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
     * @return A list with two elements containing the dates for the EOPAOD CSV file.
     */
    static List<String> exportDates(final OpinionPoll opinionPoll) {
        List<String> elements = new ArrayList<String>();
        DateMonthOrYear fieldworkStart = opinionPoll.getFieldworkStart();
        DateMonthOrYear fieldworkEnd = opinionPoll.getFieldworkEnd();
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
     * Exports the participation rate from a response scenario.
     *
     * @param responseScenario The response scenario to export the participation rate for.
     * @param opinionPoll      The opinion poll the response scenario belongs to.
     * @return A string representing the participation rate for the response scenario.
     */
    static String exportParticipationRate(final ResponseScenario responseScenario, final OpinionPoll opinionPoll) {
        ResultValue noResponses = responseScenario.getNoResponses();
        if (noResponses == null) {
            noResponses = opinionPoll.getNoResponses();
        }
        if (noResponses != null) {
            return noResponses.getPrecision().getFormat().format(ONE_HUNDRED - noResponses.getNominalValue());
        }
        DecimalNumber excluded = responseScenario.getExcluded();
        if (excluded == null) {
            excluded = opinionPoll.getExcluded();
        }
        if (excluded != null) {
            return new DecimalNumber(ONE_HUNDRED - excluded.value(), excluded.numberOfDecimals()).toString();
        } else {
            return null;
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
     * @param opinionPoll         The opinion poll to extract the results from.
     * @param electoralListIdSets The sets of IDs of the electoral lists for which to extract the results.
     * @return A set of numbers representing the results.
     */
    private static Set<ResultValue> extractResults(final OpinionPoll opinionPoll,
            final List<Set<String>> electoralListIdSets) {
        Set<ResultValue> result = new HashSet<ResultValue>();
        for (Set<String> electoralListIdSet : electoralListIdSets) {
            addToSetUnlessNull(result, opinionPoll.getResult(electoralListIdSet));
        }
        addToSetUnlessNull(result, opinionPoll.getOther());
        return result;
    }

    /**
     * Extracts all the results from a response scenario.
     *
     * @param responseScenario    The response scenario to extract the results from.
     * @param electoralListIdSets The sets of IDs of the electoral lists for which to extract the results.
     * @return A set of numbers representing the results.
     */
    private static Set<ResultValue> extractResults(final ResponseScenario responseScenario,
            final List<Set<String>> electoralListIdSets) {
        Set<ResultValue> result = new HashSet<ResultValue>();
        for (Set<String> electoralListIdSet : electoralListIdSets) {
            addToSetUnlessNull(result, responseScenario.getResult(electoralListIdSet));
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
